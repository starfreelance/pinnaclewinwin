package com.pinnacle.winwin.ui.baazaarhistory.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class CustomTableRowDateAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CustomTableCellDataAdapter.class.getSimpleName();
    /*private static final int TYPE_BAAZAAR_HISTORY = 0;
    private static final int TYPE_WALLET_TRANSACTION = 1;*/

    private Context mContext;
    private ArrayList<String> transactionDates;
    private int mTableType = -1;

    public CustomTableRowDateAdapter(Context mContext, ArrayList<T> dataList, int tableType) {
        this.mContext = mContext;
        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.get(0) instanceof String) {
                transactionDates = (ArrayList<String>) dataList;
                mTableType = tableType;
            }
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewRowHeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRowHeader = itemView.findViewById(R.id.textViewRowHeader);
            textViewRowHeader.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewRowHeader.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_row_header_layout, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        switch (mTableType) {
            case AppConstant.TABLE_TYPE_BAAZAAR_HISTORY:
            case AppConstant.TABLE_TYPE_WALLET_TRANSACTION:

                String date = transactionDates.get(position);

                /*if (date != null && !date.isEmpty()) {
                    Date transactionDate = DateUtils.getDateFromString(date, new SimpleDateFormat(DateUtils.UNIX_TIME_FORMAT, Locale.getDefault()));
                    String dateString = DateUtils.getStringFormattedDate(transactionDate, DateUtils.TRANSACTION_DATE_FORMAT);
                    LogUtils.e(TAG, transactionDate.toString());
                    LogUtils.e(TAG, dateString);
                    holder.textViewRowHeader.setText(dateString);
                }*/

                holder.textViewRowHeader.setText(date);

                break;
        }


    }

    @Override
    public int getItemCount() {
        switch (mTableType) {
            case AppConstant.TABLE_TYPE_BAAZAAR_HISTORY:
            case AppConstant.TABLE_TYPE_WALLET_TRANSACTION:
                return transactionDates.size();
        }

        return 0;
    }
}
