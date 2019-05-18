package com.edischool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.edischool.absences.AbsenceActivity;
import com.edischool.bulletins.BulletinActivity;
import com.edischool.exams.ExamActivity;
import com.edischool.finances.FinanceActivity;
import com.edischool.manuelscolaires.ManuelscolaireActivity;
import com.edischool.messages.MessageActivity;
import com.edischool.notes.NotesActivity;
import com.edischool.pojo.Student;
import com.edischool.punitions.PunitionActivity;
import com.edischool.timetable.TimeTableActivity;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class InnerMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "InnerMenuActivity";
    Context context;

    Student currentStudent;

    LinearLayout llGrade;
    LinearLayout llResult;
    LinearLayout llAttendance;
    LinearLayout llTextbook;
    LinearLayout llPunishment;
    LinearLayout llTimetable;
    LinearLayout llPayment;
    LinearLayout llMessage;
    LinearLayout llExam;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_menu);
        context = getApplicationContext();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Details");
        }

        llGrade = findViewById(R.id.grade);
        llResult = findViewById(R.id.result);
        llAttendance = findViewById(R.id.attendance);
        llTextbook = findViewById(R.id.textbook);
        llPunishment = findViewById(R.id.punishment);
        llPayment = findViewById(R.id.payment);
        llMessage = findViewById(R.id.message);
        llTimetable = findViewById(R.id.timetable);
        llExam = findViewById(R.id.exam);

        llGrade.setOnClickListener(this);
        llTextbook.setOnClickListener(this);
        llAttendance.setOnClickListener(this);
        llPunishment.setOnClickListener(this);
        llResult.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        llTimetable.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llExam.setOnClickListener(this);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentStudent = (Student) getIntent().getSerializableExtra("student");
        Log.i(TAG, "Student id " + currentStudent.getStudentId() + " " + currentStudent.getFormId());
        db.collection(Constante.STUDENTS_COLLECTION).whereEqualTo(Constante.STUDENT_KEY, currentStudent.getStudentId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.w(TAG, "Error getting documents.", e);
                            return;
                        }
                        for(DocumentSnapshot doc : snapshots){
                            Log.i(TAG, doc.getData() + "");
                            if(doc.get(Constante.STUDENT_KEY).equals(currentStudent.getStudentId())){
                                currentStudent = doc.toObject(Student.class);
                            }
                        }
                        getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.grade:
                intent = new Intent(context, NotesActivity.class);
                break;
            case R.id.result:
                intent = new Intent(context, BulletinActivity.class);
                break;
            case R.id.attendance:
                intent = new Intent(context, AbsenceActivity.class);
                break;
            case R.id.punishment:
                intent = new Intent(context, PunitionActivity.class);
                break;
            case R.id.payment:
                intent = new Intent(context, FinanceActivity.class);
                break;
            case R.id.timetable:
                intent = new Intent(context, TimeTableActivity.class);
                break;
            case R.id.message:
                intent = new Intent(context, MessageActivity.class);
                break;
            case R.id.textbook:
                intent = new Intent(context, ManuelscolaireActivity.class);
                break;
            case  R.id.exam:
                intent = new Intent(context, ExamActivity.class);
                break;
        }
        if(intent != null) {
            intent.putExtra("student", currentStudent);
            startActivity(intent);
        }
    }
}
