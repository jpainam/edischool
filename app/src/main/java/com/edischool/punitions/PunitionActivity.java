package com.edischool.punitions;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Punishment;
import com.edischool.pojo.Student;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PunitionActivity extends AppCompatActivity {

    private static final String TAG = "PunitionActivity";
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PunitionRecyclerViewAdapter mAdapter = null;
    Context context;
    List<Punishment> punitionList = new ArrayList<>();
    AlertDialog dialog = null;
    Student currentStudent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punitions);
        recyclerView = findViewById(R.id.recyclerView);
        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        currentStudent = (Student) getIntent().getSerializableExtra("student");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Punishments");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        context = getApplicationContext();
        if(mAdapter == null) {
            mAdapter = new PunitionRecyclerViewAdapter(this, punitionList);
        }
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(dialog != null){
            dialog.show();
        }
        Log.e(TAG, currentStudent.getStudentId());
       db.collection(Constante.PUNISHMENTS_COLLECTION).document(currentStudent.getStudentId())
       .collection("studentPunishments").addSnapshotListener(
               (snapshots, e) -> {
                   if (e != null) {
                       Log.w(TAG, "Error getting documents.", e);
                       return;
                   }
                   punitionList.clear();
                   for (DocumentSnapshot doc : snapshots) {
                       Log.d(TAG, doc.getId() + " => " + doc.getData());
                       Punishment punition = doc.toObject(Punishment.class);
                       punitionList.add(punition);
                   }
                   mAdapter.notifyDataSetChanged();
                   if(dialog != null){
                       dialog.dismiss();
                   }
               }
       );
    }
}
