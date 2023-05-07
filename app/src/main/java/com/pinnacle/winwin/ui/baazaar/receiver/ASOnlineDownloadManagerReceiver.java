package com.pinnacle.winwin.ui.baazaar.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.listener.DownloadManagerReceiverListener;

public class ASOnlineDownloadManagerReceiver extends BroadcastReceiver {

    private static final String TAG = ASOnlineDownloadManagerReceiver.class.getSimpleName();

    private DownloadManagerReceiverListener downloadManagerReceiverListener;

    public ASOnlineDownloadManagerReceiver(DownloadManagerReceiverListener downloadManagerReceiverListener) {
        this.downloadManagerReceiverListener = downloadManagerReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Fetching the download id received with the broadcast
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (AppConstant.downloadID == id) {
                /*LogUtils.e(TAG, "DOWNLOAD COMPLETED!!");*/
                downloadManagerReceiverListener.onDownloadCompleted(intent);
            }
        }

    }
}
