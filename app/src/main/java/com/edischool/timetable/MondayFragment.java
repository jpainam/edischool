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
import com.edischool.pojo.WeekListWrapper;
import com.edischool.utils.Constante;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

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
        args.putParcelable("student", student);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        listView = view.findViewById(R.id.mondaylist);
        Bundle args = getArguments();
        currentStudent = args.getParcelable("student");
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference timeRef = db.collection(Constante.TIMETABLE_COLLECTION)
                .document(Constante.INSTITUTION).collection(KEY_MONDAY_FRAGMENT);
        Query query = timeRef.whereEqualTo(Constante.FORM_KEY, currentStudent.getStudentId());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Error getting documents.", e);
                    return;
                }
                weekList.clear();
                for (DocumentSnapshot doc : snapshots) {
                    Log.d(TAG, doc.getId() + " => " + doc.getData());
                    WeekListWrapper wrapper = doc.toObject(WeekListWrapper.class);
                    weekList.addAll(wrapper.getTimetables());
                }
                adapter.notifyDataSetChanged();
                if(dialog != null){
                    dialog.dismiss();
                }
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