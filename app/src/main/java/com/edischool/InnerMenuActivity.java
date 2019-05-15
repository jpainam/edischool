package com.edischool;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.edischool.absences.AbsenceActivity;
import com.edischool.bulletins.BulletinActivity;
import com.edischool.convocations.ConvocationActivity;
import com.edischool.finances.FinanceActivity;
import com.edischool.manuelscolaires.ManuelscolaireActivity;
import com.edischool.notes.NotesActivity;
import com.edischool.pojo.Student;
import com.edischool.punitions.PunitionActivity;
import com.edischool.timetable.TimeTableActivity;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class InnerMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "InnerMenuActivity";
    Context context;

    Student currentStudent;

    CardView noteCard;
    CardView bulletinCard;
    CardView absenceCard;
    CardView manuelCard;
    CardView punitionCard;
    CardView emploiCard;
    CardView financeCard;
    CardView convocationCard;
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

        noteCard = findViewById(R.id.noteCard);
        bulletinCard = findViewById(R.id.bulletinCard);
        absenceCard = findViewById(R.id.absenceCard);
        manuelCard = findViewById(R.id.manuelCard);
        punitionCard = findViewById(R.id.punitionCard);
        financeCard = findViewById(R.id.financeCard);
        convocationCard = findViewById(R.id.convocationCard);
        emploiCard = findViewById(R.id.emploiCard);

        noteCard.setOnClickListener(this);
        manuelCard.setOnClickListener(this);
        absenceCard.setOnClickListener(this);
        punitionCard.setOnClickListener(this);
        bulletinCard.setOnClickListener(this);
        financeCard.setOnClickListener(this);
        emploiCard.setOnClickListener(this);
        convocationCard.setOnClickListener(this);
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
            case R.id.noteCard:
                intent = new Intent(context, NotesActivity.class);
                break;
            case R.id.bulletinCard:
                intent = new Intent(context, BulletinActivity.class);
                break;
            case R.id.absenceCard:
                intent = new Intent(context, AbsenceActivity.class);
                break;
            case R.id.punitionCard:
                intent = new Intent(context, PunitionActivity.class);
                break;
            case R.id.financeCard:
                intent = new Intent(context, FinanceActivity.class);
                break;
            case R.id.emploiCard:
                intent = new Intent(context, TimeTableActivity.class);
                break;
            case R.id.convocationCard:
                intent = new Intent(context, ConvocationActivity.class);
                break;
            case R.id.manuelCard:
                intent = new Intent(context, ManuelscolaireActivity.class);
                break;
        }
        if(intent != null) {
            intent.putExtra("student", currentStudent);
            startActivity(intent);
        }
    }
}
