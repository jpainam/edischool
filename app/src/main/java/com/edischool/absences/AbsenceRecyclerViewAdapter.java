package com.edischool.absences;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.edischool.R;
import com.edischool.pojo.Absence;

import java.util.List;

public class AbsenceRecyclerViewAdapter extends RecyclerView.Adapter<AbsenceRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Absence> absenceList;
    public AbsenceRecyclerViewAdapter(Context context, List<Absence> absenceList){
        this.context = context;
        this.absenceList = absenceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.absence_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = absenceList.get(position);
        Absence a = absenceList.get(position);
        holder.tvAbsence.setText(a.getStartHour() + " - " + a.getEndHour() + " Heure(s)");
        holder.tvJour.setText(a.getDay());
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvAbsence;
        public TextView tvJour;
        public Absence mItem;
        public ViewHolder(View v){
            super(v);
            tvAbsence = v.findViewById(R.id.tvAbsence);
            tvJour = v.findViewById(R.id.tvJour);
        }

        @Override
        public String toString() {
            return tvAbsence.getText() + " '" + tvJour.getText();
        }
    }
}
