package com.edischool.emplois;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edischool.pojo.Emploi;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class EmploiTableDataAdapter extends TableDataAdapter<Emploi> {
    Context context;
    List<Emploi> data;
    public EmploiTableDataAdapter(Context context, List<Emploi> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Emploi emploi = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = new TextView(context);
                ((TextView) renderedView).setText(emploi.getMatiere());
                break;
        }

        return renderedView;
    }

}