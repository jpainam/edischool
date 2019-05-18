package com.edischool.exams;

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
import com.edischool.finances.FinanceRecyclerViewAdapter;
import com.edischool.pojo.Exam;
import com.edischool.pojo.Finance;
import com.edischool.pojo.FinanceListWrapper;
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

public class ExamActivity extends AppCompatActivity {

    private static final String TAG = "ExamActivity";
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExamRecyclerViewAdapter mAdapter = null;
    Context context;
    List<Exam> examList = new ArrayList<>();
    AlertDialog dialog = null;
    Student currentStudent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        recyclerView = findViewById(R.id.recyclerView);
        if (dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        currentStudent = (Student) getIntent().getSerializableExtra("student");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Exam Programs");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new EdisDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 5));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        context = getApplicationContext();
        if (mAdapter == null) {
            mAdapter = new ExamRecyclerViewAdapter(this, examList);
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new EdisRecyclerTouchListener(getApplicationContext(), recyclerView,
                new EdisRecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Exam f = examList.get(position);
                        Toast.makeText(getApplicationContext(), f.getSubject(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dialog != null) {
            dialog.show();
        }
        db.collection(Constante.EXAMS_COLLECTION).document(currentStudent.getFormId())
                .collection("classExams").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Error getting documents.", e);
                            return;
                        }
                        examList.clear();
                        for (DocumentSnapshot doc : snapshots) {
                            Log.d(TAG, doc.getId() + " => " + doc.getData());
                            Exam exam = doc.toObject(Exam.class);
                            examList.add(exam);
                        }
                        mAdapter.notifyDataSetChanged();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }
        );

    }
}
