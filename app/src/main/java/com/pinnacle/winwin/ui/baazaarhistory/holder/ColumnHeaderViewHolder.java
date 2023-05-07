package com.pinnacle.winwin.ui.baazaarhistory.holder;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.baazaarhistory.model.ColumnHeader;

public class ColumnHeaderViewHolder extends AbstractViewHolder {

    private ConstraintLayout columnHeaderContainer;
    private AppCompatTextView textViewColumnHeader;

    public ColumnHeaderViewHolder(View itemView) {
        super(itemView);

        columnHeaderContainer = itemView.findViewById(R.id.columnHeaderContainer);

        textViewColumnHeader = itemView.findViewById(R.id.textViewColumnHeader);
    }

    public void setColumnHeader(ColumnHeader columnHeader) {
        textViewColumnHeader.setText(String.valueOf(columnHeader.getmData()));

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can remove them.

        // It is necessary to remeasure itself.
        /*columnHeaderContainer.getLayoutParams().width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        columnHeaderContainer.requestLayout();*/
    }
}
