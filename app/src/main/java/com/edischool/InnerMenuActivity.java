package com.edischool;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import com.edischool.absences.AbsenceActivity;
import com.edischool.bulletins.BulletinActivity;
import com.edischool.convocations.ConvocationActivity;
import com.edischool.emplois.EmploiActivity;
import com.edischool.finances.FinanceActivity;
import com.edischool.manuelscolaires.ManuelscolaireActivity;
import com.edischool.notes.NotesActivity;
import com.edischool.pojo.Student;
import com.edischool.punitions.PunitionActivity;
import com.edischool.timetable.activities.TimeTableActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_menu);
        context = getApplicationContext();
        Intent i = getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Details");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getClasse());
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
