package com.edischool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.edischool.sql.DatabaseHelper;
import com.edischool.utils.ActivityUtils;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference preference = findPreference(getString(R.string.delete_account));
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.i(TAG,"Delete Account clicked");
                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(
                            getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
                    pref.edit().clear().apply();
                    PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().clear().apply();
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
                    try {
                        databaseHelper.close();
                    }catch (Exception ex){
                        //In case the database is already closed
                    }
                    getActivity().getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            });

            Preference aboutPref = findPreference("about_preference");
            aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ActivityUtils.showAboutDialog(getActivity());
                    return true;
                }
            });
            SwitchPreferenceCompat aboutPreference = (SwitchPreferenceCompat)findPreference(getString(R.string.night_mode));

            if(aboutPreference != null) {
               aboutPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                   @Override
                   public boolean onPreferenceChange(Preference preference, Object newValue) {
                       boolean switched = ((SwitchPreferenceCompat) preference).isChecked();
                       boolean set_night_mode = !switched;
                       if(set_night_mode){
                           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                       }else{
                           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                       }
                       getActivity().recreate();
                       Log.i(TAG, switched + "");
                       return true;
                   }
               });
            }
        }
    }

}