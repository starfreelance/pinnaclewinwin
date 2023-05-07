package com.pinnacle.winwin.data.local.db;

import android.os.AsyncTask;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.network.model.CustomGameAmountResponse;
import com.pinnacle.winwin.network.model.CustomGamesResponse;
import com.pinnacle.winwin.network.model.MatkaInitialData;

import java.util.ArrayList;
import java.util.List;

public class SaveInitialDataInDbTask extends AsyncTask<MatkaInitialData, Void, Void> {

    private static final String TAG = SaveInitialDataInDbTask.class.getSimpleName();

    private MatkaInitialData mMatkaInitialData;
    private SaveInitialDataInDbListener listener;

    public SaveInitialDataInDbTask(MatkaInitialData matkaInitialData, SaveInitialDataInDbListener listener) {
        mMatkaInitialData = matkaInitialData;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(MatkaInitialData... matkaInitialData) {

        try {

            List<BazaarDetailsResponse> bazaarDetails = new ArrayList<>();
//            int[] imgResourceArray = {R.drawable.ic_dice, R.drawable.ic_poker_chip, R.drawable.ic_cards, R.drawable.ic_chip};
            int[] imgResourceArray = {R.drawable.ic_time_open, R.drawable.ic_time_close,
                    R.drawable.ic_milanday_open, R.drawable.ic_dice, R.drawable.ic_milanday_close,
                    R.drawable.ic_poker_chip, R.drawable.ic_milannight_open, R.drawable.ic_cards,
                    R.drawable.ic_milannight_close, R.drawable.ic_chip};
            for (int i = 0; i < imgResourceArray.length; i++) {
                BazaarDetailsResponse bazaarDetail = mMatkaInitialData.getBazaarDetails().get(i);
                bazaarDetail.setImgResource(imgResourceArray[i]);

                bazaarDetails.add(bazaarDetail);
            }

            ASOnlineApplication.appDatabase.baazaarDetailsDao().insertAll(bazaarDetails);

            List<CustomGamesResponse> customGames = new ArrayList<>();
            int[] imgResArray = {R.drawable.ic_milanday_open};

            for (int i = 0; i < imgResArray.length; i++) {
                CustomGamesResponse customGame = mMatkaInitialData.getCustomGames().get(i);
                customGame.setImgResource(imgResArray[i]);

                customGames.add(customGame);
            }

            ASOnlineApplication.appDatabase.customGamesDao().insertAll(customGames);

            for (CustomGamesResponse customGame : mMatkaInitialData.getCustomGames()) {
                for (CustomGameAmountResponse customGameAmountDetail : customGame.getAmountDetails()) {
                    customGameAmountDetail.setGameId(customGame.getGameId());
                    ASOnlineApplication.appDatabase.customGameAmountDetailsDao().insert(customGameAmountDetail);
                }
            }

        } catch (Exception e) {
            if (listener != null) {
                listener.onInitialDataSaveFailed();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (listener != null) {
            listener.onInitialDataSavedSuccessfully();
        }
    }

    public interface SaveInitialDataInDbListener {
        void onInitialDataSavedSuccessfully();

        void onInitialDataSaveFailed();
    }
}
