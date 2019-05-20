package com.edischool.absences;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Absence;
import com.edischool.pojo.Student;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AbsenceActivity extends AppCompatActivity {

    private static final String TAG = "AbsenceActivity";
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AbsenceRecyclerViewAdapter mAdapter = null;
    Context context;
    List<Absence> absenceList = new ArrayList<>();
    AlertDialog dialog = null;
    Student currentStudent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);
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
            getSupportActionBar().setTitle("Absences");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        context = getApplicationContext();
        if(mAdapter == null) {
            mAdapter = new AbsenceRecyclerViewAdapter(this, absenceList);
        }
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(dialog != null){
            dialog.show();
        }

        db.collection(Constante.ABSENCES_COLLECTION).document(currentStudent.getStudentId()).collection("studentAbsences")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.d(TAG, "Error getting documents.", e);
                        return;
                    }
                    absenceList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Log.d(TAG, doc.getId() + " => " + doc.getData());
                        Absence ab = doc.toObject(Absence.class);
                        absenceList.add(ab);
                    }
                    mAdapter.notifyDataSetChanged();
                    if(dialog != null){
                        dialog.dismiss();
                    }
                });
    }
}
