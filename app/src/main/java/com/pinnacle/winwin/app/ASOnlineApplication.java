package com.pinnacle.winwin.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pinnacle.winwin.data.local.db.AppDatabase;
import com.pinnacle.winwin.ui.headtailgame.HeadTailGameScreenActivity;

public class ASOnlineApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ASOnlineApplication.class.getSimpleName();

    private static ASOnlineApplication mInstance;
    public static AppDatabase appDatabase;
    public static boolean isHeadTailGameActivityResumed;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        appDatabase = AppDatabase.getInstance(this);
        registerActivityLifecycleCallbacks(this);
//        LogUtils.e(TAG, BuildConfig.BASE_URL);
    }

    public static ASOnlineApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (activity instanceof HeadTailGameScreenActivity) {
            isHeadTailGameActivityResumed = true;
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (activity instanceof HeadTailGameScreenActivity) {
            isHeadTailGameActivityResumed = false;
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
