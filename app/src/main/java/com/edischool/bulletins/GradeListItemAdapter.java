package com.edischool.bulletins;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.edischool.R;
import com.edischool.pojo.Grade;

import java.util.List;

public class GradeListItemAdapter extends ArrayAdapter<Grade> {
    Context context;
    private int resource;

    public GradeListItemAdapter(Context context, int resource, List<Grade> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            v = layoutInflater.inflate(this.resource, null);
        }
        Grade m = getItem(position);
        if(m != null){
            TextView tv_subject = (TextView) v.findViewById(R.id.subject);
            TextView tv_marks = (TextView) v.findViewById(R.id.marks);
            TextView tv_grade = (TextView) v.findViewById(R.id.grade);
            TextView tv_comment = (TextView) v.findViewById(R.id.comment);
            LinearLayout subjectColr = (LinearLayout) v.findViewById(R.id.subjectcolor);

            tv_subject.setText(m.getSubject());
            tv_marks.setText(m.getMark());
            tv_grade.setText(m.getGrade());
            tv_comment.setText(m.getComment());
            if (position % 2 == 0) {
                subjectColr.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                subjectColr.setBackgroundColor(context.getResources().getColor(R.color.row));
            }
        }
        return v;
    }

}
