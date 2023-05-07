package com.pinnacle.winwin.ui.paanagame;

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
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.PaanaDetailsResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.paanagame.adapter.PaanaNumberPagerAdapter;
import com.pinnacle.winwin.ui.paanagame.listener.PaanaGroupCheckBoxListener;
import com.pinnacle.winwin.ui.paanagame.listener.PaanaTypeGroupListener;
import com.pinnacle.winwin.ui.paanagame.model.PaanaGroupModel;
import com.pinnacle.winwin.ui.singlegame.adapter.SelectPointsAdapter;
import com.pinnacle.winwin.ui.singlegame.fragment.CustomDualButtonDialogFragment;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.singlegame.listener.SelectPointsListener;
import com.pinnacle.winwin.ui.singlegame.listener.SingleGameNumberListener;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;

public class PaanaNumberSwipeableScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, SingleGameNumberListener, SelectPointsListener, CustomDualButtonDialgoListener,
        CustomSingleButtonDialogListener, PaanaGroupCheckBoxListener, PaanaTypeGroupListener {

    private static final String TAG = PaanaNumberSwipeableScreenActivity.class.getSimpleName();

    private AppCompatTextView lblPoints;
    private AppCompatTextView lblSingleGame;
    private AppCompatTextView lblTimer;
    private AppCompatTextView lblClearAll;
    private AppCompatTextView lblHistory;
    private AppCompatTextView lblTotal;
    private AppCompatTextView textViewSelectPaana;
    private AppCompatTextView textViewFinal;
    private AppCompatTextView lblFinalNumber;
    private AppCompatTextView lblBaazaarHistory;

    private Button btnWalletBalance;
    private Button btnTimer;
    private Button btnTotal;
    private Button btnBetOk;

    private TabLayout tabLayout;

    private ViewPager numberViewPager;

    private PaanaNumberPagerAdapter pagerAdapter;

    private RecyclerView recyclerViewPoints;
    private SelectPointsAdapter selectPointsAdapter;

    private RelativeLayout layoutOTCCheckbox;

    private AppCompatCheckBox checkboxOTC;

    private View parentLayout;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;
    private CustomDualButtonDialogFragment mCustomDualButtonDialogFragment;

    private ArrayList<PaanaDetailsResponse> paanaDetails;
    private ArrayList<PaanaGroupModel> paanaGroupList;
    private ArrayList<PaanaDetailsResponse> spPaanaDetails;
    private ArrayList<PaanaDetailsResponse> dpPaanaDetails;
    private ArrayList<AmountDetailsResponse> amountDetails;

    private int walletBalance;
    private int selectedIndex = -1;
    private int selectedGroupIndex = -1;
    private int baazaarId = -1;
    private int gameId = -1;
    private int selectedPaanaNumber = -1;
    private int totalPoints = 0;
    private int selectedPoints = 0;
    private String finalNumber;
    private long leftMilliSeconds;
    private String lastResult;
    private Handler handler;
    private boolean isSPChecked;
    private boolean isDPChecked;
    private int spPoints = 0;
    private int dpPoints = 0;
    public int totalSPPoints = 0;
    private int totalDPPoints = 0;
    private int totalGroupPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paana_number_swipeable_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
        updateFinalOrLastResult();
        handler = new Handler();
        paanaDetails = fetchPaanaDetailsFromDb();
        paanaGroupList = getPaanaGroupList();

        loadNumberPager();

        amountDetails = fetchAmountDetailsFromDb();
        if (amountDetails != null && !amountDetails.isEmpty()) {
            loadPointsAdapter();
        }

        spPaanaDetails = fetchSPPaanaDetailsFromDb();
        dpPaanaDetails = fetchDPPaanaDetailsFromDb();
        LogUtils.d(TAG, "SP " + String.valueOf(spPaanaDetails.size()));
        LogUtils.d(TAG, "DP " + String.valueOf(dpPaanaDetails.size()));
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
        lblSingleGame.setText(getResources().getString(R.string.title_paana_game));
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
        if (finalNumber != null && !finalNumber.isEmpty()) {
            textViewFinal.setText(finalNumber);
        } else {
            textViewFinal.setText(getResources().getString(R.string.final_number_empty));
        }

        lblFinalNumber = findViewById(R.id.lblFinalNumber);
        lblFinalNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblFinalNumber.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblBaazaarHistory = findViewById(R.id.lblBaazaarHistory);
        lblBaazaarHistory.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblBaazaarHistory.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        lblBaazaarHistory.setOnClickListener(this);

        tabLayout = findViewById(R.id.tabLayout);

        numberViewPager = findViewById(R.id.numberViewPager);
        numberViewPager.addOnPageChangeListener(pageChangeListener);
        tabLayout.setupWithViewPager(numberViewPager);

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

            if (getIntent().hasExtra(AppConstant.KEY_SELECTED_PAANA_NUMBER)) {
                selectedPaanaNumber = getIntent().getIntExtra(AppConstant.KEY_SELECTED_PAANA_NUMBER, -1);
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

    private ArrayList<PaanaDetailsResponse> fetchPaanaDetailsFromDb() {
        return (ArrayList<PaanaDetailsResponse>) ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetailsWithSingleValue(selectedPaanaNumber);
    }

    private ArrayList<PaanaDetailsResponse> fetchSPPaanaDetailsFromDb() {
        return (ArrayList<PaanaDetailsResponse>) ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetailsWithSingleValueAndPaanaType(selectedPaanaNumber, AppConstant.PAANA_TYPE_SP);
    }

    private ArrayList<PaanaDetailsResponse> fetchDPPaanaDetailsFromDb() {
        return (ArrayList<PaanaDetailsResponse>) ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetailsWithSingleValueAndPaanaType(selectedPaanaNumber, AppConstant.PAANA_TYPE_DP);
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

    private void loadNumberPager() {
        if (pagerAdapter == null) {
            pagerAdapter = new PaanaNumberPagerAdapter(getSupportFragmentManager(),
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, paanaDetails, paanaGroupList, selectedPaanaNumber);
            numberViewPager.setAdapter(pagerAdapter);
        } else {
            pagerAdapter.updateData(paanaDetails, paanaGroupList);
        }
    }

    private ArrayList<PaanaGroupModel> getPaanaGroupList() {
        ArrayList<PaanaGroupModel> paanaGroupList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            PaanaGroupModel paanaGroupModel = new PaanaGroupModel();
            if (i == 0) {
                paanaGroupModel.setPaanaType(AppConstant.PAANA_TYPE_SP);
            } else {
                paanaGroupModel.setPaanaType(AppConstant.PAANA_TYPE_DP);
            }
            paanaGroupList.add(paanaGroupModel);
        }

        return paanaGroupList;
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
        for (PaanaDetailsResponse paanaDetail : paanaDetails) {
            if (paanaDetail.isSelected()) {
                totalPoints = totalPoints + paanaDetail.getPointsValue();
            }
        }

        return totalPoints;
    }

    private void updateTotalGroupPoints() {
        if (checkboxOTC.isChecked()) {
            btnTotal.setText(String.valueOf(getTotalGroupPoints() * 2));
        } else {
            btnTotal.setText(String.valueOf(getTotalGroupPoints()));
        }
    }

    private int getTotalGroupPoints() {
        int totalGroupPoints = 0;
        for (PaanaGroupModel paanaGroupModel : paanaGroupList) {
            if (paanaGroupModel.isSelected()) {
                totalGroupPoints = totalGroupPoints + paanaGroupModel.getTotalPaanaPoints();
            }
        }

        return totalGroupPoints;
    }

    private void clearAllBetData() {
        paanaDetails.clear();
        paanaDetails = fetchPaanaDetailsFromDb();
        selectedIndex = -1;
        checkboxOTC.setChecked(false);
        updateTotalPoints();
//        genericNumberAdapter.updateData(paanaDetails);
        loadNumberPager();
    }

    private void clearAllGroupBetData() {
        paanaGroupList.clear();
        paanaGroupList = getPaanaGroupList();
        selectedGroupIndex = -1;
        checkboxOTC.setChecked(false);
        updateTotalGroupPoints();
        loadNumberPager();
    }

    private void applyBet() {
        if (getSelectedNumberList().size() > 0) {

            if (getSelectedNumberWithoutPoints() != -1) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.point_error), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            } else {
                showBetConfirmationDialog();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.empty_bet_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
        }
    }

    private ArrayList<PaanaDetailsResponse> getSelectedNumberList() {
        ArrayList<PaanaDetailsResponse> paanaDetailsResponses = new ArrayList<>();
        if (paanaDetails != null && !paanaDetails.isEmpty()) {
            for (PaanaDetailsResponse paanaDetail : paanaDetails) {
                if (paanaDetail.isSelected()) {
                    paanaDetailsResponses.add(paanaDetail);
                }
            }
        }

        return paanaDetailsResponses;
    }

    private int getSelectedNumberWithoutPoints() {
        int index = -1;
        if (paanaDetails != null && !paanaDetails.isEmpty()) {
            for (int i = 0; i < paanaDetails.size(); i++) {
                PaanaDetailsResponse paanaDetail = paanaDetails.get(i);
                if (paanaDetail.isSelected() &&
                        paanaDetail.getPointsValue() == 0) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    private void applyGroupBet() {
        LogUtils.d(TAG, "Group Bet");
        if (getSelectedPaanaTypeList().size() > 0) {
            LogUtils.d(TAG, "Checked either of them");
            if (getSelectedPaanaTypeWithoutPoints() != -1) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.paana_group_point_error), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            } else {
                showBetConfirmationDialog();
            }

        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.paana_checkbox_select_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
        }
    }

    private ArrayList<PaanaGroupModel> getSelectedPaanaTypeList() {
        ArrayList<PaanaGroupModel> paanaGroupModels = new ArrayList<>();

        if (paanaGroupList != null && !paanaGroupList.isEmpty()) {
            for (PaanaGroupModel paanaGroupModel : paanaGroupList) {
                if (paanaGroupModel.isSelected()) {
                    paanaGroupModels.add(paanaGroupModel);
                }
            }
        }

        return paanaGroupModels;
    }

    private int getSelectedPaanaTypeWithoutPoints() {
        int index = -1;
        if (paanaGroupList != null && !paanaGroupList.isEmpty()) {
            for (int i = 0; i < paanaGroupList.size(); i++) {
                PaanaGroupModel paanaGroupModel = paanaGroupList.get(i);
                if (paanaGroupModel.isSelected() &&
                        paanaGroupModel.getPointsValue() == 0) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    private void disableGame() {
        btnBetOk.setVisibility(View.INVISIBLE);
//        genericNumberAdapter.isItemClickable = false;
        if (numberViewPager.getCurrentItem() == 0) {
            PaanaIndividualPagerFragment paanaIndividualPagerFragment = (PaanaIndividualPagerFragment) pagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
            paanaIndividualPagerFragment.genericNumberAdapter.isItemClickable = false;
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

    private void showBetTimeExpiredDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.bet_time_expired_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_BET_TIME_EXPIRED);
    }

    private void dismissCustomSingleButtonDialog() {
        if (customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible() && !isFinishing()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }


    private void showClearBetDialog() {
        dismissClearBetDialog();
        mCustomDualButtonDialogFragment = CustomDualButtonDialogFragment.newInstance(getResources().getString(R.string.clear_bet_msg));
        mCustomDualButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_CLEAR_BET);
    }

    private void dismissClearBetDialog() {
        if (mCustomDualButtonDialogFragment != null &&
                mCustomDualButtonDialogFragment.isVisible() && !isFinishing()) {
            mCustomDualButtonDialogFragment.dismissAllowingStateLoss();
            mCustomDualButtonDialogFragment = null;
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LogUtils.d(TAG, String.valueOf(position));
            if (position == numberViewPager.getCurrentItem() && getTotalPoints() > 0) {
                numberViewPager.setCurrentItem(0, true);
                if (mCustomDualButtonDialogFragment == null) {
                    showClearBetDialog();
                }
                LogUtils.d(TAG, "Are you sure you want to continue without placing a bet");
            } else if (position == numberViewPager.getCurrentItem() && getTotalGroupPoints() > 0) {
                numberViewPager.setCurrentItem(1, true);
                if (mCustomDualButtonDialogFragment == null) {
                    showClearBetDialog();
                }
                LogUtils.d(TAG, "GROUP Are you sure you want to continue without placing a bet");
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void callAddNewBetApi() {
        Intent intent = new Intent();
        /*intent.putExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST, getAddNewBetRequest());*/
        intent.putExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST, getAddNewBetOtcRequest());
        callAppServer(AppConstant.REQ_API_TYPE_ADD_NEW_BET, intent, true);
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

    private AddNewBetOtcRequest getAddNewBetOtcRequest() {
        AddNewBetOtcRequest addNewBetOtcRequest = new AddNewBetOtcRequest();
        addNewBetOtcRequest.setBetRequests(getAddNewBetRequestList());

        return addNewBetOtcRequest;
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

    private ArrayList<AddNewBetRequest> getAddNewBetRequestList() {
        ArrayList<AddNewBetRequest> addNewBetRequests = new ArrayList<>();
        addNewBetRequests.add(getAddNewBetRequest());
        if (checkboxOTC.isChecked()) {
            addNewBetRequests.add(getOTCBetRequest());
        }

        return addNewBetRequests;
    }

    private ArrayList<BetPaanaData> getBetPaanaDataList() {

        ArrayList<BetPaanaData> betPaanaDataList = new ArrayList<>();

        if (paanaDetails != null &&
                !paanaDetails.isEmpty()) {
            for (PaanaDetailsResponse paanaDetail : paanaDetails) {
                if (paanaDetail.isSelected() &&
                        paanaDetail.getPointsValue() > 0) {
                    BetPaanaData betPaanaData = new BetPaanaData();
                    betPaanaData.setSelectedPaana(paanaDetail.getPaanaNo());
                    betPaanaData.setAmountPerPaana(paanaDetail.getPointsValue());
                    betPaanaData.setTotalAmount(paanaDetail.getPointsValue());
                    betPaanaData.setPaanaType(paanaDetail.getPaanaType());

                    betPaanaDataList.add(betPaanaData);
                }
            }
        }

        return betPaanaDataList;
    }

    private void callAddNewGroupBetApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST, getAddNewGroupBetOtcRequest());
        callAppServer(AppConstant.REQ_API_TYPE_ADD_NEW_BET, intent, true);
    }

    private AddNewBetOtcRequest getAddNewGroupBetOtcRequest() {
        AddNewBetOtcRequest addNewBetOtcRequest = new AddNewBetOtcRequest();
        addNewBetOtcRequest.setBetRequests(getAddNewGroupBetRequestList());

        return addNewBetOtcRequest;
    }

    private ArrayList<AddNewBetRequest> getAddNewGroupBetRequestList() {
        ArrayList<AddNewBetRequest> addNewBetRequests = new ArrayList<>();
        addNewBetRequests.add(getAddNewGroupBetRequest());
        if (checkboxOTC.isChecked()) {
            addNewBetRequests.add(getOTCGroupBetRequest());
        }

        return addNewBetRequests;
    }

    private AddNewBetRequest getAddNewGroupBetRequest() {
        AddNewBetRequest addNewBetRequest = new AddNewBetRequest();
        addNewBetRequest.setBazaarId(baazaarId);
        addNewBetRequest.setGameId(gameId);
        addNewBetRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        addNewBetRequest.setGameDate(DateUtils.getStringFormattedDate(new Date(), DateUtils.ADD_NEW_DATE_FORMAT));
        addNewBetRequest.setBetPaanaDataList(getGroupBetPaanaDataList());
        addNewBetRequest.setStatus(true);
        addNewBetRequest.setCreatedBy(0);
        addNewBetRequest.setUpdatedBy(0);

        return addNewBetRequest;
    }

    private AddNewBetRequest getOTCGroupBetRequest() {
        AddNewBetRequest addNewBetRequest = new AddNewBetRequest();
        addNewBetRequest.setBazaarId(Utils.getOTCBaazaarId(baazaarId));
        addNewBetRequest.setGameId(gameId);
        addNewBetRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        addNewBetRequest.setGameDate(DateUtils.getStringFormattedDate(new Date(), DateUtils.ADD_NEW_DATE_FORMAT));
        addNewBetRequest.setBetPaanaDataList(getGroupBetPaanaDataList());
        addNewBetRequest.setStatus(true);
        addNewBetRequest.setCreatedBy(0);
        addNewBetRequest.setUpdatedBy(0);

        return addNewBetRequest;
    }

    private ArrayList<BetPaanaData> getGroupBetPaanaDataList() {

        ArrayList<BetPaanaData> betPaanaDataList = new ArrayList<>();

        if (paanaGroupList != null && !paanaGroupList.isEmpty()) {
            for (PaanaGroupModel paanaGroupModel : paanaGroupList) {
                if (paanaGroupModel.getPaanaType().equals(AppConstant.PAANA_TYPE_SP) && paanaGroupModel.isSelected()) {
                    if (spPaanaDetails != null &&
                            !spPaanaDetails.isEmpty()) {
                        for (PaanaDetailsResponse paanaDetail : spPaanaDetails) {
                            BetPaanaData betPaanaData = new BetPaanaData();
                            betPaanaData.setSelectedPaana(paanaDetail.getPaanaNo());
                            betPaanaData.setAmountPerPaana(paanaGroupModel.getPointsValue());
                            betPaanaData.setTotalAmount(paanaGroupModel.getPointsValue());
                            betPaanaData.setPaanaType(paanaDetail.getPaanaType());

                            betPaanaDataList.add(betPaanaData);
                        }
                    }
                } else if (paanaGroupModel.getPaanaType().equals(AppConstant.PAANA_TYPE_DP) && paanaGroupModel.isSelected()) {
                    if (dpPaanaDetails != null &&
                            !dpPaanaDetails.isEmpty()) {
                        for (PaanaDetailsResponse paanaDetail : dpPaanaDetails) {
                            BetPaanaData betPaanaData = new BetPaanaData();
                            betPaanaData.setSelectedPaana(paanaDetail.getPaanaNo());
                            betPaanaData.setAmountPerPaana(paanaGroupModel.getPointsValue());
                            betPaanaData.setTotalAmount(paanaGroupModel.getPointsValue());
                            betPaanaData.setPaanaType(paanaDetail.getPaanaType());

                            betPaanaDataList.add(betPaanaData);
                        }
                    }
                }
            }

        }

        return betPaanaDataList;
    }

    private void callGetBaazaarRemainingTimeApi() {
        Intent intent = new Intent();
//        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
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

    /**
     * @param response
     */
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
                if (numberViewPager.getCurrentItem() == 0) {
                    clearAllBetData();
                } else if (numberViewPager.getCurrentItem() == 1) {
                    clearAllGroupBetData();
                }
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

    /**
     * @param index
     */
    @Override
    public void onIndexSelectedListener(int index) {

        PaanaIndividualPagerFragment paanaIndividualPagerFragment = (PaanaIndividualPagerFragment) pagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
        if (!paanaDetails.get(index).isSelected()) {
            if (selectedIndex == -1) {
                selectedIndex = index;
                paanaDetails.get(index).setSelected(true);
                paanaIndividualPagerFragment.loadGenericNumberAdapter();
                paanaIndividualPagerFragment.genericNumberAdapter.updateData(paanaDetails);
            } else {
                if (paanaDetails.get(selectedIndex).getPointsValue() > 0) {
                    selectedIndex = index;
                    paanaDetails.get(index).setSelected(true);
                    paanaIndividualPagerFragment.loadGenericNumberAdapter();
                    paanaIndividualPagerFragment.genericNumberAdapter.updateData(paanaDetails);
                }
            }
        } else {
            paanaDetails.get(index).setSelected(false);
            paanaDetails.get(index).setPointsValue(0);
            updateTotalPoints();
            selectedIndex = -1;
            paanaIndividualPagerFragment.loadGenericNumberAdapter();
            paanaIndividualPagerFragment.genericNumberAdapter.updateData(paanaDetails);
        }
    }

    /**
     * @param amountDetail
     */
    @Override
    public void onPointsSelectListener(AmountDetailsResponse amountDetail) {
        if (amountDetail != null) {
            if (numberViewPager.getCurrentItem() == 0) {
                if (selectedIndex != -1) {
                    PaanaIndividualPagerFragment paanaIndividualPagerFragment = (PaanaIndividualPagerFragment) pagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
                    int totalPoints = paanaDetails.get(selectedIndex).getPointsValue() + amountDetail.getAmountValue();
                    if (totalPoints <= AppConstant.MAX_PAANA_GAME_BET_POINTS) {
                        paanaDetails.get(selectedIndex).setPointsValue(totalPoints);
                        updateTotalPoints();
                        paanaIndividualPagerFragment.loadGenericNumberAdapter();
                        paanaIndividualPagerFragment.genericNumberAdapter.updateData(paanaDetails);
                    } else {
                        Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.maximum_paana_game_bet_error),
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    }
                }
            } else if (numberViewPager.getCurrentItem() == 1) {
                /*if (isSPChecked || isDPChecked) {
                    if (isSPChecked) {
                        spPoints += amountDetail.getAmountValue();
                        totalSPPoints = spPaanaDetails.size() * spPoints;
                        *//*for (PaanaDetailsResponse paanaDetailsResponse : spPaanaDetails) {
                            paanaDetailsResponse.setSelected(true);
                            paanaDetailsResponse.setPointsValue(spPoints);
                        }*//*
                    }
                    if (isDPChecked) {
                        dpPoints += amountDetail.getAmountValue();
                        totalDPPoints = dpPaanaDetails.size() * dpPoints;
                        *//*for (PaanaDetailsResponse paanaDetailsResponse : spPaanaDetails) {
                            paanaDetailsResponse.setSelected(true);
                            paanaDetailsResponse.setPointsValue(spPoints);
                        }*//*
                    }
                    totalGroupPoints = totalSPPoints + totalDPPoints;
                    updateTotalGroupPoints();
                }*/
                if (selectedGroupIndex != -1) {
                    PaanaGroupPagerFragment paanaGroupPagerFragment = (PaanaGroupPagerFragment) pagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
                    PaanaGroupModel paanaGroupModel = paanaGroupList.get(selectedGroupIndex);
                    int paanaPoints = paanaGroupModel.getPointsValue() + amountDetail.getAmountValue();
                    paanaGroupModel.setPointsValue(paanaPoints);

                    switch (paanaGroupModel.getPaanaType()) {
                        case AppConstant.PAANA_TYPE_SP:
                            paanaGroupModel.setTotalPaanaPoints(paanaPoints * spPaanaDetails.size());
                            break;
                        case AppConstant.PAANA_TYPE_DP:
                            paanaGroupModel.setTotalPaanaPoints(paanaPoints * dpPaanaDetails.size());
                            break;
                    }
                    updateTotalGroupPoints();
                    paanaGroupPagerFragment.loadPaanaTypeGroupAdapter();
                    paanaGroupPagerFragment.paanaTypeGroupAdapter.updateData(paanaGroupList);
                }
            }
        }
    }

    /**
     * CustomDualButtonDialgoListener
     */
    @Override
    public void onClickYesButtonListener() {
//        dismissCustomDualButtonDialog();
        LogUtils.e(TAG, "On Click Yes Btn");
        if (mCustomDualButtonDialogFragment != null) {
            if (mCustomDualButtonDialogFragment.getTag() != null) {
                if (mCustomDualButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_CLEAR_BET)) {
                    dismissClearBetDialog();
                    LogUtils.d(TAG, "CLEAR DATA");
                    if (numberViewPager.getCurrentItem() == 0) {
                        clearAllBetData();
                        numberViewPager.setCurrentItem(1, true);
                    } else if (numberViewPager.getCurrentItem() == 1) {
                        clearAllGroupBetData();
                        numberViewPager.setCurrentItem(0, true);
                    }
                }
            }
        } else if (customDualButtonDialogFragment != null) {
            dismissCustomDualButtonDialog();
            if (numberViewPager.getCurrentItem() == 0) {
                callAddNewBetApi();
            } else if (numberViewPager.getCurrentItem() == 1) {
                callAddNewGroupBetApi();
            }
        }
    }

    @Override
    public void onClickNoButtonListener() {
        if (mCustomDualButtonDialogFragment != null) {
            dismissClearBetDialog();
        } else if (customDualButtonDialogFragment != null) {
            dismissCustomDualButtonDialog();
        }
    }

    /**
     * CustomSingleButtonDialogListener
     */
    @Override
    public void onClickSingleButtonListener() {
        dismissCustomSingleButtonDialog();
        navigateToBaazaarListScreen();
    }

    /**
     * PaanaTypeGroupListener
     *
     * @param index
     */
    @Override
    public void onPaanaTypeCheckedChanged(int index) {
        PaanaGroupPagerFragment paanaGroupPagerFragment = (PaanaGroupPagerFragment) pagerAdapter.instantiateItem(numberViewPager, numberViewPager.getCurrentItem());
        if (!paanaGroupList.get(index).isSelected()) {
            if (selectedGroupIndex == -1) {
                selectedGroupIndex = index;
                paanaGroupList.get(index).setSelected(true);
                paanaGroupPagerFragment.loadPaanaTypeGroupAdapter();
                paanaGroupPagerFragment.paanaTypeGroupAdapter.updateData(paanaGroupList);
            } else {
                if (paanaGroupList.get(selectedGroupIndex).getPointsValue() > 0) {
                    selectedGroupIndex = index;
                    paanaGroupList.get(index).setSelected(true);
                    paanaGroupPagerFragment.loadPaanaTypeGroupAdapter();
                    paanaGroupPagerFragment.paanaTypeGroupAdapter.updateData(paanaGroupList);
                }
            }
        } else {
            paanaGroupList.get(index).setSelected(false);
            paanaGroupList.get(index).setPointsValue(0);
            paanaGroupList.get(index).setTotalPaanaPoints(0);
            updateTotalGroupPoints();
            selectedGroupIndex = -1;
            paanaGroupPagerFragment.loadPaanaTypeGroupAdapter();
            paanaGroupPagerFragment.paanaTypeGroupAdapter.updateData(paanaGroupList);
        }
    }

    /**
     * PaanaGroupCheckBoxListener
     *
     * @param isChecked
     */
    @Override
    public void onSPCheckedChangeListener(boolean isChecked) {
        isSPChecked = isChecked;
        LogUtils.d(TAG, "SP " + isChecked);
        if (!isSPChecked && totalSPPoints > 0) {
            spPoints = 0;
            totalSPPoints = 0;
            /*for (PaanaDetailsResponse paanaDetailsResponse : spPaanaDetails) {
                paanaDetailsResponse.setSelected(false);
                paanaDetailsResponse.setPointsValue(spPoints);
            }*/
            if (isDPChecked) {
                totalGroupPoints = totalDPPoints;
            } else {
                totalGroupPoints = totalSPPoints;
            }
            updateTotalGroupPoints();
        }
    }

    @Override
    public void onDPCheckedChangeListener(boolean isChecked) {
        isDPChecked = isChecked;
        LogUtils.d(TAG, "DP " + isChecked);
        if (!isDPChecked && totalDPPoints > 0) {
            dpPoints = 0;
            totalDPPoints = 0;
            if (isSPChecked) {
                totalGroupPoints = totalSPPoints;
            } else {
                totalGroupPoints = totalDPPoints;
            }
            updateTotalGroupPoints();
        }
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblClearAll:
                if (numberViewPager.getCurrentItem() == 0) {
                    clearAllBetData();
                } else if (numberViewPager.getCurrentItem() == 1) {
                    clearAllGroupBetData();
                }
                break;
            case R.id.btnBetOk:
                if (walletBalance >= getTotalPoints()) {
                    if (numberViewPager.getCurrentItem() == 0) {
                        applyBet();
                    } else if (numberViewPager.getCurrentItem() == 1) {
                        applyGroupBet();
                    }
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.insufficient_points_error), Snackbar.LENGTH_LONG,
                            getResources().getString(R.string.btn_okay)).show();
                }
                break;
            case R.id.lblHistory:
                navigateToGameHistoryScreen();
                break;
            case R.id.lblBaazaarHistory:
                navigateToBaazaarHistoryScreen();
                break;
            case R.id.checkboxOTC:
                checkboxOTC.setChecked(checkboxOTC.isChecked());
                if (numberViewPager.getCurrentItem() == 1) {
                    updateTotalGroupPoints();
                } else {
                    updateTotalPoints();
                }
                break;
        }
    }
}