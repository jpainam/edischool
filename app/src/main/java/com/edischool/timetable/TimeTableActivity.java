package com.edischool.timetable;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.edischool.R;

import com.edischool.pojo.Student;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;



public class TimeTableActivity extends AppCompatActivity{

    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private boolean switchSevenDays;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        currentStudent = (Student)getIntent().getParcelableExtra("student");
        initAll();
    }

    private void initAll() {
        setupFragments();
        setupSevenDaysPref();

        if(switchSevenDays) changeFragments(true);

    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        adapter.addFragment(MondayFragment.newInstance(currentStudent), getResources().getString(R.string.monday));
        adapter.addFragment(TuesdayFragment.newInstance(currentStudent), getResources().getString(R.string.tuesday));
        adapter.addFragment(WednesdayFragment.newInstance(currentStudent), getResources().getString(R.string.wednesday));
        adapter.addFragment(ThursdayFragment.newInstance(currentStudent), getResources().getString(R.string.thursday));
        adapter.addFragment(FridayFragment.newInstance(currentStudent), getResources().getString(R.string.friday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void changeFragments(boolean isChecked) {
        if(isChecked) {
            TabLayout tabLayout = findViewById(R.id.tabLayout);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            adapter.addFragment(SaturdayFragment.newInstance(currentStudent), getResources().getString(R.string.saturday));
            adapter.addFragment(SundayFragment.newInstance(currentStudent), getResources().getString(R.string.sunday));
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            if(adapter.getFragmentList().size() > 5) {
                adapter.removeFragment(SaturdayFragment.newInstance(currentStudent), 5);
                adapter.removeFragment(SundayFragment.newInstance(currentStudent), 5);
            }
        }
        adapter.notifyDataSetChanged();
    }
    


    private void setupSevenDaysPref() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        switchSevenDays = sharedPref.getBoolean("sevendays", false);
    }

}
