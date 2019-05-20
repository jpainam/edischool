package com.edischool.timetable;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class MondayFragment extends Fragment {

    private static final String TAG = "MondayFragment";
    public static final String KEY_MONDAY_FRAGMENT = "Monday";
    private ListView listView;
    private WeekAdapter adapter;
    private ImageView popup;
    AlertDialog dialog = null;
    ArrayList<Week> weekList = new ArrayList<>();
    private Student currentStudent;

    public static MondayFragment newInstance(Student student) {
        MondayFragment f = new MondayFragment();
        Bundle args = new Bundle();
        args.putSerializable("student", student);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        listView = view.findViewById(R.id.mondaylist);
        Bundle args = getArguments();
        currentStudent = (Student) args.getSerializable("student");
        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, weekList);
        listView.setAdapter(adapter);
        popup = view.findViewById(R.id.popupbtn);
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
        if(dialog != null){
            dialog.show();
        }
        ///timetables/1/classTimetables/Monday/dayTimetables
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constante.TIMETABLE_COLLECTION)
                .document(Constante.INSTITUTION + currentStudent.getFormId())
                .collection(Constante.CLASS_TIMETABLES_COLLECTION)
                .document(KEY_MONDAY_FRAGMENT).collection(Constante.DAY_TIMETABLES_COLLECTION)
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

    private void setupAdapter(View view) {

    }

    /*private void setupListViewMultiSelect() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(FragmentHelper.setupListViewMultiSelect(getActivity(), listView, adapter, db));
    }*/
}