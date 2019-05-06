package com.edischool.notes;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.edischool.R;
import com.edischool.pojo.Note;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private final List<Note> mValues;
    private final NotesActivityFragment.OnListFragmentInteractionListener mListener;

    public NotesRecyclerViewAdapter(List<Note> items, NotesActivityFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Note n = mValues.get(position);
        holder.tvMatiere.setText(n.getMatiere());
        holder.tvNote.setText(n.getNote() + " " + n.getObservation());
        holder.tvSequence.setText(n.getSequence());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
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
        public Note mItem;

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
