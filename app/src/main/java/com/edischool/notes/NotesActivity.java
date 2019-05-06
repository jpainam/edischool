package com.edischool.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edischool.R;
import com.edischool.pojo.Note;
import com.edischool.pojo.Student;
import com.edischool.sync.SyncAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "NotesActivity";
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private boolean swipeEnabled = false;
    List<Note> noteList = new ArrayList<>();
    public Student currentStudent;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        context = getApplicationContext();
        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(NotesActivity.this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        Intent i = getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Grades");
            getSupportActionBar().setSubtitle(currentStudent.getFirstName() + " - " + currentStudent.getClasse());
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.noteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(noteList);
        recyclerView.setAdapter(notesRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeEnabled = true;
                SyncAdapter.doManualSyncAsync(context);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        initListView();
    }

    private void initListView(){
        noteList.clear();
        AsyncTask<URL, Integer, List<Note>> asyncTask = new AsyncTask<URL, Integer, List<Note>>() {
            @Override
            protected List<Note> doInBackground(URL... urls) {
                NotesDao dao = new NotesDao(context);
                Log.i(TAG, currentStudent.getId() + "");
                Log.i(TAG, dao.getAllNotes().toString());
                return dao.getNotes(currentStudent.getId());
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                noteList.addAll(notes);
                notesRecyclerViewAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        };
        asyncTask.execute();
    }
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sAction = intent.getAction();
            if(sAction.equals(getString(R.string.SYNC_STATUS_ACTION))) {
                String status = intent.getExtras().getString(getString(R.string.sync_status));
                Log.e(TAG, "status " + status);
                if (status.equals(getString(R.string.sync_running))) {
                    Log.i(TAG, "Progress Bar Running");
                    if (!swipeEnabled) {
                        dialog.show();
                    }
                } else if (status.equals(getString(R.string.sync_finished))) {
                    Log.i(TAG, "Progress Bar Finished");
                    if (swipeEnabled) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, getString(R.string.refreshed_message), Toast.LENGTH_SHORT).show();
                        swipeEnabled = false;
                    } else {
                        dialog.dismiss();
                    }
                    initListView();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction(getString(R.string.SYNC_STATUS_ACTION));
        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver, myFilter);
        return super.onCreateView(parent, name, context, attrs);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(myReceiver);
    }
}
