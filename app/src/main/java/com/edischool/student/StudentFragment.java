package com.edischool.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edischool.LoginActivity;
import com.edischool.R;
import com.edischool.SettingsActivity;
import com.edischool.pojo.Publicite;
import com.edischool.pojo.Student;
import com.edischool.pojo.User;
import com.edischool.publicite.PubAdapteur;
import com.edischool.utils.Constante;
import com.edischool.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class StudentFragment extends Fragment {
    private static final String TAG = "StudentFragment";

    AlertDialog dialog = null;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    public List<Student> studentList = new ArrayList<>();
    public String studentListJson = new String();
    public StudentRecyclerViewAdapter studentRecyclerViewAdapter = null;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    boolean swipeEnabled = false;
    private String phoneNumber;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Pub
     */
    public PubAdapteur pubAdapteur = null;
    public String publicitesJson = new String();
    public List<Publicite> publiciteList = new ArrayList<>();
    @BindView(R.id.pub_list)
    RecyclerView recyclerViewPub;
    private LinearLayoutManager PLinearLayout;
    Timer timer = null;

    public StudentFragment() {
    }

    @SuppressWarnings("unused")
    public static StudentFragment newInstance(int columnCount) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            studentListJson = savedInstanceState.getString("studentList");
            Type type = new TypeToken<List<Student>>() {
            }.getType();
            studentList = new Gson().fromJson(studentListJson, type);
            if (studentRecyclerViewAdapter == null) {
                studentRecyclerViewAdapter = new StudentRecyclerViewAdapter(getContext(),
                        studentList, mListener);
            }
            studentRecyclerViewAdapter.notifyDataSetChanged();
        }
        if (dialog == null) {
            Log.e(TAG, "Instantiate dialog");
            dialog = new SpotsDialog.Builder()
                    .setContext(getContext()).setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        SharedPreferences pref = getContext().getSharedPreferences(
                getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        Log.w(TAG, "phone number " + phoneNumber);
        if (phoneNumber == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        ButterKnife.bind(this, view);
        final Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (studentRecyclerViewAdapter == null) {
            studentRecyclerViewAdapter = new StudentRecyclerViewAdapter(context,
                    studentList, mListener);
        }
        recyclerView.setAdapter(studentRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeEnabled = true;
                readData(phoneNumber);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        PLinearLayout = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPub.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    cursor=PLinearLayout.findLastVisibleItemPosition();
                    //  PLinearLayout.scrollToPosition(cursor);
                }
            }
        });
        recyclerViewPub.setLayoutManager(PLinearLayout);
        pubAdapteur = new PubAdapteur(getContext(), publiciteList);
        recyclerViewPub.setAdapter(pubAdapteur);
        pubAdapteur.notifyDataSetChanged();

        return view;
    }

    private void readPub(String phoneNumber) {
        Publicite pub = new Publicite();
        publiciteList.clear();
        pub.setId(1);
        pub.setText("pub1");
        pub.setImage("image i");
        pub.setType("simple");
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);
        publiciteList.add(pub);

        Log.e(TAG, "ajoute");

        pubAdapteur.notifyDataSetChanged();
        Gson gson = new Gson();
        publicitesJson = gson.toJson(publiciteList);

    }

    private void readData(String phoneNumber) {
        DocumentReference userDoc = db.collection(Constante.USERS_COLLECTION).document(phoneNumber);

        if (!swipeEnabled) {
            dialog.show();
        }
        if (studentList == null) {
            studentList = new ArrayList<>();
            getActivity().recreate();
        }
        userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Error getting documents.", e);
                    return;
                }
                User user = snapshot != null ? snapshot.toObject(User.class) : null;
                if (null != user) {
                    Log.d(TAG, snapshot.getId() + " => " + snapshot.getData());
                    studentList.clear();
                    if (user.getStudents() != null) {
                        studentList.addAll(user.getStudents());
                        studentRecyclerViewAdapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        studentListJson = gson.toJson(studentList);
                    }
                }

                if (swipeEnabled) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), getString(R.string.refreshed_message), Toast.LENGTH_SHORT).show();
                    swipeEnabled = false;
                } else {
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Enter onview created");
        if( swipeEnabled && !Utils.isConnected(getContext())) {
            Snackbar.make(swipeRefreshLayout, "You are not connected. " +
                    "Check your Internet connection", Snackbar.LENGTH_LONG).show();
            return;
        }
        readData(phoneNumber);
        //readPub(phoneNumber);
        //int itemCountOfAdvertisement = recyclerViewPub.getAdapter().getItemCount();
        //timer = new Timer();
        //timer.schedule(new AdvertisementTimerTask(itemCountOfAdvertisement),
        //        0, 2*1000);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(timer != null){
            timer.cancel();
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "Attach Method - Register receiver");
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.student_menu, menu);
        MenuItem refreshmenu = menu.findItem(R.id.refresh_menu_item);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu_item:
                readData(phoneNumber);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("studentList", studentListJson);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Student item);
    }

    int cursor = 0;
    class AdvertisementTimerTask extends TimerTask {
        final int count;

        public AdvertisementTimerTask(int count) {
            this.count=count;
        }

        @Override
        public void run() {
            if(cursor<count){
                getActivity().runOnUiThread(() -> {
                    recyclerViewPub.scrollToPosition(cursor);
                    cursor++;
                });
            }
            if(cursor>count){
                cursor=0;
            }
        }
    }
}
