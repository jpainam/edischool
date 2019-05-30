package com.edischool.bulletins;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Grade;
import com.edischool.pojo.GradeBook;
import com.edischool.pojo.Student;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BulletinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "BulletinActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spnSubject;
    RecyclerView.LayoutManager layoutManager;
    ArrayAdapter<String> spinnerAdapter;
    List<String> spinnerList;
    RecyclerView recyclerView;
    Context context;
    List<GradeBook> gradeBookList;
    List<Grade> markList = new ArrayList<>();
    private Student currentStudent;
    private ListView resultsubject_list;
    GradeListItemAdapter mAdapter;

    TextView tv_gpa;
    TextView tv_marks;
    TextView tv_total;
    TextView tv_fail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);
        this.spnSubject = (Spinner) findViewById(R.id.spnSubject);
         spinnerList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, spinnerList);
        this.spnSubject.setAdapter(spinnerAdapter);
        this.spnSubject.setOnItemSelectedListener(this);
        this.currentStudent = (Student) getIntent().getSerializableExtra("student");
        this.context = getApplicationContext();
        this.resultsubject_list = findViewById(R.id.resultsubject_list);
        mAdapter = new GradeListItemAdapter(context, R.layout.fragment_examresultsubjectlistview, markList);
        this.resultsubject_list.setAdapter(mAdapter);
        tv_gpa = findViewById(R.id.marks_gpa);
        tv_marks = findViewById(R.id.marks_available);
        tv_total = findViewById(R.id.marks_total);
        tv_fail = findViewById(R.id.fail);
    }



    @Override
    protected void onStart() {
        super.onStart();
        db.collection(Constante.SUBDIVISIONS_COLLECTION).document(Constante.INSTITUTION)
                .addSnapshotListener((snapshot, e) -> {
                    if(e != null) {
                        Log.e(TAG, "Error requesting subdivisions ", e);
                        return;
                    }
                    Map<String, Object> map = snapshot != null ? snapshot.getData() : null;
                    if(null == map) return;
                    for(Map.Entry<String, Object> entry : map.entrySet()){
                        if((boolean)entry.getValue()) {
                            spinnerList.add(entry.getKey());
                        }
                        Log.d(TAG, entry.getKey() + " " + entry.getValue());
                    }
                    spinnerAdapter.notifyDataSetChanged();

                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String sequence = (String)this.spnSubject.getSelectedItem();
        Log.d(TAG, sequence + " is selected");
        db.collection(Constante.GRADEBOOKS_COLLECTION).document(currentStudent.getStudentId()).collection(sequence)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error fetching grades ", e);
                        return;
                    }
                    markList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Log.i(TAG, doc.getId() + "=>" + doc.getData());
                        Grade mark = doc.toObject(Grade.class);
                        markList.add(mark);
                    }
                    mAdapter.notifyDataSetChanged();
                    ListAdapter listadp = resultsubject_list.getAdapter();
                    if (listadp != null) {
                        int totalHeight = 0;
                        for (int i = 0; i < listadp.getCount(); i++) {
                            View listItem = listadp.getView(i, null, resultsubject_list);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        ViewGroup.LayoutParams params = resultsubject_list.getLayoutParams();
                        params.height = totalHeight + (resultsubject_list.getDividerHeight() * (listadp.getCount() - 1));
                        resultsubject_list.setLayoutParams(params);
                        resultsubject_list.requestLayout();
                    }
                });
        db.collection(Constante.GRADEBOOKS_COLLECTION).document(currentStudent.getStudentId()).addSnapshotListener(
                (snapshot, e) -> {
                    if(e != null){
                        Log.e(TAG, "Error fetching grade recapitulatif " + e);
                        return;
                    }

                    Map<String, Object> map = snapshot != null ? snapshot.getData() : null;
                    if(null == map) return;
                    if(map.containsKey(sequence)){
                        Map<String, String> recap = (Map<String, String>) map.get(sequence);
                        tv_gpa.setText(recap.get("gpa"));
                        tv_marks.setText(recap.get("obtainedMarks"));
                        tv_total.setText(recap.get("outOfMark"));
                        tv_fail.setText(recap.get("status"));
                    }else{
                        tv_gpa.setText("");
                        tv_marks.setText("");
                        tv_total.setText("");
                        tv_fail.setText("");
                    }
                }
        );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
