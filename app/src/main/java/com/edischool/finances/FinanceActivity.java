package com.edischool.finances;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
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

public class FinanceActivity extends AppCompatActivity {

    private static final String TAG = "FinanceActivity";
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FinanceRecyclerViewAdapter mAdapter = null;
    Context context;
    List<Finance> financeList = new ArrayList<>();
    AlertDialog dialog = null;
    Student currentStudent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
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
            getSupportActionBar().setTitle("Finances");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new EdisDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 5));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        context = getApplicationContext();
        if(mAdapter == null) {
            mAdapter = new FinanceRecyclerViewAdapter(this, financeList);
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new EdisRecyclerTouchListener(getApplicationContext(), recyclerView,
                new EdisRecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Finance f = financeList.get(position);
                Toast.makeText(getApplicationContext(), f.getDepositTime(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(dialog != null){
            dialog.show();
        }
        CollectionReference absencesRef = db.collection(Constante.FINANCES_COLLECTION);
        Query query = absencesRef.whereEqualTo(Constante.STUDENT_KEY, currentStudent.getStudentId());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Error getting documents.", e);
                    return;
                }
                financeList.clear();
                for (DocumentSnapshot doc : value) {
                    Log.d(TAG, doc.getId() + " => " + doc.getData());
                    FinanceListWrapper wrapper = doc.toObject(FinanceListWrapper.class);
                    financeList.addAll(wrapper.getTransactions());
                }
                mAdapter.notifyDataSetChanged();
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
    }
}
