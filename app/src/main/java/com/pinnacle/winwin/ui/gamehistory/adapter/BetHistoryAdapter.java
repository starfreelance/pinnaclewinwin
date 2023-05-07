package com.pinnacle.winwin.ui.gamehistory.adapter;

import android.content.Context;
import android.graphics.PorterDuff;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.network.model.BetHistoryData;
import com.pinnacle.winwin.ui.gamehistory.listener.BetHistoryListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class BetHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_BET_HISTORY = 0;
    private static final int ITEM_TYPE_FOOTER = 1;

    private Context mContext;
    private ArrayList<BetHistoryData> betHistoryList;
    private BetHistoryListener betHistoryListener;
    public static boolean isEntireListLoaded = false;

    public BetHistoryAdapter(Context mContext, ArrayList<BetHistoryData> betHistoryList) {
        this.mContext = mContext;
        this.betHistoryList = betHistoryList;
        betHistoryListener = (BetHistoryListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        /*AppCompatTextView textViewGameName;
        AppCompatTextView textViewBaazaarName;
        AppCompatTextView textViewGameDate;
        AppCompatTextView textViewBetNo;
        AppCompatTextView textViewAmount;
        AppCompatTextView textViewBaazaarResult;
        AppCompatTextView textViewWinningAmount;
        Button btnClaim;*/

        ConstraintLayout containerLayout;
        AppCompatTextView textViewBaazaarName;
        AppCompatTextView textViewBetNo;
        AppCompatTextView textViewBetDate;
        AppCompatTextView textViewGameName;
        AppCompatTextView textViewBetPoints;
        AppCompatTextView textViewBetTiming;
        AppCompatTextView textViewWinningAmount;
        Button btnClaim;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            /*textViewGameName = itemView.findViewById(R.id.textViewGameName);
            textViewGameName.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewGameName.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            textViewBaazaarName = itemView.findViewById(R.id.textViewBaazaarName);

            textViewGameDate = itemView.findViewById(R.id.textViewGameDate);

            textViewBetNo = itemView.findViewById(R.id.textViewBetNo);

            textViewAmount = itemView.findViewById(R.id.textViewAmount);

            textViewBaazaarResult = itemView.findViewById(R.id.textViewBaazaarResult);

            textViewWinningAmount = itemView.findViewById(R.id.textViewWinningAmount);

            btnClaim = itemView.findViewById(R.id.btnClaim);
            btnClaim.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            btnClaim.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
            btnClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (betHistoryListener != null) {
                        betHistoryListener.onBetClaimClickedListener(getAdapterPosition());
                    }
                }
            });*/

            containerLayout = itemView.findViewById(R.id.containerLayout);

            textViewBaazaarName = itemView.findViewById(R.id.textViewBaazaarName);

            textViewBetNo = itemView.findViewById(R.id.textViewBetNo);

            textViewBetDate = itemView.findViewById(R.id.textViewBetDate);

            textViewGameName = itemView.findViewById(R.id.textViewGameName);

            textViewBetPoints = itemView.findViewById(R.id.textViewBetPoints);

            textViewBetTiming = itemView.findViewById(R.id.textViewBetTiming);

            textViewWinningAmount = itemView.findViewById(R.id.textViewWinningAmount);

            btnClaim = itemView.findViewById(R.id.btnClaim);
            btnClaim.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            btnClaim.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
            btnClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    containerLayout.performClick();
                }
            });

            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (betHistoryListener != null) {
                        betHistoryListener.onBetClaimClickedListener(getAdapterPosition());
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
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_BET_HISTORY:
                View view = LayoutInflater.from(mContext).inflate(R.layout.cell_game_history_new, viewGroup, false);
                viewHolder = new ViewHolder(view);
                break;
            case ITEM_TYPE_FOOTER:
                View footerView = LayoutInflater.from(mContext).inflate(R.layout.cell_footer_progress, viewGroup, false);
                viewHolder = new FooterViewHolder(footerView);
                break;
        }
        /*View view = LayoutInflater.from(mContext).inflate(R.layout.cell_game_history, viewGroup, false);*/

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_TYPE_BET_HISTORY:
                ViewHolder mViewHolder = (ViewHolder) viewHolder;

                BetHistoryData betHistoryData = betHistoryList.get(position);

                mViewHolder.textViewBaazaarName.setText("");
                mViewHolder.textViewBaazaarName.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_baazar_name),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                mViewHolder.textViewBaazaarName.append(Utils.getCustomSpannableString(mContext, " " + betHistoryData.getBazaarName(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                mViewHolder.textViewBetNo.setText("");
                mViewHolder.textViewBetNo.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_no),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                mViewHolder.textViewBetNo.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getSelectedPaana(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                mViewHolder.textViewBetDate.setText("");
                mViewHolder.textViewBetDate.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_date),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                mViewHolder.textViewBetDate.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getBookingTime(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                mViewHolder.textViewGameName.setText("");
                mViewHolder.textViewGameName.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_game_name),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                mViewHolder.textViewGameName.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getGameName(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                mViewHolder.textViewBetPoints.setText("");
                mViewHolder.textViewBetPoints.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_points),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                mViewHolder.textViewBetPoints.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getAmountPerPaana(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                mViewHolder.textViewBetTiming.setText("");
                mViewHolder.textViewBetTiming.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_timing),
                        mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                mViewHolder.textViewBetTiming.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getBookingTime(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

                if (betHistoryData.getBetStatus().equalsIgnoreCase(AppConstant.BET_STATUS_WON_UNCLAIMED)) {
                    mViewHolder.btnClaim.setVisibility(View.VISIBLE);
                    mViewHolder.textViewWinningAmount.setVisibility(View.GONE);
                } else if (betHistoryData.getBetStatus().equalsIgnoreCase(AppConstant.BET_STATUS_WON_CLAIMED)) {

                    mViewHolder.btnClaim.setVisibility(View.INVISIBLE);
                    mViewHolder.textViewWinningAmount.setVisibility(View.VISIBLE);

                    mViewHolder.textViewWinningAmount.setText("");
                    mViewHolder.textViewWinningAmount.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_you_won),
                            mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
                    if (betHistoryData.getWinningAmount() != 0) {
                        mViewHolder.textViewWinningAmount.append(Utils.getCustomSpannableString(mContext, " " + betHistoryData.getWinningAmount(),
                                mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
                    } else {
                        mViewHolder.textViewWinningAmount.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.final_number_empty),
                                mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
                    }
                } else {
                    mViewHolder.btnClaim.setVisibility(View.INVISIBLE);
                    mViewHolder.textViewWinningAmount.setVisibility(View.GONE);
                }
                break;
            case ITEM_TYPE_FOOTER:
                FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
                if (!isEntireListLoaded && footerViewHolder.getAdapterPosition() >= betHistoryList.size()) {
                    footerViewHolder.progressBarFooter.setVisibility(View.VISIBLE);
                    if (betHistoryListener != null) {
                        betHistoryListener.onLoadMoreData();
                    }
                } else {
                    footerViewHolder.progressBarFooter.setVisibility(View.GONE);
                }
                break;
        }



        /*ViewHolder mViewHolder = (ViewHolder) viewHolder;

        BetHistoryData betHistoryData = betHistoryList.get(i);

        mViewHolder.textViewGameName.setText(betHistoryData.getGameName());

        mViewHolder.textViewBaazaarName.setText("");
        mViewHolder.textViewBaazaarName.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_baazar_name),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
        mViewHolder.textViewBaazaarName.append(Utils.getCustomSpannableString(mContext, " " + betHistoryData.getBazaarName(),
                mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

        mViewHolder.textViewGameDate.setText("");
        mViewHolder.textViewGameDate.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_booking_time),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
        mViewHolder.textViewGameDate.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getBookingTime(),
                mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

        mViewHolder.textViewBetNo.setText("");
        mViewHolder.textViewBetNo.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_bet_no),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
        mViewHolder.textViewBetNo.append(Utils.getCustomSpannableString(mContext, " "  + betHistoryData.getSelectedPaana(),
                mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

        mViewHolder.textViewAmount.setText("");
        mViewHolder.textViewAmount.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_amount),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
        mViewHolder.textViewAmount.append(Utils.getCustomSpannableString(mContext, " " + betHistoryData.getAmountPerPaana(),
                mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));

        mViewHolder.textViewBaazaarResult.setText("");
        mViewHolder.textViewBaazaarResult.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_baazar_result),
                mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
        if (betHistoryData.getBazaarResult() != null &&
                !betHistoryData.getBazaarResult().isEmpty()) {
            mViewHolder.textViewBaazaarResult.append(Utils.getCustomSpannableString(mContext, " " + betHistoryData.getBazaarResult(),
                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
        } else {
            mViewHolder.textViewBaazaarResult.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.final_number_empty),
                    mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
        }

        if (betHistoryData.getBetStatus().equalsIgnoreCase(AppConstant.BET_STATUS_WON_UNCLAIMED)) {
            mViewHolder.btnClaim.setVisibility(View.VISIBLE);
            mViewHolder.textViewWinningAmount.setVisibility(View.GONE);
        } else if (betHistoryData.getBetStatus().equalsIgnoreCase(AppConstant.BET_STATUS_WON_CLAIMED)) {

            mViewHolder.btnClaim.setVisibility(View.GONE);
            mViewHolder.textViewWinningAmount.setVisibility(View.VISIBLE);

            mViewHolder.textViewWinningAmount.setText("");
            mViewHolder.textViewWinningAmount.append(Utils.getCustomSpannableStringWithGradient(mContext, mContext.getResources().getString(R.string.lbl_you_won),
                    mContext.getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(mContext)));
            if (betHistoryData.getWinningAmount() != 0) {
                mViewHolder.textViewWinningAmount.append(Utils.getCustomSpannableString(mContext, " " + betHistoryData.getWinningAmount(),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
            } else {
                mViewHolder.textViewWinningAmount.append(Utils.getCustomSpannableString(mContext, " " + mContext.getResources().getString(R.string.final_number_empty),
                        mContext.getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mContext)));
            }
        } else {
            mViewHolder.btnClaim.setVisibility(View.GONE);
            mViewHolder.textViewWinningAmount.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        if (isEntireListLoaded) {
            return betHistoryList.size();
        } else {
            return betHistoryList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_BET_HISTORY;
        }
    }

    public void updateData(ArrayList<BetHistoryData> betHistoryList) {
        if (betHistoryList != null) {
            this.betHistoryList = betHistoryList;
            notifyDataSetChanged();
        }
    }

    public void updateItem(int position, BetHistoryData betHistoryData) {
        notifyItemChanged(position, betHistoryData);
    }

    private boolean isPositionFooter(int position) {
        return (position == betHistoryList.size());
    }
}
