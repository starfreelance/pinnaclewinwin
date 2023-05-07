package com.pinnacle.winwin.ui.chartgame;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.listener.CustomSingleButtonDialogListener;
import com.pinnacle.winwin.network.model.AddNewBetOtcRequest;
import com.pinnacle.winwin.network.model.AddNewBetRequest;
import com.pinnacle.winwin.network.model.AddNewBetResponse;
import com.pinnacle.winwin.network.model.AmountDetailsResponse;
import com.pinnacle.winwin.network.model.BaazaarRemainingTimeData;
import com.pinnacle.winwin.network.model.BaazaarRemainingTimeRequest;
import com.pinnacle.winwin.network.model.BaazaarRemainingTimeResponse;
import com.pinnacle.winwin.network.model.BetPaanaData;
import com.pinnacle.winwin.network.model.ChartDetailsResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.ui.chartgame.adapter.NumberPagerAdapter;
import com.pinnacle.winwin.ui.chartgame.adapter.NumberTabAdapter;
import com.pinnacle.winwin.ui.chartgame.listener.ChartTabNumberListener;
import com.pinnacle.winwin.ui.chartgame.model.ChartDetail;
import com.pinnacle.winwin.ui.chartgame.model.ChartTabNumber;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.adapter.SelectPointsAdapter;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.singlegame.listener.SelectPointsListener;
import com.pinnacle.winwin.ui.singlegame.listener.SingleGameNumberListener;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChartGameScreenActivity extends ASOnlineBaseActivity implements
        SingleGameNumberListener, View.OnClickListener, SelectPointsListener, ChartTabNumberListener,
        CustomDualButtonDialgoListener, CustomSingleButtonDialogListener {

    private static final String TAG = ChartGameScreenActivity.class.getSimpleName();

    private AppCompatTextView lblPoints;
    private AppCompatTextView lblSingleGame;
    private AppCompatTextView lblTimer;
    private AppCompatTextView lblClearAll;
    private AppCompatTextView lblHistory;
    private AppCompatTextView lblTotal;
    private AppCompatTextView textViewFinal;
    private AppCompatTextView lblFinalNumber;
    private AppCompatTextView lblBaazaarHistory;

    private Button btnWalletBalance;
    private Button btnTimer;
    private Button btnTotal;
    private Button btnBetOk;

//    private RecyclerView recyclerViewTabs;
//    private LinearLayoutManager linearLayoutManager;

    private TabLayout tabLayout;

    private ViewPager numberViewPager;

    private NumberTabAdapter numberTabAdapter;
    private NumberPagerAdapter numberPagerAdapter;

    private RecyclerView recyclerViewPoints;
    private SelectPointsAdapter selectPointsAdapter;

    private RelativeLayout layoutOTCCheckbox;

    private AppCompatCheckBox checkboxOTC;

    private View parentLayout;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private ArrayList<String> numberTabs;

    private ArrayList<AmountDetailsResponse> amountDetails;
    private ArrayList<ChartDetailsResponse> chartDetails;
    private ArrayList<ChartDetail> chartDetailList;
    private ArrayList<String> gameNumbers;
    private ArrayList<ChartTabNumber> chartTabNumbers;
    private ArrayList<List<String>> chartCombinationCount;

    private int walletBalance;
    private int selectedIndex = -1;
    private int baazaarId = -1;
    private int gameId = -1;
    private int totalPoints = 0;
    private int selectedPoints = 0;
    private String finalNumber;
    private String lastResult;
    private long leftMilliSeconds;
    private Handler handler;
    private boolean isGameDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_game_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
        updateFinalOrLastResult();
        handler = new Handler();
//        numberTabs = getTabs();
//        loadNumberPager();

        initChartGameNumbers();

        amountDetails = fetchAmountDetailsFromDb();
        if (amountDetails != null && !amountDetails.isEmpty()) {
            loadPointsAdapter();
        }

        callGetBaazaarRemainingTimeApi();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        lblPoints = findViewById(R.id.lblPoints);
        lblPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblSingleGame = findViewById(R.id.lblSingleGame);
        lblSingleGame.setTypeface(Utils.getTypeFaceBodoni72OS(this));
        lblSingleGame.setText(getResources().getString(R.string.title_chart_game));
        lblSingleGame.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lblSingleGame.setLetterSpacing(0.2f);
        }

        lblTimer = findViewById(R.id.lblTimer);
        lblTimer.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblTimer.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblClearAll = findViewById(R.id.lblClearAll);
        lblClearAll.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblClearAll.setOnClickListener(this);
        lblClearAll.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblHistory = findViewById(R.id.lblHistory);
        lblHistory.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblHistory.setOnClickListener(this);
        lblHistory.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblTotal = findViewById(R.id.lblTotal);
        lblTotal.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblTotal.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        btnWalletBalance = findViewById(R.id.btnWalletBalance);
        btnWalletBalance.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnWalletBalance.setText(String.valueOf(walletBalance));

        btnTimer = findViewById(R.id.btnTimer);
        btnTimer.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnTotal = findViewById(R.id.btnTotal);
        btnTotal.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnBetOk = findViewById(R.id.btnBetOk);
        btnBetOk.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnBetOk.setOnClickListener(this);
        btnBetOk.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewFinal = findViewById(R.id.textViewFinal);
        textViewFinal.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewFinal.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        /*if (finalNumber != null && !finalNumber.isEmpty()) {
            textViewFinal.setText(finalNumber);
        } else {
            textViewFinal.setText(getResources().getString(R.string.final_number_empty));
        }*/

        lblFinalNumber = findViewById(R.id.lblFinalNumber);
        lblFinalNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblFinalNumber.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblBaazaarHistory = findViewById(R.id.lblBaazaarHistory);
        lblBaazaarHistory.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblBaazaarHistory.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        lblBaazaarHistory.setOnClickListener(this);

