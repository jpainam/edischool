package com.edischool;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.edischool.notification.NotificationDao;
import com.edischool.pojo.Notification;
import com.edischool.sql.DatabaseHelper;
import com.edischool.utils.Constante;
import com.edischool.utils.DateUtils;
import com.edischool.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by sadjang on 18/06/2018.
 */


public class EdisFirebaseMessagingService extends FirebaseMessagingService {
    DatabaseHelper myDb;
    private static String TAG = "EdisFirebaseMessagingService";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onNewToken(String s) {
        this.context = this;
        Log.e("NEW_TOKEN", s);
        String refreshedToken = s;
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.firebase_token), refreshedToken);
        editor.apply();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            sendRegistrationToServer(refreshedToken, user.getPhoneNumber());
            Utils.sendRegistrationToPhpServer(context, refreshedToken, user.getPhoneNumber());
        }
    }

    private void sendRegistrationToServer(String token, String phone_number){
        Log.i(TAG, "Send Token to Server " + token);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                getString(R.string.shared_preference_file), Context.MODE_PRIVATE);


        db.collection(Constante.USERS_COLLECTION).document(phone_number).update("token", token)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Token successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "errorr updating token", e);
            }
        });

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        this.context = this;
        myDb = DatabaseHelper.getInstance(this);
        Log.e(TAG, "Message received " + remoteMessage.getNotification().getTitle());

        if (remoteMessage.getNotification().getBody() != null){
            final String title = remoteMessage.getNotification().getTitle();
            final String body = remoteMessage.getNotification().getBody();

            String click_action = remoteMessage.getNotification().getClickAction();
            Log.e("Message Received title", title);
            Log.e("Message Received body", body);
            Notification notifcation = new Notification();
            notifcation.setNotificationTitle(title);
            notifcation.setNotificationMessage(body);
            createNotification(title, body, click_action, notifcation);
            if(notifcation.getNotificationTitle().contains("1234")) {
                notifcation.setNotificationType("Notification");
                notifcation.setNotificationTitle(notifcation.getNotificationTitle().replace("1234", ""));
                notifcation.setCreateAt(DateUtils.getCurrentDate());
                notifcation.setRead(false);
                insererDansFirebaseTemporairement(notifcation);
            }
            sendBroacast(title, body);
        }
    }
    public void sendBroacast(String title, String body) {
        Intent intent = new Intent("com.edis.eschool_TARGET_NOTIFICATION");
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
    }

   public void insererDansFirebaseTemporairement(Notification notification){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       db.collection(Constante.NOTIFICATIONS_COLLECTION).document(user.getPhoneNumber())
               .collection(Constante.USER_NOTIFICATIONS_COLLECTION).document()
               .set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Log.d(TAG, "Success");
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Log.e(TAG, "Error", e);
           }
       });
   }


    public void createNotification(String title, String message, String click_action, Notification notification) {
        /**Creates an explicit intent for an Activity in your app**/

        Intent resultIntent = null;
        if (click_action != null && click_action.equals("com.edis.eschool_TARGET_NOTIFICATION")) {
            resultIntent = new Intent(this, DetailNotification.class);
            resultIntent.putExtra("notification", notification);

        } else {
            resultIntent = new Intent(this, DetailNotification.class);
            resultIntent.putExtra("notification", notification);
            /////     resultIntent=new Intent(this,DetailNotification.class);
        }

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("body", message);
        resultIntent.putExtras(bundle);

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_full_open_on_phone);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }


}
