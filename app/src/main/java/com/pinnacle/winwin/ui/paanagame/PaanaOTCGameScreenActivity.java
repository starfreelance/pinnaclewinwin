package com.pinnacle.winwin.ui.paanagame;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
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
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.adapter.SelectPointsAdapter;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.singlegame.listener.SelectPointsListener;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;

public class PaanaOTCGameScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, SelectPointsListener, CustomDualButtonDialgoListener {

    private static final String TAG = PaanaOTCGameScreenActivity.class.getSimpleName();

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

    private AppCompatEditText editTextPaanaNumber;

    private Button btnWalletBalance;
    private Button btnTimer;
    private Button btnTotal;
    private Button btnBetOk;

    private RecyclerView recyclerViewPoints;
    private LinearLayoutManager layoutManager;

    private View parentLayout;

    private SelectPointsAdapter selectPointsAdapter;

    private AppCompatCheckBox checkboxOTC;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private ArrayList<PaanaDetailsResponse> paanaDetails;
    private ArrayList<AmountDetailsResponse> amountDetails;
    private PaanaDetailsResponse paanaDetail;

    private int walletBalance;
    private int selectedIndex = -1;
    private int baazaarId = -1;
    private int gameId = -1;
    private int selectedPaanaNumber = -1;
    private String finalNumber;
    private Handler handler;
    private long leftMilliSeconds;
    private String lastResult;
    private int mTotalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paana_otcgame_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
        updateFinalOrLastResult();
        handler = new Handler();
        amountDetails = fetchAmountDetailsFromDb();
        if (amountDetails != null && !amountDetails.isEmpty()) {
            loadPointsAdapter();
        }
        LogUtils.d(TAG, String.valueOf(Utils.getOTCBaazaarId(baazaarId)));
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

        recyclerViewPoints = findViewById(R.id.recyclerViewPoints);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPoints.setLayoutManager(layoutManager);

