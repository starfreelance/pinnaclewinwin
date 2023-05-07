package com.pinnacle.winwin.ui.withdrawalhistory;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.app.WithdrawalStatus;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.CancelWithdrawPointsData;
import com.pinnacle.winwin.network.model.CancelWithdrawPointsRequest;
import com.pinnacle.winwin.network.model.CancelWithdrawPointsResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.WithdrawHistoryRequest;
import com.pinnacle.winwin.network.model.WithdrawHistoryResponse;
import com.pinnacle.winwin.network.model.WithdrawalHistoryData;
import com.pinnacle.winwin.network.model.WithdrawalHistoryDataResponse;
import com.pinnacle.winwin.ui.withdrawal.WithdrawalScreenActivity;
import com.pinnacle.winwin.ui.withdrawalhistory.adapter.WithdrawHistoryAdapter;
import com.pinnacle.winwin.ui.withdrawalhistory.listener.WithdrawalHistoryListener;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class WithdrawalHistoryScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, WithdrawalHistoryListener {

    private static final String TAG = WithdrawalHistoryScreenActivity.class.getSimpleName();
    private static final int REQ_CODE_WITHDRAW_POINTS = 100;

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewNoWithdrawalHistory;

//    private AppCompatImageView imgViewAdd;
    private AppCompatTextView textViewWithdrawPoints;

    private SwipeRefreshLayout swipeRefreshWithdrawal;

    private RecyclerView recyclerViewWithdrawalHistory;
    private WithdrawHistoryAdapter mAdapter;

    private View parentLayout;

    private ArrayList<WithdrawalHistoryData> withdrawalHistoryList;

    private int currentPage = 1;
    private int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_history_screen);

        initViews();
        callGetWithdrawHistoryApi(true);
    }

    @Override
    protected void onDestroy() {
        WithdrawHistoryAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQ_CODE_WITHDRAW_POINTS) {
            if (data != null) {
                if (data.getBooleanExtra(AppConstant.KEY_WITHDRAW_POINTS_STATUS, false)) {
                    refreshWithdrawHistoryList(true);
                }
            }
        }
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewNoWithdrawalHistory = findViewById(R.id.textViewNoWithdrawalHistory);
        textViewNoWithdrawalHistory.setTypeface(Utils.getTypeFaceBodoni72(this));

