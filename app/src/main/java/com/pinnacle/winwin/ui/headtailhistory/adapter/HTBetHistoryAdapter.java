package com.pinnacle.winwin.ui.headtailhistory.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.HTBetHistoryData;
import com.pinnacle.winwin.ui.headtailhistory.listener.HTBetHistoryListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class HTBetHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_HT_BET_HISTORY = 0;
    private static final int ITEM_TYPE_FOOTER = 1;

    private Context mContext;
    private ArrayList<HTBetHistoryData> htBetHistoryList;
    private HTBetHistoryListener htBetHistoryListener;
    public static boolean isEntireListLoaded = false;

    public HTBetHistoryAdapter(Context mContext, ArrayList<HTBetHistoryData> htBetHistoryList) {
        this.mContext = mContext;
        this.htBetHistoryList = htBetHistoryList;
        htBetHistoryListener = (HTBetHistoryListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout containerLayout;
        AppCompatTextView textViewBetId;
        AppCompatTextView textViewBet;
        AppCompatTextView textViewBetDate;
        AppCompatTextView textViewBetResult;
        AppCompatTextView textViewBetPoints;
        AppCompatTextView textViewBetTiming;
        AppCompatTextView textViewWinningAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.containerLayout);

            textViewBetId = itemView.findViewById(R.id.textViewBetId);

            textViewBet = itemView.findViewById(R.id.textViewBet);

            textViewBetDate = itemView.findViewById(R.id.textViewBetDate);

            textViewBetResult = itemView.findViewById(R.id.textViewBetResult);

            textViewBetPoints = itemView.findViewById(R.id.textViewBetPoints);

            textViewBetTiming = itemView.findViewById(R.id.textViewBetTiming);

            textViewWinningAmount = itemView.findViewById(R.id.textViewWinningAmount);
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
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_HT_BET_HISTORY:
                View view = LayoutInflater.from(mContext).inflate(R.layout.cell_ht_history, parent, false);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_TYPE_HT_BET_HISTORY:
                ViewHolder viewHolder = (ViewHolder) holder;

                HTBetHistoryData htBetHistoryData = htBetHistoryList.get(position);

                viewHolder.textViewBetId.setText("");
                viewHolder.textViewBetId.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_id),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewBetId.append(Utils.getCustomSpannableString(mContext, " " + htBetHistoryData.getBetId(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewBet.setText("");
                viewHolder.textViewBet.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_type),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewBet.append(Utils.getCustomSpannableString(mContext, " " + htBetHistoryData.getBetType(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewBetDate.setText("");
                viewHolder.textViewBetDate.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_date),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewBetDate.append(Utils.getCustomSpannableString(mContext, " " + htBetHistoryData.getBookingTime(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewBetResult.setText("");
                viewHolder.textViewBetResult.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_result),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                if (htBetHistoryData.getResult() != null &&
                        !htBetHistoryData.getResult().isEmpty()) {
                    viewHolder.textViewBetResult.append(Utils.getCustomSpannableString(mContext, " "  + htBetHistoryData.getResult(),
                            mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
                } else {
                    viewHolder.textViewBetResult.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.final_number_empty),
                            mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
                }
                viewHolder.textViewBetPoints.setText("");
                viewHolder.textViewBetPoints.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_points),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewBetPoints.append(Utils.getCustomSpannableString(mContext, " " + htBetHistoryData.getTotalAmount(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                viewHolder.textViewBetTiming.setText("");
                viewHolder.textViewBetTiming.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_timing),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                viewHolder.textViewBetTiming.append(Utils.getCustomSpannableString(mContext, " " + htBetHistoryData.getBookingTime(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                if (htBetHistoryData.getWinner()) {
                    viewHolder.textViewWinningAmount.setVisibility(View.VISIBLE);
                    viewHolder.textViewWinningAmount.setText("");
                    viewHolder.textViewWinningAmount.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_you_won),
                            mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                    viewHolder.textViewWinningAmount.append(Utils.getCustomSpannableString(mContext, " " + htBetHistoryData.getWinningAmount(),
                            mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
                } else {
                    viewHolder.textViewWinningAmount.setVisibility(View.INVISIBLE);
                }

                break;
            case ITEM_TYPE_FOOTER:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                if (!isEntireListLoaded && footerViewHolder.getAdapterPosition() >= htBetHistoryList.size()) {
                    footerViewHolder.progressBarFooter.setVisibility(View.VISIBLE);
                    if (htBetHistoryListener != null) {
                        htBetHistoryListener.onLoadMoreData();
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
            return htBetHistoryList.size();
        } else {
            return htBetHistoryList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_HT_BET_HISTORY;
        }
    }

    private boolean isPositionFooter(int position) {
        return (position == htBetHistoryList.size());
    }

    public void updateData(ArrayList<HTBetHistoryData> htBetHistoryList) {
        if (htBetHistoryList != null) {
            this.htBetHistoryList = htBetHistoryList;
            notifyDataSetChanged();
        }
    }
}
