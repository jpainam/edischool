package com.edischool.emplois;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Emploi;


import java.util.List;

public class EmploiRecyclerViewAdapter  extends RecyclerView.Adapter<EmploiRecyclerViewAdapter.ViewHolder>  {

    private List<Emploi> emploiList;
    Context context;

    public EmploiRecyclerViewAdapter(List<Emploi> items, Context context) {
        this.context = context;
        emploiList = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emploi_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = emploiList.get(position);
        Emploi n = emploiList.get(position);
        holder.tvMatiere.setText(n.getMatiere());
    }

    @Override
    public int getItemCount() {
        return emploiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvMatiere;
        public Emploi mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvMatiere = view.findViewById(R.id.tvMatiere);

        }

        @Override
        public String toString() {
            return tvMatiere.getText() + "";
        }
    }
}
