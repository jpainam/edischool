package com.edischool.notification;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.SettingsActivity;
import com.edischool.pojo.Notification;
import com.edischool.utils.Constante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;


public class ListeNotifications extends Fragment {
    private static final String TAG = "ListeNotification";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog dialog = null;

    RecyclerView myrv;
    String seachnotificatio;
    NotificationsAdapteur myAdapter;
    List<Notification> notificationList = new ArrayList<>();
    Context context;
    private final String ACTION_RECEIVE_NOTIFICATION = "com.edis.eschool_TARGET_NOTIFICATION";
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private static final int DELEITEM=104;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myrv = (RecyclerView) getActivity().findViewById(R.id.recycleviewnotification);
        myrv.setLayoutManager(new LinearLayoutManager(context));
        myAdapter = new NotificationsAdapteur(context, notificationList);
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
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if(savedInstanceState!=null) {
            seachnotificatio=savedInstanceState.getString("seachnotificatio");
            /////////////////////////  seachnotification.setText(seachnotificatio);
            if(myAdapter == null){
                myAdapter = new NotificationsAdapteur(getContext(), notificationList);
            }
            myAdapter.notifyDataSetChanged();

        }else{
            Log.e("restoration", "ici null");
        }
        if (dialog == null) {
            Log.e(TAG, "Instantiate dialog");
            dialog = new SpotsDialog.Builder()
                    .setContext(getContext()).setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_liste_notifications, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = container.getContext();
        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("seachnotificatio",seachnotificatio);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (dialog != null) {
            dialog.show();
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference ref = db.collection(Constante.NOTIFICATIONS_COLLECTION).document(user.getPhoneNumber())
                .collection(Constante.USER_NOTIFICATIONS_COLLECTION);
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.w(TAG, "Error getting documents.", e);
                    return;
                }
                notificationList.clear();
                for (DocumentSnapshot doc : snapshots){
                    Notification notif = doc.toObject(Notification.class);
                    notif.setNotificationId(doc.getId());
                    notificationList.add(notif);
                }
                myAdapter.notifyDataSetChanged();
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
    }

    private void filterNotification(String texte) {
        myAdapter.serchNotifi(texte);
    }


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
            Notification notif=(Notification)data.getSerializableExtra("notifi");
        }
    }
}
