package com.pinnacle.winwin.ui.withdrawalhistory.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.WithdrawalStatus;
import com.pinnacle.winwin.network.model.WithdrawalHistoryData;

import com.pinnacle.winwin.ui.withdrawalhistory.listener.WithdrawalHistoryListener;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WithdrawHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_WITHDRAW_HISTORY = 0;
    private static final int ITEM_TYPE_FOOTER = 1;

    private Context mContext;
    private ArrayList<WithdrawalHistoryData> withdrawalHistoryList;
    private WithdrawalHistoryListener withdrawalHistoryListener;
    public static boolean isEntireListLoaded = false;

    public WithdrawHistoryAdapter(Context mContext, ArrayList<WithdrawalHistoryData> withdrawalHistoryList) {
        this.mContext = mContext;
        this.withdrawalHistoryList = withdrawalHistoryList;
        withdrawalHistoryListener = (WithdrawalHistoryListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewTransactionId;
        AppCompatTextView textViewTransactionDate;
        AppCompatTextView textViewPoints;
        AppCompatTextView textViewTransactionStatus;
        AppCompatTextView textViewMessage;

        Button btnCancel;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textViewTransactionId = itemView.findViewById(R.id.textViewTransactionId);
            textViewTransactionDate = itemView.findViewById(R.id.textViewTransactionDate);
            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            textViewTransactionStatus = itemView.findViewById(R.id.textViewTransactionStatus);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);

            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnCancel.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            btnCancel.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (withdrawalHistoryListener != null) {
                        withdrawalHistoryListener.onClickedCancel(getAdapterPosition());
                    }
                }
            });
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBarFooter;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBarFooter = itemView.findViewById(R.id.progressBarFooter);
            // Change the default color of progress bar programmatically
            progressBarFooter.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_WITHDRAW_HISTORY:
                View view = LayoutInflater.from(mContext).inflate(R.layout.cell_transaction_history, parent, false);
                viewHolder = new ViewHolder(view);
                break;
            case ITEM_TYPE_FOOTER:
                View footerView = LayoutInflater.from(mContext).inflate(R.layout.cell_footer_progress, parent, false);
                viewHolder = new FooterViewHolder(footerView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_WITHDRAW_HISTORY:
                ViewHolder viewHolder = (ViewHolder) holder;

                WithdrawalHistoryData withdrawalHistoryData = withdrawalHistoryList.get(position);

                viewHolder.textViewTransactionId.setText("");
                viewHolder.textViewTransactionId.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_transaction_id),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewTransactionId.append(Utils.getCustomSpannableString(mContext, " " + withdrawalHistoryData.getCustPaymentId(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewTransactionDate.setText("");
                viewHolder.textViewTransactionDate.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_date_time),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewTransactionDate.append(Utils.getCustomSpannableString(mContext, " " + getTransactionDateString(withdrawalHistoryData),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewPoints.setText("");
                viewHolder.textViewPoints.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_amount),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewPoints.append(Utils.getCustomSpannableString(mContext, " " + withdrawalHistoryData.getInitiatedAmount(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewMessage.setText("");
                viewHolder.textViewMessage.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_message),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));

                if (withdrawalHistoryData.getStatus().equalsIgnoreCase(WithdrawalStatus.INITIATED.name())) {
                    viewHolder.btnCancel.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnCancel.setVisibility(View.GONE);
                }

                setTransactionStatus(viewHolder, withdrawalHistoryData);

                break;
            case ITEM_TYPE_FOOTER:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                if (!isEntireListLoaded && footerViewHolder.getAdapterPosition() >= withdrawalHistoryList.size()) {
                    footerViewHolder.progressBarFooter.setVisibility(View.VISIBLE);
                    if (withdrawalHistoryListener != null) {
                        withdrawalHistoryListener.onLoadMoreData();
                    }
                } else {
                    footerViewHolder.progressBarFooter.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (isEntireListLoaded) {
            return withdrawalHistoryList.size();
        } else {
            return withdrawalHistoryList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_WITHDRAW_HISTORY;
        }
    }

    private boolean isPositionFooter(int position) {
        return (position == withdrawalHistoryList.size());
    }

    public void updateData(ArrayList<WithdrawalHistoryData> withdrawalHistoryList) {
        if (withdrawalHistoryList != null) {
            this.withdrawalHistoryList = withdrawalHistoryList;
            notifyDataSetChanged();
        }
    }

    private String getTransactionDateString(WithdrawalHistoryData withdrawalHistoryData) {
        SimpleDateFormat simpleDateFormat = new
                SimpleDateFormat(DateUtils.UNIX_TIME_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = DateUtils.getDateFromString(withdrawalHistoryData.getCreatedAt(), simpleDateFormat);
        return DateUtils.getStringFormattedDate(date, DateUtils.CHAT_DATE_FORMAT);
    }

    private void setTransactionStatus(ViewHolder viewHolder, WithdrawalHistoryData withdrawalHistoryData) {
        viewHolder.textViewTransactionStatus.setText("");
        viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_status),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
        if (withdrawalHistoryData.getStatus().equals(WithdrawalStatus.INITIATED.name())) {
            viewHolder.textViewMessage.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.withdrawal_initiated_msg),
                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + withdrawalHistoryData.getStatus(),
                    mContext.getResources().getColor(R.color.colorInProgress), Utils.getTypeFaceBodoni72(mContext)));
        } else if (withdrawalHistoryData.getStatus().equals(WithdrawalStatus.SUCCESS.name())) {
            viewHolder.textViewMessage.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.withdrawal_success_msg),
                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + withdrawalHistoryData.getStatus(),
                    mContext.getResources().getColor(R.color.colorSuccess), Utils.getTypeFaceBodoni72(mContext)));
        } else {
            viewHolder.textViewMessage.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.withdrawal_cancelled_msg),
                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + withdrawalHistoryData.getStatus(),
                    mContext.getResources().getColor(R.color.colorFailed), Utils.getTypeFaceBodoni72(mContext)));
        }
    }
}
