package com.edischool.punitions;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.PunishementListWrapper;
import com.edischool.pojo.Punishment;
import com.edischool.pojo.Student;
import com.edischool.utils.Constante;
import com.edischool.utils.EdisDividerItemDecoration;
import com.edischool.utils.EdisRecyclerTouchListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
        currentStudent = (Student) getIntent().getParcelableExtra("student");
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
        CollectionReference punishRef = db.collection(Constante.PUNISHMENTS_COLLECTION);
        Query query = punishRef.whereEqualTo(Constante.STUDENT_KEY, currentStudent.getStudentId());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Error getting documents.", e);
                    return;
                }
                punitionList.clear();
                for (DocumentSnapshot doc : value) {
                    Log.d(TAG, doc.getId() + " => " + doc.getData());
                    PunishementListWrapper wrapper = doc.toObject(PunishementListWrapper.class);
                    punitionList.addAll(wrapper.getPunishments());
                }
                mAdapter.notifyDataSetChanged();
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
    }
}
