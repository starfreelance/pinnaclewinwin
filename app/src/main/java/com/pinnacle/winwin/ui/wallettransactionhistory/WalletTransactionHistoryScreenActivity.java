package com.pinnacle.winwin.ui.wallettransactionhistory;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.SyncHorizontalScrollView;
import com.pinnacle.winwin.network.model.CustomerTransactionData;
import com.pinnacle.winwin.network.model.CustomerTransactionDataResponse;
import com.pinnacle.winwin.network.model.CustomerTransactionRequest;
import com.pinnacle.winwin.network.model.CustomerTransactionResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.ui.baazaarhistory.adapter.CustomTableCellDataAdapter;
import com.pinnacle.winwin.ui.baazaarhistory.adapter.CustomTableRowDateAdapter;
import com.pinnacle.winwin.ui.baazaarhistory.listener.CustomTableCellListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class WalletTransactionHistoryScreenActivity extends ASOnlineBaseActivity implements
        SyncHorizontalScrollView.OnScrollViewListener, CustomTableCellListener {

    private static final String TAG = WalletTransactionHistoryScreenActivity.class.getSimpleName();
    private static final int DATA_PER_PAGE = 25;

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewHeaderDate;
    private AppCompatTextView textViewHeaderDescription;
    private AppCompatTextView textViewHeaderWithdraw;
    private AppCompatTextView textViewHeaderDeposit;
    private AppCompatTextView textViewHeaderBalance;

    private SyncHorizontalScrollView syncHorizontalHeaderView;
    private SyncHorizontalScrollView syncHorizontalDataView;

    private RecyclerView recyclerViewDates;
    private CustomTableRowDateAdapter rowDateAdapter;

    private RecyclerView recyclerViewTransactionData;
    private CustomTableCellDataAdapter cellDataAdapter;

    private View parentLayout;

    private ArrayList<CustomerTransactionData> customerTransactions;
    private ArrayList<String> transactionDates;

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transaction_history_screen);

        initViews();
        callGetCustomerTransactionsApi(true);
    }

    @Override
    protected void onDestroy() {
        CustomTableCellDataAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_wallet_history));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewHeaderDate = findViewById(R.id.textViewHeaderDate);
        textViewHeaderDate.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderDate.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderDescription = findViewById(R.id.textViewHeaderDescription);
        textViewHeaderDescription.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderDescription.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderWithdraw = findViewById(R.id.textViewHeaderWithdraw);
        textViewHeaderWithdraw.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderWithdraw.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderDeposit = findViewById(R.id.textViewHeaderDeposit);
        textViewHeaderDeposit.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderDeposit.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderBalance = findViewById(R.id.textViewHeaderBalance);
        textViewHeaderBalance.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderBalance.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        syncHorizontalHeaderView = findViewById(R.id.syncHorizontalHeaderView);
        syncHorizontalHeaderView.setOnScrollViewListener(this);

        syncHorizontalDataView = findViewById(R.id.syncHorizontalDataView);
        syncHorizontalDataView.setOnScrollViewListener(this);

        recyclerViewDates = findViewById(R.id.recyclerViewDates);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDates.setLayoutManager(layoutManager);
        recyclerViewDates.addOnScrollListener(scrollListener);

        recyclerViewTransactionData = findViewById(R.id.recyclerViewTransactionData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTransactionData.setLayoutManager(linearLayoutManager);
        recyclerViewTransactionData.addOnScrollListener(scrollListener);
    }

    private void loadRowDateAdapter() {
        if (rowDateAdapter == null) {
            rowDateAdapter = new CustomTableRowDateAdapter(this, transactionDates, AppConstant.TABLE_TYPE_WALLET_TRANSACTION);
            recyclerViewDates.setAdapter(rowDateAdapter);
        } else {
            rowDateAdapter.notifyDataSetChanged();
        }
    }

    private void loadCellDataAdapter() {
        if (cellDataAdapter == null) {
            cellDataAdapter = new CustomTableCellDataAdapter(this, customerTransactions);
            recyclerViewTransactionData.setAdapter(cellDataAdapter);
        } else {
            cellDataAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<String> getTransactionDates(ArrayList<CustomerTransactionData> customerTransactionDataList) {
        ArrayList<String> transactionDateList = new ArrayList<>();

        if (customerTransactionDataList != null && !customerTransactionDataList.isEmpty()) {
            for (CustomerTransactionData customerTransaction : customerTransactionDataList) {
                /*transactionDateList.add(customerTransaction.getCreatedAt());*/
                transactionDateList.add(customerTransaction.getTrxnDate());
            }
        }

        return transactionDateList;
    }

    private void callGetCustomerTransactionsApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_CUSTOMER_TRANSACTIONS_REQUEST, getCustomerTransactionRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_CUSTOMER_TRANSACTIONS, intent, isProgressVisible);
    }

    private CustomerTransactionRequest getCustomerTransactionRequest() {
        CustomerTransactionRequest customerTransactionRequest = new CustomerTransactionRequest();
        customerTransactionRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        customerTransactionRequest.setAdminId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_ADMIN_ID, -1));
        customerTransactionRequest.setCurrentPage(currentPage);
        customerTransactionRequest.setDataPerPage(DATA_PER_PAGE);
        return customerTransactionRequest;
    }

    private void scroll(RecyclerView recyclerView, int dx, int dy) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(scrollListener);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (recyclerViewDates != recyclerView) {
                scroll(recyclerViewDates, dx, dy);
            } else {
                scroll(recyclerViewTransactionData, dx, dy);
            }
        }
    };

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof CustomerTransactionResponse) {
            CustomerTransactionResponse customerTransactionResponse = (CustomerTransactionResponse) response;
            CustomerTransactionDataResponse customerTransactionDataResponse = customerTransactionResponse.getCustomerTransactionDataResponse();
            if (customerTransactionDataResponse != null) {
                if (customerTransactionDataResponse.getCustomerTransactions() != null &&
                        !customerTransactionDataResponse.getCustomerTransactions().isEmpty()) {
                    if (customerTransactions == null || customerTransactions.isEmpty()) {
                        customerTransactions = (ArrayList<CustomerTransactionData>) customerTransactionDataResponse.getCustomerTransactions();
                        transactionDates = getTransactionDates(customerTransactions);
                    } else {
                        customerTransactions.addAll(customerTransactionDataResponse.getCustomerTransactions());
                        transactionDates.addAll(getTransactionDates((ArrayList<CustomerTransactionData>) customerTransactionDataResponse.getCustomerTransactions()));
                    }
                    if (customerTransactionDataResponse.getTotalData() == customerTransactions.size()) {
                        CustomTableCellDataAdapter.isEntireListLoaded = true;
                    }
                    loadCellDataAdapter();
                    if (transactionDates != null) {
                        loadRowDateAdapter();
                    }
                }
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.something_went_wrong_error),
                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        }
        /*if (response instanceof Response) {
            Response mResponse = (Response) response;
            switch (mResponse.code()) {
                default:
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.something_went_wrong_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    break;
            }
        }*/
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    //Event Handlers
    //SyncHorizontalScrollView.OnScrollViewListener
    @Override
    public void onScrollChanged(SyncHorizontalScrollView syncHorizontalScrollView, int l, int t, int oldl, int oldt) {
        if (syncHorizontalScrollView == syncHorizontalHeaderView) {
            syncHorizontalDataView.scrollTo(l, t);
        } else if (syncHorizontalScrollView == syncHorizontalDataView) {
            syncHorizontalHeaderView.scrollTo(l, t);
        }
    }

    /**
     * CustomTableCellListener
     */
    @Override
    public void onLoadMoreData() {
        currentPage++;
        callGetCustomerTransactionsApi(false);
    }
}
