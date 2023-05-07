package com.pinnacle.winwin;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.MatkaMasterResponse;
import com.pinnacle.winwin.ui.LoginScreenActivity;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;

public class MainActivity extends ASOnlineBaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigateToScreenWithDelay();
        /*callGetMasterDataApi();*/
    }

    @Override
    protected void showInternetError() {

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
    private void navigateToScreenWithDelay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToDesiredScreen();

            }
        }, AppConstant.SPLASH_DELAY_TIME);
    }

    private void navigateToDesiredScreen() {
        boolean isLogin = ASOnlinePreferenceManager.getBoolean(this, AppConstant.KEY_IS_LOGIN, false);
        Intent intent;
        if (isLogin) {
//            intent = new Intent(this, BaazaarGridScreenActivity.class);
            intent = new Intent(this, BaazaarNewListScreenActivity.class);
        } else {
            intent = new Intent(this, LoginScreenActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void callGetMasterDataApi() {
        callAppServer(AppConstant.REQ_API_TYPE_GET_MASTER_DATA, null, true);
    }

    //API Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof  MatkaMasterResponse) {
            MatkaMasterResponse matkaMasterResponse = (MatkaMasterResponse) response;
            LogUtils.e(TAG, String.valueOf(matkaMasterResponse.getMasterDataResponse().getBazaarDetails().size()));
        }
    }

    @Override
    protected void onFailure(Object response) {

    }
}
