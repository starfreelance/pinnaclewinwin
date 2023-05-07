package com.pinnacle.winwin.ui.home;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.GameDetailsResponse;
import com.pinnacle.winwin.ui.bracketgame.BracketGameScreenActivity;
import com.pinnacle.winwin.ui.chartgame.ChartGameScreenActivity;
import com.pinnacle.winwin.ui.commongame.CommonGameScreenActivity;
import com.pinnacle.winwin.ui.cpgame.CPGameScreenActivity;
import com.pinnacle.winwin.ui.home.adapter.SelectGameAdapter;
import com.pinnacle.winwin.ui.home.listener.SelectGameListener;
import com.pinnacle.winwin.ui.home.model.SelectGameModel;
import com.pinnacle.winwin.ui.motorgame.MotorGameScreenActivity;
import com.pinnacle.winwin.ui.paanagame.PaanaGameScreenActivity;
import com.pinnacle.winwin.ui.paanagame.PaanaOTCGameScreenActivity;
import com.pinnacle.winwin.ui.singlegame.SingleGameScreenActivity;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class HomeScreenActivity extends ASOnlineBaseActivity implements
        SelectGameListener {

    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;

    private RecyclerView recyclerViewGames;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;

    private SelectGameAdapter mAdapter;

    private ArrayList<SelectGameModel> mSelectGameList;

    private ArrayList<GameDetailsResponse> gameDetails;
    private int baazaarId = -1;
    private String finalNumber;
    private String lastResult;
    private ArrayList<Integer> gameMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        processIntentData();
        initViews();
        /*mSelectGameList = getSelectGameList();
        if (mSelectGameList != null && !mSelectGameList.isEmpty()) {
            loadAdapter();
        }*/
        fetchGameDetailsFromDb();
    }

    @Override
    protected void showInternetError() {

    }

    @Override
    protected void onFailure(Object response) {

    }

    @Override
    protected void onSuccess(Object response) {

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
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
    }

    //Local Methods
    private void initViews() {

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_games));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textViewTitle.setLetterSpacing(0.2f);
        }*/

        recyclerViewGames = findViewById(R.id.recyclerViewGames);
        /*layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);*/
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerViewGames.setLayoutManager(gridLayoutManager);
    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_BAAZAAR_ID)) {
                baazaarId = getIntent().getIntExtra(AppConstant.KEY_BAAZAAR_ID, -1);
            }

            if (getIntent().hasExtra(AppConstant.KEY_FINAL_NUMBER) &&
                    getIntent().getStringExtra(AppConstant.KEY_FINAL_NUMBER) != null) {
                finalNumber = getIntent().getStringExtra(AppConstant.KEY_FINAL_NUMBER);
            }

            if (getIntent().hasExtra(AppConstant.KEY_GAME_MAP) &&
                    getIntent().getIntegerArrayListExtra(AppConstant.KEY_GAME_MAP) != null) {
                gameMap = getIntent().getIntegerArrayListExtra(AppConstant.KEY_GAME_MAP);
            }

            if (getIntent().hasExtra(AppConstant.KEY_LAST_RESULT) &&
                    getIntent().getStringExtra(AppConstant.KEY_LAST_RESULT) != null) {
                lastResult = getIntent().getStringExtra(AppConstant.KEY_LAST_RESULT);
            }
        }
    }

    private void fetchGameDetailsFromDb() {
        gameDetails = (ArrayList<GameDetailsResponse>) ASOnlineApplication.appDatabase.gameDetailsDao().getGameDetails();
        if (gameDetails != null && !gameDetails.isEmpty()) {
            if (gameMap != null && !gameMap.isEmpty()) {
                gameDetails.clear();
                for (Integer gameId : gameMap) {
                    GameDetailsResponse gameDetailsResponse = ASOnlineApplication.appDatabase.gameDetailsDao().getGameDetailWithGameId(gameId);
                    gameDetails.add(gameDetailsResponse);
                }
            }
            loadAdapter();
        }
    }

    private ArrayList<SelectGameModel> getSelectGameList() {

        ArrayList<SelectGameModel> selectGameList = new ArrayList<>();

        String[] selectGameNameArray = getResources().getStringArray(R.array.game_name_array);
        int[] selectGameTypeArray = {AppConstant.GAME_TYPE_SINGLE, AppConstant.GAME_TYPE_PAANA,
                AppConstant.GAME_TYPE_CP, AppConstant.GAME_TYPE_MOTOR};
        int[] selectGameImgResArray = {R.drawable.ic_single_game, /*R.drawable.ic_paana_game*/R.drawable.ic_spade_new,
                R.drawable.ic_cp_game, R.drawable.ic_motor_game};

        for (int i = 0; i < selectGameNameArray.length; i++) {
            SelectGameModel selectGameModel = new SelectGameModel();
            selectGameModel.setGameName(selectGameNameArray[i]);
            selectGameModel.setGameType(selectGameTypeArray[i]);
            selectGameModel.setImgResource(selectGameImgResArray[i]);

            selectGameList.add(selectGameModel);
        }

        return selectGameList;
    }

    private void loadAdapter() {
        if (mAdapter == null) {
            mAdapter = new SelectGameAdapter(this, gameDetails);
            recyclerViewGames.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void navigateToSingleGameScreen(int gameId) {
        Intent intent = new Intent(this, SingleGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToPaanaGameScreen(int gameId) {
        Intent intent = new Intent(this, PaanaGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToPaanaOTCGameScreen(int gameId) {
        Intent intent = new Intent(this, PaanaOTCGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToCPGameScreen(int gameId) {
        Intent intent = new Intent(this, CPGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToMotorGameScreen(int gameId) {
        Intent intent = new Intent(this, MotorGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToBracketGameScreen(int gameId) {
        Intent intent = new Intent(this, BracketGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToChartGameScreen(int gameId) {
        Intent intent = new Intent(this, ChartGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    private void navigateToCommonGameScreen(int gameId) {
        Intent intent = new Intent(this, CommonGameScreenActivity.class);
        /*Intent intent = new Intent(this, HeadTailGameScreenActivity.class);*/
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    //Event Handlers
    //SelectGameListener
    @Override
    public void onGameSelectListener(GameDetailsResponse gameDetail) {
        if (gameDetail != null) {
            switch (gameDetail.getGameId()) {
                case AppConstant.GAME_TYPE_SINGLE:
                    navigateToSingleGameScreen(gameDetail.getGameId());
                    break;
                case AppConstant.GAME_TYPE_PAANA:
                    navigateToPaanaGameScreen(gameDetail.getGameId());
                    /*navigateToPaanaOTCGameScreen(gameDetail.getGameId());*/
                    break;
                case AppConstant.GAME_TYPE_CP:
                    navigateToCPGameScreen(gameDetail.getGameId());
                    break;
                case AppConstant.GAME_TYPE_MOTOR:
                    navigateToMotorGameScreen(gameDetail.getGameId());
                    break;
                case AppConstant.GAME_TYPE_BRACKET:
                    navigateToBracketGameScreen(gameDetail.getGameId());
                    break;
                case AppConstant.GAME_TYPE_CHART:
                    navigateToChartGameScreen(gameDetail.getGameId());
                    break;
                case AppConstant.GAME_TYPE_COMMON:
                    navigateToCommonGameScreen(gameDetail.getGameId());
                    break;
            }
        }
    }
}
