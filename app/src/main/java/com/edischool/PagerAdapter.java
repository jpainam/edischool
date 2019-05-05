package com.edischool;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    String[] tabArray = new String[]{"Elèves", "Notifications", "Settings"};
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
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 2:
                SettingFragment settingFragment = new SettingFragment();
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
