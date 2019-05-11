package com.edischool.bulletins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.edischool.R;
import com.edischool.absences.AbsenceRecyclerViewAdapter;
import com.edischool.pojo.Bulletin;

import java.util.List;

public class BulletinActivity extends AppCompatActivity {
    private static final String TAG = "BulletinActivity";

    Spinner spinnerSequence;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    Context context;
    BulletinRecyclerViewAdapter mAdapter;
    List<Bulletin> bulletinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);
        spinnerSequence = findViewById(R.id.spinnerSequence);

        spinnerSequence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, parent.getSelectedItem().toString());
                Log.e(TAG, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        context = getApplicationContext();
        if(mAdapter == null) {
            mAdapter = new BulletinRecyclerViewAdapter(this, bulletinList);
        }
        recyclerView.setAdapter(mAdapter);
    }
}
