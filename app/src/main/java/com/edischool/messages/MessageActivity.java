package com.edischool.messages;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edischool.R;
import com.edischool.pojo.Message;
import com.edischool.utils.Constante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Message> messageList = new ArrayList<>();
    MessageRecyclerViewAdapter mAdapter;
    RecyclerView recyclerView;
    AlertDialog dialog = null;
    Animation animation;
    SwipeRefreshLayout swipeRefreshLayout;
    public boolean swipeEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView = findViewById(R.id.recyclerView);
        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.loading_message))
                    .build();
        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("Messages");
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new MessageRecyclerViewAdapter(MessageActivity.this, messageList);

        this.animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        recyclerView.setLayoutAnimation(new LayoutAnimationController(this.animation));
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeEnabled = true;
                readData();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void readData(){
        if(dialog != null && !swipeEnabled){
            dialog.show();
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(Constante.MESSAGES_COLLECTION).whereArrayContains("receivers", user.getPhoneNumber()).addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.e(TAG, "Error fetching messages", e);
                            return;
                        }
                        if(messageList == null){
                            recreate();
                            return;
                        }
                        messageList.clear();

                        for(DocumentSnapshot doc: snapshots){
                            Message msg = doc.toObject(Message.class);
                            messageList.add(msg);
                        }
                        mAdapter.notifyDataSetChanged();
                        if(dialog != null && !swipeEnabled){
                            dialog.dismiss();
                        }else{
                            swipeEnabled = false;
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(dialog != null){
            dialog.show();
        }
        readData();
    }


}
