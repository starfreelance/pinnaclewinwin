package com.pinnacle.winwin.ui.rechargehistory.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.RechargeHistoryData;
import com.pinnacle.winwin.ui.rechargehistory.listener.RechargeHistoryListener;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RechargeHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_RECHARGE_HISTORY = 0;
    private static final int ITEM_TYPE_FOOTER = 1;

    private Context mContext;
    private ArrayList<RechargeHistoryData> rechargeHistoryList;
    private RechargeHistoryListener rechargeHistoryListener;
    public static boolean isEntireListLoaded = false;

    public RechargeHistoryAdapter(Context mContext, ArrayList<RechargeHistoryData> rechargeHistoryList) {
        this.mContext = mContext;
        this.rechargeHistoryList = rechargeHistoryList;
        rechargeHistoryListener = (RechargeHistoryListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewTransactionId;
        AppCompatTextView textViewTransactionDate;
        AppCompatTextView textViewPoints;
        AppCompatTextView textViewTransactionStatus;
        AppCompatTextView textViewMessage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textViewTransactionId = itemView.findViewById(R.id.textViewTransactionId);
            textViewTransactionDate = itemView.findViewById(R.id.textViewTransactionDate);
            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            textViewTransactionStatus = itemView.findViewById(R.id.textViewTransactionStatus);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);

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
            case ITEM_TYPE_RECHARGE_HISTORY:
                View view = LayoutInflater.from(mContext).inflate(R.layout.cell_transaction_history, parent, false);
                viewHolder = new ViewHolder(view);
                break;
//            case ITEM_TYPE_FOOTER:
//                View footerView = LayoutInflater.from(mContext).inflate(R.layout.cell_footer_progress, parent, false);
//                viewHolder = new FooterViewHolder(footerView);
//                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_TYPE_RECHARGE_HISTORY:
                ViewHolder viewHolder = (ViewHolder) holder;

                RechargeHistoryData rechargeHistoryData = rechargeHistoryList.get(position);

                viewHolder.textViewTransactionId.setText("");
                viewHolder.textViewTransactionId.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bill_id),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewTransactionId.append(Utils.getCustomSpannableString(mContext, " " + rechargeHistoryData.getBillId(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewTransactionId.setVisibility(View.GONE);

                viewHolder.textViewTransactionDate.setText("");
                viewHolder.textViewTransactionDate.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_date_time),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewTransactionDate.append(Utils.getCustomSpannableString(mContext, " " + getTransactionDateString(rechargeHistoryData),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewPoints.setText("");
                viewHolder.textViewPoints.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_amount),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewPoints.append(Utils.getCustomSpannableString(mContext, " " + rechargeHistoryData.getAmountPoint(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewMessage.setText("");
                viewHolder.textViewMessage.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_message),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));

                setTransactionStatus(viewHolder, rechargeHistoryData);

                break;
//            case ITEM_TYPE_FOOTER:
//                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
//                if (!isEntireListLoaded && footerViewHolder.getAdapterPosition() >= rechargeHistoryList.size()) {
//                    footerViewHolder.progressBarFooter.setVisibility(View.VISIBLE);
//                    if (rechargeHistoryListener != null) {
//                        rechargeHistoryListener.onLoadMoreData();
//                    }
//                } else {
//                    footerViewHolder.progressBarFooter.setVisibility(View.GONE);
//                }
//                break;
        }
    }

    @Override
    public int getItemCount() {
//        if (isEntireListLoaded) {
            return rechargeHistoryList.size();
//        } else {
//            return rechargeHistoryList.size() + 1;
//        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (isPositionFooter(position)) {
//            return ITEM_TYPE_FOOTER;
//        } else {
            return ITEM_TYPE_RECHARGE_HISTORY;
//        }
    }

    private boolean isPositionFooter(int position) {
        return (position == rechargeHistoryList.size());
    }

    public void updateData(ArrayList<RechargeHistoryData> rechargeHistoryList) {
        if (rechargeHistoryList != null) {
            this.rechargeHistoryList = rechargeHistoryList;
            notifyDataSetChanged();
        }
    }

    private String getTransactionDateString(RechargeHistoryData rechargeHistoryData) {
        SimpleDateFormat simpleDateFormat = new
                SimpleDateFormat(DateUtils.DATE_FORMAT_24_hrs, Locale.getDefault());
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = DateUtils.getDateFromString(rechargeHistoryData.getDateTime(), simpleDateFormat);
        return DateUtils.getStringFormattedDate(date, DateUtils.CHAT_DATE_FORMAT);
    }

    private void setTransactionStatus(ViewHolder viewHolder, RechargeHistoryData rechargeHistoryData) {
        viewHolder.textViewTransactionStatus.setText("");
        viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_status),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
//        if (rechargeHistoryData.getStatus().equals(RechargeStatus.INPROGRESS.name())) {
//            viewHolder.textViewMessage.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.recharge_inprogress_msg),
//                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
//            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + rechargeHistoryData.getStatus(),
//                    mContext.getResources().getColor(R.color.colorInProgress), Utils.getTypeFaceBodoni72(mContext)));
//        } else if (rechargeHistoryData.getStatus().equals(RechargeStatus.SUCCESS.name())) {
            viewHolder.textViewMessage.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.recharge_success_msg),
                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
//            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + rechargeHistoryData.getStatus(),
//                    mContext.getResources().getColor(R.color.colorSuccess), Utils.getTypeFaceBodoni72(mContext)));
            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + "success",
                    mContext.getResources().getColor(R.color.colorSuccess), Utils.getTypeFaceBodoni72(mContext)));
//        } else {
//            viewHolder.textViewMessage.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.recharge_failed_msg),
//                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
//            viewHolder.textViewTransactionStatus.append(Utils.getCustomSpannableString(mContext, " " + rechargeHistoryData.getStatus(),
//                    mContext.getResources().getColor(R.color.colorFailed), Utils.getTypeFaceBodoni72(mContext)));
//        }
    }
}
