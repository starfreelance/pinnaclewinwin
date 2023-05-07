package com.pinnacle.winwin.ui.headtailgame;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomHTCongratulationsDialogFragment;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.custom.EasyFlipView;
import com.pinnacle.winwin.listener.CustomHTCongratulationsDialogListener;
import com.pinnacle.winwin.listener.CustomSingleButtonDialogListener;
import com.pinnacle.winwin.network.model.AddHTNewBetData;
import com.pinnacle.winwin.network.model.AddHTNewBetRequest;
import com.pinnacle.winwin.network.model.AddHTNewBetResponse;
import com.pinnacle.winwin.network.model.CustomGameAmountResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.HTCancelBetData;
import com.pinnacle.winwin.network.model.HTCancelBetRequest;
import com.pinnacle.winwin.network.model.HTCancelBetResponse;
import com.pinnacle.winwin.network.model.HTExistingBetResponse;
import com.pinnacle.winwin.network.model.HTInitialData;
import com.pinnacle.winwin.network.model.HTInitialRequest;
import com.pinnacle.winwin.network.model.HTInitialResponse;
import com.pinnacle.winwin.network.model.HTLastResultResponse;
import com.pinnacle.winwin.network.model.HTNewBet;
import com.pinnacle.winwin.network.model.HTNewBetData;
import com.pinnacle.winwin.network.model.HTResultData;
import com.pinnacle.winwin.network.model.HTResultRequest;
import com.pinnacle.winwin.network.model.HTResultResponse;
import com.pinnacle.winwin.ui.headtailgame.adapter.HeadTailPointsAdapter;
import com.pinnacle.winwin.ui.headtailgame.adapter.HeadTailResultAdapter;
import com.pinnacle.winwin.ui.headtailgame.listener.HeadTailPointsListener;
import com.pinnacle.winwin.ui.headtailgame.model.HTBetModel;
import com.pinnacle.winwin.ui.headtailhistory.HeadTailHistoryScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.NetworkUtils;
import com.pinnacle.winwin.utils.Utils;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class HeadTailGameScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener,
        HeadTailPointsListener, CustomSingleButtonDialogListener, AudioManager.OnAudioFocusChangeListener,
        CustomHTCongratulationsDialogListener, EasyFlipView.OnFlipAnimationListener {

    private static final String TAG = HeadTailGameScreenActivity.class.getSimpleName();

    private AppCompatTextView lblPoints;
    private AppCompatTextView lblSingleGame;
    private AppCompatTextView lblTimer;
    private AppCompatTextView lblClearAll;
    private AppCompatTextView lblHistory;
    private AppCompatTextView lblTotal;
    private AppCompatTextView lblLast10Results;
    private AppCompatTextView lblCancelBet;
    private AppCompatTextView textViewHeadPoints;
    private AppCompatTextView textViewTailPoints;

    private Button btnWalletBalance;
    private Button btnTimer;
    private Button btnTotal;
    private Button btnBetOk;

    private AppCompatImageView imgViewHead;
    private AppCompatImageView imgViewTail;
    private AppCompatImageView imgViewSelectHead;
    private AppCompatImageView imgViewSelectTail;

    private RecyclerView recyclerViewLastResults;
    private HeadTailResultAdapter headTailResultAdapter;

    private RecyclerView recyclerViewPoints;
    private HeadTailPointsAdapter headTailPointsAdapter;

    private EasyFlipView flipView;
    private CircularProgressBar circularProgressBar;

    private Group groupHeadPoints;
    private Group groupTailPoints;

    private KonfettiView konfettiView;

    private View parentLayout;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;
    private CustomHTCongratulationsDialogFragment customHTCongratulationsDialogFragment;

    private ArrayList<HTLastResultResponse> htLastResults;
    private ArrayList<CustomGameAmountResponse> customGameAmounts;
    private ArrayList<HTExistingBetResponse> htExistingBets;

    private int walletBalance;
    private int selectedIndex = -1;
    private int baazaarId = -1;
    private int gameId = -1;
    private int totalPoints = 0;
    private int selectedPoints = 0;
    private String finalNumber;
    private long leftMilliSeconds;
    private Handler handler;
    private Handler flipCoinHandler;
    private Handler progressBarHandler;
    private Handler dialogHandler;
    private Handler konfettiHandler;
    private int totalProgress = 0;
    private int elapsedProgress = 55;
    private int elapsedFlipCoinTime = 5;
    private MediaPlayer mp;
    private int intervalTime;
    private int closesBefore;
    private int remainingTime;
    private boolean isApiCalled = true;
    private String betType = "";
    private String slotId;
    private int betId = -1;
    private boolean isExistingBet;
    private HTResultData htResultData;
    private HTBetModel headBetModel;
    private HTBetModel tailBetModel;
    private HTBetModel previousHeadBetModel;
    private HTBetModel previousTailBetModel;
    private String maxHTBetPoints = "10,000";
    private boolean isResultEventCalled;

    //Socket
    private Socket mSocket;
    private Gson gson;

    //Audio Focus
    private AudioManager audioManager;
    private AudioAttributes playbackAttributes;
    private AudioFocusRequest focusRequest;

    //Phonse State
    private TelephonyManager telephonyManager;
    private int phoneState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_tail_game_screen);

        processIntentData();
        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
        initViews();
