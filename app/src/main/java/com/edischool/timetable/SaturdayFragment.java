package com.edischool.timetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.edischool.R;

public class SaturdayFragment extends Fragment {

    public static final String KEY_SATURDAY_FRAGMENT = "Saturday";
    private TimeTableDao db;
    private ListView listView;
    private WeekAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saturday, container, false);
        setupAdapter(view);
        setupListViewMultiSelect();
        return view;
    }

    private void setupAdapter(View view) {
        db = new TimeTableDao(getActivity());
        listView = view.findViewById(R.id.saturdaylist);
        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, db.getWeek(KEY_SATURDAY_FRAGMENT));
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(FragmentHelper.setupListViewMultiSelect(getActivity(), listView, adapter, db));
    }
}