//        imgViewAdd = findViewById(R.id.imgViewAdd);
//        imgViewAdd.setOnClickListener(this);

        textViewWithdrawPoints = findViewById(R.id.textViewWithdrawPoints);
        textViewWithdrawPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewWithdrawPoints.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        textViewWithdrawPoints.setOnClickListener(this);

        swipeRefreshWithdrawal = findViewById(R.id.swipeRefreshWithdrawal);
        swipeRefreshWithdrawal.setOnRefreshListener(refreshListener);

        recyclerViewWithdrawalHistory = findViewById(R.id.recyclerViewWithdrawalHistory);

    }

    private void loadAdapter() {
        toggleNoWithdrawalHistoryText(false);
        if (mAdapter == null) {
            mAdapter = new WithdrawHistoryAdapter(this, withdrawalHistoryList);
            recyclerViewWithdrawalHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerViewWithdrawalHistory.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(withdrawalHistoryList);
        }
    }

    private void toggleNoWithdrawalHistoryText(boolean isVisible) {
        if (isVisible) {
            recyclerViewWithdrawalHistory.setVisibility(View.GONE);
            textViewNoWithdrawalHistory.setVisibility(View.VISIBLE);
        } else {
            textViewNoWithdrawalHistory.setVisibility(View.GONE);
            recyclerViewWithdrawalHistory.setVisibility(View.VISIBLE);
        }
    }

    private void refreshWithdrawHistoryList(boolean isProgressVisible) {
        currentPage = 1;
        if (withdrawalHistoryList != null) {
            withdrawalHistoryList.clear();
        }
        callGetWithdrawHistoryApi(isProgressVisible);
    }

    private void navigateToWithdrawalScreen() {
        Intent intent = new Intent(this, WithdrawalScreenActivity.class);
        startActivityForResult(intent, REQ_CODE_WITHDRAW_POINTS);
    }

    private void callGetWithdrawHistoryApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_WITHDRAW_HISTORY_REQUEST, getWithdrawHistoryRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_WITHDRAW_HISTORY, intent, isProgressVisible);
    }

    private WithdrawHistoryRequest getWithdrawHistoryRequest() {
        WithdrawHistoryRequest withdrawHistoryRequest = new WithdrawHistoryRequest();
        withdrawHistoryRequest.setCurrentPage(currentPage);
        withdrawHistoryRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        return withdrawHistoryRequest;
    }

    private void callCancelWithdrawPointsApi(int custPaymentId) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_CANCEL_WITHDRAW_POINTS_REQUEST, getCancelWithdrawPointsRequest(custPaymentId));
        callAppServer(AppConstant.REQ_API_TYPE_CANCEL_WITHDRAW_POINTS, intent, true);
    }

    private CancelWithdrawPointsRequest getCancelWithdrawPointsRequest(int custPaymentId) {
        CancelWithdrawPointsRequest cancelWithdrawPointsRequest = new CancelWithdrawPointsRequest();
        cancelWithdrawPointsRequest.setCustPaymentId(custPaymentId);

        return cancelWithdrawPointsRequest;
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshWithdrawHistoryList(false);
        }
    };

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof WithdrawHistoryResponse) {
            swipeRefreshWithdrawal.setRefreshing(false);
            WithdrawHistoryResponse withdrawHistoryResponse = (WithdrawHistoryResponse) response;
            if (withdrawHistoryResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                WithdrawalHistoryDataResponse withdrawalHistoryDataResponse =
                        withdrawHistoryResponse.getWithdrawalHistoryDataResponse();
                if (withdrawalHistoryDataResponse != null) {
                    if (withdrawalHistoryDataResponse.getWithdrawalHistoryDataList() != null &&
                            !withdrawalHistoryDataResponse.getWithdrawalHistoryDataList().isEmpty()) {
                        if (withdrawalHistoryList == null || withdrawalHistoryList.isEmpty()) {
                            withdrawalHistoryList = (ArrayList<WithdrawalHistoryData>) withdrawalHistoryDataResponse.getWithdrawalHistoryDataList();
                        } else {
                            withdrawalHistoryList.addAll(withdrawalHistoryDataResponse.getWithdrawalHistoryDataList());
                        }
                        if (withdrawalHistoryDataResponse.getTotalData() == withdrawalHistoryList.size()) {
                            WithdrawHistoryAdapter.isEntireListLoaded = true;
                        }
                        loadAdapter();
                    } else {
                        toggleNoWithdrawalHistoryText(true);
                    }
                } else {
                    toggleNoWithdrawalHistoryText(true);
                }
            }
        } else if (response instanceof CancelWithdrawPointsResponse) {
            CancelWithdrawPointsResponse cancelWithdrawPointsResponse = (CancelWithdrawPointsResponse) response;
            if (cancelWithdrawPointsResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                CancelWithdrawPointsData cancelWithdrawPointsData = cancelWithdrawPointsResponse.getCancelWithdrawPointsData();
                ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, cancelWithdrawPointsData.getPoints());
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.cancel_withdraw_points_success_msg),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                WithdrawalHistoryData withdrawalHistoryData = withdrawalHistoryList.get(selectedIndex);
                withdrawalHistoryData.setStatus(WithdrawalStatus.CANCELLED.name());
                withdrawalHistoryList.set(selectedIndex, withdrawalHistoryData);
                mAdapter.updateData(withdrawalHistoryList);

            }
        }

    }

    @Override
    protected void onFailure(Object response) {
        swipeRefreshWithdrawal.setRefreshing(false);
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.something_went_wrong_error),
                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        }
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    @Override
    public void onLoadMoreData() {
        currentPage++;
        callGetWithdrawHistoryApi(false);
    }

    @Override
    public void onClickedCancel(int index) {
        if (index != -1) {
            selectedIndex = index;
            callCancelWithdrawPointsApi(withdrawalHistoryList.get(index).getCustPaymentId());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.textViewWithdrawPoints) {
            navigateToWithdrawalScreen();
        }
    }
}