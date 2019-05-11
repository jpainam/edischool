package com.edischool.absences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.edischool.InnerMenuActivity;
import com.edischool.R;
import com.edischool.pojo.Absence;
import com.edischool.pojo.Student;

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
    /*TextView studentDetail;
    TextView classeDetail;*/
    Student currentStudent;
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
        Intent i = getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Absences");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        /*studentDetail = findViewById(R.id.studentDetail);
        classeDetail = findViewById(R.id.classeDetail);
        studentDetail.setText(currentStudent.getFirstName());
        classeDetail.setText("Absences: " + currentStudent.getClasse());*/

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        context = getApplicationContext();
        if(mAdapter == null) {
            mAdapter = new AbsenceRecyclerViewAdapter(this, absenceList);
        }
        recyclerView.setAdapter(mAdapter);

        initData();
    }

    private void initData(){
        AsyncTask<Void, Integer, List<Absence>> asyncTask = new AsyncTask<Void, Integer, List<Absence>>() {
            @Override
            protected List<Absence> doInBackground(Void... voids) {
                Log.i(TAG,"Starting InitView");
                AbsenceDao dao = new AbsenceDao(context);
                return dao.getAbsences(Integer.parseInt(currentStudent.getStudentId()));
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(dialog != null){
                    dialog.show();
                }
            }

            @Override
            protected void onPostExecute(List<Absence> o) {
                super.onPostExecute(o);
                Log.i(TAG, "Init View Finished");
                Log.i(TAG, o.toString());
                absenceList.addAll(o);
                mAdapter.notifyDataSetChanged();
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        };
        asyncTask.execute();
    }
}
