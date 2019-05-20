package com.edischool.exams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Exam;

import java.util.List;

public class ExamRecyclerViewAdapter extends RecyclerView.Adapter<ExamRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Exam> examList;
    public ExamRecyclerViewAdapter(Context context, List<Exam> exams){
        this.context = context;
        this.examList = exams;
    }

    @NonNull
    @Override
    public ExamRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_item, parent, false);
        return new ExamRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = examList.get(position);
        Exam a = examList.get(position);
        holder.exam_title.setText(a.getSubject());
        holder.exam_comment.setText(a.getTeacher());
        holder.exam_date.setText(a.getDate());
    }

    @Override
    public int getItemCount() {
        if(examList != null) {
            return examList.size();
        }
        return 0;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exam_title;
        public TextView exam_comment;
        public TextView exam_date;
        private Exam mItem;
        public ViewHolder(View v){
            super(v);
            exam_title = v.findViewById(R.id.exam_title);
            exam_comment = v.findViewById(R.id.exam_comment);
            exam_date = v.findViewById(R.id.exam_date);
        }

        @Override
        public String toString() {
            return exam_title.getText() + " '" + exam_comment.getText();
        }
    }
}
