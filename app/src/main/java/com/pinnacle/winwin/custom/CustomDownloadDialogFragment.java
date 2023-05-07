package com.pinnacle.winwin.custom;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.listener.DownloadManagerReceiverListener;
import com.pinnacle.winwin.ui.baazaar.receiver.ASOnlineDownloadManagerReceiver;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class CustomDownloadDialogFragment extends DialogFragment implements DownloadManagerReceiverListener {

    private static final String TAG = CustomDownloadDialogFragment.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewPercentage;
    private AppCompatTextView textViewTotalPercentage;
    private ProgressBar progressBarDownload;

    private Activity mActivity;

    private DownloadManager downloadManager;
    private boolean downloading;
    private ASOnlineDownloadManagerReceiver asOnlineDownloadManagerReceiver;

    public static CustomDownloadDialogFragment newInstance() {

        Bundle args = new Bundle();

        CustomDownloadDialogFragment fragment = new CustomDownloadDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = null;
        if (window != null) {
            display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            window.setLayout((int) (size.x * 0.70), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }

        registerASOnlineDownloadManagerReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();

        unregisterDownloadManagerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_download, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        downloadApk("https://asonlinegames.com/api/apk/asonline.apk");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.e("DIALOG FRAGMENT", "Exception", e);
        }
    }

    //Local Methods
    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));


        textViewPercentage = view.findViewById(R.id.textViewPercentage);
        textViewPercentage.setTypeface(Utils.getTypeFaceBodoni72(mActivity));


        textViewTotalPercentage = view.findViewById(R.id.textViewTotalPercentage);
        textViewTotalPercentage.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        progressBarDownload = view.findViewById(R.id.progressBarDownload);

    }

    private void registerASOnlineDownloadManagerReceiver() {
        asOnlineDownloadManagerReceiver = new ASOnlineDownloadManagerReceiver(this);
        mActivity.registerReceiver(asOnlineDownloadManagerReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void unregisterDownloadManagerReceiver() {
        if (asOnlineDownloadManagerReceiver != null) {
            mActivity.unregisterReceiver(asOnlineDownloadManagerReceiver);
        }
    }

    private void downloadApk(String apkUrl) {
        Uri uri = Uri.parse(apkUrl);

        DownloadManager.Request request = new DownloadManager
                .Request(uri)
                .setMimeType("application/vnd.android.package-archive")
                .setTitle("App Update")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        uri.getLastPathSegment()
                ).setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
//                .setRequiresCharging(false)// Set if charging is required to begin the download
//                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(false);// Set if download is allowed on roaming network
        request.allowScanningByMediaScanner();

        downloadManager = (DownloadManager) mActivity.getSystemService(DOWNLOAD_SERVICE);
        AppConstant.downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

        updateDownloadProgress();

    }

    private void updateDownloadProgress() {

        new Thread() {
            @Override
            public void run() {
                downloading = true;

                while (downloading) {

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(AppConstant.downloadID);

                    Cursor cursor = downloadManager.query(query);
                    cursor.moveToFirst();

                    double bytesDownloaded = cursor.getDouble(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    double bytesTotal = cursor.getDouble(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final double downloadProgress = bytesTotal > 0 ? (bytesDownloaded * 100 / bytesTotal) : 0;

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTotalPercentage((int) downloadProgress);
                            progressBarDownload.setProgress((int) downloadProgress);
                        }
                    });
                    Log.d(TAG, String.valueOf(downloadProgress));
                    Log.d(TAG, statusMessage(cursor));
                    cursor.close();
                }

            }
        }.start();
    }

    private String statusMessage(Cursor cursor) {
        String msg = "???";

        switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;
            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return msg;
    }

    private void setTotalPercentage(int downloadPercentage) {
        textViewPercentage.setText(String.valueOf(downloadPercentage));
        textViewTotalPercentage.setText(downloadPercentage + "/100");
    }

    private void openDownloadedAttachment(Context context, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String downloadMimeType =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
            }
        }
        cursor.close();
    }

    private void openDownloadedAttachment(Context context, Uri attachmentUri, String attachmentMimeType) {
        Uri mAttachmentUri = attachmentUri;
        if (mAttachmentUri != null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(mAttachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(mAttachmentUri.getPath());
                mAttachmentUri = FileProvider.getUriForFile(context, "com.pinnacle.winwin", file);
            }

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(mAttachmentUri, "application/vnd.android.package-archive");
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Unable to open file", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onDownloadCompleted(Intent intent) {
        LogUtils.e(TAG, "DOWNLOAD COMPLETED APK");
        dismiss();
        openDownloadedAttachment(mActivity, AppConstant.downloadID);
    }
}
