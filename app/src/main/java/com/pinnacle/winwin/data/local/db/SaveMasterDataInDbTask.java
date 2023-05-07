package com.pinnacle.winwin.data.local.db;

import android.os.AsyncTask;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.network.model.AmountDetailsResponse;
import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.network.model.GameDetailsResponse;
import com.pinnacle.winwin.network.model.MasterDataResponse;

import java.util.ArrayList;
import java.util.List;

public class SaveMasterDataInDbTask extends AsyncTask<MasterDataResponse, Void, Void> {

    private static final String TAG = SaveMasterDataInDbTask.class.getSimpleName();

    private MasterDataResponse masterDataResponse;
    private SaveMasterDataListener listener;

    public SaveMasterDataInDbTask(MasterDataResponse masterDataResponse, SaveMasterDataListener listener) {
        this.masterDataResponse = masterDataResponse;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(MasterDataResponse... masterDataResponses) {

        try {

            List<BazaarDetailsResponse> bazaarDetails = new ArrayList<>();
//            int[] imgResourceArray = {R.drawable.ic_dice, R.drawable.ic_poker_chip, R.drawable.ic_cards, R.drawable.ic_chip};
            int[] imgResourceArray = {R.drawable.ic_time_open, R.drawable.ic_time_close,
                    R.drawable.ic_milanday_open, R.drawable.ic_dice, R.drawable.ic_milanday_close,
                    R.drawable.ic_poker_chip, R.drawable.ic_milannight_open, R.drawable.ic_cards,
                    R.drawable.ic_milannight_close, R.drawable.ic_chip};
            for (int i = 0; i < imgResourceArray.length; i++) {
                BazaarDetailsResponse bazaarDetail = masterDataResponse.getBazaarDetails().get(i);
                bazaarDetail.setImgResource(imgResourceArray[i]);

                bazaarDetails.add(bazaarDetail);
            }

            ASOnlineApplication.appDatabase.baazaarDetailsDao().insertAll(bazaarDetails);

            List<GameDetailsResponse> gameDetails = new ArrayList<>();
            /*int[] selectGameImgResArray = {R.drawable.ic_single_game, R.drawable.ic_spade_new,
                    R.drawable.ic_cp_game, R.drawable.ic_motor_game, R.drawable.ic_bracket_game};*/
//            String[] imgResNameArray = {"ic_single_game", "ic_spade_new", "ic_cp_game", "ic_motor_game", "ic_bracket_game"};
            String[] imgResNameArray = {"ic_single_game", "ic_spade_new", "ic_cp_game", "ic_motor_game",
                    "ic_bracket_game", "ic_chart_game", "ic_common_game"};
            for (int j = 0; j <imgResNameArray.length; j++) {
                GameDetailsResponse gameDetail = masterDataResponse.getGameDetails().get(j);
                /*gameDetail.setImgResource(selectGameImgResArray[j]);*/
                gameDetail.setImageResourceName(imgResNameArray[j]);

                gameDetails.add(gameDetail);
            }

            ASOnlineApplication.appDatabase.gameDetailsDao().insertAll(gameDetails);

            for (GameDetailsResponse gameDetail : masterDataResponse.getGameDetails()) {
                for (AmountDetailsResponse amountDetail : gameDetail.getAmountDetail()) {
                    amountDetail.setGameId(gameDetail.getGameId());
                    ASOnlineApplication.appDatabase.amountDetailsDao().insert(amountDetail);
                }
            }

            ASOnlineApplication.appDatabase.paanaDetailsDao().insertAll(masterDataResponse.getPaanaDetails());
            ASOnlineApplication.appDatabase.cpDetailsDao().insertAll(masterDataResponse.getCpPaanaDetails());
            ASOnlineApplication.appDatabase.motorCombDetailsDao().insertAll(masterDataResponse.getMotorCombDetails());
            ASOnlineApplication.appDatabase.chartDetailsDao().insertAll(masterDataResponse.getChartDetails());

        } catch (Exception e) {
            listener.onMasterDataSaveFailed();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        listener.onMasterDataSavedSuccessfully();

    }

    public interface SaveMasterDataListener {
        void onMasterDataSavedSuccessfully();
        void onMasterDataSaveFailed();
    }
}