//        callGetHTInitialDataApi();
        customGameAmounts = fetchCustomGameAmountsFromDb();
        if (customGameAmounts != null && !customGameAmounts.isEmpty()) {
            loadHTPointsAdapter();
        }

        initHTBetModel();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this, new Handler())
                    .build();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int res = audioManager.requestAudioFocus(focusRequest);
        }

        mp = MediaPlayer.create(this, R.raw.tick);
        flipCoinHandler = new Handler();
        progressBarHandler = new Handler();
        dialogHandler = new Handler();
        konfettiHandler = new Handler();

        gson = new Gson();
        try {
            mSocket = IO.socket(BuildConfig.BASE_SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d(TAG, "Socket Connected");
                callHTInitialData();

            }
        });

        mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];

                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                        String jwtToken = "jwt=" + ASOnlinePreferenceManager.getString(HeadTailGameScreenActivity.this, AppConstant.KEY_USER_TOKEN, "") + ";";
                        headers.put("Cookie", Arrays.asList(jwtToken));
                    }
                });
            }
        });

        mSocket.on("ht-initial-data-done", onHTInitialDataDone);
        mSocket.on("ht-initial-data-error", onHTInitialDataError);
        mSocket.on("cancel-ht-bet-done", onCancelHTBetDone);
        mSocket.on("cancel-ht-bet-error", onCancelHTBetError);
        mSocket.on("new-ht-bet-done", onNewHTBetDone);
        mSocket.on("new-ht-bet-error", onNewHTBetError);
        mSocket.on("ht-results-done", onHTResultDone);
        mSocket.on("ht-results-error", onHTResultError);
        mSocket.connect();

        DecimalFormat decimalFormat = new DecimalFormat("#,##,###");
        maxHTBetPoints = decimalFormat.format(AppConstant.MAX_HEAD_TAIL_BET_POINTS);
        LogUtils.d(TAG, maxHTBetPoints);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "ONPAUSE");
        /*progressBarHandler.removeCallbacks(progressBarRunnable);
        flipCoinHandler.removeCallbacks(flipRunnable);*/
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
        mSocket.disconnect();
        progressBarHandler.removeCallbacks(progressBarRunnable);
        flipCoinHandler.removeCallbacks(flipRunnable);
        dialogHandler.removeCallbacks(dialogRunnable);
        konfettiHandler.removeCallbacks(konfettiRunnable);
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        lblPoints = findViewById(R.id.lblPoints);
        lblPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        /*lblPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        lblSingleGame = findViewById(R.id.lblSingleGame);
        lblSingleGame.setTypeface(Utils.getTypeFaceGeorgiaBold(this));
        lblSingleGame.setText(getResources().getString(R.string.title_head_tail_game));
        /*lblSingleGame.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lblSingleGame.setLetterSpacing(0.2f);
        }

        lblTimer = findViewById(R.id.lblTimer);
        lblTimer.setTypeface(Utils.getTypeFaceBodoni72(this));
        /*lblTimer.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        lblClearAll = findViewById(R.id.lblClearAll);
        lblClearAll.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblClearAll.setOnClickListener(this);
        /*lblClearAll.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        lblHistory = findViewById(R.id.lblHistory);
        lblHistory.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblHistory.setOnClickListener(this);
        /*lblHistory.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        lblTotal = findViewById(R.id.lblTotal);
        lblTotal.setTypeface(Utils.getTypeFaceBodoni72(this));
        /*lblTotal.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        lblLast10Results = findViewById(R.id.lblLast10Results);
        lblLast10Results.setTypeface(Utils.getTypeFaceBodoni72(this));

        lblCancelBet = findViewById(R.id.lblCancelBet);
        lblCancelBet.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblCancelBet.setOnClickListener(this);

        textViewHeadPoints = findViewById(R.id.textViewHeadPoints);
        textViewHeadPoints.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewTailPoints = findViewById(R.id.textViewTailPoints);
        textViewTailPoints.setTypeface(Utils.getTypeFaceBodoni72(this));

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

        recyclerViewLastResults = findViewById(R.id.recyclerViewLastResults);
        recyclerViewLastResults.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        recyclerViewPoints = findViewById(R.id.recyclerViewPoints);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        flipView = findViewById(R.id.flipView);
        flipView.setOnFlipListener(this);

        circularProgressBar = findViewById(R.id.circularProgressBar);
//        circularProgressBar.setProgressMax(55);

        imgViewHead = findViewById(R.id.imgViewHead);
        imgViewHead.setOnClickListener(this);

        imgViewTail = findViewById(R.id.imgViewTail);
        imgViewTail.setOnClickListener(this);

        imgViewSelectHead = findViewById(R.id.imgViewSelectHead);

        imgViewSelectTail = findViewById(R.id.imgViewSelectTail);

        groupHeadPoints = findViewById(R.id.groupHeadPoints);

        groupTailPoints = findViewById(R.id.groupTailPoints);

        konfettiView = findViewById(R.id.konfettiView);

    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_GAME_ID)) {
                gameId = getIntent().getIntExtra(AppConstant.KEY_GAME_ID, -1);
            }
        }
    }

    private ArrayList<CustomGameAmountResponse> fetchCustomGameAmountsFromDb() {
        return (ArrayList<CustomGameAmountResponse>) ASOnlineApplication.appDatabase.customGameAmountDetailsDao().getCustomGameAmountDetailsWithGameId(gameId);
    }

    private void loadHTPointsAdapter() {
        if (headTailPointsAdapter == null) {
            headTailPointsAdapter = new HeadTailPointsAdapter(this, customGameAmounts);
            recyclerViewPoints.setAdapter(headTailPointsAdapter);
        } else {
            headTailPointsAdapter.notifyDataSetChanged();
        }
    }

    private void loadHeadTailResultAdapter() {
        if (headTailResultAdapter == null) {
            headTailResultAdapter = new HeadTailResultAdapter(this, htLastResults);
            recyclerViewLastResults.setAdapter(headTailResultAdapter);
        } else {
            headTailResultAdapter.notifyDataSetChanged();
        }
    }

    private void initHTBetModel() {
        headBetModel = new HTBetModel();
        headBetModel.setTotalAmount(0);
        headBetModel.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        headBetModel.setBetType(AppConstant.HT_BET_TYPE_HEAD);
        headBetModel.setSelected(false);

        tailBetModel = new HTBetModel();
        tailBetModel.setTotalAmount(0);
        tailBetModel.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        tailBetModel.setBetType(AppConstant.HT_BET_TYPE_TAIL);
        tailBetModel.setSelected(false);
    }

    private void updateTotalPoints() {
        btnTotal.setText(String.valueOf(getTotalPoints()));
    }

    private int getTotalPoints() {
        totalPoints = headBetModel.getTotalAmount() + tailBetModel.getTotalAmount();
        return totalPoints;
    }

    private void clearAllBetData() {
        /*betType = "";
        totalPoints = 0;
//        betId = -1;
        updateTotalPoints();*/
        selectedIndex = -1;
        headBetModel = null;
        tailBetModel = null;
        initHTBetModel();
        updateTotalPoints();
        groupHeadPoints.setVisibility(View.GONE);
        groupTailPoints.setVisibility(View.GONE);
        imgViewHead.setImageResource(R.drawable.play_head);
        imgViewTail.setImageResource(R.drawable.play_tail);
