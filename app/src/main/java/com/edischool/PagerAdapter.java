package com.edischool;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.edischool.news.NewsFragment;
import com.edischool.notification.ListeNotifications;
import com.edischool.student.StudentFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    String[] tabArray = new String[]{"Home", "Notifs", "News"};
    private int numberOfTabs;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.numberOfTabs = tabArray.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                StudentFragment studentFragment = new StudentFragment();
                return studentFragment;
            case  1:
                ListeNotifications notificationFragment = new ListeNotifications();
                return notificationFragment;
            case 2:
                NewsFragment settingFragment = new NewsFragment();
                return settingFragment;
                default:
                    return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
