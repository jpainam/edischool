package com.edischool.timetable;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.edischool.pojo.Week;
import com.edischool.R;

import java.util.ArrayList;



public class WeekAdapter extends ArrayAdapter<Week> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<Week> weeklist;
    private Week week;
    private ListView mListView;

    private static class ViewHolder {
        TextView subject;
        TextView teacher;
        TextView time;
        TextView room;
        ImageView popup;
        CardView cardView;
    }

    public WeekAdapter(Activity activity, ListView listView, int resource, ArrayList<Week> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mResource = resource;
        weeklist = objects;
        mListView = listView;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String subject = (getItem(position)).getSubject();
        String teacher = (getItem(position)).getTeacher();
        String time_from = (getItem(position)).getFromTime();
        String time_to =(getItem(position)).getToTime();
        String room = (getItem(position)).getRoom();
        String color = getItem(position).getColor();

        week = new Week(subject, teacher, room, time_from, time_to, color);
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.subject = convertView.findViewById(R.id.subject);
            holder.teacher = convertView.findViewById(R.id.teacher);
            holder.time = convertView.findViewById(R.id.time);
            holder.room = convertView.findViewById(R.id.room);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            holder.cardView = convertView.findViewById(R.id.week_cardview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.subject.setText(week.getSubject());
        holder.teacher.setText(week.getTeacher());
        holder.time.setText(week.getRoom());
        holder.room.setText(week.getFromTime() + " - " + week.getToTime());
        if(week.getColor() != null) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(week.getColor()));
        }
        return convertView;
    }

    public ArrayList<Week> getWeekList() {
        return weeklist;
    }

    public Week getWeek() {
        return week;
    }
}
