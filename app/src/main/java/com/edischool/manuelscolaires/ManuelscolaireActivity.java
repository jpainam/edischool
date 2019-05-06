package com.edischool.manuelscolaires;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.edischool.R;
import com.edischool.pojo.Manuelscolaire;
import com.edischool.pojo.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ManuelscolaireActivity extends AppCompatActivity {
    private static final String TAG = "ManuelscolaireActivity";
    public Student currentStudent;
    ExpandableListView expandableListView;
    ManuelExpandableListAdapter manuelExpandableListAdapter;
    List<String> subjects = new ArrayList<>();
    HashMap<String, List<Manuelscolaire>> manualscolaires = new HashMap<>();
    Context context;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuelscolaire);
        context = getApplicationContext();
        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        Intent i = getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Manuels Scolaires");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getClasse());
        }
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
        /*studentDetail = findViewById(R.id.studentDetail);
        classeDetail = findViewById(R.id.classeDetail);
        studentDetail.setText(currentStudent.getFirstName());
        classeDetail.setText("Manuels scolaires: " + currentStudent.getClasse());*/
        expandableListView = findViewById(R.id.expandableListView);
        manuelExpandableListAdapter = new ManuelExpandableListAdapter(context, subjects, manualscolaires);
        expandableListView.setAdapter(manuelExpandableListAdapter);
        initView();
    }

    private void initView(){
        AsyncTask<Void, Integer, HashMap<String, List<Manuelscolaire>>> asyncTask = new AsyncTask<Void, Integer, HashMap<String, List<Manuelscolaire>>>() {
            @Override
            protected HashMap<String, List<Manuelscolaire>> doInBackground(Void... voids) {
                Log.i(TAG,"Starting InitView");
                ManuelscolaireDao dao = new ManuelscolaireDao(context);
                return dao.getManualsBySubject(currentStudent.getId());
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(dialog != null){
                    dialog.show();
                }
            }

            @Override
            protected void onPostExecute(HashMap<String, List<Manuelscolaire>> manualListHashMap) {
                super.onPostExecute(manualListHashMap);
                Log.i(TAG, "Init View Finished");
                Log.i(TAG, manualListHashMap.toString());
                manualscolaires.putAll(manualListHashMap);
                subjects.addAll(manualListHashMap.keySet());
                manuelExpandableListAdapter.notifyDataSetChanged();
                // Expand the first group
                if(manualscolaires.size() > 0) {
                    expandableListView.expandGroup(0, true);
                }
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        };
        asyncTask.execute();
    }


}
