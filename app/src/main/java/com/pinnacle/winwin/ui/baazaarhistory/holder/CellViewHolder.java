package com.pinnacle.winwin.ui.baazaarhistory.holder;

import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.baazaarhistory.model.Cell;

public class CellViewHolder extends AbstractViewHolder {

    private AppCompatTextView textViewBaazaarResult;
    private Cell cell;

    public CellViewHolder(View itemView) {
        super(itemView);

        textViewBaazaarResult = itemView.findViewById(R.id.textViewBaazaarResult);

    }

    public void setCell(Cell cell) {
        this.cell = cell;
        textViewBaazaarResult.setText(String.valueOf(cell.getmData()));
    }
}