//        initPreviousHTBetModel();
    }

    private void clearPreviousBetData() {
        previousHeadBetModel = null;
        previousTailBetModel = null;
        initHTBetModel();
    }

    private void setPreviousBetData(AddHTNewBetData addHTNewBetData) {
        toggleBetType(addHTNewBetData);
    }

    private void setPreviousBetData(HTExistingBetResponse htExistingBetResponse) {
        toggleBetType(htExistingBetResponse);
    }

    private void initPreviousHTBetModel() {
        if (previousHeadBetModel == null) {
            previousHeadBetModel = new HTBetModel();
        }
        previousHeadBetModel.setTotalAmount(previousHeadBetModel.getTotalAmount() + headBetModel.getTotalAmount());
        previousHeadBetModel.setCustId(headBetModel.getCustId());
        previousHeadBetModel.setBetType(headBetModel.getBetType());

        if (previousTailBetModel == null) {
            previousTailBetModel = new HTBetModel();
        }
        previousTailBetModel.setTotalAmount(previousTailBetModel.getTotalAmount() + tailBetModel.getTotalAmount());
        previousTailBetModel.setCustId(tailBetModel.getCustId());
        previousTailBetModel.setBetType(tailBetModel.getBetType());

        if (previousHeadBetModel.getTotalAmount() > 0) {
            groupHeadPoints.setVisibility(View.VISIBLE);
            textViewHeadPoints.setText(String.valueOf(previousHeadBetModel.getTotalAmount()));
        }
        if (previousTailBetModel.getTotalAmount() > 0) {
            groupTailPoints.setVisibility(View.VISIBLE);
            textViewTailPoints.setText(String.valueOf(previousTailBetModel.getTotalAmount()));
        }
    }

    private void initPreviousHTBetModel(ArrayList<HTExistingBetResponse> htExistingBets) {
        for (HTExistingBetResponse htExistingBet : htExistingBets) {
            if (htExistingBet.getBetType().equals(AppConstant.HT_BET_TYPE_HEAD)) {
                if (previousHeadBetModel == null) {
                    previousHeadBetModel = new HTBetModel();
                }
                previousHeadBetModel.setTotalAmount(htExistingBet.getTotalAmount());
                previousHeadBetModel.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
                previousHeadBetModel.setBetType(htExistingBet.getBetType());
                if (previousHeadBetModel.getTotalAmount() > 0) {
                    groupHeadPoints.setVisibility(View.VISIBLE);
                    textViewHeadPoints.setText(String.valueOf(previousHeadBetModel.getTotalAmount()));
                }
            } else if (htExistingBet.getBetType().equals(AppConstant.HT_BET_TYPE_TAIL)) {
                if (previousTailBetModel == null) {
                    previousTailBetModel = new HTBetModel();
                }
                previousTailBetModel = new HTBetModel();
                previousTailBetModel.setTotalAmount(htExistingBet.getTotalAmount());
                previousTailBetModel.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
                previousTailBetModel.setBetType(htExistingBet.getBetType());
                if (previousTailBetModel.getTotalAmount() > 0) {
                    groupTailPoints.setVisibility(View.VISIBLE);
                    textViewTailPoints.setText(String.valueOf(previousTailBetModel.getTotalAmount()));
                }
            }
        }
    }

    private void toggleBetType(AddHTNewBetData addHTNewBetData) {
        switch (addHTNewBetData.getBetType()) {
            case AppConstant.HT_BET_TYPE_HEAD:
                imgViewHead.setImageResource(R.drawable.play_head_selected);
                groupHeadPoints.setVisibility(View.VISIBLE);
                textViewHeadPoints.setText(String.valueOf(addHTNewBetData.getTotalAmount()));
                break;
            case AppConstant.HT_BET_TYPE_TAIL:
                imgViewTail.setImageResource(R.drawable.play_tail_selected);
                groupTailPoints.setVisibility(View.VISIBLE);
                textViewTailPoints.setText(String.valueOf(addHTNewBetData.getTotalAmount()));
                break;
        }
    }

    private void toggleBetType(HTExistingBetResponse htExistingBetResponse) {
        switch (htExistingBetResponse.getBetType()) {
            case AppConstant.HT_BET_TYPE_HEAD:
                imgViewHead.setImageResource(R.drawable.play_head_selected);
                groupHeadPoints.setVisibility(View.VISIBLE);
                textViewHeadPoints.setText(String.valueOf(htExistingBetResponse.getTotalAmount()));
                break;
            case AppConstant.HT_BET_TYPE_TAIL:
                imgViewTail.setImageResource(R.drawable.play_tail_selected);
                groupTailPoints.setVisibility(View.VISIBLE);
                textViewTailPoints.setText(String.valueOf(htExistingBetResponse.getTotalAmount()));
                break;
        }
    }

    private void disableGame() {
        betType = "";
        btnBetOk.setVisibility(View.INVISIBLE);
        imgViewHead.setClickable(false);
        imgViewTail.setClickable(false);
    }

    private void enableGame() {
        btnBetOk.setVisibility(View.VISIBLE);
        imgViewHead.setClickable(true);
        imgViewTail.setClickable(true);
    }

    private void applyBet() {
        if (walletBalance >= totalPoints) {
            if (/*betType != null && !betType.isEmpty()*/headBetModel.isSelected() || tailBetModel.isSelected()) {
                if (totalPoints > 0) {
//                    callAddHTNewBetApi();
                    addNewBet();
                } else {
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.ht_point_error), Snackbar.LENGTH_LONG,
                            getResources().getString(R.string.btn_okay)).show();
                }
            } else {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.ht_empty_bet_error), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.insufficient_points_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
        }
    }

    public void animatePointsTextView(int initialValue, int finalValue) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                btnWalletBalance.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }

    private void showKonfettiView() {
        Size sizeArray[] = {new Size(10, 5f), new Size(10, 5f)};
        konfettiView.build()
                .addColors(Color.parseColor("#FFbe965d"), Color.parseColor("#FFbe965d"), Color.parseColor("#FFbe965d"), Color.parseColor("#FFbe965d"))
                .setDirection(0.0, 359.0)
                .setSpeed(4f, 7f)
                .setFadeOutEnabled(true)
                .setTimeToLive(3000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(sizeArray)
                .setPosition(konfettiView.getX() + konfettiView.getWidth() / 2f, konfettiView.getY() + konfettiView.getHeight() / 3f)
                .burst(150);
    }

    private void callHTInitialData() {
        if (!mSocket.connected()) return;

        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(getHTInitialRequest()));
            mSocket.emit("ht-initial-data", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addNewBet() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            showInternetError();
            return;
        }

        if (!mSocket.connected()) return;

        try {
            /*JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            JSONObject betObject = new JSONObject();
            betObject.put("total_amount", 10);
            betObject.put("cust_id", 4);
            betObject.put("bet_type", "T");

            jsonArray.put(betObject);

            jsonObject.put("bets", jsonArray);*/
            JSONObject jsonObject = new JSONObject(gson.toJson(getAddHTNewBetRequest()));

            mSocket.emit("new-ht-bet", jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callCancelHTBet() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            showInternetError();
            return;
        }

        if (!mSocket.connected()) return;

        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(getHTCancelBetRequest()));
            mSocket.emit("cancel-ht-bet", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callHTResults() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            showInternetError();
            return;
        }

        if (!mSocket.connected()) return;

        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(getHTResultRequest()));
            mSocket.emit("ht-results", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void navigateToHeadTailHistoryScreen() {
        Intent intent = new Intent(this, HeadTailHistoryScreenActivity.class);
        startActivity(intent);
    }

    private void displayResult() {
        if (isExistingBet && htResultData != null) {
            if (htResultData.getWinningAmount() > 0) {
                LogUtils.d(TAG, "You Have Won");
                ASOnlinePreferenceManager.saveInteger(HeadTailGameScreenActivity.this, AppConstant.KEY_USER_POINTS, htResultData.getUpdatedPoints());
                showKonfettiView();
                showHTCongratulationsDialog(htResultData);
            } else {
                LogUtils.d(TAG, "You Have Lost");
                Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, parentLayout,
                        getResources().getString(R.string.ht_lost_msg), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            LogUtils.d(TAG, "No Bets Applied");
        }
        isExistingBet = false;
    }

    private void showCongratulationsDialog(String winningAmount) {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                String.format(getResources().getString(R.string.ht_winner_msg), winningAmount));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_HT_CONGRATULATIONS);

        dialogHandler.postDelayed(dialogRunnable, 2000);
    }

    private void showHTCongratulationsDialog(HTResultData htResultData) {
        dismissCustomHTCongratulationsDialog();
        customHTCongratulationsDialogFragment = CustomHTCongratulationsDialogFragment.newInstance(htResultData);
        customHTCongratulationsDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_HT_CONGRATULATIONS);

        dialogHandler.postDelayed(dialogRunnable, 2000);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    private void dismissCustomHTCongratulationsDialog() {
        if (!isFinishing() && customHTCongratulationsDialogFragment != null &&
                customHTCongratulationsDialogFragment.isVisible()) {
            customHTCongratulationsDialogFragment.dismissAllowingStateLoss();
            customHTCongratulationsDialogFragment = null;
        }
    }

    private Emitter.Listener onHTInitialDataDone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    LogUtils.d(TAG, data.toString());

                    HTInitialResponse htInitialResponse = gson.fromJson(data.toString(), HTInitialResponse.class);
                    LogUtils.d(TAG, "INITIAL DATA " + htInitialResponse.getStatusCode());

                    if (htInitialResponse.getHtInitialData() != null) {
                        HTInitialData htInitialData = htInitialResponse.getHtInitialData();
                        if (htInitialData != null) {
                            LogUtils.d(TAG, String.valueOf(htInitialData.getHtCurrentSlotResponse().getRemainingTime()));
                            htExistingBets = (ArrayList<HTExistingBetResponse>) htInitialData.getHtExistingBets();
                            if (htExistingBets != null && !htExistingBets.isEmpty()) {
//                        disableGame();
//                        setPreviousBetData(htExistingBetResponse);
                                isExistingBet = true;
                                initPreviousHTBetModel(htExistingBets);
                            }
                            if (htInitialData.getHtLastResults() != null &&
                                    !htInitialData.getHtLastResults().isEmpty()) {
                                htLastResults = (ArrayList<HTLastResultResponse>) htInitialData.getHtLastResults();
                                loadHeadTailResultAdapter();
                            }
                            if (htInitialData.getHtCurrentSlotResponse() != null) {
                                slotId = htInitialData.getHtCurrentSlotResponse().getSlotNo();
                                if (htInitialData.getHtCurrentSlotResponse().getRemainingTime() > 0) {
                                    leftMilliSeconds = htInitialData.getHtCurrentSlotResponse().getRemainingTime() * 1000;

                                    remainingTime = htInitialData.getHtCurrentSlotResponse().getRemainingTime();

//                            circularProgressBar.setProgressMax(remainingTime);

                                    intervalTime = htInitialData.getIntervalTime() * 60;
                                    closesBefore = htInitialData.getCloseBefore();

                                    intervalTime = intervalTime - closesBefore;
                                    elapsedProgress = intervalTime;
                                    elapsedFlipCoinTime = closesBefore;

                                    circularProgressBar.setProgressMax(intervalTime);
                                    totalProgress = intervalTime - remainingTime;
                                    circularProgressBar.setProgress(totalProgress);

                                    progressBarHandler.postDelayed(progressBarRunnable, 0);
                                } else if (htInitialData.getHtCurrentSlotResponse().getRemainingTime() <= 0) {
                                    int value = Math.abs(htInitialData.getHtCurrentSlotResponse().getRemainingTime());
                                    LogUtils.d(TAG, String.valueOf(value));
                                    intervalTime = htInitialData.getIntervalTime() * 60;
                                    closesBefore = htInitialData.getCloseBefore();

                                    intervalTime = intervalTime - closesBefore;
                                    elapsedFlipCoinTime = closesBefore - value;
                                    flipCoinHandler.postDelayed(flipRunnable, 0);
                                }
                            }
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onHTInitialDataError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d(TAG, "ERROR INITIAL DATA");
                    String message = (String) args[0];
                    if (message != null && !message.isEmpty()) {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                message,
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    } else {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                getResources().getString(R.string.something_went_wrong_error),
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewHTBetDone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    LogUtils.d(TAG, "NEW HT BET");

                    AddHTNewBetResponse addHTNewBetResponse = gson.fromJson(data.toString(), AddHTNewBetResponse.class);
                    ;
                    if (addHTNewBetResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                        isExistingBet = true;
//                disableGame();
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, parentLayout,
                                getResources().getString(R.string.bet_placed_success_msg), Snackbar.LENGTH_LONG,
                                getResources().getString(R.string.btn_okay)).show();
                        HTNewBetData htNewBetData = addHTNewBetResponse.getHtNewBetData();
                        if (htNewBetData != null) {
                            ArrayList<AddHTNewBetData> addHTNewBetDataList = (ArrayList<AddHTNewBetData>) htNewBetData.getAddHTNewBetDataList();
                            if (addHTNewBetDataList != null && !addHTNewBetDataList.isEmpty()) {
                                slotId = htNewBetData.getSlot();
//                        betId = addHTNewBetDataList.get(0).getBetId();
//                    btnTotal.setText(String.valueOf(totalPoints));
//                    setPreviousBetData(addHTNewBetData);
                                initPreviousHTBetModel();
                                imgViewHead.setImageResource(R.drawable.play_head);
                                imgViewTail.setImageResource(R.drawable.play_tail);
                                selectedIndex = -1;
                                headBetModel = null;
                                tailBetModel = null;
                                initHTBetModel();
                                updateTotalPoints();
//                        UserData userData = addHTNewBetDataList.get(0).getUserData();
                                ASOnlinePreferenceManager.saveInteger(HeadTailGameScreenActivity.this,
                                        AppConstant.KEY_USER_POINTS, htNewBetData.getPoints());
                                walletBalance = ASOnlinePreferenceManager.getInteger(HeadTailGameScreenActivity.this,
                                        AppConstant.KEY_USER_POINTS, 0);
                                btnWalletBalance.setText(String.valueOf(walletBalance));
//                    clearAllBetData();
                            }
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewHTBetError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d(TAG, "ERROR NEW HT BET");
                    String message = (String) args[0];
                    if (message != null && !message.isEmpty()) {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                message,
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    } else {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                getResources().getString(R.string.something_went_wrong_error),
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onCancelHTBetDone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    HTCancelBetResponse htCancelBetResponse = gson.fromJson(data.toString(), HTCancelBetResponse.class);
                    LogUtils.d(TAG, "CANCEL HT BET " + htCancelBetResponse.getStatusCode());

                    if (htCancelBetResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                        HTCancelBetData htCancelBetData = htCancelBetResponse.getHtCancelBetData();
                        isExistingBet = false;
                        betId = -1;
                        clearAllBetData();
                        previousHeadBetModel = null;
                        previousTailBetModel = null;
                        enableGame();
                        if (htCancelBetData != null) {
                            Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, parentLayout,
                                    getResources().getString(R.string.ht_bet_cancel_msg), Snackbar.LENGTH_LONG,
                                    getResources().getString(R.string.btn_okay)).show();
                            ASOnlinePreferenceManager.saveInteger(HeadTailGameScreenActivity.this, AppConstant.KEY_USER_POINTS, htCancelBetData.getPoints());
                            walletBalance = ASOnlinePreferenceManager.getInteger(HeadTailGameScreenActivity.this, AppConstant.KEY_USER_POINTS, 0);
                            btnWalletBalance.setText(String.valueOf(walletBalance));
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onCancelHTBetError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d(TAG, "ERROR CANCEL HT BET");
                    String message = (String) args[0];
                    if (message != null && !message.isEmpty()) {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                message,
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    } else {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                getResources().getString(R.string.something_went_wrong_error),
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    }
                }
            });

        }
    };

    private Emitter.Listener onHTResultDone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d(TAG, "HT RESULT " + "TRUE");
                    JSONObject data = (JSONObject) args[0];
                    isResultEventCalled = true;
                    HTResultResponse htResultResponse = gson.fromJson(data.toString(), HTResultResponse.class);
                    if (htResultResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                        htResultData = htResultResponse.getHtResultData();
                        if (htResultData != null) {
                            if (htResultData.getResult() != null && !htResultData.getResult().isEmpty()) {
                                HTLastResultResponse htLastResultResponse = new HTLastResultResponse();
                                htLastResultResponse.setBetType(htResultData.getResult());
                                if (htLastResults.size() > 0) {
                                    if (htLastResults.size() == 10) {
                                        htLastResults.add(0, htLastResultResponse);
                                        htLastResults.remove(htLastResults.size() - 1);
                                    } else {
                                        htLastResults.add(0, htLastResultResponse);
                                    }
                                    headTailResultAdapter.updateData(htLastResults);
                                } else {
                                    htLastResults.add(htLastResultResponse);
                                    headTailResultAdapter.updateData(htLastResults);
                                }
                            }
                            /*if (isExistingBet) {
                                if (htResultData.getWinningAmount() > 0) {
                                    LogUtils.d(TAG, "You Have Won");
                                    ASOnlinePreferenceManager.saveInteger(HeadTailGameScreenActivity.this, AppConstant.KEY_USER_POINTS, htResultData.getUpdatedPoints());
                                    showKonfettiView();
//                                    showCongratulationsDialog(String.valueOf(htResultData.getWinningAmount()));
                                    showHTCongratulationsDialog(htResultData);
                                } else {
                                    LogUtils.d(TAG, "You Have Lost");
                                    Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, parentLayout,
                                            getResources().getString(R.string.ht_lost_msg), Snackbar.LENGTH_LONG,
                                            getResources().getString(R.string.btn_okay)).show();
                                }
                            } else {
                                LogUtils.d(TAG, "No Bets Applied");
                            }
                            isExistingBet = false;*/
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onHTResultError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d(TAG, "ERROR NEW HT BET");
                    String message = (String) args[0];
                    if (message != null && !message.isEmpty()) {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                message,
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    } else {
                        Utils.showCustomSnackBarMessageView(HeadTailGameScreenActivity.this, findViewById(R.id.parentLayout),
                                getResources().getString(R.string.something_went_wrong_error),
                                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    }
                }
            });
        }
    };

    private void callGetHTInitialDataApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_HT_INITIAL_REQUEST, getHTInitialRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_HT_INITIAL_DATA, intent, true);
    }

    private HTInitialRequest getHTInitialRequest() {
        HTInitialRequest htInitialRequest = new HTInitialRequest();
        htInitialRequest.setCustId(ASOnlinePreferenceManager.
                getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        return htInitialRequest;
    }

    private void callAddHTNewBetApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_ADD_HT_NEW_BET_REQUEST, getAddHTNewBetRequest());
        callAppServer(AppConstant.REQ_API_TYPE_ADD_HT_NEW_BET, intent, true);
    }

    private AddHTNewBetRequest getAddHTNewBetRequest() {
        AddHTNewBetRequest addHTNewBetRequest = new AddHTNewBetRequest();
        addHTNewBetRequest.setHtNewBets(getHTNewBetList());
        return addHTNewBetRequest;
    }

    private ArrayList<HTNewBet> getHTNewBetList() {
        ArrayList<HTNewBet> htNewBets = new ArrayList<>();
        if (headBetModel.getTotalAmount() > 0) {
            HTNewBet htNewBet = new HTNewBet();
            htNewBet.setTotalAmount(headBetModel.getTotalAmount());
            htNewBet.setCustId(headBetModel.getCustId());
            htNewBet.setBetType(headBetModel.getBetType());

            htNewBets.add(htNewBet);
        }
        if (tailBetModel.getTotalAmount() > 0) {
            HTNewBet htNewBet = new HTNewBet();
            htNewBet.setTotalAmount(tailBetModel.getTotalAmount());
            htNewBet.setCustId(tailBetModel.getCustId());
            htNewBet.setBetType(tailBetModel.getBetType());

            htNewBets.add(htNewBet);
        }

        return htNewBets;
    }

    private void callGetHTResultApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_HT_RESULT_REQUEST, getHTResultRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_HT_RESULT, intent, true);
    }

    private HTResultRequest getHTResultRequest() {
        HTResultRequest htResultRequest = new HTResultRequest();
        htResultRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        if (isExistingBet) {
            htResultRequest.setSlotNo(slotId);
        }

        return htResultRequest;
    }

    private void callCancelHTBetApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_CANCEL_HT_BET_REQUEST, getHTCancelBetRequest());
        callAppServer(AppConstant.REQ_API_TYPE_CANCEL_HT_BET, intent, true);
    }

    private HTCancelBetRequest getHTCancelBetRequest() {
        HTCancelBetRequest htCancelBetRequest = new HTCancelBetRequest();
        htCancelBetRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        htCancelBetRequest.setSlotId(slotId);
        return htCancelBetRequest;
    }

    Runnable flipRunnable = new Runnable() {
        @Override
        public void run() {
            /*if (elapsedFlipCoinTime == 0) {
                LogUtils.d(TAG, "Flip Coin Complete");
                flipCoinHandler.removeCallbacks(flipRunnable);
                isApiCalled = false;
                if (!isFinishing() && !isApiCalled) {
                    elapsedProgress = intervalTime;
                    elapsedFlipCoinTime = closesBefore;
                    leftMilliSeconds = intervalTime * 1000;
                    clearPreviousBetData();
                    clearAllBetData();
                    enableGame();
                    htExistingBets = null;
                    callHTResults();
                    betId = -1;
                    circularProgressBar.setProgressMax(intervalTime);
                    progressBarHandler.postDelayed(progressBarRunnable, 0);
                }
            } else {
                flipView.flipTheView();
                elapsedFlipCoinTime = elapsedFlipCoinTime - 1;
                flipCoinHandler.postDelayed(this, 1000);
            }*/
            flipView.flipTheView();
            elapsedFlipCoinTime = elapsedFlipCoinTime - 1;
            flipCoinHandler.postDelayed(this, 1000);
        }
    };

    Runnable progressBarRunnable = new Runnable() {
        @Override
        public void run() {
            /*if (isApiCalled) {*/
            if (leftMilliSeconds == 0) {
                LogUtils.d(TAG, "Progress complete");
                disableGame();
                btnTimer.setText("00:00");
                circularProgressBar.setProgress(0);
                totalProgress = 0;
                progressBarHandler.removeCallbacks(progressBarRunnable);
                flipCoinHandler.postDelayed(flipRunnable, 0);
                if (mp != null) {
                    mp.stop();
                    mp = null;
                }
            } else {
                leftMilliSeconds = leftMilliSeconds - 1000;

                long seconds = leftMilliSeconds / 1000 % 60;
                long minutes = leftMilliSeconds / (60 * 1000) % 60;
                long hours = leftMilliSeconds / (60 * 60 * 1000) % 24;

//                    btnTimer.setText(String.format("%02d : %02d : %02d", hours, minutes, seconds));
                btnTimer.setText(String.format("%02d : %02d", minutes, seconds));

                LogUtils.d(TAG, String.valueOf(totalProgress));
                if (isApiCalled) {
                    if (totalProgress >= (int) (intervalTime * 0.8)) {
                        LogUtils.d(TAG, "TRUE" + String.valueOf(totalProgress));
                        circularProgressBar.setProgressBarColor(Color.RED);
                        /*disableGame();*/
                    } else {
                        circularProgressBar.setProgressBarColor(Color.GREEN);
                    }
                } else {
                    if (totalProgress >= (int) (intervalTime * 0.8)) {
                        LogUtils.d(TAG, "TRUE" + String.valueOf(totalProgress));
                        circularProgressBar.setProgressBarColor(Color.RED);
                    } else {
                        circularProgressBar.setProgressBarColor(Color.GREEN);
                    }
                }

                totalProgress = totalProgress + 1;
                circularProgressBar.setProgress(totalProgress);
                elapsedProgress = elapsedProgress - 1;

                if (mp == null && ASOnlineApplication.isHeadTailGameActivityResumed && phoneState == TelephonyManager.CALL_STATE_IDLE) {
                    mp = MediaPlayer.create(HeadTailGameScreenActivity.this, R.raw.tick);
                }
                if (mp != null) {
                    if (!mp.isPlaying()) {
                        mp.start();
                    }
                }
                progressBarHandler.postDelayed(this, 1000);
            }
            /*} else {
                if (elapsedProgress == 0) {
                    LogUtils.d(TAG, "Progress complete");
                    progressBarHandler.removeCallbacks(progressBarRunnable);
                    flipCoinHandler.postDelayed(flipRunnable, 0);
                    if (mp != null) {
                        mp.stop();
                    }
                } else {
                    if (totalProgress == (intervalTime * 0.8)) {
                        circularProgressBar.setProgressBarColor(Color.RED);
                    }
                    totalProgress = totalProgress + 1;
                    circularProgressBar.setProgress(totalProgress);
                    elapsedProgress = elapsedProgress - 1;

                    if (mp != null) {
                        if (!mp.isPlaying()) {
                            mp.start();
                        }
                    }
                    progressBarHandler.postDelayed(this, 1000);
                }
            }*/
        }
    };

    Runnable dialogRunnable = new Runnable() {
        @Override
        public void run() {
            dismissCustomHTCongratulationsDialog();
            animatePointsTextView(walletBalance, htResultData.getUpdatedPoints());
            htResultData = null;
        }
    };

    Runnable konfettiRunnable = new Runnable() {
        @Override
        public void run() {
            showKonfettiView();
        }
    };

    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //INCOMING call
                //do all necessary action to pause the audio
                phoneState = TelephonyManager.CALL_STATE_RINGING;
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    mp.release();
                    mp = null;
                }

            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                //Not IN CALL
                //do anything if the phone-state is idle
                phoneState = TelephonyManager.CALL_STATE_IDLE;
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                //A call is dialing, active or on hold
                //do all necessary action to pause the audio
                //do something here
                phoneState = TelephonyManager.CALL_STATE_OFFHOOK;
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    mp.release();
                    mp = null;
                }
            }
            super.onCallStateChanged(state, phoneNumber);
        }
    };

    /**
     * @param response
     */
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof HTInitialResponse) {
            HTInitialResponse htInitialResponse = (HTInitialResponse) response;
            if (htInitialResponse.getHtInitialData() != null) {
                HTInitialData htInitialData = htInitialResponse.getHtInitialData();
                if (htInitialData != null) {
                    LogUtils.d(TAG, String.valueOf(htInitialData.getHtCurrentSlotResponse().getRemainingTime()));
                    htExistingBets = (ArrayList<HTExistingBetResponse>) htInitialData.getHtExistingBets();
                    if (htExistingBets != null && !htExistingBets.isEmpty()) {
//                        disableGame();
//                        setPreviousBetData(htExistingBetResponse);
                        isExistingBet = true;
                        initPreviousHTBetModel(htExistingBets);
                    }
                    if (htInitialData.getHtLastResults() != null &&
                            !htInitialData.getHtLastResults().isEmpty()) {
                        htLastResults = (ArrayList<HTLastResultResponse>) htInitialData.getHtLastResults();
                        loadHeadTailResultAdapter();
                    }
                    if (htInitialData.getHtCurrentSlotResponse() != null) {
                        slotId = htInitialData.getHtCurrentSlotResponse().getSlotNo();
                        if (htInitialData.getHtCurrentSlotResponse().getRemainingTime() > 0) {
                            leftMilliSeconds = htInitialData.getHtCurrentSlotResponse().getRemainingTime() * 1000;

                            remainingTime = htInitialData.getHtCurrentSlotResponse().getRemainingTime();

//                            circularProgressBar.setProgressMax(remainingTime);

                            intervalTime = htInitialData.getIntervalTime() * 60;
                            closesBefore = htInitialData.getCloseBefore();

                            intervalTime = intervalTime - closesBefore;
                            elapsedProgress = intervalTime;
                            elapsedFlipCoinTime = closesBefore;

                            circularProgressBar.setProgressMax(intervalTime);
                            totalProgress = intervalTime - remainingTime;
                            circularProgressBar.setProgress(totalProgress);

                            progressBarHandler.postDelayed(progressBarRunnable, 0);
                        } else if (htInitialData.getHtCurrentSlotResponse().getRemainingTime() <= 0) {
                            int value = Math.abs(htInitialData.getHtCurrentSlotResponse().getRemainingTime());
                            LogUtils.d(TAG, String.valueOf(value));
                            intervalTime = htInitialData.getIntervalTime() * 60;
                            closesBefore = htInitialData.getCloseBefore();

                            intervalTime = intervalTime - closesBefore;
                            elapsedFlipCoinTime = closesBefore - value;
                            flipCoinHandler.postDelayed(flipRunnable, 0);
                        }
                    }
                }
            }
        } else if (response instanceof AddHTNewBetResponse) {
            AddHTNewBetResponse addHTNewBetResponse = (AddHTNewBetResponse) response;
            if (addHTNewBetResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                isExistingBet = true;
//                disableGame();
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.bet_placed_success_msg), Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.btn_okay)).show();
                HTNewBetData htNewBetData = addHTNewBetResponse.getHtNewBetData();
                if (htNewBetData != null) {
                    ArrayList<AddHTNewBetData> addHTNewBetDataList = (ArrayList<AddHTNewBetData>) htNewBetData.getAddHTNewBetDataList();
                    if (addHTNewBetDataList != null && !addHTNewBetDataList.isEmpty()) {
                        slotId = htNewBetData.getSlot();
//                        betId = addHTNewBetDataList.get(0).getBetId();
//                    btnTotal.setText(String.valueOf(totalPoints));
//                    setPreviousBetData(addHTNewBetData);
                        initPreviousHTBetModel();
                        imgViewHead.setImageResource(R.drawable.play_head);
                        imgViewTail.setImageResource(R.drawable.play_tail);
                        selectedIndex = -1;
                        headBetModel = null;
                        tailBetModel = null;
                        initHTBetModel();
                        updateTotalPoints();
//                        UserData userData = addHTNewBetDataList.get(0).getUserData();
                        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, htNewBetData.getPoints());
                        walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
                        btnWalletBalance.setText(String.valueOf(walletBalance));
//                    clearAllBetData();
                    }
                }
            }
        } else if (response instanceof HTResultResponse) {
            HTResultResponse htResultResponse = (HTResultResponse) response;
            if (htResultResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                htResultData = htResultResponse.getHtResultData();
                if (htResultData != null) {
                    if (htResultData.getResult() != null && !htResultData.getResult().isEmpty()) {
                        HTLastResultResponse htLastResultResponse = new HTLastResultResponse();
                        htLastResultResponse.setBetType(htResultData.getResult());
                        if (htLastResults.size() > 0) {
                            if (htLastResults.size() == 10) {
                                htLastResults.add(0, htLastResultResponse);
                                htLastResults.remove(htLastResults.size() - 1);
                            } else {
                                htLastResults.add(0, htLastResultResponse);
                            }
                            headTailResultAdapter.updateData(htLastResults);
                        } else {
                            htLastResults.add(htLastResultResponse);
                            headTailResultAdapter.updateData(htLastResults);
                        }
                    }
                    if (isExistingBet) {
                        if (htResultData.getWinningAmount() > 0) {
                            LogUtils.d(TAG, "You Have Won");
                        /*Utils.showCustomSnackBarMessageView(this, parentLayout,
                                getResources().getString(R.string.ht_winner_msg), Snackbar.LENGTH_LONG,
                                getResources().getString(R.string.btn_okay)).show();*/
                            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, htResultData.getUpdatedPoints());
                            showKonfettiView();
//                            showCongratulationsDialog(String.valueOf(htResultData.getWinningAmount()));
                            showHTCongratulationsDialog(htResultData);
                        } else {
                            LogUtils.d(TAG, "You Have Lost");
                            Utils.showCustomSnackBarMessageView(this, parentLayout,
                                    getResources().getString(R.string.ht_lost_msg), Snackbar.LENGTH_LONG,
                                    getResources().getString(R.string.btn_okay)).show();
                        }
                    } else {
                        LogUtils.d(TAG, "No Bets Applied");
                    }
                    isExistingBet = false;
                }
            }
        } else if (response instanceof HTCancelBetResponse) {
            HTCancelBetResponse htCancelBetResponse = (HTCancelBetResponse) response;
            if (htCancelBetResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                HTCancelBetData htCancelBetData = htCancelBetResponse.getHtCancelBetData();
                isExistingBet = false;
                betId = -1;
                clearAllBetData();
                enableGame();
                if (htCancelBetData != null) {
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.ht_bet_cancel_msg), Snackbar.LENGTH_LONG,
                            getResources().getString(R.string.btn_okay)).show();
                    ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, htCancelBetData.getPoints());
                    walletBalance = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0);
                    btnWalletBalance.setText(String.valueOf(walletBalance));
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
     * @param customGameAmount
     */
    @Override
    public void onPointsSelectListener(CustomGameAmountResponse customGameAmount) {
        if (customGameAmount != null) {
            if (selectedIndex != -1) {

                if (selectedIndex == 0) {
                    int totalPoints = headBetModel.getTotalAmount() + customGameAmount.getAmountValue();
                    groupHeadPoints.setVisibility(View.VISIBLE);
                    if (previousHeadBetModel != null && previousHeadBetModel.getTotalAmount() > 0) {
                        int previousPoints = previousHeadBetModel.getTotalAmount() + totalPoints;
                        if (previousPoints <= AppConstant.MAX_HEAD_TAIL_BET_POINTS) {
                            headBetModel.setTotalAmount(totalPoints);
                            textViewHeadPoints.setText(String.valueOf(previousPoints));
                        } else {
                            Utils.showCustomSnackBarMessageView(this, parentLayout, String.format(getResources().getString(R.string.maximum_head_bet_error), maxHTBetPoints),
                                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                        }
                    } else {
                        if (totalPoints <= AppConstant.MAX_HEAD_TAIL_BET_POINTS) {
                            headBetModel.setTotalAmount(totalPoints);
                            textViewHeadPoints.setText(String.valueOf(totalPoints));
                        } else {
                            Utils.showCustomSnackBarMessageView(this, parentLayout, String.format(getResources().getString(R.string.maximum_head_bet_error), maxHTBetPoints),
                                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                        }
                    }
                } else if (selectedIndex == 1) {
                    int totalPoints = tailBetModel.getTotalAmount() + customGameAmount.getAmountValue();
                    groupTailPoints.setVisibility(View.VISIBLE);
                    if (previousTailBetModel != null && previousTailBetModel.getTotalAmount() > 0) {
                        int previousPoints = previousTailBetModel.getTotalAmount() + totalPoints;
                        if (previousPoints <= AppConstant.MAX_HEAD_TAIL_BET_POINTS) {
                            tailBetModel.setTotalAmount(totalPoints);
                            textViewTailPoints.setText(String.valueOf(previousPoints));
                        } else {
                            Utils.showCustomSnackBarMessageView(this, parentLayout, String.format(getResources().getString(R.string.maximum_tail_bet_error), maxHTBetPoints),
                                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                        }
                    } else {
                        if (totalPoints <= AppConstant.MAX_HEAD_TAIL_BET_POINTS) {
                            tailBetModel.setTotalAmount(totalPoints);
                            textViewTailPoints.setText(String.valueOf(totalPoints));
                        } else {
                            Utils.showCustomSnackBarMessageView(this, parentLayout, String.format(getResources().getString(R.string.maximum_tail_bet_error), maxHTBetPoints),
                                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                        }
                    }
                }
                updateTotalPoints();
            }
            /*if (betType != null && !betType.isEmpty()) {

                totalPoints = totalPoints + customGameAmount.getAmountValue();
                updateTotalPoints();
                if (betType.equals(AppConstant.HT_BET_TYPE_HEAD)) {
                    if (groupHeadPoints.getVisibility() == View.GONE) {
                        groupHeadPoints.setVisibility(View.VISIBLE);
                    }
                    textViewHeadPoints.setText(String.valueOf(totalPoints));
                } else if (betType.equals(AppConstant.HT_BET_TYPE_TAIL)) {
                    if (groupTailPoints.getVisibility() == View.GONE) {
                        groupTailPoints.setVisibility(View.VISIBLE);
                    }
                    textViewTailPoints.setText(String.valueOf(totalPoints));
                }
            }*/
        }
    }

    /**
     * CustomSingleButtonDialogListener
     */
    @Override
    public void onClickSingleButtonListener() {
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_HT_CONGRATULATIONS)) {
                    dismissCustomSingleButtonDialog();
                    animatePointsTextView(walletBalance, htResultData.getUpdatedPoints());
                }
            }
        } else {
            super.onClickSingleButtonListener();
        }
    }

    /**
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                LogUtils.d(TAG, "AUDIOFOCUS_GAIN");
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                LogUtils.d(TAG, "AUDIOFOCUS_LOSS");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                LogUtils.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                LogUtils.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                break;
        }
    }

    /**
     * CustomHTCongratulationsDialogListener
     */
    @Override
    public void onClickPositiveButton() {
        if (customHTCongratulationsDialogFragment != null) {
            if (customHTCongratulationsDialogFragment.getTag() != null) {
                if (customHTCongratulationsDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_HT_CONGRATULATIONS)) {
                    dismissCustomHTCongratulationsDialog();
                    animatePointsTextView(walletBalance, htResultData.getUpdatedPoints());
                }
            }
        }
    }

    /**
     * @param easyFlipView
     * @param newCurrentSide After animation, the new side of the view. Either can be
     */
    @Override
    public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
        if (elapsedFlipCoinTime <= 0) {
            LogUtils.d(TAG, "ELAPSED TIME 0");
            if (!isResultEventCalled) {
                callHTResults();
                LogUtils.d(TAG, "RESULT EVENT CALLED");
                elapsedFlipCoinTime = closesBefore;
            }
        }
        if (newCurrentSide == EasyFlipView.FlipState.FRONT_SIDE) {
            LogUtils.d(TAG, "Front Side");
            if (htResultData != null && htResultData.getResult() != null && !htResultData.getResult().isEmpty()) {
                if (htResultData.getResult().equals(AppConstant.HT_BET_TYPE_HEAD)) {
                    LogUtils.d(TAG, "Flip Coin HEAD Complete");
                    displayResult();
                    flipCoinHandler.removeCallbacks(flipRunnable);
                    isApiCalled = false;
                    isResultEventCalled = false;
                    if (!isFinishing() && !isApiCalled) {
                        elapsedProgress = intervalTime;
                        elapsedFlipCoinTime = closesBefore;
                        leftMilliSeconds = intervalTime * 1000;
                        clearPreviousBetData();
                        clearAllBetData();
                        enableGame();
                        htExistingBets = null;
                        betId = -1;
                        if (htResultData.getWinningAmount() == 0) {
                            htResultData = null;
                        }
                        circularProgressBar.setProgressMax(intervalTime);
                        progressBarHandler.postDelayed(progressBarRunnable, 0);
                    }
                }
            }
        } else if (newCurrentSide == EasyFlipView.FlipState.BACK_SIDE) {
            LogUtils.d(TAG, "Back Side");
            if (htResultData != null && htResultData.getResult() != null && !htResultData.getResult().isEmpty()) {
                if (htResultData.getResult().equals(AppConstant.HT_BET_TYPE_TAIL)) {
                    LogUtils.d(TAG, "Flip Coin TAIL Complete");
                    displayResult();
                    flipCoinHandler.removeCallbacks(flipRunnable);
                    isApiCalled = false;
                    isResultEventCalled = false;
                    if (!isFinishing() && !isApiCalled) {
                        elapsedProgress = intervalTime;
                        elapsedFlipCoinTime = closesBefore;
                        leftMilliSeconds = intervalTime * 1000;
                        clearPreviousBetData();
                        clearAllBetData();
                        enableGame();
                        htExistingBets = null;
                        betId = -1;
                        if (htResultData.getWinningAmount() == 0) {
                            htResultData = null;
                        }
                        circularProgressBar.setProgressMax(intervalTime);
                        progressBarHandler.postDelayed(progressBarRunnable, 0);
                    }
                }
            }
        }
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblClearAll:
                /*if (betId == -1) {
                    clearAllBetData();
                }*/
                try {
                    if (headBetModel.getTotalAmount() > 0 ||
                            tailBetModel.getTotalAmount() > 0) {
                        clearAllBetData();
                        initPreviousHTBetModel();
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "" + e);
                }

                break;
            case R.id.lblCancelBet:
                try {
                    if (leftMilliSeconds > 0 && (previousHeadBetModel.getTotalAmount() > 0 || //todo-> NPE
                            previousTailBetModel.getTotalAmount() > 0)) {
//                    callCancelHTBetApi();
                        callCancelHTBet();
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "" + e);
                }
                break;
            case R.id.btnBetOk:
                applyBet();
