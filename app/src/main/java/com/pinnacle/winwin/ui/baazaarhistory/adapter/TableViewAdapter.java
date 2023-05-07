package com.pinnacle.winwin.ui.baazaarhistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.baazaarhistory.holder.CellViewHolder;
import com.pinnacle.winwin.ui.baazaarhistory.holder.ColumnHeaderViewHolder;
import com.pinnacle.winwin.ui.baazaarhistory.holder.RowHeaderViewHolder;
import com.pinnacle.winwin.ui.baazaarhistory.model.Cell;
import com.pinnacle.winwin.ui.baazaarhistory.model.ColumnHeader;
import com.pinnacle.winwin.ui.baazaarhistory.model.RowHeader;

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    public TableViewAdapter(Context context) {
        super(context);
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_layout_baazaar_history,
                parent, false);
        return new CellViewHolder(view);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {

        Cell cell = (Cell) cellItemModel;

        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.setCell(cell);
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_column_header_baazaar_history,
                parent, false);
        return new ColumnHeaderViewHolder(view);
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {

        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeader);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_row_header_layout, parent, false);
        return new RowHeaderViewHolder(view);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {

        RowHeader rowHeader = (RowHeader) rowHeaderItemModel;

        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.setRowHeader(rowHeader);
    }

    @Override
    public View onCreateCornerView() {
        // Get Corner xml layout
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_corner_row_baazaar_history, null);
        return view;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }
}
