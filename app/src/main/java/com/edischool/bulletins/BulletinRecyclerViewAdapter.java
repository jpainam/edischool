package com.edischool.bulletins;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.pojo.GradeBook;

import java.util.List;


public class BulletinRecyclerViewAdapter extends RecyclerView.Adapter<BulletinRecyclerViewAdapter.ViewHolder> {

    public BulletinRecyclerViewAdapter(Context context, List<GradeBook> gradeBookList){

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
