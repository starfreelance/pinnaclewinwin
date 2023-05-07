package com.pinnacle.winwin.ui.gamehistory;

import android.content.Intent;

import androidx.annotation.Nullable;

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
import com.pinnacle.winwin.network.model.BetClaimRequest;
import com.pinnacle.winwin.network.model.BetClaimResponse;
import com.pinnacle.winwin.network.model.BetHistoryData;
import com.pinnacle.winwin.network.model.BetHistoryDataResponse;
import com.pinnacle.winwin.network.model.BetHistoryRequest;
import com.pinnacle.winwin.network.model.BetHistoryResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.ui.bethistorydetails.BetHistoryDetailScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.adapter.BetHistoryAdapter;
import com.pinnacle.winwin.ui.gamehistory.listener.BetHistoryListener;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class GameHistoryScreenActivity extends ASOnlineBaseActivity implements BetHistoryListener {

    private static final String TAG = GameHistoryScreenActivity.class.getSimpleName();
    private static final int REQ_CODE_BET_DETAILS = 101;

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewGameTitle;
    private AppCompatTextView textViewNoBetHistory;

    private RecyclerView recyclerViewGameHistory;
    private BetHistoryAdapter mAdapter;

    private View parentLayout;

    private ArrayList<BetHistoryData> betHistoryList;

    private int baazaarId = -1;
    private int gameId = -1;
    private int selectedIndex = -1;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history_screen);

        processIntentData();
        initViews();
        updateGameTitle();
        callGetBetHistoryApi(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        BetHistoryAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQ_CODE_BET_DETAILS) {
            if (data != null) {
                BetHistoryData betHistoryData = data.getParcelableExtra(AppConstant.KEY_BET_HISTORY_CLAIM_DATA);
                if (betHistoryData != null && selectedIndex != -1) {
                    betHistoryList.set(selectedIndex, betHistoryData);
                    mAdapter.updateItem(selectedIndex, betHistoryData);
                }
            }
        }
    }

    //Local Methods
    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewGameTitle = findViewById(R.id.textViewGameTitle);
        textViewGameTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewGameTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewNoBetHistory = findViewById(R.id.textViewNoBetHistory);
        textViewNoBetHistory.setTypeface(Utils.getTypeFaceBodoni72(this));

        recyclerViewGameHistory = findViewById(R.id.recyclerViewGameHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewGameHistory.setLayoutManager(layoutManager);
    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_BAAZAAR_ID)) {
                baazaarId = getIntent().getIntExtra(AppConstant.KEY_BAAZAAR_ID, -1);
            }

            if (getIntent().hasExtra(AppConstant.KEY_GAME_ID)) {
                gameId = getIntent().getIntExtra(AppConstant.KEY_GAME_ID, -1);
            }
        }
    }

    private void updateGameTitle() {
        if (gameId == -1) {
            textViewGameTitle.setText(getResources().getString(R.string.title_all));
        } else if (gameId == AppConstant.GAME_TYPE_SINGLE) {
            textViewGameTitle.setText(getResources().getString(R.string.title_single_game));
        } else if (gameId == AppConstant.GAME_TYPE_PAANA) {
            textViewGameTitle.setText(getResources().getString(R.string.title_paana_game));
        } else if (gameId == AppConstant.GAME_TYPE_CP) {
            textViewGameTitle.setText(getResources().getString(R.string.title_cp_game));
        } else if (gameId == AppConstant.GAME_TYPE_MOTOR) {
            textViewGameTitle.setText(getResources().getString(R.string.title_motor_game));
        } else if (gameId == AppConstant.GAME_TYPE_BRACKET) {
            textViewGameTitle.setText(getResources().getString(R.string.title_bracket_game));
        } else if (gameId == AppConstant.GAME_TYPE_CHART) {
            textViewGameTitle.setText(getResources().getString(R.string.title_chart_game));
        } else if (gameId == AppConstant.GAME_TYPE_COMMON) {
            textViewGameTitle.setText(getResources().getString(R.string.title_common_game));
        }
    }

    private void loadAdapter() {
        toggleNoBetHistoryText(false);
        if (mAdapter == null) {
            mAdapter = new BetHistoryAdapter(this, betHistoryList);
            recyclerViewGameHistory.setAdapter(mAdapter);
        } else {
            /*mAdapter.notifyDataSetChanged();*/
            mAdapter.updateData(betHistoryList);
        }
    }

    private void toggleNoBetHistoryText(boolean isVisible) {
        if (isVisible) {
            recyclerViewGameHistory.setVisibility(View.GONE);
            textViewNoBetHistory.setVisibility(View.VISIBLE);
        } else {
            textViewNoBetHistory.setVisibility(View.GONE);
            recyclerViewGameHistory.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToBetHistoryDetailScreen(BetHistoryData betHistoryData) {
        Intent intent = new Intent(this, BetHistoryDetailScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BET_HISTORY_DATA, betHistoryData);
        /*startActivity(intent);*/
        startActivityForResult(intent, REQ_CODE_BET_DETAILS);
    }

    private void callGetBetHistoryApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_BET_HISTORY_REQUEST, getBetHistoryRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_BET_HISTORY, intent, isProgressVisible);
    }

    private void callClaimBetApi(int betId) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_CLAIM_BET_REQUEST, getBetClaimRequest(betId));
        callAppServer(AppConstant.REQ_API_TYPE_CLAIM_BET, intent, true);
    }

    private BetClaimRequest getBetClaimRequest(int betId) {
        BetClaimRequest betClaimRequest = new BetClaimRequest();
        betClaimRequest.setBetId(betId);

        return betClaimRequest;
    }

    private BetHistoryRequest getBetHistoryRequest() {
        BetHistoryRequest betHistoryRequest = new BetHistoryRequest();
        betHistoryRequest.setCustId(
                ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        if (baazaarId != -1) {
            betHistoryRequest.setBazaarId(baazaarId);
        }
        if (gameId != -1) {
            betHistoryRequest.setGameId(gameId);
        }
        /*betHistoryRequest.setGameDate(DateUtils.getStringFormattedDate(new Date(), DateUtils.ADD_NEW_DATE_FORMAT));*/
        /*betHistoryRequest.setGameDate("2019-05-11");*/
        betHistoryRequest.setCurrentPage(currentPage);
        return betHistoryRequest;
    }

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof BetHistoryResponse) {
            BetHistoryResponse betHistoryResponse = (BetHistoryResponse) response;
            if (betHistoryResponse.getBetHistoryDataResponse() != null) {
                BetHistoryDataResponse betHistoryDataResponse = betHistoryResponse.getBetHistoryDataResponse();
                if (betHistoryDataResponse.getBetHistoryList() != null &&
                        !betHistoryDataResponse.getBetHistoryList().isEmpty()) {
                    LogUtils.e(TAG, String.valueOf(betHistoryDataResponse.getBetHistoryList().size()));
                    if (betHistoryList == null || betHistoryList.isEmpty()) {
                        betHistoryList = (ArrayList<BetHistoryData>) betHistoryDataResponse.getBetHistoryList();
                    } else {
                        betHistoryList.addAll((ArrayList<BetHistoryData>) betHistoryDataResponse.getBetHistoryList());
                    }
                    if (betHistoryDataResponse.getTotalData() == betHistoryList.size()) {
                        BetHistoryAdapter.isEntireListLoaded = true;
                    }
                    loadAdapter();
                } else {
                    toggleNoBetHistoryText(true);
                }
            } else {
                toggleNoBetHistoryText(true);
            }
        } else if (response instanceof BetClaimResponse) {
            BetClaimResponse betClaimResponse = (BetClaimResponse) response;
            if (betClaimResponse.getBetHistoryData() != null) {
                BetHistoryData betHistoryData = betClaimResponse.getBetHistoryData();
                ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, betHistoryData.getUpdatedPoints());
                betHistoryList.set(selectedIndex, betHistoryData);
                mAdapter.updateData(betHistoryList);
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

    @Override
    public void onBetClaimClickedListener(int index) {
        /*if (index != -1) {
            selectedIndex = index;
            callClaimBetApi(betHistoryList.get(index).getBetId());
        }*/
        if (index != -1) {
            selectedIndex = index;
            navigateToBetHistoryDetailScreen(betHistoryList.get(index));
        }
    }

    @Override
    public void onLoadMoreData() {
        currentPage++;
        callGetBetHistoryApi(false);
    }
}
