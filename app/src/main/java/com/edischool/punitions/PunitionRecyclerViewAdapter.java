package com.edischool.punitions;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Punishment;

import java.util.List;

public class PunitionRecyclerViewAdapter extends RecyclerView.Adapter<PunitionRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Punishment> punishmentList;
    public PunitionRecyclerViewAdapter(Context context, List<Punishment> punishmentList){
        this.context = context;
        this.punishmentList = punishmentList;
    }

    @NonNull
    @Override
    public PunitionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.punishement_item, parent, false);
        return new PunitionRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PunitionRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = this.punishmentList.get(position);
        Punishment a = this.punishmentList.get(position);
        holder.description.setText(a.getDescription());
        holder.punishBy.setText("By: " + a.getBy());
        holder.punishmentDate.setText(a.getDate());
        holder.punishmentPoint.setText("Points: " + a.getPoint());
        holder.punishmentType.setText(a.getType());
        if(a.getType().equals("Exclusion")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#04826F"));
        }else if(a.getType().equals("Retenue")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#061877"));
        }else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#941125"));
        }
    }

    @Override
    public int getItemCount() {
        if(punishmentList != null) {
            return punishmentList.size();
        }
        return 0;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView description;
        public TextView punishBy;
        public TextView punishmentDate;
        public TextView punishmentType;
        public TextView punishmentPoint;
        public CardView cardView;
        private Punishment mItem;
        public ViewHolder(View v){
            super(v);
            description = v.findViewById(R.id.tvDescription);
            punishBy = v.findViewById(R.id.tvPunishBy);
            punishmentDate = v.findViewById(R.id.tvPunishmentDate);
            punishmentPoint = v.findViewById(R.id.tvPunishmentPoint);
            punishmentType = v.findViewById(R.id.tvPunishmentType);
            cardView = v.findViewById(R.id.card_view);
        }

        @Override
        public String toString() {
            return description.getText() + " '" + punishmentDate.getText();
        }
    }
}
