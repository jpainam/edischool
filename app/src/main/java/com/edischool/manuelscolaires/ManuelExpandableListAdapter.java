package com.edischool.manuelscolaires;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.edischool.R;
import com.edischool.pojo.Book;

import java.util.HashMap;
import java.util.List;

public class ManuelExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> subjectList;
    private HashMap<String, List<Book>> manualList;

    public ManuelExpandableListAdapter(Context context, List<String> subjectList, HashMap<String, List<Book>> manualList){
        this.context = context;
        this.subjectList = subjectList;
        this.manualList = manualList;
    }



    @Override
    public int getGroupCount() {
        if(this.subjectList != null) {
            return this.subjectList.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.manualList.get(this.subjectList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.subjectList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.manualList.get(this.subjectList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String)getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.manual_parent_item, null);
        }
        TextView tvParent = convertView.findViewById(R.id.tvParent);
        tvParent.setTypeface(null, Typeface.BOLD);
        tvParent.setText(groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Book m = (Book)getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.manual_child_item, null);
        }
        TextView tvManual = convertView.findViewById(R.id.tvManuel);
        TextView tvAuteurs = convertView.findViewById(R.id.tvAuteurs);
        TextView tvEditeurs = convertView.findViewById(R.id.tvEditeurs);
        TextView tvEdition = convertView.findViewById(R.id.tvEdition);
        TextView tvPrix = convertView.findViewById(R.id.tvPrix);
        tvManual.setText(m.getTitle());
        tvAuteurs.setText(m.getAuthors());
        tvEditeurs.setText(m.getEditors());
        tvPrix.setText(m.getPrice());
        tvEdition.setText(m.getEdition());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
