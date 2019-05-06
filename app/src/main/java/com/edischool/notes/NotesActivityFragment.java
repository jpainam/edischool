package com.edischool.notes;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edischool.R;

import com.edischool.pojo.Note;
import com.edischool.pojo.Student;
import com.edischool.sync.SyncAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class NotesActivityFragment extends Fragment {
    private static final String TAG = "NotesActivityFragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private boolean swipeEnabled = false;
    List<Note> noteList = new ArrayList<>();
    public Student currentStudent;
    AlertDialog dialog = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotesActivityFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotesActivityFragment newInstance(int columnCount) {
        NotesActivityFragment fragment = new NotesActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(getContext())
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        Intent i = getActivity().getIntent();
        currentStudent = (Student) i.getSerializableExtra("student");
        Log.e(TAG, currentStudent.getFirstName() + " " + currentStudent.getLastName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        // Set the adapter
        if (view instanceof SwipeRefreshLayout) {
            Context context = view.getContext();
            swipeRefreshLayout = (SwipeRefreshLayout) view;
            recyclerView = view.findViewById(R.id.noteRecyclerView);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(noteList, mListener);
            recyclerView.setAdapter(notesRecyclerViewAdapter);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeEnabled = true;
                    SyncAdapter.doManualSyncAsync(getContext());
                }
            });
            // Configure the refreshing colors
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

        }

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(noteList.isEmpty()){
            initListView();
        }
    }

    private void initListView(){
        noteList.clear();
        AsyncTask<URL, Integer, List<Note>> asyncTask = new AsyncTask<URL, Integer, List<Note>>() {
            @Override
            protected List<Note> doInBackground(URL... urls) {
                NotesDao dao = new NotesDao(getContext());
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction(getString(R.string.SYNC_STATUS_ACTION));
        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver, myFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Note item);
    }
}
