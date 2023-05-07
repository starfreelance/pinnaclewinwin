package com.pinnacle.winwin.ui.rechargehistory;

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
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.RechargeHistoryData;
import com.pinnacle.winwin.network.model.RechargeHistoryDataResponse;
import com.pinnacle.winwin.network.model.RechargeHistoryRequest;
import com.pinnacle.winwin.network.model.RechargeHistoryResponse;
import com.pinnacle.winwin.ui.recharge.RechargeScreenActivity;
import com.pinnacle.winwin.ui.rechargehistory.adapter.RechargeHistoryAdapter;
import com.pinnacle.winwin.ui.rechargehistory.listener.RechargeHistoryListener;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class RechargeHistoryScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, RechargeHistoryListener {

    private static final String TAG = RechargeHistoryScreenActivity.class.getSimpleName();
    private static final int REQ_CODE_RECHARGE_POINTS = 100;

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewNoRechargeHistory;

//    private AppCompatImageView imgViewAdd;
    private AppCompatTextView textViewAddPoints;

    private SwipeRefreshLayout swipeRefreshRecharge;

    private RecyclerView recyclerViewRechargeHistory;
    private RechargeHistoryAdapter mAdapter;

    private View parentLayout;

    private ArrayList<RechargeHistoryData> rechargeHistoryList;

    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history_screen);

        initViews();
        callRechargeHistoryApi(true);
    }

    @Override
    protected void onDestroy() {
        RechargeHistoryAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQ_CODE_RECHARGE_POINTS) {
            if (data != null) {
                if (data.getBooleanExtra(AppConstant.KEY_RECHARGE_INFO_STATUS, false)) {
                    refreshRechargeHistoryList(true);
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

        textViewNoRechargeHistory = findViewById(R.id.textViewNoRechargeHistory);
        textViewNoRechargeHistory.setTypeface(Utils.getTypeFaceBodoni72(this));

//        imgViewAdd = findViewById(R.id.imgViewAdd);
//        imgViewAdd.setOnClickListener(this);

        textViewAddPoints = findViewById(R.id.textViewAddPoints);
        textViewAddPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewAddPoints.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        textViewAddPoints.setOnClickListener(this);

        swipeRefreshRecharge = findViewById(R.id.swipeRefreshRecharge);
        swipeRefreshRecharge.setOnRefreshListener(refreshListener);

        recyclerViewRechargeHistory = findViewById(R.id.recyclerViewRechargeHistory);

    }

    private void loadAdapter() {
        toggleNoRechargeHistoryText(false);
        if (mAdapter == null) {
            mAdapter = new RechargeHistoryAdapter(this, rechargeHistoryList);
            recyclerViewRechargeHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerViewRechargeHistory.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(rechargeHistoryList);
        }
    }

    private void toggleNoRechargeHistoryText(boolean isVisible) {
        if (isVisible) {
            recyclerViewRechargeHistory.setVisibility(View.GONE);
            textViewNoRechargeHistory.setVisibility(View.VISIBLE);
        } else {
            textViewNoRechargeHistory.setVisibility(View.GONE);
            recyclerViewRechargeHistory.setVisibility(View.VISIBLE);
        }
    }

    private void refreshRechargeHistoryList(boolean isProgressVisible) {
//        currentPage = 1;
        if (rechargeHistoryList != null) {
            rechargeHistoryList.clear();
        }
        callRechargeHistoryApi(isProgressVisible);
    }

    private void navigateToRechargeScreen() {
        Intent intent = new Intent(this, RechargeScreenActivity.class);
        startActivityForResult(intent, REQ_CODE_RECHARGE_POINTS);
        finish();
    }

    private void callRechargeHistoryApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_RECHARGE_HISTORY_REQUEST, getRechargeHistoryRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_RECHARGE_HISTORY, intent, isProgressVisible);
    }

    private RechargeHistoryRequest getRechargeHistoryRequest() {
        RechargeHistoryRequest rechargeHistoryRequest = new RechargeHistoryRequest();
        rechargeHistoryRequest.setCurrentPage(currentPage);
        rechargeHistoryRequest.setCustId(String.valueOf(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1)));
        rechargeHistoryRequest.setFullName("null");
        rechargeHistoryRequest.setMobNo("null");
        return rechargeHistoryRequest;
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            currentPage = 1;
//            if (rechargeHistoryList != null) {
//                rechargeHistoryList.clear();
//            }
//            callRechargeHistoryApi(false);
            refreshRechargeHistoryList(false);
        }
    };

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof RechargeHistoryResponse) {
            swipeRefreshRecharge.setRefreshing(false);
            RechargeHistoryResponse rechargeHistoryResponse = (RechargeHistoryResponse) response;
            if (rechargeHistoryResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                RechargeHistoryDataResponse rechargeHistoryDataResponse = rechargeHistoryResponse.getRechargeHistoryDataResponse();
                if (rechargeHistoryDataResponse != null) {
                    if (rechargeHistoryDataResponse.getRechargeHistoryDataList() != null &&
                            !rechargeHistoryDataResponse.getRechargeHistoryDataList().isEmpty()) {
                        if (rechargeHistoryList == null || rechargeHistoryList.isEmpty()) {
                            rechargeHistoryList = (ArrayList<RechargeHistoryData>) rechargeHistoryDataResponse.getRechargeHistoryDataList();
                        } else {
                            rechargeHistoryList.addAll(rechargeHistoryDataResponse.getRechargeHistoryDataList());
                        }
                        if (rechargeHistoryDataResponse.getTotalData() == rechargeHistoryList.size()) {
                            RechargeHistoryAdapter.isEntireListLoaded = true;
                        }
                        loadAdapter();
                    } else {
                        toggleNoRechargeHistoryText(true);
                    }
                } else {
                    toggleNoRechargeHistoryText(true);
                }
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        swipeRefreshRecharge.setRefreshing(false);
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
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    @Override
    public void onLoadMoreData() {
        //currentPage++;
        callRechargeHistoryApi(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.textViewAddPoints) {
            navigateToRechargeScreen();
        }
    }
}