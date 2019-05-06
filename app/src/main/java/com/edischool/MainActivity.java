package com.edischool;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.edischool.news.NewsFragment;
import com.edischool.pojo.Student;
import com.edischool.sql.DatabaseHelper;
import com.edischool.student.StudentFragment;
import com.edischool.utils.Constante;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.TableLayout;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, StudentFragment.OnListFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener {


    private static final String TAG = "MainActivity";
    Account mAccount;
    DatabaseHelper databaseHelper;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        //if (!pref.contains(getString(R.string.visite))) {
        // TODO : Open the tuto page
        //}else
        if (!pref.contains(getString(R.string.phone_number))) {
            // User not authenticated
            Log.i(TAG, "Start Login Activity - First time run");
            startLoginActivity();
        }else {
            Fresco.initialize(this);
            /**
             * First run, force manual sync
             */
            if (!pref.contains(getString(R.string.last_time_sync))) {
                mAccount = CreateSyncAccount(this);
            }
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            final TabLayout tabLayout = findViewById(R.id.tablayout);
            //tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
            //tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
            //tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

            viewPager = findViewById(R.id.viewpager);

            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });
            // Use {@link #addOnPageChangeListener(OnPageChangeListener)}
            viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            updateTokenAsync();
        }
    }

    private void updateTokenAsync(){
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                String url = getString(R.string.update_token_url);
                SharedPreferences pref = getApplicationContext().getSharedPreferences(
                        getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
                String phone = pref.getString(getString(R.string.phone_number), null);
                String token = pref.getString(getString(R.string.firebase_token), null);

                if(phone != null && token != null){
                    OkHttpClient client =new OkHttpClient();
                    Log.e(TAG, url);
                    RequestBody body =new FormBody.Builder()
                            .add("phone_number", phone)
                            .add("token", token)
                            .build();
                    Request newReq=new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    try {
                        Response response = client.newCall(newReq).execute();
                        String jsonData = response.body().string();
                        return true;
                    }catch (Exception ex){
                        Log.e(TAG, ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(final Boolean sucess) {
                super.onPostExecute(sucess);
                if(sucess) {
                    Log.i(TAG, "Update token in background successful");
                }else{
                    Log.i(TAG, "Update token in background failed");
                }
            }
        };
        asyncTask.execute();
    }

    /**
     * Create a new edis account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                Constante.ACCOUNT, Constante.ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        ContentResolver.setSyncAutomatically(newAccount, Constante.AUTHORITY, true);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            if(ContentResolver.getIsSyncable(newAccount, Constante.AUTHORITY) == 0) {
                ContentResolver.setIsSyncable(newAccount, Constante.AUTHORITY, 1);
            }
            ContentResolver.setSyncAutomatically(newAccount,
                    Constante.AUTHORITY, false);
            ContentResolver.addPeriodicSync(newAccount, Constante.AUTHORITY,
                    new Bundle(), 86400 ); // 24hours = 86 400 seconds
            Log.i(TAG,
                    "Account " + Constante.ACCOUNT + " " + Constante.ACCOUNT_TYPE + " created");
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.i(TAG, "Account already exists");
            return newAccount;
        }
    }
    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        active.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_notifs) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_news) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "Our shearing message goes here";
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(intent, "Choose sharing method"));
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "Your help message goes here";
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(intent, "Choose message method"));
        }else if(id == R.id.nav_settings){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences pref = getApplicationContext().getSharedPreferences(
                    getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
            pref.edit().remove(getString(R.string.phone_number)).apply();
            startLoginActivity();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Student item) {
        Log.i(TAG, item.getFirstName() + " " + item.getLastName());
        Intent intent = new Intent(getApplicationContext(), InnerMenuActivity.class);
        intent.putExtra("student", item);
        startActivity(intent);
    }
}
