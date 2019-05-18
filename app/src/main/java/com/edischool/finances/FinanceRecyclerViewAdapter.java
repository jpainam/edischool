package com.edischool.finances;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Finance;

import java.util.List;

public class FinanceRecyclerViewAdapter extends RecyclerView.Adapter<FinanceRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Finance> financeList;
    public FinanceRecyclerViewAdapter(Context context, List<Finance> financeList){
        this.context = context;
        this.financeList = financeList;
    }

    @NonNull
    @Override
    public FinanceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finance_item, parent, false);
        return new FinanceRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = financeList.get(position);
        Finance a = financeList.get(position);
        holder.description.setText(a.getDescription());
        holder.amount.setText(a.getAmount());
        holder.depositTime.setText(a.getDepositTime());
    }

    @Override
    public int getItemCount() {
        if(financeList != null) {
            return financeList.size();
        }
        return 0;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView description;
        public TextView amount;
        public TextView depositTime;
        private Finance mItem;
        public ViewHolder(View v){
            super(v);
            description = v.findViewById(R.id.description);
            amount = v.findViewById(R.id.amount);
            depositTime = v.findViewById(R.id.depositTime);
        }

        @Override
        public String toString() {
            return description.getText() + " '" + amount.getText();
        }
    }
}
