package com.edischool.manuelscolaires;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.edischool.R;
import com.edischool.pojo.Book;
import com.edischool.pojo.Student;
import com.edischool.pojo.TextBook;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    HashMap<String, List<Book>> manualscolaires = new HashMap<>();
    Context context;
    AlertDialog dialog = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuelscolaire);
        context = getApplicationContext();
        if (dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        Intent i = getIntent();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Textbooks");
            currentStudent = (Student) i.getSerializableExtra("student");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getForm());
        }
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
        expandableListView = findViewById(R.id.expandableListView);
        manuelExpandableListAdapter = new ManuelExpandableListAdapter(context, subjects, manualscolaires);
        expandableListView.setAdapter(manuelExpandableListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dialog != null) {
            dialog.show();
        }

        ///textbooks_r/1/classBooks/subjectId
        db.collection(Constante.TEXTBOOKS_COLLECTION).document(Constante.INSTITUTION + currentStudent.getFormId())
                .collection("classBooks").addSnapshotListener(
                (snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Error getting documents.", e);
                        return;
                    }
                    manualscolaires.clear();
                    subjects.clear();
                    for (QueryDocumentSnapshot doc : snapshots){
                        Log.d(TAG, doc.getId() + " => " + doc.getData());
                        TextBook textBook = doc.toObject(TextBook.class);
                        String subject = textBook.getSubject();
                        subjects.add(subject);
                        List<Book> books = textBook.getBooks();
                        manualscolaires.put(subject, books);
                    }
                    manuelExpandableListAdapter.notifyDataSetChanged();
                    // Expand the first group
                    if (manualscolaires.size() > 0) {
                        expandableListView.expandGroup(0, true);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
        );
    }


    @Override
    protected void onStop() {
        super.onStop();
        Query query = db.collection(Constante.TEXTBOOKS_COLLECTION).whereEqualTo(Constante.FORM_KEY, currentStudent.getFormId());
        ListenerRegistration registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

            }
            // ...
        });
        // Stop listening to changes
        registration.remove();
    }
}
