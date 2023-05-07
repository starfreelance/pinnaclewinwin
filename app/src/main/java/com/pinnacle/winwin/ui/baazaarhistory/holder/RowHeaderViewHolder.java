package com.pinnacle.winwin.ui.baazaarhistory.holder;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.baazaarhistory.model.RowHeader;

public class RowHeaderViewHolder extends AbstractViewHolder {

    private ConstraintLayout rowHeaderContainer;
    private AppCompatTextView textViewRowHeader;

    public RowHeaderViewHolder(View itemView) {
        super(itemView);

        textViewRowHeader = itemView.findViewById(R.id.textViewRowHeader);
    }

    public void setRowHeader(RowHeader rowHeader) {
        textViewRowHeader.setText(String.valueOf(rowHeader.getmData()));

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can remove them.

        // It is necessary to remeasure itself.
        /*rowHeaderContainer.getLayoutParams().width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        rowHeaderContainer.requestLayout();*/
    }
}
