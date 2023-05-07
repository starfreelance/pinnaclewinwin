package com.pinnacle.winwin.ui.singlegame;

import android.content.Intent;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.adapter.GenericNumberAdapter;
import com.pinnacle.winwin.ui.singlegame.adapter.SelectPointsAdapter;
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

public class SingleGameScreenActivity extends ASOnlineBaseActivity implements
        SingleGameNumberListener, View.OnClickListener, SelectPointsListener,
        CustomDualButtonDialgoListener, CustomSingleButtonDialogListener {

    private static final String TAG = SingleGameScreenActivity.class.getSimpleName();

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

    /*private CustomDualButtonDialogFragment customDualButtonDialogFragment;*/
    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private ArrayList<SingleGameNumberModel> singleGameNumberList;
    private ArrayList<PointsModel> pointsList;
    private ArrayList<SingleGameNumberModel> selectedGameNumberList = new ArrayList<>();
    private SingleGameNumberModel selectedGameNumberModel;

    private ArrayList<AmountDetailsResponse> amountDetails;

    private int totalPointsSelected;
    private int walletBalance;
    private int selectedIndex = -1;
    private int lastSelectedIndex = -1;

    private int baazaarId = -1;
    private int gameId = -1;

    private Handler handler;
    private long leftMilliSeconds;
    private String finalNumber;
    private String lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_game_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
        updateFinalOrLastResult();
        singleGameNumberList = getSingleGameNumberList();
        if (singleGameNumberList != null && !singleGameNumberList.isEmpty()) {
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
        handler = new Handler();
        callGetBaazaarRemainingTimeApi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        /*handler.postDelayed(timerRunnable, 0);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        /*handler.removeCallbacks(timerRunnable);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        handler.removeCallbacks(timerRunnable);
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        lblPoints = findViewById(R.id.lblPoints);
        lblPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblSingleGame = findViewById(R.id.lblSingleGame);
        lblSingleGame.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblSingleGame.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lblSingleGame.setLetterSpacing(0.2f);
        }*/

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

    private ArrayList<SingleGameNumberModel> getSingleGameNumberList() {

        ArrayList<SingleGameNumberModel> singleGameNumberList = new ArrayList<>();

        String[] singleGameNumberArray = getResources().getStringArray(R.array.game_number_array);

        for (int i = 0; i < singleGameNumberArray.length; i++) {
            SingleGameNumberModel singleGameNumberModel = new SingleGameNumberModel();
            singleGameNumberModel.setNumber(singleGameNumberArray[i]);
            singleGameNumberModel.setPointsValue(0);
            singleGameNumberModel.setSelected(false);

            singleGameNumberList.add(singleGameNumberModel);
        }

        return singleGameNumberList;
    }

    private void loadGenericNumberAdapter() {
        if (genericNumberAdapter == null) {
            genericNumberAdapter = new GenericNumberAdapter(this, singleGameNumberList, gameId);
            recyclerViewNumbers.setAdapter(genericNumberAdapter);
        } else {
            genericNumberAdapter.notifyDataSetChanged();
        }
    }

    private void clearAllBetData() {
        singleGameNumberList.clear();
        singleGameNumberList = getSingleGameNumberList();
        selectedIndex = -1;
        updateTotalPoints();
        genericNumberAdapter.updateData(singleGameNumberList);
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

            /*if (singleGameNumberList.get(selectedIndex).isSelected() &&
                    singleGameNumberList.get(selectedIndex).getPointsValue() > 0) {
                *//*Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.bet_placed_success_msg), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();*//*
                showBetConfirmationDialog();
                *//*clearAllBetData();*//*
            } else if (singleGameNumberList.get(selectedIndex).isSelected() &&
                    singleGameNumberList.get(selectedIndex).getPointsValue() == 0) {
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

    private ArrayList<SingleGameNumberModel> getSelectedNumberList() {
        ArrayList<SingleGameNumberModel> selectedGameNumberList = new ArrayList<>();
        if (singleGameNumberList != null && !singleGameNumberList.isEmpty()) {
            for (SingleGameNumberModel singleGameNumberModel : singleGameNumberList) {
                if (singleGameNumberModel.isSelected()) {
                    selectedGameNumberList.add(singleGameNumberModel);
                }
            }
        }

        return selectedGameNumberList;
    }

    private int getSelectedNumberWithoutPoints() {
        int index = -1;
        if (singleGameNumberList != null && !singleGameNumberList.isEmpty()) {
            for (int i = 0; i < singleGameNumberList.size(); i++) {
                SingleGameNumberModel singleGameNumberModel = singleGameNumberList.get(i);
                if (singleGameNumberModel.isSelected() &&
                        singleGameNumberModel.getPointsValue() == 0) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    private void updateTotalPoints() {
        /*int totalPoints = 0;
        for (SingleGameNumberModel singleGameNumberModel : singleGameNumberList) {
            if (singleGameNumberModel.isSelected()) {
                totalPoints = totalPoints + singleGameNumberModel.getPointsValue();
            }
        }*/
        btnTotal.setText(String.valueOf(getTotalPoints()));
    }

    private int getTotalPoints() {
        int totalPoints = 0;
        for (SingleGameNumberModel singleGameNumberModel : singleGameNumberList) {
            if (singleGameNumberModel.isSelected()) {
                totalPoints = totalPoints + singleGameNumberModel.getPointsValue();
            }
        }

        return totalPoints;
    }

    private int getTotalNumberSelectedWithPoints() {
        int totalSize = 0;
        for (SingleGameNumberModel singleGameNumberModel : singleGameNumberList) {
            if (singleGameNumberModel.isSelected() &&
                    singleGameNumberModel.getPointsValue() > 0) {
                totalSize = totalSize + 1;
            }
        }

        return totalSize;
    }

    private void navigateToGameHistoryScreen() {
        Intent intent = new Intent(this, GameHistoryScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        startActivity(intent);
    }

    /*private void showBetConfirmationDialog() {
        dismissCustomDualButtonDialog();
        customDualButtonDialogFragment = CustomDualButtonDialogFragment.newInstance();
        customDualButtonDialogFragment.show(getSupportFragmentManager(), "BetConfirm");
    }

    private void dismissCustomDualButtonDialog() {
        if (customDualButtonDialogFragment != null &&
                customDualButtonDialogFragment.isVisible() && !isFinishing()) {
            customDualButtonDialogFragment.dismissAllowingStateLoss();
            customDualButtonDialogFragment = null;
        }
    }*/

    private void callAddNewBetApi() {
        Intent intent = new Intent();
        /*intent.putExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST, getAddNewBetRequest());*/
        intent.putExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST, getAddNewBetOtcRequest());
        /*try {
            CryptLib _crypt = new CryptLib();
            Gson gson = new Gson();
            String jsonString = gson.toJson(getAddNewBetRequest());
            String key = CryptLib.SHA256("7c3acb8b876hbd8a95b6a26d15029cf060444ec", 32); //32 bytes = 256 bit
            String iv = "6P81acb8b8uikuyesgteMasTE";//CryptLib.generateRandomIV(16); //16 bytes = 128 bit
            String encryptedString = _crypt.encrypt(jsonString, key, iv); //encrypt
            LogUtils.e(TAG, encryptedString);
            LogUtils.e(TAG, _crypt.decrypt(encryptedString, key, iv));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

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

        if (singleGameNumberList != null &&
                !singleGameNumberList.isEmpty()) {
            for (SingleGameNumberModel singleGameNumberModel : singleGameNumberList) {
                if (singleGameNumberModel.isSelected() &&
                        singleGameNumberModel.getPointsValue() > 0) {
                    BetPaanaData betPaanaData = new BetPaanaData();
                    betPaanaData.setSelectedPaana(singleGameNumberModel.getNumber());
                    betPaanaData.setAmountPerPaana(singleGameNumberModel.getPointsValue());
                    betPaanaData.setTotalAmount(singleGameNumberModel.getPointsValue());

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

    /*private Thread timerThread = new Thread() {
        long leftMilliSeconds = 60000l;
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {

                    Thread.sleep(1000);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (leftMilliSeconds <= 0) {
                                btnTimer.setText("0");
                                timerThread.interrupt();
                            } else {
                                leftMilliSeconds = leftMilliSeconds - 1000;
                                btnTimer.setText(String.valueOf(leftMilliSeconds));
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {

            }
        }
    };*/

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
                /*Date date = DateUtils.getDateFromString(baazaarRemainingTimeData.getRemainingTime(),
                        new SimpleDateFormat(DateUtils.BAAZAAR_REMAINING_TIME_FORMAT, Locale.getDefault()));
                if (date != null) {
                    *//*leftMilliSeconds = date.getTime();*//*
                 *//*leftMilliSeconds = date.getTime();
                    LogUtils.e(TAG, String.valueOf(date.getTime()));
                    handler.postDelayed(timerRunnable, 0);*//*
                }*/
                if (baazaarRemainingTimeData.getRemainingTime() > 0) {
                    leftMilliSeconds = baazaarRemainingTimeData.getRemainingTime() * 1000;
                    handler.postDelayed(timerRunnable, 0);
                } else {
                    disableGame();
                    /*showBetTimeExpiredDialog();*/
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

        if (!singleGameNumberList.get(index).isSelected()) {
            if (selectedIndex == -1) {
                selectedIndex = index;
                singleGameNumberList.get(index).setSelected(true);
                genericNumberAdapter.updateData(singleGameNumberList);
            } else {
                if (singleGameNumberList.get(selectedIndex).getPointsValue() > 0) {
                    lastSelectedIndex = selectedIndex;
                    selectedIndex = index;
                    singleGameNumberList.get(index).setSelected(true);
                    genericNumberAdapter.updateData(singleGameNumberList);
                }
            }
        } else {
            singleGameNumberList.get(index).setSelected(false);
            singleGameNumberList.get(index).setPointsValue(0);
            updateTotalPoints();
            selectedIndex = -1;
//            if (lastSelectedIndex != -1) {
//                selectedIndex = lastSelectedIndex;
//            } else {
//                selectedIndex = -1;
//            }
//            if (getTotalNumberSelectedWithPoints() == 0) {
//                selectedIndex = -1;
//            }
            genericNumberAdapter.updateData(singleGameNumberList);
        }
    }


    //SelectPointsListener
    @Override
    public void onPointsSelectListener(/*PointsModel pointsModel*/ AmountDetailsResponse amountDetail) {
        if (amountDetail != null) {
            if (selectedIndex != -1) {
                /*int totalPoints = singleGameNumberList.get(selectedIndex).getPointsValue() + Integer.parseInt(pointsModel.getPointValue());
                singleGameNumberList.get(selectedIndex).setPointsValue(totalPoints);
                updateTotalPoints();
                genericNumberAdapter.updateData(singleGameNumberList);*/
                int totalPoints = singleGameNumberList.get(selectedIndex).getPointsValue() + amountDetail.getAmountValue();
                if (totalPoints <= AppConstant.MAX_SINGLE_GAME_BET_POINTS) {
                    singleGameNumberList.get(selectedIndex).setPointsValue(totalPoints);
                    updateTotalPoints();
                    genericNumberAdapter.updateData(singleGameNumberList);
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout, getResources().getString(R.string.maximum_single_game_bet_error),
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
        /*Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.bet_placed_success_msg), Snackbar.LENGTH_LONG,
                getResources().getString(R.string.btn_okay)).show();*/
        /*clearAllBetData();*/
        callAddNewBetApi();
    }

    @Override
    public void onClickNoButtonListener() {
        dismissCustomDualButtonDialog();
    }

    //CustomSingleButtonDialogListener
    @Override
    public void onClickSingleButtonListener() {
        super.onClickSingleButtonListener();
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_BET_TIME_EXPIRED)) {
                    dismissCustomSingleButtonDialog();
                    navigateToBaazaarListScreen();
                }
            }
        }
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