//        recyclerViewTabs = findViewById(R.id.recyclerViewTabs);
//        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        recyclerViewTabs.setLayoutManager(linearLayoutManager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        numberViewPager = findViewById(R.id.numberViewPager);
        tabLayout.setupWithViewPager(numberViewPager);
        numberViewPager.addOnPageChangeListener(pageChangeListener);

        recyclerViewPoints = findViewById(R.id.recyclerViewPoints);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        layoutOTCCheckbox = findViewById(R.id.layoutOTCCheckbox);
        layoutOTCCheckbox.setVisibility(View.VISIBLE);

        checkboxOTC = findViewById(R.id.checkboxOTC);

        if (baazaarId == AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE || baazaarId == AppConstant.BAAZAAR_TYPE_MAIN_CLOSE ||
                baazaarId == AppConstant.BAAZAAR_TYPE_TIME_CLOSE || baazaarId == AppConstant.BAAZAAR_TYPE_MILAN_DAY_CLOSE ||
                baazaarId == AppConstant.BAAZAAR_TYPE_MILAN_NIGHT_CLOSE) {
            layoutOTCCheckbox.setVisibility(View.GONE);
        } else {
            layoutOTCCheckbox.setVisibility(View.VISIBLE);
            checkboxOTC.setOnClickListener(this);
            checkboxOTC.setTypeface(Utils.getTypeFaceBodoni72(this));
            checkboxOTC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                    getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        }
    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_BAAZAAR_ID)) {
                baazaarId = getIntent().getIntExtra(AppConstant.KEY_BAAZAAR_ID, -1);
            }

            if (getIntent().hasExtra(AppConstant.KEY_GAME_ID)) {
                gameId = getIntent().getIntExtra(AppConstant.KEY_GAME_ID, -1);
            }

            if (getIntent().hasExtra(AppConstant.KEY_FINAL_NUMBER) &&
                    getIntent().getStringExtra(AppConstant.KEY_FINAL_NUMBER) != null) {
                finalNumber = getIntent().getStringExtra(AppConstant.KEY_FINAL_NUMBER);
            }

            if (getIntent().hasExtra(AppConstant.KEY_LAST_RESULT) &&
                    getIntent().getStringExtra(AppConstant.KEY_LAST_RESULT) != null) {
                lastResult = getIntent().getStringExtra(AppConstant.KEY_LAST_RESULT);
            }
        }
    }

    private void updateFinalOrLastResult() {
        if (lastResult != null && !lastResult.isEmpty()) {
            lblFinalNumber.setAllCaps(true);
            lblFinalNumber.setText(getResources().getString(R.string.lbl_baazaar_last_result));
            textViewFinal.setText(lastResult);
        } else {
            lblFinalNumber.setText(getResources().getString(R.string.lbl_final));
            if (finalNumber != null && !finalNumber.isEmpty()) {
                textViewFinal.setText(finalNumber);
            } else {
                textViewFinal.setText(getResources().getString(R.string.final_number_empty));
            }
        }
    }

    private void initChartGameNumbers() {
        gameNumbers = fetchGameNumbersFromDb();
        if (gameNumbers != null && !gameNumbers.isEmpty()) {
            LogUtils.d(TAG, String.valueOf(gameNumbers.size()));
            chartTabNumbers = new ArrayList<>();
            /*int combinationArray[] = {AppConstant.CHART_GAME_20_PAANA, AppConstant.CHART_GAME_30_PAANA, AppConstant.CHART_GAME_40_PAANA, AppConstant.CHART_GAME_50_PAANA,
                    AppConstant.CHART_GAME_60_PAANA, AppConstant.CHART_GAME_70_PAANA};*/
            for (int i = 0; i < gameNumbers.size(); i++) {
                ChartTabNumber chartTabNumber = new ChartTabNumber();
                int combinationCount = 0;
                String number = gameNumbers.get(i);
                chartTabNumber.setGameNo(number);
                if (i == 0) {
                    chartTabNumber.setSelected(true);
                }
                if (getUpdatedChartDetailList(number) != null) {
                    chartTabNumber.setChartDetailList(getUpdatedChartDetailList(number));
                }
                if (chartTabNumber.getChartDetailList() != null && !chartTabNumber.getChartDetailList().isEmpty()) {
                    for (ChartDetail chartDetail : chartTabNumber.getChartDetailList()) {
                        combinationCount = chartDetail.getCombinationCount();
                        LogUtils.d(TAG, "Combination Count " + combinationCount);
                        break;

                    }
                }
                chartTabNumber.setCombinationCount(combinationCount);

                chartTabNumbers.add(chartTabNumber);
            }
//            loadTabsAdapter();
            for (ChartTabNumber chartTabNumber : chartTabNumbers) {
                tabLayout.addTab(tabLayout.newTab().setText(chartTabNumber.getGameNo()));
            }
            loadNumberPager();
        }
    }

    private ArrayList<String> getTabs() {

        String[] numberTabArray = getResources().getStringArray(R.array.number_tab_array);

        return new ArrayList<>(Arrays.asList(numberTabArray));
    }

    /*private void loadTabsAdapter() {
        if (numberTabAdapter == null) {
            *//*numberTabAdapter = new NumberTabAdapter(this, numberTabs);*//*
            numberTabAdapter = new NumberTabAdapter(this, chartTabNumbers);
            recyclerViewTabs.setAdapter(numberTabAdapter);
        } else {
//            numberTabAdapter.notifyDataSetChanged();
            numberTabAdapter.updateData(chartTabNumbers);
        }
    }*/

    private ArrayList<String> fetchGameNumbersFromDb() {
        return (ArrayList<String>) ASOnlineApplication.appDatabase.chartDetailsDao().getGameNumbers();
    }

    private ArrayList<ChartDetail> getUpdatedChartDetailList(String gameNumber) {
        chartDetails = fetchChartDetailsFromDb(gameNumber);
        if (chartDetails != null && !chartDetails.isEmpty()) {
            LogUtils.d(TAG, String.valueOf(chartDetails.size()));
            chartDetailList = new ArrayList<>();
            for (ChartDetailsResponse chartDetailsResponse : chartDetails) {
                ChartDetail chartDetail = new ChartDetail();
                chartDetail.setGameNo(chartDetailsResponse.getGameNo());
                chartDetail.setSingleValue(chartDetailsResponse.getSingleValue());
                chartDetail.setChartName(chartDetailsResponse.getChartName());
                chartDetail.setCombinationCount(fetchChartPaanas(chartDetailsResponse.getChartName()).size());

                chartDetailList.add(chartDetail);
            }

            Set<ChartDetail> uniqueChartDetails = new LinkedHashSet<>(chartDetailList);

            ArrayList<ChartDetail> updatedChartDetailList = new ArrayList<>(uniqueChartDetails);

            LogUtils.d(TAG + "LIST", String.valueOf(updatedChartDetailList.size()));

            return updatedChartDetailList;
        }

        return null;
    }

    private void loadNumberPager() {
        if (numberPagerAdapter == null) {
            numberPagerAdapter = new NumberPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    chartTabNumbers);
            numberViewPager.setAdapter(numberPagerAdapter);
        } else {
            numberPagerAdapter.updateData(chartTabNumbers);
        }
    }

    private ArrayList<ChartDetailsResponse> fetchChartDetailsFromDb(String gameNumber) {
        return (ArrayList<ChartDetailsResponse>) ASOnlineApplication.appDatabase.chartDetailsDao().getChartDetailsWithGameNumber(gameNumber);
    }

    private ArrayList<String> fetchChartPaanas(String chartName) {
        return (ArrayList<String>) ASOnlineApplication.appDatabase.chartDetailsDao().getChartPaanas(chartName);
    }

    private ArrayList<AmountDetailsResponse> fetchAmountDetailsFromDb() {
        return (ArrayList<AmountDetailsResponse>) ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetailsWithGameId(gameId);
    }

    private void loadPointsAdapter() {
        if (selectPointsAdapter == null) {
            selectPointsAdapter = new SelectPointsAdapter(this, amountDetails);
            recyclerViewPoints.setAdapter(selectPointsAdapter);
        } else {
            selectPointsAdapter.notifyDataSetChanged();
        }
    }

    private void updateTotalPoints() {
        if (checkboxOTC.isChecked()) {
            btnTotal.setText(String.valueOf(getTotalPoints() * 2));
        } else {
            btnTotal.setText(String.valueOf(getTotalPoints()));
        }
    }

    private int getTotalPoints() {
        int totalPoints = 0;
        for (ChartTabNumber chartTabNumber : chartTabNumbers) {
            for (ChartDetail chartDetail : chartTabNumber.getChartDetailList()) {
                if (chartDetail.isSelected() && chartDetail.getPointsValue() > 0) {
                    totalPoints = totalPoints + chartDetail.getTotalPoint();
                }
            }
        }
        return totalPoints;
    }

    private void clearAllBetData() {
        gameNumbers.clear();
        chartTabNumbers.clear();
        initChartGameNumbers();
        btnTotal.setText("0");

    }

    private void applyBet() {
        if (getTotalPoints() > 0) {
            if (walletBalance >= getTotalPoints()) {
                showBetConfirmationDialog();
            } else {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.insufficient_points_error), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.empty_bet_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
        }
    }

    private void navigateToGameHistoryScreen() {
        Intent intent = new Intent(this, GameHistoryScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        startActivity(intent);
    }

    private void navigateToBaazaarListScreen() {
        Intent intent = new Intent(this, BaazaarNewListScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void disableGame() {
        btnBetOk.setVisibility(View.INVISIBLE);
//        genericNumberAdapter.isItemClickable = false;
        for (ChartTabNumber chartTabNumber : chartTabNumbers) {
            chartTabNumber.setItemClickable(false);
        }
        numberPagerAdapter.updateData(chartTabNumbers);
    }

    private void showBetTimeExpiredDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.bet_time_expired_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_BET_TIME_EXPIRED);
    }

    protected void dismissCustomSingleButtonDialog() {
        if (customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible() && !isFinishing()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    /**
     *
     */
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (ChartTabNumber chartTabNumber : chartTabNumbers) {
                chartTabNumber.setSelected(false);
            }

            chartTabNumbers.get(position).setSelected(true);
//            selectedIndex = -1;
//            numberTabAdapter.updateData(chartTabNumbers);
//            recyclerViewTabs.smoothScrollToPosition(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * TabSelected Listener
     */
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void callAddNewBetApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST, getAddNewBetOtcRequest());
        callAppServer(AppConstant.REQ_API_TYPE_ADD_NEW_BET, intent, true);
    }

    private AddNewBetOtcRequest getAddNewBetOtcRequest() {
        AddNewBetOtcRequest addNewBetOtcRequest = new AddNewBetOtcRequest();
        addNewBetOtcRequest.setBetRequests(getAddNewBetRequestList());

        return addNewBetOtcRequest;
    }

    private ArrayList<AddNewBetRequest> getAddNewBetRequestList() {
        ArrayList<AddNewBetRequest> addNewBetRequests = new ArrayList<>();
        addNewBetRequests.add(getAddNewBetRequest());
        if (checkboxOTC.isChecked()) {
            addNewBetRequests.add(getOTCBetRequest());
        }

        return addNewBetRequests;
    }

    private AddNewBetRequest getOTCBetRequest() {
        AddNewBetRequest addNewBetRequest = new AddNewBetRequest();
        addNewBetRequest.setBazaarId(Utils.getOTCBaazaarId(baazaarId));
        addNewBetRequest.setGameId(gameId);
        addNewBetRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        addNewBetRequest.setGameDate(DateUtils.getStringFormattedDate(new Date(), DateUtils.ADD_NEW_DATE_FORMAT));
        addNewBetRequest.setBetPaanaDataList(getBetPaanaDataList());
        addNewBetRequest.setStatus(true);
        addNewBetRequest.setCreatedBy(0);
        addNewBetRequest.setUpdatedBy(0);

        return addNewBetRequest;
    }

    private AddNewBetRequest getAddNewBetRequest() {
        AddNewBetRequest addNewBetRequest = new AddNewBetRequest();
        addNewBetRequest.setBazaarId(baazaarId);
        addNewBetRequest.setGameId(gameId);
        addNewBetRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        addNewBetRequest.setGameDate(DateUtils.getStringFormattedDate(new Date(), DateUtils.ADD_NEW_DATE_FORMAT));
        addNewBetRequest.setBetPaanaDataList(getBetPaanaDataList());
        addNewBetRequest.setStatus(true);
        addNewBetRequest.setCreatedBy(0);
        addNewBetRequest.setUpdatedBy(0);

        return addNewBetRequest;
    }

    private ArrayList<BetPaanaData> getBetPaanaDataList() {

        ArrayList<BetPaanaData> betPaanaDataList = new ArrayList<>();

        if (chartTabNumbers != null &&
                !chartTabNumbers.isEmpty()) {
            for (ChartTabNumber chartTabNumber : chartTabNumbers) {
                for (ChartDetail chartDetail : chartTabNumber.getChartDetailList()) {
                    if (chartDetail.isSelected() && chartDetail.getPointsValue() > 0) {
                        BetPaanaData betPaanaData = new BetPaanaData();
                        betPaanaData.setSelectedPaana(chartDetail.getChartName());
                        betPaanaData.setAmountPerPaana(chartDetail.getPointsValue());
                        betPaanaData.setTotalAmount(chartDetail.getTotalPoint());

                        betPaanaDataList.add(betPaanaData);
                    }
                }
            }
        }

        return betPaanaDataList;
    }

    private void callGetBaazaarRemainingTimeApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_BAAZAAR_REMAINING_TIME_REQUEST, getBaazaarRemainingTimeRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_BAAZAAR_REMAINING_TIME, intent, true);
    }

    private BaazaarRemainingTimeRequest getBaazaarRemainingTimeRequest() {
        BaazaarRemainingTimeRequest baazaarRemainingTimeRequest = new BaazaarRemainingTimeRequest();
        baazaarRemainingTimeRequest.setBaazaarId(baazaarId);
        /*Gson gson = new Gson();
        String jsonString = gson.toJson(baazaarRemainingTimeRequest);
        return RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
        return baazaarRemainingTimeRequest;
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (leftMilliSeconds == 0) {
                btnTimer.setText("00:00:00");
                handler.removeCallbacks(timerRunnable);
                showBetTimeExpiredDialog();
            } else {
                leftMilliSeconds = leftMilliSeconds - 1000;

                /*long seconds = leftMilliSeconds % 60;
                long minutes = (leftMilliSeconds / 60) % 60;
                long hours = (leftMilliSeconds / 3600);*/
                long seconds = leftMilliSeconds / 1000 % 60;
                long minutes = leftMilliSeconds / (60 * 1000) % 60;
                long hours = leftMilliSeconds / (60 * 60 * 1000) % 24;

                btnTimer.setText(String.format("%02d : %02d : %02d", hours, minutes, seconds));

                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof AddNewBetResponse) {
            AddNewBetResponse addNewBetResponse = (AddNewBetResponse) response;
            if (addNewBetResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.bet_placed_success_msg), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
                UserData userData = addNewBetResponse.getUserData();
                ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, userData.getPoints());
                walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
                btnWalletBalance.setText(String.valueOf(walletBalance));
                clearAllBetData();
            }
        } else if (response instanceof BaazaarRemainingTimeResponse) {
            BaazaarRemainingTimeResponse baazaarRemainingTimeResponse = (BaazaarRemainingTimeResponse) response;
            if (baazaarRemainingTimeResponse.getBaazaarRemainingTimeData() != null) {
                BaazaarRemainingTimeData baazaarRemainingTimeData = baazaarRemainingTimeResponse.getBaazaarRemainingTimeData();
                LogUtils.e(TAG, "Remaining Time :" + baazaarRemainingTimeData.getRemainingTime());
                if (baazaarRemainingTimeData.getRemainingTime() > 0) {
                    leftMilliSeconds = baazaarRemainingTimeData.getRemainingTime() * 1000;
                    handler.postDelayed(timerRunnable, 0);
                } else {
                    disableGame();
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
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    @Override
    public void onIndexSelectedListener(int index) {
        LogUtils.d(TAG, String.valueOf(index));

        ArrayList<ChartDetail> chartDetails = (ArrayList<ChartDetail>) chartTabNumbers.
                get(numberViewPager.getCurrentItem()).getChartDetailList();
        if (!chartDetails.get(index).isSelected()) {
            int selectedIndex = chartTabNumbers.get(numberViewPager.getCurrentItem()).getSelectedIndex();
            if (selectedIndex == -1) {
                selectedIndex = index;
                chartDetails.get(index).setSelected(true);
                chartTabNumbers.get(numberViewPager.getCurrentItem()).setSelectedIndex(selectedIndex);
                loadNumberPager();
//                NumberPagerFragment numberPagerFragment = (NumberPagerFragment) getSupportFragmentManager().getFragments().get(numberViewPager.getCurrentItem());
                NumberPagerFragment numberPagerFragment = (NumberPagerFragment) numberPagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
                numberPagerFragment.loadGenericNumberAdapter();
                numberPagerFragment.genericNumberAdapter.updateData(chartDetails);
            } else {
                if (chartDetails.get(selectedIndex).getPointsValue() > 0) {
                    selectedIndex = index;
                    chartDetails.get(index).setSelected(true);
                    chartTabNumbers.get(numberViewPager.getCurrentItem()).setSelectedIndex(selectedIndex);
                    loadNumberPager();
//                    NumberPagerFragment numberPagerFragment = (NumberPagerFragment) getSupportFragmentManager().getFragments().get(numberViewPager.getCurrentItem());
                    NumberPagerFragment numberPagerFragment = (NumberPagerFragment) numberPagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
                    numberPagerFragment.loadGenericNumberAdapter();
                    numberPagerFragment.genericNumberAdapter.updateData(chartDetails);
                }
            }
        } else {
            chartTabNumbers.get(numberViewPager.getCurrentItem()).setSelectedIndex(-1);
            chartDetails.get(index).setSelected(false);
            chartDetails.get(index).setPointsValue(0);
            chartDetails.get(index).setTotalPoint(0);
            updateTotalPoints();
            loadNumberPager();
            NumberPagerFragment numberPagerFragment = (NumberPagerFragment) numberPagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
            numberPagerFragment.loadGenericNumberAdapter();
            numberPagerFragment.genericNumberAdapter.updateData(chartDetails);
        }
    }

    /**
     * @param amountDetail
     */
    @Override
    public void onPointsSelectListener(AmountDetailsResponse amountDetail) {
        if (amountDetail != null) {
            int selectedIndex = chartTabNumbers.get(numberViewPager.getCurrentItem()).getSelectedIndex();
            if (selectedIndex != -1) {
                ArrayList<ChartDetail> chartDetails = (ArrayList<ChartDetail>) chartTabNumbers.
                        get(numberViewPager.getCurrentItem()).getChartDetailList();
//                NumberPagerFragment numberPagerFragment = (NumberPagerFragment) getSupportFragmentManager().getFragments().get(numberViewPager.getCurrentItem());
                NumberPagerFragment numberPagerFragment = (NumberPagerFragment) numberPagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
                int totalPoints = chartDetails.get(selectedIndex).getPointsValue() + amountDetail.getAmountValue();
                numberPagerFragment.loadGenericNumberAdapter();
                if (totalPoints <= AppConstant.MAX_SINGLE_GAME_BET_POINTS) {
                    chartDetails.get(selectedIndex).setPointsValue(totalPoints);
                    chartDetails.get(selectedIndex).setTotalPoint(chartTabNumbers.get(numberViewPager.getCurrentItem()).getCombinationCount() * totalPoints);
                    updateTotalPoints();
                    loadNumberPager();
                    numberPagerFragment.genericNumberAdapter.updateData(chartDetails);
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.maximum_single_game_bet_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                }
            }
        }
    }

    /**
     * @param index
     */
    @Override
    public void onTabSelected(int index) {

        /*for (ChartTabNumber chartTabNumber : chartTabNumbers) {
            chartTabNumber.setSelected(false);
        }

        chartTabNumbers.get(index).setSelected(true);
//        selectedIndex = -1;
        numberTabAdapter.updateData(chartTabNumbers);*/
        numberViewPager.setCurrentItem(index, true);
    }

    /**
     * CustomDualButtonDialgoListener
     */
    @Override
    public void onClickYesButtonListener() {
        dismissCustomDualButtonDialog();
        LogUtils.e(TAG, "On Click Yes Btn");
        callAddNewBetApi();
    }

    @Override
    public void onClickNoButtonListener() {
        dismissCustomDualButtonDialog();
    }

    //CustomSingleButtonDialogListener
    @Override
    public void onClickSingleButtonListener() {
        dismissCustomSingleButtonDialog();
        navigateToBaazaarListScreen();
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblClearAll:
                clearAllBetData();
                break;
            case R.id.btnBetOk:
                applyBet();
                break;
            case R.id.lblHistory:
                navigateToGameHistoryScreen();
                break;
            case R.id.lblBaazaarHistory:
                navigateToBaazaarHistoryScreen();
                break;
            case R.id.checkboxOTC:
                checkboxOTC.setChecked(checkboxOTC.isChecked());
                updateTotalPoints();
                break;
        }
    }
}