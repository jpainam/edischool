package com.edischool.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Mark;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private final List<Mark> mValues;

    public NotesRecyclerViewAdapter(List<Mark> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Mark n = mValues.get(position);
        holder.tvMatiere.setText(n.getSubject());
        holder.tvNote.setText(n.getMark() + " " + n.getComment());
        holder.tvSequence.setText(n.getSequence());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvMatiere;
        public final TextView tvNote;
        public final TextView tvSequence;
        public Mark mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvMatiere = (TextView) view.findViewById(R.id.tvMatiere);
            tvNote = (TextView) view.findViewById(R.id.tvNote);
            tvSequence = (TextView) view.findViewById(R.id.tvSequence);
        }

        @Override
        public String toString() {
            return tvMatiere.getText() + " '" + tvNote.getText() + "'" + tvSequence.getText();
        }
    }
}
