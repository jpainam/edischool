package com.edischool.timetable;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.edischool.R;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;



public class TimeTableActivity extends AppCompatActivity{

    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private boolean switchSevenDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
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
        adapter.addFragment(new MondayFragment(), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getResources().getString(R.string.friday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void changeFragments(boolean isChecked) {
        if(isChecked) {
            TabLayout tabLayout = findViewById(R.id.tabLayout);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            adapter.addFragment(new SaturdayFragment(), getResources().getString(R.string.saturday));
            adapter.addFragment(new SundayFragment(), getResources().getString(R.string.sunday));
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            if(adapter.getFragmentList().size() > 5) {
                adapter.removeFragment(new SaturdayFragment(), 5);
                adapter.removeFragment(new SundayFragment(), 5);
            }
        }
        adapter.notifyDataSetChanged();
    }
    


    private void setupSevenDaysPref() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        switchSevenDays = sharedPref.getBoolean("sevendays", false);
    }

}
