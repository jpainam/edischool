package com.edischool;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.edischool.pojo.Notifications;

public class DetailNotification extends AppCompatActivity {
    TextView message, title,date;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);
        mContext=this;
        Intent intent=this.getIntent();
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */

        final Notifications notifications = (Notifications)intent.getSerializableExtra("notifications");
        message=(TextView)findViewById(R.id.message);
        title=(TextView)findViewById(R.id.title);
        date=(TextView)findViewById(R.id.date);

        message.setText(notifications.getMessage());
        title.setText(notifications.getTitre());
        date.setText(notifications.getDate());
        FloatingActionButton delefloating =findViewById(R.id.deleflating);
        delefloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("notifi",notifications);
                setResult(RESULT_OK, returnIntent);
                //overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
        finish();
      }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //overridePendingTransition(android.R.anim.slide_out_right ,android.R.anim.slide_in_left);//transition simple
                finish();
                break;
         }
        return super.onOptionsItemSelected(item);
       }
}