//                addNewBet();
//                showHTCongratulationsDialog("20");
//                konfettiHandler.postDelayed(konfettiRunnable, 500);
                break;
            case R.id.imgViewHead:
                /*if (betType.equals(AppConstant.HT_BET_TYPE_TAIL)) {
                    imgViewTail.setImageResource(R.drawable.play_tail);
                    if (groupTailPoints.getVisibility() == View.VISIBLE) {
                        groupTailPoints.setVisibility(View.GONE);
                    }
                    totalPoints = 0;
                    updateTotalPoints();
                }
                betType = AppConstant.HT_BET_TYPE_HEAD;*/
                /*if (headNewBet == null) {
                    headNewBet = new HTNewBet();
                    headNewBet.setTotalAmount(totalPoints);
                    headNewBet.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
                    headNewBet.setBetType(AppConstant.HT_BET_TYPE_HEAD);
                }*/
                try {
                    if (!headBetModel.isSelected()) {
                        if (selectedIndex == -1) {
                            selectedIndex = 0;
                            headBetModel.setSelected(true);
                            imgViewHead.setImageResource(R.drawable.play_head_selected);
                        } else {
                            if (tailBetModel.getTotalAmount() > 0) {
                                selectedIndex = 0;
                                headBetModel.setSelected(true);
                                imgViewHead.setImageResource(R.drawable.play_head_selected);
                            }
                        }
                    } else {
                        headBetModel.setSelected(false);
                        headBetModel.setTotalAmount(0);
                        imgViewHead.setImageResource(R.drawable.play_head);
                        if (previousHeadBetModel != null && previousHeadBetModel.getTotalAmount() > 0) {
                            textViewHeadPoints.setText(String.valueOf(previousHeadBetModel.getTotalAmount()));
                        } else {
                            groupHeadPoints.setVisibility(View.GONE);
                            textViewHeadPoints.setText("");
                        }
                        updateTotalPoints();
                        selectedIndex = -1;
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "" + e);
                }
                break;
            case R.id.imgViewTail:
                /*if (betType.equals(AppConstant.HT_BET_TYPE_HEAD)) {
                    imgViewHead.setImageResource(R.drawable.play_head);
                    if (groupHeadPoints.getVisibility() == View.VISIBLE) {
                        groupHeadPoints.setVisibility(View.GONE);
                    }
                    totalPoints = 0;
                    updateTotalPoints();
                }
                betType = AppConstant.HT_BET_TYPE_TAIL;*/
                /*if (tailNewBet == null) {
                    tailNewBet = new HTNewBet();
                    tailNewBet.setTotalAmount(totalPoints);
                    tailNewBet.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
                    tailNewBet.setBetType(AppConstant.HT_BET_TYPE_TAIL);
                }*/
                try {
                    if (!tailBetModel.isSelected()) {
                        if (selectedIndex == -1) {
                            selectedIndex = 1;
                            tailBetModel.setSelected(true);
                            imgViewTail.setImageResource(R.drawable.play_tail_selected);
                        } else {
                            if (headBetModel.getTotalAmount() > 0) {
                                selectedIndex = 1;
                                tailBetModel.setSelected(true);
                                imgViewTail.setImageResource(R.drawable.play_tail_selected);
                            }
                        }
                    } else {
                        tailBetModel.setSelected(false);
                        tailBetModel.setTotalAmount(0);
                        imgViewTail.setImageResource(R.drawable.play_tail);
                        if (previousTailBetModel != null && previousTailBetModel.getTotalAmount() > 0) {
                            textViewTailPoints.setText(String.valueOf(previousTailBetModel.getTotalAmount()));
                        } else {
                            groupTailPoints.setVisibility(View.GONE);
                            textViewTailPoints.setText("");
                        }
                        updateTotalPoints();
                        selectedIndex = -1;
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "" + e);
                }
                break;
            case R.id.lblHistory:
                navigateToHeadTailHistoryScreen();
                break;
        }
    }
}