package com.pinnacle.winwin.ui.baazaarhistory.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.BaazaarHistoryData;
import com.pinnacle.winwin.network.model.CustomerTransactionData;
import com.pinnacle.winwin.ui.baazaarhistory.listener.CustomTableCellListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class CustomTableCellDataAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CustomTableCellDataAdapter.class.getSimpleName();
    private static final int TYPE_BAAZAAR_HISTORY = 0;
    private static final int TYPE_WALLET_TRANSACTION = 1;
    private static final int ITEM_TYPE_FOOTER = 2;

    private Context mContext;
    private ArrayList<CustomerTransactionData> customerTransactions;
    private ArrayList<BaazaarHistoryData> baazaarHistoryList;
    private CustomTableCellListener customTableCellListener;
    public static boolean isEntireListLoaded = false;

    public CustomTableCellDataAdapter(Context mContext, ArrayList<T> dataList) {
        this.mContext = mContext;
        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.get(0) instanceof CustomerTransactionData) {
                customerTransactions = (ArrayList<CustomerTransactionData>) dataList;
            } else if (dataList.get(0) instanceof BaazaarHistoryData) {
                baazaarHistoryList = (ArrayList<BaazaarHistoryData>) dataList;
            }
        }
        customTableCellListener = (CustomTableCellListener) mContext;
    }

    private class BaazaarHistoryViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewHeaderKOKC;
        /*AppCompatTextView textViewHeaderKC;*/
        AppCompatTextView textViewHeaderMOMC;
        /*AppCompatTextView textViewHeaderMC;*/
        AppCompatTextView textViewHeaderTOTC;
        AppCompatTextView textViewHeaderMDOMDC;
        AppCompatTextView textViewHeaderMNOMNC;

        public BaazaarHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewHeaderKOKC = itemView.findViewById(R.id.textViewHeaderKOKC);
            textViewHeaderKOKC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderKOKC.setTextColor(mContext.getResources().getColor(android.R.color.white));

            /*textViewHeaderKC = itemView.findViewById(R.id.textViewHeaderKC);
            textViewHeaderKC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderKC.setTextColor(mContext.getResources().getColor(android.R.color.white));*/

            textViewHeaderMOMC = itemView.findViewById(R.id.textViewHeaderMOMC);
            textViewHeaderMOMC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderMOMC.setTextColor(mContext.getResources().getColor(android.R.color.white));

            textViewHeaderTOTC = itemView.findViewById(R.id.textViewHeaderTOTC);
            textViewHeaderTOTC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderTOTC.setTextColor(mContext.getResources().getColor(android.R.color.white));

            textViewHeaderMDOMDC = itemView.findViewById(R.id.textViewHeaderMDOMDC);
            textViewHeaderMDOMDC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderMDOMDC.setTextColor(mContext.getResources().getColor(android.R.color.white));

            textViewHeaderMNOMNC = itemView.findViewById(R.id.textViewHeaderMNOMNC);
            textViewHeaderMNOMNC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderMNOMNC.setTextColor(mContext.getResources().getColor(android.R.color.white));

            /*textViewHeaderMC = itemView.findViewById(R.id.textViewHeaderMC);
            textViewHeaderMC.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderMC.setTextColor(mContext.getResources().getColor(android.R.color.white));*/
        }
    }

    private class WalletTransactionViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewHeaderDescription;
        AppCompatTextView textViewHeaderWithdraw;
        AppCompatTextView textViewHeaderDeposit;
        AppCompatTextView textViewHeaderBalance;


        public WalletTransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewHeaderDescription = itemView.findViewById(R.id.textViewHeaderDescription);
            textViewHeaderDescription.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderDescription.setTextColor(mContext.getResources().getColor(android.R.color.white));

            textViewHeaderWithdraw = itemView.findViewById(R.id.textViewHeaderWithdraw);
            textViewHeaderWithdraw.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderWithdraw.setTextColor(mContext.getResources().getColor(android.R.color.white));

            textViewHeaderDeposit = itemView.findViewById(R.id.textViewHeaderDeposit);
            textViewHeaderDeposit.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderDeposit.setTextColor(mContext.getResources().getColor(android.R.color.white));

            textViewHeaderBalance = itemView.findViewById(R.id.textViewHeaderBalance);
            textViewHeaderBalance.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewHeaderBalance.setTextColor(mContext.getResources().getColor(android.R.color.white));
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
            case TYPE_BAAZAAR_HISTORY:
                View baazaarHistoryView = LayoutInflater.from(mContext).inflate(
                        R.layout.cell_baazaar_history_data, viewGroup, false);
                viewHolder = new BaazaarHistoryViewHolder(baazaarHistoryView);
                break;
            case TYPE_WALLET_TRANSACTION:
                View view = LayoutInflater.from(mContext).inflate(
                        R.layout.cell_wallet_transaction_data, viewGroup, false);
                viewHolder = new WalletTransactionViewHolder(view);
                break;
            case ITEM_TYPE_FOOTER:
                View footerView = LayoutInflater.from(mContext).inflate(R.layout.cell_footer_progress, viewGroup, false);
                viewHolder = new FooterViewHolder(footerView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_BAAZAAR_HISTORY:
                BaazaarHistoryViewHolder baazaarHistoryViewHolder = (BaazaarHistoryViewHolder) viewHolder;

                BaazaarHistoryData baazaarHistoryData = baazaarHistoryList.get(position);

                baazaarHistoryViewHolder.textViewHeaderKOKC.setText(baazaarHistoryData.getkOkC());
                baazaarHistoryViewHolder.textViewHeaderMOMC.setText(baazaarHistoryData.getmOmC());
                baazaarHistoryViewHolder.textViewHeaderTOTC.setText(baazaarHistoryData.gettOTC());
                baazaarHistoryViewHolder.textViewHeaderMDOMDC.setText(baazaarHistoryData.getmDOMDC());
                baazaarHistoryViewHolder.textViewHeaderMNOMNC.setText(baazaarHistoryData.getmNOMNC());

                break;
            case TYPE_WALLET_TRANSACTION:
                WalletTransactionViewHolder walletTransactionViewHolder = (WalletTransactionViewHolder) viewHolder;

                CustomerTransactionData customerTransaction = customerTransactions.get(position);

                walletTransactionViewHolder.textViewHeaderDescription.setText(customerTransaction.getParticulars());

                if (customerTransaction.getDebitAmount() == 0) {
                    walletTransactionViewHolder.textViewHeaderWithdraw.setText(mContext.getResources().getString(R.string.final_number_empty));
                } else {
                    walletTransactionViewHolder.textViewHeaderWithdraw.setText(String.valueOf(customerTransaction.getDebitAmount()));
                }

                if (customerTransaction.getCreditAmount() == 0) {
                    walletTransactionViewHolder.textViewHeaderDeposit.setText(mContext.getResources().getString(R.string.final_number_empty));
                } else {
                    walletTransactionViewHolder.textViewHeaderDeposit.setText(String.valueOf(customerTransaction.getCreditAmount()));
                }
                walletTransactionViewHolder.textViewHeaderBalance.setText(String.valueOf(customerTransaction.getFinalAmount()));

                break;
            case ITEM_TYPE_FOOTER:
                FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
                if (customerTransactions != null) {
                    if (!isEntireListLoaded && footerViewHolder.getAdapterPosition() >= customerTransactions.size()) {
                        footerViewHolder.progressBarFooter.setVisibility(View.VISIBLE);
                        if (customTableCellListener != null) {
                            customTableCellListener.onLoadMoreData();
                        }
                    } else {
                        footerViewHolder.progressBarFooter.setVisibility(View.GONE);
                    }
                } else {
                    if (!isEntireListLoaded && footerViewHolder.getAdapterPosition() >= baazaarHistoryList.size()) {
                        footerViewHolder.progressBarFooter.setVisibility(View.VISIBLE);
                        if (customTableCellListener != null) {
                            customTableCellListener.onLoadMoreData();
                        }
                    } else {
                        footerViewHolder.progressBarFooter.setVisibility(View.GONE);
                    }
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (customerTransactions != null) {
            if (isEntireListLoaded) {
                return customerTransactions.size();
            } else {
                return customerTransactions.size() + 1;
            }
        } else if (baazaarHistoryList != null) {
            if (isEntireListLoaded) {
                return baazaarHistoryList.size();
            } else {
                return baazaarHistoryList.size() + 1;
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (customerTransactions != null) {
            if (isPositionFooter(position)) {
                return ITEM_TYPE_FOOTER;
            } else {
                return TYPE_WALLET_TRANSACTION;
            }
        } else {
            if (isPositionFooter(position)) {
                return ITEM_TYPE_FOOTER;
            } else {
                return TYPE_BAAZAAR_HISTORY;
            }
        }
    }

    private boolean isPositionFooter(int position) {
        if (customerTransactions != null) {
            return (position == customerTransactions.size());
        } else {
            return (position == baazaarHistoryList.size());
        }
    }
}
