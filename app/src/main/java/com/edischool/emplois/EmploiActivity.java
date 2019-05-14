package com.edischool.emplois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.edischool.R;
import com.edischool.notes.NotesDao;
import com.edischool.pojo.Emploi;
import com.edischool.pojo.Note;
import com.edischool.pojo.Student;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class EmploiActivity extends AppCompatActivity {
    private static final String TAG = "EmploiActivity";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Emploi> emploiList = new ArrayList<>();
    EmploiRecyclerViewAdapter emploiRecyclerViewAdapter;
    Context context;
    public Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_emploi);
        recyclerView = findViewById(R.id.recyclerView);
        Intent i = getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Time Table");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 6, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        emploiRecyclerViewAdapter = new EmploiRecyclerViewAdapter(emploiList, this);
        recyclerView.setAdapter(emploiRecyclerViewAdapter);
        loadData();
    }

    private void loadData(){
        emploiList.clear();
        AsyncTask<Void, Integer, List<Emploi>> asyncTask = new AsyncTask<Void, Integer, List<Emploi>>() {
            @Override
            protected List<Emploi> doInBackground(Void... voids) {
                EmploiDao dao = new EmploiDao(context);
                Log.i(TAG, currentStudent.getStudentId() + "");
                return dao.getEmplois(Integer.parseInt(currentStudent.getStudentId()));
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<Emploi> emplois) {
                emploiList.addAll(emplois);
                emploiRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute();
    }
}
