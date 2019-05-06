package com.edischool.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.edischool.R;

import com.edischool.pojo.Note;
import com.edischool.pojo.Student;

public class NotesActivity extends AppCompatActivity implements NotesActivityFragment.OnListFragmentInteractionListener {
    private static final String TAG = "NotesActivity";
    public Student currentStudent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        toolbar = findViewById(R.id.toolbar);

        Intent i = getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
        toolbar.setSubtitle("Notes de : " + currentStudent.getFirstName());
    }

    @Override
    public void onListFragmentInteraction(Note item) {
        Log.e(TAG, item + " clicked");
    }
}
