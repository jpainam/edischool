package com.edischool.notification;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;

import com.edischool.R;

import com.edischool.SettingsActivity;
import com.edischool.pojo.Notifications;

import static android.app.Activity.RESULT_OK;


public class ListeNotifications extends Fragment {
    RecyclerView myrv;
    //   EditText seachnotification;
    String seachnotificatio;
    NotificationsAdapteur myAdapter;
    Context context;
    int lastId = 0;
    private final String ACTION_RECEIVE_NOTIFICATION = "com.edis.eschool_TARGET_NOTIFICATION";
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private static final int DELEITEM=104;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //    seachnotification = (EditText) getActivity().findViewById(R.id.seachnotification);
        myrv = (RecyclerView) getActivity().findViewById(R.id.recycleviewnotification);
        myrv.setLayoutManager(new LinearLayoutManager(context));
        myAdapter = new NotificationsAdapteur(context);
        myrv.setAdapter(myAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int i) {
                int position = target.getAdapterPosition();
                myAdapter.removeNotification(position);
            }
        });
        helper.attachToRecyclerView(myrv);
    /*    final EditText seachnotification = (EditText) getActivity().findViewById(R.id.seachnotification);
        seachnotification.setFocusable(false);
        seachnotification.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                seachnotification.setFocusableInTouchMode(true);

                return false;
            }
        });
        seachnotification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterNotification(s.toString());
            }
        });
*/
        initNotification();
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if(savedInstanceState!=null) {
            seachnotificatio=savedInstanceState.getString("seachnotificatio");
            /////////////////////////  seachnotification.setText(seachnotificatio);
            if(myAdapter == null){
                myAdapter = new NotificationsAdapteur(getContext());
            }
            myAdapter.notifyDataSetChanged();

        }else{
            Log.e("restoration", "ici null");
            //  initNotification();

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_liste_notifications, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LocalBroadcastManager.getInstance(container.getContext()).registerReceiver(mHandler,
                new IntentFilter(ACTION_RECEIVE_NOTIFICATION));
        context = container.getContext();
        return view;
    }








    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("seachnotificatio",seachnotificatio);

    }










    private void filterNotification(String texte) {
        myAdapter.serchNotifi(texte);
    }

    private void initNotification() {
        NotificationDao myDb = new NotificationDao(context);

        Cursor res = myDb.getAllNotification();
        while (res.moveToNext()) {
            addNotification(res, 100);
        }


    }

    public void addNotification(Cursor res, int n) {
        Notifications notifications;
        notifications = new Notifications();
        int idnotification = res.getInt(0);
        notifications.setIdnotification(idnotification);
        if (lastId < idnotification) {
            lastId = idnotification;
        }
        notifications.setTitre(res.getString(1));
        String s = res.getString(2);
        String upToNCharacters = s.substring(0, Math.min(s.length(), n));
        notifications.setMessage(upToNCharacters + " ...");
        notifications.setType(res.getString(3));
        notifications.setDate(res.getString(4));
        notifications.setLu(res.getInt(5));
        myAdapter.add(notifications);
    }





    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_RECEIVE_NOTIFICATION)) {
                NotificationDao dao = new NotificationDao(context);
                Cursor res = dao.getAllNewNotification(lastId);
                while (res.moveToNext()) {
                    addNotification(res, 100);
                }
            }

        }
    };




    /*public void back(){
        Intent detailNotifi= new Intent(context, MainActivity.class);
        startActivity(detailNotifi);
        overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
        finish();
    }
     @Override
     public void onBackPressed() {
         super.onBackPressed();
         back();
     }
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()){
             case android.R.id.home:
                 back();
                 break;
         }
         return super.onOptionsItemSelected(item);
     }*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notification_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.e("onQueryTextSubmit", newText);
                    filterNotification(newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.e("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DELEITEM&&resultCode==RESULT_OK){
            Notifications notif=(Notifications)data.getSerializableExtra("notifi");
            myAdapter.removeNotifications(notif);
        }
    }
}
