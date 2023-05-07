package com.pinnacle.winwin.ui.commongame;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
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
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.adapter.GenericNumberAdapter;
import com.pinnacle.winwin.ui.singlegame.adapter.SelectPointsAdapter;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.singlegame.listener.SelectPointsListener;
import com.pinnacle.winwin.ui.singlegame.listener.SingleGameNumberListener;
import com.pinnacle.winwin.ui.singlegame.model.SingleGameNumberModel;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;

public class CommonGameScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, SingleGameNumberListener, SelectPointsListener, CustomDualButtonDialgoListener,
        CustomSingleButtonDialogListener {

    private static final String TAG = CommonGameScreenActivity.class.getSimpleName();

    private AppCompatTextView lblPoints;
    private AppCompatTextView lblSingleGame;
    private AppCompatTextView lblTimer;
    private AppCompatTextView lblClearAll;
    private AppCompatTextView lblHistory;
    private AppCompatTextView lblTotal;
//    private AppCompatTextView textViewMotorPoints;
    private AppCompatTextView textViewFinal;
    private AppCompatTextView lblFinalNumber;
    private AppCompatTextView lblBaazaarHistory;

    private Button btnWalletBalance;
    private Button btnTimer;
    private Button btnTotal;
    private Button btnBetOk;

    private RecyclerView recyclerViewNumbers;
    private GenericNumberAdapter genericNumberAdapter;

    private RecyclerView recyclerViewPoints;
    private SelectPointsAdapter selectPointsAdapter;

    private RelativeLayout layoutCheckbox;

    private AppCompatCheckBox checkboxSP;
    private AppCompatCheckBox checkboxDP;
    private AppCompatCheckBox checkboxTP;

    private View parentLayout;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private ArrayList<SingleGameNumberModel> commonGameNumberList;

    private ArrayList<AmountDetailsResponse> amountDetails;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_game_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
        updateFinalOrLastResult();
        handler = new Handler();
        commonGameNumberList = getCommonGameNumberList();
        if (commonGameNumberList != null && !commonGameNumberList.isEmpty()) {
            loadGenericNumberAdapter();
        }
        amountDetails = fetchAmountDetailsFromDb();
        if (amountDetails != null && !amountDetails.isEmpty()) {
            loadPointsAdapter();
        }

        callGetBaazaarRemainingTimeApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        lblPoints = findViewById(R.id.lblPoints);
        lblPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblSingleGame = findViewById(R.id.lblSingleGame);
        lblSingleGame.setTypeface(Utils.getTypeFaceBodoni72OS(this));
        lblSingleGame.setText(getResources().getString(R.string.title_common_game));
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

        recyclerViewNumbers = findViewById(R.id.recyclerViewNumbers);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerViewNumbers.setLayoutManager(gridLayoutManager);

        recyclerViewPoints = findViewById(R.id.recyclerViewPoints);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        layoutCheckbox = findViewById(R.id.layoutCheckbox);
        layoutCheckbox.setVisibility(View.VISIBLE);

        checkboxSP = findViewById(R.id.checkboxSP);
        checkboxSP.setOnClickListener(this);
        checkboxSP.setTypeface(Utils.getTypeFaceBodoni72(this));
        checkboxSP.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        checkboxDP = findViewById(R.id.checkboxDP);
        checkboxDP.setOnClickListener(this);
        checkboxDP.setTypeface(Utils.getTypeFaceBodoni72(this));
        checkboxDP.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        checkboxTP = findViewById(R.id.checkboxTP);
        checkboxTP.setOnClickListener(this);
        checkboxTP.setTypeface(Utils.getTypeFaceBodoni72(this));
        checkboxTP.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        /*textViewMotorPoints = findViewById(R.id.textViewMotorPoints);
        textViewMotorPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewMotorPoints.setVisibility(View.VISIBLE);*/

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

    private ArrayList<AmountDetailsResponse> fetchAmountDetailsFromDb() {
        return (ArrayList<AmountDetailsResponse>) ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetailsWithGameId(gameId);
    }

    private ArrayList<SingleGameNumberModel> getCommonGameNumberList() {

        ArrayList<SingleGameNumberModel> motorGameNumberList = new ArrayList<>();

        String[] commonGameNumberArray = getResources().getStringArray(R.array.game_number_array);

        for (int i = 0; i < commonGameNumberArray.length; i++) {
            SingleGameNumberModel commonGameNumberModel = new SingleGameNumberModel();
            commonGameNumberModel.setNumber(commonGameNumberArray[i]);
            commonGameNumberModel.setPointsValue(0);
            commonGameNumberModel.setSelected(false);

            motorGameNumberList.add(commonGameNumberModel);
        }

        return motorGameNumberList;
    }

    private void loadGenericNumberAdapter() {
        if (genericNumberAdapter == null) {
            genericNumberAdapter = new GenericNumberAdapter(this, commonGameNumberList, gameId);
            recyclerViewNumbers.setAdapter(genericNumberAdapter);
        } else {
            genericNumberAdapter.notifyDataSetChanged();
        }
    }

    private void loadPointsAdapter() {
        if (selectPointsAdapter == null) {
            selectPointsAdapter = new SelectPointsAdapter(this, amountDetails);
            recyclerViewPoints.setAdapter(selectPointsAdapter);
        } else {
            selectPointsAdapter.notifyDataSetChanged();
        }
    }

    private void clearAllBetData() {
        commonGameNumberList.clear();
        commonGameNumberList = getCommonGameNumberList();
        btnTotal.setText("0");
        selectedIndex = -1;
        /*textViewMotorPoints.setText("");
        textViewMotorPoints.append(getResources().getString(R.string.lbl_selected_points));
        textViewMotorPoints.append(" " + 0);*/
        checkboxSP.setChecked(false);
        checkboxDP.setChecked(false);
        checkboxTP.setChecked(false);
        genericNumberAdapter.updateData(commonGameNumberList);
    }

    private void clearAllCheckBoxes() {
        checkboxSP.setChecked(false);
        checkboxDP.setChecked(false);
        checkboxTP.setChecked(false);
    }

    private int getSelectedCombinationCount() {
        int count = 0;
        if (selectedIndex != -1 && (checkboxSP.isChecked() ||
                checkboxDP.isChecked() || checkboxTP.isChecked())) {
            if (checkboxSP.isChecked() && checkboxDP.isChecked() && checkboxTP.isChecked()) {
                count = AppConstant.COMMON_GAME_SP_COMBINATIONS + AppConstant.COMMON_GAME_DP_COMBINATIONS +
                        AppConstant.COMMON_GAME_TP_COMBINATIONS;
            } else if ((checkboxSP.isChecked() && checkboxDP.isChecked() && !checkboxTP.isChecked())) {
                count = AppConstant.COMMON_GAME_SP_COMBINATIONS + AppConstant.COMMON_GAME_DP_COMBINATIONS;
            } else if (checkboxSP.isChecked() && !checkboxDP.isChecked() && checkboxTP.isChecked()) {
                count = AppConstant.COMMON_GAME_SP_COMBINATIONS + AppConstant.COMMON_GAME_TP_COMBINATIONS;
            } else if (!checkboxSP.isChecked() && checkboxDP.isChecked() && checkboxTP.isChecked()) {
                count = AppConstant.COMMON_GAME_DP_COMBINATIONS + AppConstant.COMMON_GAME_TP_COMBINATIONS;
            } else if (checkboxSP.isChecked() ||
                    checkboxDP.isChecked() || checkboxTP.isChecked()) {
                if (checkboxSP.isChecked()) {
                    count = AppConstant.COMMON_GAME_SP_COMBINATIONS;
                } else if (checkboxDP.isChecked()) {
                    count = AppConstant.COMMON_GAME_DP_COMBINATIONS;
                } else if (checkboxTP.isChecked()) {
                    count = AppConstant.COMMON_GAME_TP_COMBINATIONS;
                }
            }

            return count;

        }

        return count;
    }

    private ArrayList<SingleGameNumberModel> getSelectedNumberList() {
        ArrayList<SingleGameNumberModel> selectedGameNumberList = new ArrayList<>();
        if (commonGameNumberList != null && !commonGameNumberList.isEmpty()) {
            for (SingleGameNumberModel singleGameNumberModel : commonGameNumberList) {
                if (singleGameNumberModel.isSelected()) {
                    selectedGameNumberList.add(singleGameNumberModel);
                }
            }
        }

        return selectedGameNumberList;
    }

    private void computeCommonLogic() {
        if (selectedIndex != -1) {
            int count = getSelectedCombinationCount();
            LogUtils.d(TAG, String.valueOf(commonGameNumberList.get(selectedIndex).getCombinationCount()));
            commonGameNumberList.get(selectedIndex).setCombinationCount(count);
            if (commonGameNumberList.get(selectedIndex).getPointsValue() > 0) {
                int combinationValue = commonGameNumberList.get(selectedIndex).getPointsValue() * count;
                commonGameNumberList.get(selectedIndex).setCombinationPointsValue(combinationValue);
                LogUtils.d(TAG, String.valueOf(commonGameNumberList.get(selectedIndex).getCombinationPointsValue()));
//                totalPoints = totalPoints + combinationValue;
//                btnTotal.setText(String.valueOf(totalPoints));
                updateTotalPoints();
            }
        }
//        if (getSelectedCombinationCount() > 0 && points > 0) {
//            totalPoints = points * getSelectedCombinationCount();
//            btnTotal.setText(String.valueOf(totalPoints));
//            /*textViewMotorPoints.setText("");
//            textViewMotorPoints.append(getResources().getString(R.string.lbl_selected_points));
//            textViewMotorPoints.append(" " + selectedPoints);*/
//        } else {
            /*totalPoints = 0;*/
            /*selectedPoints = 0;
            textViewMotorPoints.setText("");
            textViewMotorPoints.append(getResources().getString(R.string.lbl_selected_points));
            textViewMotorPoints.append(" " + selectedPoints);*/
            /*btnTotal.setText(String.valueOf(totalPoints));*/
//        }
    }

    private void updateTotalPoints() {
        btnTotal.setText(String.valueOf(getTotalPoints()));
    }

    private int getTotalPoints() {
        int totalPoints = 0;
        for (SingleGameNumberModel singleGameNumberModel : commonGameNumberList) {
            if (singleGameNumberModel.isSelected()) {
                totalPoints = totalPoints + singleGameNumberModel.getCombinationPointsValue();
            }
        }

        return totalPoints;
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
        genericNumberAdapter.isItemClickable = false;
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
        /*if (checkboxOTC.isChecked()) {
            addNewBetRequests.add(getOTCBetRequest());
        }*/

        return addNewBetRequests;
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

    private ArrayList<BetPaanaData> getBetPaanaDataList() {

        ArrayList<BetPaanaData> betPaanaDataList = new ArrayList<>();

        if (commonGameNumberList != null &&
                !commonGameNumberList.isEmpty()) {
            for (SingleGameNumberModel singleGameNumberModel : commonGameNumberList) {
                if (singleGameNumberModel.isSelected() &&
                        singleGameNumberModel.getPointsValue() > 0) {
                    if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_SP_COMBINATIONS) {
                        BetPaanaData betPaanaData = new BetPaanaData();
                        betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betPaanaData.setTotalAmount(singleGameNumberModel.getCombinationPointsValue());
                        betPaanaData.setPaanaType(AppConstant.PAANA_TYPE_SP);

                        betPaanaDataList.add(betPaanaData);
                    } else if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_DP_COMBINATIONS) {
                        BetPaanaData betPaanaData = new BetPaanaData();
                        betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betPaanaData.setTotalAmount(singleGameNumberModel.getCombinationPointsValue());
                        betPaanaData.setPaanaType(AppConstant.PAANA_TYPE_DP);

                        betPaanaDataList.add(betPaanaData);
                    } else if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_TP_COMBINATIONS) {
                        BetPaanaData betPaanaData = new BetPaanaData();
                        betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betPaanaData.setTotalAmount(singleGameNumberModel.getCombinationPointsValue());
                        betPaanaData.setPaanaType(AppConstant.PAANA_TYPE_TP);

                        betPaanaDataList.add(betPaanaData);
                    } else if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_SP_DP_TP_COMBINATIONS) {
                        BetPaanaData betPaanaData = new BetPaanaData();
                        betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betPaanaData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_SP_COMBINATIONS);
                        betPaanaData.setPaanaType(AppConstant.PAANA_TYPE_SP);

                        betPaanaDataList.add(betPaanaData);

                        BetPaanaData betDpData = new BetPaanaData();
                        betDpData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betDpData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betDpData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_DP_COMBINATIONS);
                        betDpData.setPaanaType(AppConstant.PAANA_TYPE_DP);

                        betPaanaDataList.add(betDpData);

                        BetPaanaData betTpData = new BetPaanaData();
                        betTpData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betTpData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betTpData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_TP_COMBINATIONS);
                        betTpData.setPaanaType(AppConstant.PAANA_TYPE_TP);

                        betPaanaDataList.add(betTpData);
                    } else if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_SP_DP_COMBINATIONS) {
                        BetPaanaData betPaanaData = new BetPaanaData();
                        betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betPaanaData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_SP_COMBINATIONS);
                        betPaanaData.setPaanaType(AppConstant.PAANA_TYPE_SP);

                        betPaanaDataList.add(betPaanaData);

                        BetPaanaData betDpData = new BetPaanaData();
                        betDpData.setSelectedPaana(singleGameNumberModel.getNumber());
                        betDpData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                        betDpData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_DP_COMBINATIONS);
                        betDpData.setPaanaType(AppConstant.PAANA_TYPE_DP);

                        betPaanaDataList.add(betDpData);
                    }

                } else if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_SP_TP_COMBINATIONS) {
                    BetPaanaData betPaanaData = new BetPaanaData();
                    betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                    betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                    betPaanaData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_SP_COMBINATIONS);
                    betPaanaData.setPaanaType(AppConstant.PAANA_TYPE_SP);

                    betPaanaDataList.add(betPaanaData);

                    BetPaanaData betTpData = new BetPaanaData();
                    betTpData.setSelectedPaana(singleGameNumberModel.getNumber());
                    betTpData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                    betTpData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_TP_COMBINATIONS);
                    betTpData.setPaanaType(AppConstant.PAANA_TYPE_TP);

                    betPaanaDataList.add(betTpData);
                } else if (singleGameNumberModel.getCombinationCount() == AppConstant.COMMON_GAME_DP_TP_COMBINATIONS) {
                    BetPaanaData betDpData = new BetPaanaData();
                    betDpData.setSelectedPaana(singleGameNumberModel.getNumber());
                    betDpData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                    betDpData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_DP_COMBINATIONS);
                    betDpData.setPaanaType(AppConstant.PAANA_TYPE_DP);

                    betPaanaDataList.add(betDpData);

                    BetPaanaData betTpData = new BetPaanaData();
                    betTpData.setSelectedPaana(singleGameNumberModel.getNumber());
                    betTpData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                    betTpData.setTotalAmount(singleGameNumberModel.getPointsValue() * AppConstant.COMMON_GAME_TP_COMBINATIONS);
                    betTpData.setPaanaType(AppConstant.PAANA_TYPE_TP);

                    betPaanaDataList.add(betTpData);
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

    /**
     * SingleGameNumberListener
     *
     * @param index
     */
    @Override
    public void onIndexSelectedListener(int index) {
        if (!commonGameNumberList.get(index).isSelected()) {
            if (selectedIndex == -1) {
                selectedIndex = index;
                commonGameNumberList.get(index).setSelected(true);
                genericNumberAdapter.updateData(commonGameNumberList);
            } else {
                if (commonGameNumberList.get(selectedIndex).getPointsValue() > 0 &&
                        commonGameNumberList.get(selectedIndex).getCombinationCount() > 0) {
                    clearAllCheckBoxes();
                    selectedIndex = index;
                    commonGameNumberList.get(index).setSelected(true);
                    genericNumberAdapter.updateData(commonGameNumberList);
                }
            }
        } else {
            commonGameNumberList.get(index).setSelected(false);
            commonGameNumberList.get(index).setPointsValue(0);
            commonGameNumberList.get(index).setCombinationCount(0);
            commonGameNumberList.get(index).setCombinationPointsValue(0);
            clearAllCheckBoxes();
            updateTotalPoints();
            selectedIndex = -1;
            genericNumberAdapter.updateData(commonGameNumberList);
        }
    }

    /**
     * @param amountDetail
     */
    @Override
    public void onPointsSelectListener(AmountDetailsResponse amountDetail) {
        if (amountDetail != null) {
            if (selectedIndex != -1) {
                int totalPoints = commonGameNumberList.get(selectedIndex).getPointsValue() + amountDetail.getAmountValue();
                if (totalPoints <= AppConstant.MAX_PAANA_GAME_BET_POINTS) {
                    commonGameNumberList.get(selectedIndex).setPointsValue(totalPoints);
                    genericNumberAdapter.updateData(commonGameNumberList);
                    computeCommonLogic();
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.maximum_paana_game_bet_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                }
            }
        }
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
            case R.id.checkboxSP:
                checkboxSP.setChecked(checkboxSP.isChecked());
                /*LogUtils.e(TAG, String.valueOf(getSelectedCombinationCount()));*/
                computeCommonLogic();
                break;
            case R.id.checkboxDP:
                checkboxDP.setChecked(checkboxDP.isChecked());
                /*LogUtils.e(TAG, String.valueOf(getSelectedCombinationCount()));*/
                computeCommonLogic();
                break;
            case R.id.checkboxTP:
                checkboxTP.setChecked(checkboxTP.isChecked());
                /*LogUtils.e(TAG, String.valueOf(getSelectedCombinationCount()));*/
                computeCommonLogic();
                break;
            case R.id.lblHistory:
                navigateToGameHistoryScreen();
                break;
            case R.id.lblBaazaarHistory:
                navigateToBaazaarHistoryScreen();
                break;
        }
    }
}