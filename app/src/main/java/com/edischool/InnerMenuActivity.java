package com.edischool;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.edischool.absences.AbsenceActivity;
import com.edischool.manuelscolaires.ManuelscolaireActivity;
import com.edischool.notes.NotesActivity;
import com.edischool.pojo.Student;

public class InnerMenuActivity extends AppCompatActivity {
    private static final String TAG = "InnerMenuActivity";
    Context context;

    Student currentStudent;
    TextView detailStudent;
    TextView detailClasse;
    CardView noteCard;
    CardView bulletinCard;
    CardView absenceCard;
    CardView manuelCard;

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

        /*detailStudent = findViewById(R.id.studentDetail);
        detailClasse = findViewById(R.id.detailClasse);*/
        noteCard = findViewById(R.id.noteCard);
        bulletinCard = findViewById(R.id.bulletinCard);
        absenceCard = findViewById(R.id.absenceCard);
        manuelCard = findViewById(R.id.manuelCard);

        //detailStudent.setText(currentStudent.getFirstName());
        //detailClasse.setText( " Classe: " + currentStudent.getClasse());

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotesActivity.class);
                intent.putExtra("student", currentStudent);
                startActivity(intent);
            }
        });
        manuelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManuelscolaireActivity.class);
                intent.putExtra("student", currentStudent);
                startActivity(intent);
            }
        });
        absenceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AbsenceActivity.class);
                intent.putExtra("student", currentStudent);
                startActivity(intent);
            }
        });
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
}
