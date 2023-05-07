package com.pinnacle.winwin.ui.cpgame;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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
import com.pinnacle.winwin.network.model.CPDetailsResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.adapter.GenericNumberAdapter;
import com.pinnacle.winwin.ui.singlegame.adapter.SelectPointsAdapter;
import com.pinnacle.winwin.ui.singlegame.fragment.CustomDualButtonDialogFragment;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.singlegame.listener.SelectPointsListener;
import com.pinnacle.winwin.ui.singlegame.listener.SingleGameNumberListener;
import com.pinnacle.winwin.ui.singlegame.model.PointsModel;
import com.pinnacle.winwin.ui.singlegame.model.SingleGameNumberModel;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;

public class CPGameScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, SingleGameNumberListener, SelectPointsListener,
        CustomDualButtonDialgoListener, CustomSingleButtonDialogListener {

    private static final String TAG = CPGameScreenActivity.class.getSimpleName();

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

    private RecyclerView recyclerViewNumbers;
    private GridLayoutManager gridLayoutManager;

    private GenericNumberAdapter genericNumberAdapter;

    private RecyclerView recyclerViewPoints;
    private LinearLayoutManager layoutManager;

    private View parentLayout;

    private SelectPointsAdapter selectPointsAdapter;

    private CustomDualButtonDialogFragment customDualButtonDialogFragment;
    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private ArrayList<SingleGameNumberModel> cpGameNumberList;
    private ArrayList<PointsModel> pointsList;

    private ArrayList<CPDetailsResponse> cpDetails;
    private ArrayList<AmountDetailsResponse> amountDetails;

    private int walletBalance;
    private int selectedIndex = -1;
    private int lastSelectedIndex = -1;
    private int baazaarId = -1;
    private int gameId = -1;
    private String finalNumber;
    private long leftMilliSeconds;
    private Handler handler;
    private String lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpgame_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
        updateFinalOrLastResult();
        handler = new Handler();
        /*cpGameNumberList = getCPGameNumberList();
        if (cpGameNumberList != null && !cpGameNumberList.isEmpty()) {
            loadGenericNumberAdapter();
        }*/
        cpDetails = fetchCPDetailsFromDb();
        if (cpDetails != null && !cpDetails.isEmpty()) {
            loadGenericNumberAdapter();
        }

        /*pointsList = getPointsList();
        if (pointsList != null && !pointsList.isEmpty()) {
            loadPointsAdapter();
        }*/
        amountDetails = fetchAmountDetailsFromDb();
        if (amountDetails != null && !amountDetails.isEmpty()) {
            loadPointsAdapter();
        }
        callGetBaazaarRemainingTimeApi();
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
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }

    //Local Methods
    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        lblPoints = findViewById(R.id.lblPoints);
        lblPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblSingleGame = findViewById(R.id.lblSingleGame);
        lblSingleGame.setTypeface(Utils.getTypeFaceBodoni72OS(this));
        lblSingleGame.setText(getResources().getString(R.string.title_cp_game));
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
        gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerViewNumbers.setLayoutManager(gridLayoutManager);

        recyclerViewPoints = findViewById(R.id.recyclerViewPoints);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPoints.setLayoutManager(layoutManager);
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

    private ArrayList<CPDetailsResponse> fetchCPDetailsFromDb() {
        return (ArrayList<CPDetailsResponse>) ASOnlineApplication.appDatabase.cpDetailsDao().getCPDetails();
    }

    private ArrayList<AmountDetailsResponse> fetchAmountDetailsFromDb() {
        return (ArrayList<AmountDetailsResponse>) ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetailsWithGameId(gameId);
    }

    private ArrayList<PointsModel> getPointsList() {

        ArrayList<PointsModel> pointsList = new ArrayList<>();

        String[] gamePointsArray = getResources().getStringArray(R.array.game_points_array);

        for (String aGamePointsArray : gamePointsArray) {
            PointsModel pointsModel = new PointsModel();
            pointsModel.setPointValue(aGamePointsArray);

            pointsList.add(pointsModel);
        }

        return pointsList;
    }

    private void loadPointsAdapter() {
        if (selectPointsAdapter == null) {
            selectPointsAdapter = new SelectPointsAdapter(this, amountDetails);
            recyclerViewPoints.setAdapter(selectPointsAdapter);
        } else {
            selectPointsAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<SingleGameNumberModel> getCPGameNumberList() {

        ArrayList<SingleGameNumberModel> cpGameNumberList = new ArrayList<>();

        String[] cpGameNumberArray = getResources().getStringArray(R.array.cp_game_number_array);

        for (int i = 0; i < cpGameNumberArray.length; i++) {
            SingleGameNumberModel cpGameNumberModel = new SingleGameNumberModel();
            cpGameNumberModel.setNumber(cpGameNumberArray[i]);
            cpGameNumberModel.setPointsValue(0);
            cpGameNumberModel.setSelected(false);

            cpGameNumberList.add(cpGameNumberModel);
        }

        return cpGameNumberList;
    }

    private void loadGenericNumberAdapter() {
        if (genericNumberAdapter == null) {
            genericNumberAdapter = new GenericNumberAdapter(this, cpDetails, gameId);
            recyclerViewNumbers.setAdapter(genericNumberAdapter);
        } else {
            genericNumberAdapter.notifyDataSetChanged();
        }
    }

    private void clearAllBetData() {
        cpDetails.clear();
        cpDetails = fetchCPDetailsFromDb();
        selectedIndex = -1;
        updateTotalPoints();
        /*genericNumberAdapter.updateData(cpGameNumberList);*/
        genericNumberAdapter.updateData(cpDetails);
    }

    private void updateTotalPoints() {
        /*int totalPoints = 0;*/
        /*for (SingleGameNumberModel cpGameNumberModel : cpGameNumberList) {
            if (cpGameNumberModel.isSelected()) {
                totalPoints = totalPoints + cpGameNumberModel.getPointsValue();
            }
        }*/

        btnTotal.setText(String.valueOf(getTotalPoints()));
    }

    private int getTotalPoints() {
        int totalPoints = 0;
        for (CPDetailsResponse cpDetail : cpDetails) {
            if (cpDetail.isSelected()) {
                /*We need to multiply the Amount by 10 in CP as there are 10 possibilities*/
                totalPoints = totalPoints + (cpDetail.getPointsValue() * 10);
            }
        }

        return totalPoints;
    }

    private void applyBet() {
        if (/*selectedIndex != -1 && */getSelectedNumberList().size() > 0) {

            if (getSelectedNumberWithoutPoints() != -1) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.point_error), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            } else {
                showBetConfirmationDialog();
            }

            /*if (cpGameNumberList.get(selectedIndex).isSelected() &&
                    cpGameNumberList.get(selectedIndex).getPointsValue() > 0) {
                *//*Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.bet_placed_success_msg), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();*//*
                clearAllBetData();
            } else if (cpGameNumberList.get(selectedIndex).isSelected() &&
                    cpGameNumberList.get(selectedIndex).getPointsValue() == 0) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.point_error), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            }*/
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.empty_bet_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
        }
    }

    /*private ArrayList<SingleGameNumberModel> getSelectedNumberList() {
        ArrayList<SingleGameNumberModel> selectedGameNumberList = new ArrayList<>();
        if (cpGameNumberList != null && !cpGameNumberList.isEmpty()) {
            for (SingleGameNumberModel cpGameNumberModel : cpGameNumberList) {
                if (cpGameNumberModel.isSelected()) {
                    selectedGameNumberList.add(cpGameNumberModel);
                }
            }
        }

        return selectedGameNumberList;
    }*/

    private ArrayList<CPDetailsResponse> getSelectedNumberList() {
        ArrayList<CPDetailsResponse> cpDetailsResponses = new ArrayList<>();
        if (cpDetails != null && !cpDetails.isEmpty()) {
            for (CPDetailsResponse cpDetail : cpDetails) {
                if (cpDetail.isSelected()) {
                    cpDetailsResponses.add(cpDetail);
                }
            }
        }

        return cpDetailsResponses;
    }

    private int getSelectedNumberWithoutPoints() {
        int index = -1;
        if (cpDetails != null && !cpDetails.isEmpty()) {
            for (int i = 0; i < cpDetails.size(); i++) {
                CPDetailsResponse cpDetail = cpDetails.get(i);
                if (cpDetail.isSelected() &&
                        cpDetail.getPointsValue() == 0) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

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

    private ArrayList<AddNewBetRequest> getAddNewBetRequestList() {
        ArrayList<AddNewBetRequest> addNewBetRequests = new ArrayList<>();
        addNewBetRequests.add(getAddNewBetRequest());

        return addNewBetRequests;
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

    private ArrayList<BetPaanaData> getBetPaanaDataList() {

        ArrayList<BetPaanaData> betPaanaDataList = new ArrayList<>();

        if (cpDetails != null &&
                !cpDetails.isEmpty()) {
            for (CPDetailsResponse cpDetail : cpDetails) {
                if (cpDetail.isSelected() &&
                        cpDetail.getPointsValue() > 0) {
                    BetPaanaData betPaanaData = new BetPaanaData();
                    betPaanaData.setSelectedPaana(cpDetail.getPaanaNo());
                    betPaanaData.setAmountPerPaana(cpDetail.getPointsValue());
                    betPaanaData.setTotalAmount(cpDetail.getPointsValue() * 10);

                    betPaanaDataList.add(betPaanaData);
                }
            }
        }

        return betPaanaDataList;
    }

    private void disableGame() {
        btnBetOk.setVisibility(View.INVISIBLE);
        genericNumberAdapter.isItemClickable = false;
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

    protected void dismissCustomSingleButtonDialog() {
        if (customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible() && !isFinishing()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
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

    //Callbacks
    //Api Callback
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
    //SingleGameNumberListener
    @Override
    public void onIndexSelectedListener(int index) {

        /*if (!cpGameNumberList.get(index).isSelected()) {
            if (selectedIndex == -1) {
                selectedIndex = index;
                cpGameNumberList.get(index).setSelected(true);
                genericNumberAdapter.updateData(cpGameNumberList);
            } else {
                if (cpGameNumberList.get(selectedIndex).getPointsValue() > 0) {
                    lastSelectedIndex = selectedIndex;
                    selectedIndex = index;
                    cpGameNumberList.get(index).setSelected(true);
                    genericNumberAdapter.updateData(cpGameNumberList);
                }
            }
        } else  {
            cpGameNumberList.get(index).setSelected(false);
            cpGameNumberList.get(index).setPointsValue(0);
            updateTotalPoints();
            selectedIndex = -1;
            *//*if (lastSelectedIndex != -1) {
                selectedIndex = lastSelectedIndex;
            } else {
                selectedIndex = -1;
            }*//*
            genericNumberAdapter.updateData(cpGameNumberList);
        }*/
        if (!cpDetails.get(index).isSelected()) {
            if (selectedIndex == -1) {
                selectedIndex = index;
                 cpDetails.get(index).setSelected(true);
                genericNumberAdapter.updateData(cpDetails);
            } else {
                if (cpDetails.get(selectedIndex).getPointsValue() > 0) {
                    lastSelectedIndex = selectedIndex;
                    selectedIndex = index;
                    cpDetails.get(index).setSelected(true);
                    genericNumberAdapter.updateData(cpDetails);
                }
            }
        } else  {
            cpDetails.get(index).setSelected(false);
            cpDetails.get(index).setPointsValue(0);
            updateTotalPoints();
            selectedIndex = -1;
            /*if (lastSelectedIndex != -1) {
                selectedIndex = lastSelectedIndex;
            } else {
                selectedIndex = -1;
            }*/
            genericNumberAdapter.updateData(cpDetails);
        }

    }

    //SelectPointsListener
    @Override
    public void onPointsSelectListener(AmountDetailsResponse amountDetail) {
        if (amountDetail != null) {
            if (selectedIndex != -1) {
                int totalPoints = cpDetails.get(selectedIndex).getPointsValue() + amountDetail.getAmountValue();
                if (totalPoints <= AppConstant.MAX_CP_GAME_BET_POINTS) {
                    cpDetails.get(selectedIndex).setPointsValue(totalPoints);
                    updateTotalPoints();
                    genericNumberAdapter.updateData(cpDetails);
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.maximum_cp_game_bet_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                }
            }
        }
    }

    //CustomDualButtonDialgoListener
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

    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblClearAll:
                clearAllBetData();
                break;
            case R.id.btnBetOk:
                if (walletBalance >= getTotalPoints()) {
                    applyBet();
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
        }
    }
}
