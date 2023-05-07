package com.pinnacle.winwin.ui.headtailhistory;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.HTBetHistoryData;
import com.pinnacle.winwin.network.model.HTBetHistoryDataResponse;
import com.pinnacle.winwin.network.model.HTBetHistoryRequest;
import com.pinnacle.winwin.network.model.HTBetHistoryResponse;
import com.pinnacle.winwin.ui.headtailhistory.adapter.HTBetHistoryAdapter;
import com.pinnacle.winwin.ui.headtailhistory.listener.HTBetHistoryListener;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class HeadTailHistoryScreenActivity extends ASOnlineBaseActivity implements HTBetHistoryListener {

    private static final String TAG = HeadTailHistoryScreenActivity.class.getSimpleName();
    private static final int DATA_PER_PAGE = 10;

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewNoBetHistory;

    private RecyclerView recyclerViewHTHistory;
    private HTBetHistoryAdapter mAdapter;

    private View parentLayout;

    private ArrayList<HTBetHistoryData> htBetHistoryList;

    private int selectedIndex = -1;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_tail_history_screen);

        initViews();
        callGetHTBetHistoryApi(true);
    }

    @Override
    protected void onDestroy() {
        HTBetHistoryAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewNoBetHistory = findViewById(R.id.textViewNoBetHistory);
        textViewNoBetHistory.setTypeface(Utils.getTypeFaceBodoni72(this));

        recyclerViewHTHistory = findViewById(R.id.recyclerViewHTHistory);
        recyclerViewHTHistory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void loadAdapter() {
        toggleNoBetHistoryText(false);
        if (mAdapter == null) {
            mAdapter = new HTBetHistoryAdapter(this, htBetHistoryList);
            recyclerViewHTHistory.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(htBetHistoryList);
        }
    }

    private void toggleNoBetHistoryText(boolean isVisible) {
        if (isVisible) {
            recyclerViewHTHistory.setVisibility(View.GONE);
            textViewNoBetHistory.setVisibility(View.VISIBLE);
        } else {
            textViewNoBetHistory.setVisibility(View.GONE);
            recyclerViewHTHistory.setVisibility(View.VISIBLE);
        }
    }

    private void callGetHTBetHistoryApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_HT_BET_HISTORY_REQUEST, getHTBetHistoryRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_HT_BET_HISTORY, intent, isProgressVisible);
    }

    private HTBetHistoryRequest getHTBetHistoryRequest() {
        HTBetHistoryRequest htBetHistoryRequest = new HTBetHistoryRequest();
        htBetHistoryRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        htBetHistoryRequest.setDataPerPage(DATA_PER_PAGE);
        htBetHistoryRequest.setCurrentPage(currentPage);

        return htBetHistoryRequest;
    }

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof HTBetHistoryResponse) {
            HTBetHistoryResponse htBetHistoryResponse = (HTBetHistoryResponse) response;
            HTBetHistoryDataResponse htBetHistoryDataResponse = htBetHistoryResponse.getHtBetHistoryDataResponse();
            if (htBetHistoryDataResponse != null) {
                if (htBetHistoryDataResponse.getHtBetHistoryList() != null &&
                        !htBetHistoryDataResponse.getHtBetHistoryList().isEmpty()) {
                    LogUtils.e(TAG, String.valueOf(htBetHistoryDataResponse.getHtBetHistoryList().size()));
                    if (htBetHistoryList == null || htBetHistoryList.isEmpty()) {
                        htBetHistoryList = (ArrayList<HTBetHistoryData>) htBetHistoryDataResponse.getHtBetHistoryList();
                    } else {
                        htBetHistoryList.addAll((ArrayList<HTBetHistoryData>) htBetHistoryDataResponse.getHtBetHistoryList());
                    }
                    if (htBetHistoryDataResponse.getTotalData() == htBetHistoryList.size()) {
                        HTBetHistoryAdapter.isEntireListLoaded = true;
                    }
                    loadAdapter();
                } else {
                    toggleNoBetHistoryText(true);
                }
            } else {
                toggleNoBetHistoryText(true);
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
        callGetHTBetHistoryApi(false);
    }
}