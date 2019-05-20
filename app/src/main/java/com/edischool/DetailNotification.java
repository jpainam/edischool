package com.edischool;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.edischool.pojo.Notification;
import com.edischool.utils.Constante;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailNotification extends AppCompatActivity {
    private static final String TAG = "DetailNotification";
    TextView message, title, date;
    Context mContext;
    private Notification notification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);
        mContext = this;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        Bundle extras = getIntent().getExtras();
        Intent intent = this.getIntent();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */

        notification = new Notification();
        message = (TextView) findViewById(R.id.message);
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        if (extras != null) {
            /*notification.setNotificationId(extras.getString("id"));
            notification.setNotificationTitle(extras.getString("titre"));
            notification.setNotificationMessage(extras.getString("message"));
            notification.setNotificationType(extras.getString("type"));
            notification.setSenderPhoneNumber(extras.getString("numero"));
            notification.setCreateAt(extras.getString("date"));*/
            notification = (Notification) getIntent().getSerializableExtra("notification");
            message.setText(notification.getNotificationMessage());
            title.setText(notification.getNotificationTitle());
            date.setText(notification.getCreateAt());

         /*  FloatingActionButton delefloating =findViewById(R.id.deleflating);
            delefloating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("notifi", notification);
                    setResult(RESULT_OK, returnIntent);
                    //overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
                    finish();
                }
            });*/
        } else {
            Toast.makeText(this, "Empty Notification", Toast.LENGTH_LONG);
            Log.e("Notifify", "Empty Notification");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(notification != null && !notification.isRead()){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Constante.NOTIFICATIONS_COLLECTION)
                    .document(user.getPhoneNumber())
                    .collection("userNotifications").document(notification.getNotificationId())
                    .update("read", true).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Read propriety successfully updated");
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating isRead in notification ", e);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
        this.finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
