package com.edischool.timetable;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.edischool.R;
import com.edischool.pojo.Student;
import com.edischool.pojo.Week;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


public class ThursdayFragment extends Fragment {

    private static final String TAG = "ThursdayFragment";
    public static final String KEY_THURSDAY_FRAGMENT = "Thursday";
    private ListView listView;
    private WeekAdapter adapter;
    AlertDialog dialog = null;
    ArrayList<Week> weekList = new ArrayList<>();
    private Student currentStudent;

    public static ThursdayFragment newInstance(Student student) {
        ThursdayFragment f = new ThursdayFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("student", student);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thursday, container, false);
        Bundle args = getArguments();
        currentStudent = (Student) args.getSerializable("student");
        listView = view.findViewById(R.id.thursdaylist);
        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, weekList);
        listView.setAdapter(adapter);

        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(getActivity())
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(dialog != null) {
            dialog.show();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constante.TIMETABLE_COLLECTION)
                .document(Constante.INSTITUTION + currentStudent.getFormId())
                .collection(Constante.CLASS_TIMETABLES_COLLECTION)
                .document(KEY_THURSDAY_FRAGMENT).collection(Constante.DAY_TIMETABLES_COLLECTION)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Error getting documents.", e);
                        return;
                    }
                    weekList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Log.d(TAG, doc.getId() + " => " + doc.getData());
                        Week wrapper = doc.toObject(Week.class);
                        weekList.add(wrapper);
                    }
                    adapter.notifyDataSetChanged();
                    if(dialog != null){
                        dialog.dismiss();
                    }
                });
    }
}