        editTextPaanaNumber = findViewById(R.id.editTextPaanaNumber);
        editTextPaanaNumber.addTextChangedListener(paanaNumberTextWatcher);
        editTextPaanaNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextPaanaNumber.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        checkboxOTC = findViewById(R.id.checkboxOTC);
        if (baazaarId == AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE || baazaarId == AppConstant.BAAZAAR_TYPE_MAIN_CLOSE) {
            checkboxOTC.setVisibility(View.GONE);
        } else {
            checkboxOTC.setVisibility(View.VISIBLE);
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

    private ArrayList<AmountDetailsResponse> fetchAmountDetailsFromDb() {
        return (ArrayList<AmountDetailsResponse>) ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetailsWithGameId(gameId);
    }

    private PaanaDetailsResponse fetchPaanaDetailFromDb(String paanaNumber) {
        return ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetailWithPaanaNumber(paanaNumber);
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
        mTotalPoints = 0;
        updateTotalPoints();
        editTextPaanaNumber.setText("");
        if (checkboxOTC.isChecked()) {
            checkboxOTC.setChecked(false);
        }
        /*paanaDetails.clear();
        paanaDetails = fetchPaanaDetailsFromDb();
        selectedIndex = -1;
        updateTotalPoints();
        genericNumberAdapter.updateData(paanaDetails);*/
    }

    private void updateTotalPoints() {
        if (checkboxOTC.isChecked()) {
            btnTotal.setText(String.valueOf(mTotalPoints * 2));
        } else {
            btnTotal.setText(String.valueOf(mTotalPoints));
        }
    }

    /*private int getTotalPoints() {
        int totalPoints = 0;
        for (PaanaDetailsResponse paanaDetail : paanaDetails) {
            if (paanaDetail.isSelected()) {
                totalPoints = totalPoints + paanaDetail.getPointsValue();
            }
        }

        return totalPoints;
    }*/

    private void applyBet() {
        /*if (getSelectedNumberList().size() > 0) {

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
        }*/
    }

    private boolean isValidPaana(String paanaNumber) {
        if (fetchPaanaDetailFromDb(paanaNumber) != null) {
            paanaDetail = fetchPaanaDetailFromDb(paanaNumber);
            return true;
        } else {
            return false;
        }
    }

    private void disableGame() {
        btnBetOk.setVisibility(View.INVISIBLE);
    }

    private void navigateToGameHistoryScreen() {
        Intent intent = new Intent(this, GameHistoryScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        startActivity(intent);
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

        BetPaanaData betPaanaData = new BetPaanaData();
        betPaanaData.setSelectedPaana(editTextPaanaNumber.getText().toString());
        betPaanaData.setAmountPerPaana(mTotalPoints);
        betPaanaData.setTotalAmount(mTotalPoints);
        betPaanaData.setPaanaType(paanaDetail.getPaanaType());

        betPaanaDataList.add(betPaanaData);

        /*if (paanaDetails != null &&
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
        }*/

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

    private TextWatcher paanaNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String paanaNumber = charSequence.toString();
            if (paanaNumber.length() == 2) {
                if (Integer.parseInt(String.valueOf(paanaNumber.charAt(0))) == 0) {
                    if (Integer.parseInt(String.valueOf(paanaNumber.charAt(1))) != Integer.parseInt(String.valueOf(paanaNumber.charAt(0)))) {
                        editTextPaanaNumber.setText(String.valueOf(paanaNumber.charAt(0)));
                        editTextPaanaNumber.setSelection(editTextPaanaNumber.getText().toString().length());
                        LogUtils.d(TAG, String.valueOf(count));
                    } else {
                        LogUtils.d(TAG, paanaNumber);
                    }
                } else if (Integer.parseInt(String.valueOf(paanaNumber.charAt(1))) == 0) {
                    LogUtils.d(TAG, paanaNumber);
                } else if (Integer.parseInt(String.valueOf(paanaNumber.charAt(1))) < Integer.parseInt(String.valueOf(paanaNumber.charAt(0)))) {
                    editTextPaanaNumber.setText(String.valueOf(paanaNumber.charAt(0)));
                    editTextPaanaNumber.setSelection(editTextPaanaNumber.getText().toString().length());
                    LogUtils.d(TAG, String.valueOf(count));
                } else {
                    LogUtils.d(TAG, paanaNumber);
                }
            } else if (paanaNumber.length() == 3) {
                if (Integer.parseInt(String.valueOf(paanaNumber.charAt(1))) == 0) {
                    if (Integer.parseInt(String.valueOf(paanaNumber.charAt(2))) != Integer.parseInt(String.valueOf(paanaNumber.charAt(1)))) {
                        editTextPaanaNumber.setText(String.valueOf(paanaNumber.charAt(0)) + String.valueOf(paanaNumber.charAt(1)));
                        editTextPaanaNumber.setSelection(editTextPaanaNumber.getText().toString().length());
                        LogUtils.d(TAG, String.valueOf(count));
                    } else {
                        LogUtils.d(TAG, paanaNumber);
                    }
                } else if (Integer.parseInt(String.valueOf(paanaNumber.charAt(1))) != 0) {
                    if (Integer.parseInt(String.valueOf(paanaNumber.charAt(2))) == 0) {
                        LogUtils.d(TAG, paanaNumber);
                    } else if (Integer.parseInt(String.valueOf(paanaNumber.charAt(2))) < Integer.parseInt(String.valueOf(paanaNumber.charAt(1)))) {
                        editTextPaanaNumber.setText(String.valueOf(paanaNumber.charAt(0)) + String.valueOf(paanaNumber.charAt(1)));
                        editTextPaanaNumber.setSelection(editTextPaanaNumber.getText().toString().length());
                        LogUtils.d(TAG, String.valueOf(count));
                    } else {
                        LogUtils.d(TAG, paanaNumber);
                    }
                } else if (Integer.parseInt(String.valueOf(paanaNumber.charAt(2))) < Integer.parseInt(String.valueOf(paanaNumber.charAt(1)))) {
                    editTextPaanaNumber.setText(String.valueOf(paanaNumber.charAt(0)) + String.valueOf(paanaNumber.charAt(1)));
                    editTextPaanaNumber.setSelection(editTextPaanaNumber.getText().toString().length());
                    LogUtils.d(TAG, String.valueOf(count));
                } else {
                    LogUtils.d(TAG, paanaNumber);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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

    //SelectPointsListener
    @Override
    public void onPointsSelectListener(AmountDetailsResponse amountDetail) {
        if (amountDetail != null) {
            if (editTextPaanaNumber.getText().toString().length() == 3) {
                mTotalPoints = mTotalPoints + amountDetail.getAmountValue();
                if (mTotalPoints <= AppConstant.MAX_PAANA_GAME_BET_POINTS) {
                    updateTotalPoints();
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.maximum_paana_game_bet_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                }
            } else {
                Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.valid_paana_number_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
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

    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblClearAll:
                clearAllBetData();
                break;
            case R.id.btnBetOk:
                /*if (walletBalance >= getTotalPoints()) {
                    applyBet();
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.insufficient_points_error), Snackbar.LENGTH_LONG,
                            getResources().getString(R.string.btn_okay)).show();
                }*/
                if (isValidPaana(editTextPaanaNumber.getText().toString())) {
                    if (mTotalPoints == 0) {
                        Utils.showCustomSnackBarMessageView(this, parentLayout,
                                getResources().getString(R.string.point_error), Snackbar.LENGTH_LONG,
                                getResources().getString(R.string.btn_okay)).show();
                    } else if (walletBalance < mTotalPoints) {
                        Utils.showCustomSnackBarMessageView(this, parentLayout,
                                getResources().getString(R.string.insufficient_points_error), Snackbar.LENGTH_LONG,
                                getResources().getString(R.string.btn_okay)).show();
                    } else {
                        /*LogUtils.d(TAG, editTextPaanaNumber.getText().toString() + " is a valid Paana");*/
                        showBetConfirmationDialog();
                    }
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.valid_paana_number_error), Snackbar.LENGTH_LONG,
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
                if (mTotalPoints != 0) {
                    updateTotalPoints();
                }
                break;
        }
    }
}
