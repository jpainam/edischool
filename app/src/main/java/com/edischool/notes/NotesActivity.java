package com.edischool.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edischool.R;
import com.edischool.pojo.Grade;
import com.edischool.pojo.Mark;
import com.edischool.pojo.Note;
import com.edischool.pojo.Student;
import com.edischool.pojo.User;
import com.edischool.sync.SyncAdapter;
import com.edischool.utils.Constante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Nullable;

import dmax.dialog.SpotsDialog;

public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "NotesActivity";
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private boolean swipeEnabled = false;
    List<Grade> noteList = new ArrayList<>();
    public Student currentStudent;
    AlertDialog dialog = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        context = getApplicationContext();
        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(NotesActivity.this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        Intent i = getIntent();
        currentStudent = (Student) i.getParcelableExtra("student");
        if(currentStudent == null){
            Log.e(TAG, "Current student null");
            finish();
        }
        Log.e(TAG, currentStudent.getStudentId() + " " +
                currentStudent.getFirstName() + " " + currentStudent.getLastName());
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Grades");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.noteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(noteList);
        recyclerView.setAdapter(notesRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeEnabled = true;
                readData(currentStudent.getStudentId());
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        readData(currentStudent.getStudentId());
    }


    private void readData(final String studentId){
        Log.i(TAG, "Enter read data");
        /*DocumentReference markRef = db.collection(Constante.MARKS_COLLECTION).document(studentId);
        markRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Mark marks = document.toObject(Mark.class);
                    Log.i(TAG, document.getId() + " " + document.getData() + "");
                }else{
                    Log.w(TAG, "Error getting document.", task.getException());
                }
            }
        });*/
        CollectionReference markRef = db.collection(Constante.MARKS_COLLECTION);
        Query query = markRef.whereEqualTo(Constante.STUDENT_KEY, studentId);
        if (!swipeEnabled) {
            dialog.show();
        }
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w(TAG, "Error getting documents.", e);
                    return;
                }
                for (QueryDocumentSnapshot document : value) {
                    Mark mark = document.toObject(Mark.class);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    noteList.clear();
                    noteList.addAll(mark.getGrades());
                    notesRecyclerViewAdapter.notifyDataSetChanged();
                }
                if (swipeEnabled) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.refreshed_message), Toast.LENGTH_SHORT).show();
                    swipeEnabled = false;
                } else {
                    dialog.dismiss();
                }
            }
        });
       /* query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Mark mark = document.toObject(Mark.class);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        noteList.clear();
                        noteList.addAll(mark.getGrades());
                        notesRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    if (swipeEnabled) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), getString(R.string.refreshed_message), Toast.LENGTH_SHORT).show();
                        swipeEnabled = false;
                    } else {
                        dialog.dismiss();
                    }
                } else {

                }
            }
        });*/


    }
}
