package com.edischool.student;

import android.content.Context;
import android.util.Log;

import com.edischool.pojo.Notification;
import com.edischool.pojo.Student;
import com.edischool.utils.Constante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class NotifyAction {

    private static final String TAG = "NotifyAction";
    private Student currentStudent;
    private Context context;

    public NotifyAction(Context context, Student student) {
        this.context = context;
        this.currentStudent = student;
    }

    public void sentAbsenceNotification() {
        FirebaseUser connectedUser = FirebaseAuth.getInstance().getCurrentUser();
        final Notification notif = new Notification();
        notif.setNotificationMessage(currentStudent.getFirstName() + " is absent in class, Contact the school for more information");
        notif.setNotificationTitle("Notification of absence");
        notif.setSenderPhoneNumber(connectedUser.getPhoneNumber());
        notif.setCreateAt(Calendar.getInstance().getTime().toString());
        notif.setRead(false);
        notif.setNotificationType("Absence");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constante.STUDENTS_COLLECTION).whereEqualTo(Constante.STUDENT_KEY, currentStudent.getStudentId())
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Error getting documents.", e);
                        return;
                    }
                    for (DocumentSnapshot doc : snapshots) {
                        Log.i(TAG, doc.getData() + "");
                        if (doc.get(Constante.STUDENT_KEY).equals(currentStudent.getStudentId())) {
                            currentStudent = doc.toObject(Student.class);
                        }
                    }
                    List<String> destinataires = currentStudent.getResponsables();
                    if(destinataires != null && !destinataires.isEmpty()) {
                        for (String phoneNumber : destinataires) {
                            if(!phoneNumber.equals(connectedUser.getPhoneNumber())) {
                                db.collection(Constante.NOTIFICATIONS_COLLECTION).document(phoneNumber)
                                        .collection(Constante.USER_NOTIFICATIONS_COLLECTION).document()
                                        .set(notif);
                                Log.i(TAG, "Notification set for " + phoneNumber);
                            }
                        }
                    }else{
                        Log.e(TAG, "No destination phone number found in " + currentStudent.getFirstName());
                    }
                }
            });
    }
}
