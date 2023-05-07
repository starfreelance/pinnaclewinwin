package com.pinnacle.winwin.ui.baazaar;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomAppUpdateDialogFragment;
import com.pinnacle.winwin.custom.CustomDownloadDialogFragment;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.custom.SpaceItemDecoration;
import com.pinnacle.winwin.data.local.db.SaveInitialDataInDbTask;
import com.pinnacle.winwin.data.local.db.SaveMasterDataInDbTask;
import com.pinnacle.winwin.listener.CustomAppUpdateDialogListener;
import com.pinnacle.winwin.listener.CustomSingleButtonDialogListener;
import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.network.model.CustomGamesResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.MasterDataResponse;
import com.pinnacle.winwin.network.model.MatkaInitialData;
import com.pinnacle.winwin.network.model.MatkaInitialRequest;
import com.pinnacle.winwin.network.model.MatkaInitialResponse;
import com.pinnacle.winwin.network.model.MatkaMasterResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.network.model.VersionResponse;
import com.pinnacle.winwin.ui.baazaar.adapter.BaazaarAdapter;
import com.pinnacle.winwin.ui.baazaar.adapter.NavigationMenuExpandableAdapter;
import com.pinnacle.winwin.ui.baazaar.listener.BaazaarListener;
import com.pinnacle.winwin.ui.baazaar.listener.NavigationMenuListener;
import com.pinnacle.winwin.ui.baazaar.model.BaazaarModel;
import com.pinnacle.winwin.ui.baazaar.model.NavigationMenuModel;
import com.pinnacle.winwin.ui.baazaar.receiver.ASOnlineDownloadManagerReceiver;
import com.pinnacle.winwin.ui.baazaarhistory.BaazaarHistoryScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.home.HomeScreenActivity;
import com.pinnacle.winwin.ui.personalinfo.PersonalInfoScreenActivity;
import com.pinnacle.winwin.ui.singlegame.fragment.CustomDualButtonDialogFragment;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.walletinfo.WalletInfoScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.PermissionUtils;
import com.pinnacle.winwin.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class BaazaarGridScreenActivity extends ASOnlineBaseActivity implements
        BaazaarListener, SaveMasterDataInDbTask.SaveMasterDataListener, View.OnClickListener,
        SaveInitialDataInDbTask.SaveInitialDataInDbListener, ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener, CustomDualButtonDialgoListener,
        NavigationMenuListener, PermissionUtils.SettingListener,
        CustomAppUpdateDialogListener, CustomSingleButtonDialogListener {

    private static final String TAG = BaazaarGridScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewBaazaarNews;

    private ImageView imgViewLeft;
    private ImageView imgViewRight;

    private RecyclerView recyclerViewBaazaars;
    private BaazaarAdapter mAdapter;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private ExpandableListView navigationExpandableListView;
    private NavigationMenuExpandableAdapter expandableAdapter;

    private FrameLayout layoutBaazaarNews;

    private View parentLayout;

    /*private CustomDualButtonDialogFragment mCustomDualButtonDialogFragment;*/
    private CustomDownloadDialogFragment customDownloadDialogFragment;
    private CustomAppUpdateDialogFragment customAppUpdateDialogFragment;
    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private ArrayList<BaazaarModel> baazaarList;
    private ArrayList<BazaarDetailsResponse> bazaarDetails;

    private List<NavigationMenuModel> navigationMenuList = new ArrayList<>();
    private LinkedHashMap<NavigationMenuModel, List<NavigationMenuModel>> childMenuList = new LinkedHashMap<>();

    private boolean isBackBtnPressedOnce = false;
    private int serverDBVersion = -1;
    private boolean isServerDBVersionUpdated;

    /*private MatkaMasterViewModel matkaMasterViewModel;*/

    private ASOnlineDownloadManagerReceiver downloadManagerReceiver;

    private PermissionUtils permissionUtils;
    private boolean neverAskAgain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baazaar_grid_screen);

        permissionUtils = PermissionUtils.sharedInstance();
        initViews();
        /*baazaarList = getBaazaarList();
        if (baazaarList != null && !baazaarList.isEmpty()) {
            loadAdapter();
        }*/

        /*fetchBaazaarDetailsData();*/
        callGetInitialDataApi();
        processNavigationMenuList();
        loadExpandableAdapter();
        /*downloadManagerReceiver = new ASOnlineDownloadManagerReceiver();
        registerReceiver(downloadManagerReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/
        // Get a new or existing ViewModel from the ViewModelProvider.
        /*matkaMasterViewModel = ViewModelProviders.of(this).get(MatkaMasterViewModel.class);

        matkaMasterViewModel.getBazaarDetails().observe(this, new Observer<List<BazaarDetailsResponse>>() {
            @Override
            public void onChanged(@Nullable List<BazaarDetailsResponse> bazaarDetailsResponses) {
                if (bazaarDetailsResponses != null && !bazaarDetailsResponses.isEmpty()) {
                    LogUtils.e(TAG, String.valueOf(bazaarDetailsResponses.get(0).getBazaarName()));
                }
            }
        });*/
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
    public void onBackPressed() {

        if (!isBackBtnPressedOnce) {
            isBackBtnPressedOnce = true;
            showAppExitConfirmationDialog();
        } else {
            super.onBackPressed();
        }
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
        /*unregisterReceiver(downloadManagerReceiver);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionUtils.REQUEST_WRITE_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDownloadAppUpdateDialog();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissionUtils.requestWriteStoragePermission(false, this,
                                parentLayout, this);
                    } else {
                        neverAskAgain = true;
                        permissionUtils.requestWriteStoragePermission(neverAskAgain, this, parentLayout, this);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.REQ_CODE_APP_SETTINGS) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                showDownloadAppUpdateDialog();
            }
        }
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_home));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        layoutBaazaarNews = findViewById(R.id.layoutBaazaarNews);

        textViewBaazaarNews = findViewById(R.id.textViewBaazaarNews);
        textViewBaazaarNews.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewBaazaarNews.setSelected(true);

        recyclerViewBaazaars = findViewById(R.id.recyclerViewBaazaars);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewBaazaars.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.medium_margin);
        recyclerViewBaazaars.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        toolbar = findViewById(R.id.toolbar);

        imgViewLeft = findViewById(R.id.imgViewLeft);
        imgViewLeft.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationExpandableListView = findViewById(R.id.navigationExpandableListView);
        navigationExpandableListView.setOnGroupClickListener(this);
        navigationExpandableListView.setOnChildClickListener(this);

    }

    private void fetchBaazaarDetailsData() {
        if (/*ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails().isEmpty()*/
                ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetails().isEmpty()) {
            callGetMasterDataApi();
        } else {
            fetchBaazaarDetailsFromDb();
            showAppInfoDialog();

        }
    }

    private void fetchBaazaarDetailsFromDb() {
        bazaarDetails = (ArrayList<BazaarDetailsResponse>) ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails();
        //Used to move the first element of bazaar to the last
        Collections.rotate(bazaarDetails, -1);
        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails().get(0).getGameMap().size()));
        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetails().size()));
//        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetailsWithGameId(1).get(0).getAmountDisplay()));
        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetails().size()));
        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.cpDetailsDao().getCPDetails().get(0).getPaanaNo()));
        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.motorCombDetailsDao().getMotorCombDetails().get(0).getSpComb()));
        if (bazaarDetails != null && !bazaarDetails.isEmpty()) {
            loadAdapter();
        }
    }

    private ArrayList<BaazaarModel> getBaazaarList() {

        ArrayList<BaazaarModel> baazaarList = new ArrayList<>();

        String[] baazaarNameArray = getResources().getStringArray(R.array.baazaar_name_array);
        String[] baazaarTimeArray = getResources().getStringArray(R.array.baazaar_time_array);
        String[] baazaarPastResultArray = getResources().getStringArray(R.array.baazaar_past_result_array);
        int[] baazaarTypeArray = {AppConstant.BAAZAAR_TYPE_KALYAN_OPEN, AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE, AppConstant.BAAZAAR_TYPE_MAIN_OPEN,
                AppConstant.BAAZAAR_TYPE_MAIN_CLOSE, AppConstant.BAAZAAR_TYPE_KAATA_CHAAPA};
        int[] imgResourceArray = {R.drawable.ic_dice, R.drawable.ic_poker_chip, R.drawable.ic_cards, R.drawable.ic_chip, R.drawable.ic_chip};

        for (int i = 0; i < baazaarNameArray.length; i++) {
            BaazaarModel baazaarModel = new BaazaarModel();
            baazaarModel.setBaazaarName(baazaarNameArray[i]);
            baazaarModel.setBaazaarTime(baazaarTimeArray[i]);
            baazaarModel.setBaazaarPastResult(baazaarPastResultArray[i]);
            baazaarModel.setBaazaarType(baazaarTypeArray[i]);
            baazaarModel.setImgResource(imgResourceArray[i]);

            baazaarList.add(baazaarModel);
        }

        return baazaarList;
    }

    private void loadAdapter() {
        if (mAdapter == null) {
            mAdapter = new BaazaarAdapter(this, bazaarDetails);
            recyclerViewBaazaars.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void navigateToHomeScreen(int baazaarId, String finalNumber, ArrayList<Integer> gameMap, String lastResult) {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        /*Intent intent = new Intent(this, BaazaarHistoryScreenActivity.class);*/
        intent.putExtra(AppConstant.KEY_BAAZAAR_ID, baazaarId);
        intent.putExtra(AppConstant.KEY_FINAL_NUMBER, finalNumber);
        intent.putIntegerArrayListExtra(AppConstant.KEY_GAME_MAP, gameMap);
        intent.putExtra(AppConstant.KEY_LAST_RESULT, lastResult);
        startActivity(intent);
    }

    /*private void saveUserData(UserData userData) {
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_FIRST_NAME, userData.getFirstName());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_LAST_NAME, userData.getLastName());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_MOBILE_NO, userData.getMobileNo());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, userData.getPoints());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_CUST_ID, userData.getCustId());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_TOKEN, userData.getUserToken());
    }*/

    private void processNavigationMenuList() {

        NavigationMenuModel navigationMenuModel = new NavigationMenuModel();
        navigationMenuModel.setTitle(ASOnlinePreferenceManager.getString(this,
                AppConstant.KEY_USER_FIRST_NAME, "") + " " + ASOnlinePreferenceManager.getString(this,
                AppConstant.KEY_USER_LAST_NAME, ""));

        navigationMenuList.add(navigationMenuModel);

        String[] groupTitleArray = getResources().getStringArray(R.array.navigation_menu_group);
        int[] intentTypeArray = {AppConstant.NAVIGATION_INTENT_PERSONAL_INFO, AppConstant.NAVIGATION_INTENT_TYPE_GAME_HISTORY,
                AppConstant.NAVIGATION_INTENT_TYPE_BAAZAR_HISTORY, AppConstant.NAVIGATION_INTENT_TYPE_WALLET_BALANCE};

        for (int i = 0; i < groupTitleArray.length; i++) {
            navigationMenuModel = new NavigationMenuModel();
            navigationMenuModel.setTitle(groupTitleArray[i]);
            navigationMenuModel.setNavigationIntentType(intentTypeArray[i]);
            if (i == 1) {
                childMenuList.put(navigationMenuModel, getChildMenuGameHistoryList());
            }
            navigationMenuList.add(navigationMenuModel);
        }
    }

    private void loadExpandableAdapter() {
        if (expandableAdapter == null) {
            expandableAdapter = new NavigationMenuExpandableAdapter(this, navigationMenuList, childMenuList);
            navigationExpandableListView.setAdapter(expandableAdapter);
        } else {
            expandableAdapter.notifyDataSetChanged();
        }
    }

    private List<NavigationMenuModel> getChildMenuGameHistoryList() {

        List<NavigationMenuModel> childMenuList = new ArrayList<>();

        String[] childGameHistoryArray = getResources().getStringArray(R.array.child_menu_game_history);
        int[] intentTypeArray = {AppConstant.NAVIGATION_INTENT_TYPE_ALL_GAMES_HISTORY, AppConstant.NAVIGATION_INTENT_TYPE_SINGLE_GAME_HISTORY, AppConstant.NAVIGATION_INTENT_TYPE_PAANA_GAME_HISTORY,
                AppConstant.NAVIGATION_INTENT_TYPE_CP_GAME_HISTORY, AppConstant.NAVIGATION_INTENT_TYPE_MOTOR_GAME_HISTORY, AppConstant.NAVIGATION_INTENT_TYPE_BRACKET_GAME_HISTORY};

        for (int i = 0; i < childGameHistoryArray.length; i++) {
            NavigationMenuModel navigationMenuModel = new NavigationMenuModel();
            navigationMenuModel.setTitle(childGameHistoryArray[i]);
            navigationMenuModel.setNavigationIntentType(intentTypeArray[i]);

            childMenuList.add(navigationMenuModel);
        }

        return childMenuList;
    }

    private void navigateToGameHistoryScreen(int gameId) {
        Intent intent = new Intent(this, GameHistoryScreenActivity.class);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        startActivity(intent);
    }

    private void callGetMasterDataApi() {
        callAppServer(AppConstant.REQ_API_TYPE_GET_MASTER_DATA, null, true);
    }

    private void callGetInitialDataApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_MATKA_INITIAL_REQUEST, getMatkaInitialRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_INITIAL_DATA, intent, true);
    }

    private MatkaInitialRequest getMatkaInitialRequest() {
        MatkaInitialRequest matkaInitialRequest = new MatkaInitialRequest();
        matkaInitialRequest.setCustomerId(ASOnlinePreferenceManager.
                getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));

        /*Gson gson = new Gson();
        String jsonString = gson.toJson(matkaInitialRequest);
        *//*return RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*//*
        return getEncryptedString(jsonString);*/
        return matkaInitialRequest;
    }

    private void callSaveInitialDataInDbTask(MatkaInitialData matkaInitialData) {
        SaveInitialDataInDbTask saveInitialDataInDbTask = new
                SaveInitialDataInDbTask(matkaInitialData, this);
        saveInitialDataInDbTask.execute();
    }

    private void saveUserData(UserData userData) {
        if (userData != null) {
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_FIRST_NAME, userData.getFirstName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_LAST_NAME, userData.getLastName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_MOBILE_NO, userData.getMobileNo());
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, userData.getPoints());
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_CUST_ID, userData.getCustId());
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_ADMIN_ID, userData.getAdminId());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ADMIN_NAME, userData.getAdminName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ADMIN_MOBILE_NO, userData.getAdminMobileNo());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_PROFILE_IMAGE, userData.getProfileImage());
        }
    }

    private void checkWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestWriteStoragePermission(neverAskAgain, this,
                        parentLayout, this);
            } else {
                showDownloadAppUpdateDialog();
            }

        } else {
            showDownloadAppUpdateDialog();
        }
    }

    private void showAppExitConfirmationDialog() {
        dismissCustomDualButtonDialog();
        customDualButtonDialogFragment = CustomDualButtonDialogFragment.newInstance(
                getResources().getString(R.string.app_exit_confirmaton));
        customDualButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_APP_EXIT);
    }

    private void showLogOutConfirmationDialog() {
        dismissCustomDualButtonDialog();
        customDualButtonDialogFragment = CustomDualButtonDialogFragment.newInstance(
                getResources().getString(R.string.log_out_confirmaton));
        customDualButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_LOG_OUT);
    }

    private void showDownloadAppUpdateDialog() {
        dismissDownloadAppUpdateDialog();
        customDownloadDialogFragment = CustomDownloadDialogFragment.newInstance();
        customDownloadDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_DOWNLOAD_APP_UPDATE);
    }

    private void dismissDownloadAppUpdateDialog() {
        if (!isFinishing() && customDownloadDialogFragment != null &&
                customDownloadDialogFragment.isVisible()) {
            customDownloadDialogFragment.dismissAllowingStateLoss();
            customDownloadDialogFragment = null;
        }
    }

    private void showAppUpdateInfoDialog() {
        dismissAppUpdateInfoDialog();
        customAppUpdateDialogFragment = CustomAppUpdateDialogFragment.newInstance();
        customAppUpdateDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_APP_UPDATE_INFO);
    }

    private void dismissAppUpdateInfoDialog() {
        if (!isFinishing() && customAppUpdateDialogFragment != null &&
                customAppUpdateDialogFragment.isVisible()) {
            customAppUpdateDialogFragment.dismissAllowingStateLoss();
            customAppUpdateDialogFragment = null;
        }
    }

    private void showAppInfoDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.app_info_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_APP_INFO);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    private void clearDatabase() {
        ASOnlineApplication.appDatabase.clearAllTables();
    }

    private void loadBaazaarNews(String notes) {
        if (notes != null && !notes.isEmpty()) {
            textViewBaazaarNews.setText("");
            textViewBaazaarNews.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_news),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewBaazaarNews.append(Utils.getCustomSpannableString(this, " " + notes,
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            layoutBaazaarNews.setVisibility(View.VISIBLE);
            Animation marquee = AnimationUtils.loadAnimation(this, R.anim.marquee);
            textViewBaazaarNews.setAnimation(marquee);
        } else {
            layoutBaazaarNews.setVisibility(View.GONE);
        }
    }

    /*Download Manager Test Code*/
    private void downloadTestData() {

        File file = new File(getExternalFilesDir(null), "Dummy");

        DownloadManager.Request request = new DownloadManager
//                .Request(Uri.parse("http://speedtest.ftp.otenet.gr/files/test10Mb.db"))
                .Request(Uri.parse("https://sit-matka.herokuapp.com/uploads/cust1.png"))
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
//                .setRequiresCharging(false)// Set if charging is required to begin the download
//                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        AppConstant.downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }


    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof MatkaMasterResponse) {
            MatkaMasterResponse matkaMasterResponse = (MatkaMasterResponse) response;
            if (matkaMasterResponse.getMasterDataResponse() != null) {
                MasterDataResponse masterDataResponse = matkaMasterResponse.getMasterDataResponse();
                if (masterDataResponse != null) {
                    if (masterDataResponse.getBazaarDetails() != null &&
                            !masterDataResponse.getBazaarDetails().isEmpty()) {
                        SaveMasterDataInDbTask saveMasterDataInDbTask = new
                                SaveMasterDataInDbTask(masterDataResponse, this);
                        saveMasterDataInDbTask.execute();
                    }
                }
            }
        } else if (response instanceof MatkaInitialResponse) {
            MatkaInitialResponse matkaInitialResponse = (MatkaInitialResponse) response;
            if (matkaInitialResponse.getMatkaInitialData() != null) {
                MatkaInitialData matkaInitialData = matkaInitialResponse.getMatkaInitialData();
                if (matkaInitialData != null) {
                    /*LogUtils.e(TAG, matkaInitialData.getVersionResponse().getDbVersion());*/
                    saveUserData(matkaInitialData.getUserData());
                    loadBaazaarNews(matkaInitialData.getNotes());
                    VersionResponse versionResponse = matkaInitialData.getVersionResponse();
                    /*AppVersionResponse appVersionResponse = matkaInitialData.getAppVersionResponse();*/
                    int appVersion = 0;
                    if (matkaInitialData.getAppVersion() != null &&
                            !matkaInitialData.getAppVersion().isEmpty()) {
                        appVersion = Integer.parseInt(matkaInitialData.getAppVersion());
                    }
                    if (appVersion > Utils.getVersionCode(this)) {
                        showAppUpdateInfoDialog();
                    } else {
                        if (ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_SERVER_DB_VERSION, -1) == -1) {
                            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_SERVER_DB_VERSION, versionResponse.getDbVersionId());
                            LogUtils.e(TAG, "FIRST TIME " + ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_SERVER_DB_VERSION, -1));
                            callSaveInitialDataInDbTask(matkaInitialData);
                        } else if (ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_SERVER_DB_VERSION, -1) !=
                                versionResponse.getDbVersionId()) {
                            serverDBVersion = versionResponse.getDbVersionId();
                            isServerDBVersionUpdated = true;
                            LogUtils.e(TAG, "VERSION UPDATED " + serverDBVersion);
                            clearDatabase();
                            callGetMasterDataApi();
                        } else {
                            LogUtils.e(TAG, "ELSE CONDITION " + ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_SERVER_DB_VERSION, -1));
                            callSaveInitialDataInDbTask(matkaInitialData);
                        }
                    }
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
        fetchBaazaarDetailsFromDb();

    }

    //Event Handlers
    //SaveMasterDataListener
    @Override
    public void onMasterDataSavedSuccessfully() {
        if (isServerDBVersionUpdated) {
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_SERVER_DB_VERSION, serverDBVersion);
            isServerDBVersionUpdated = false;
        }
        fetchBaazaarDetailsFromDb();
        showAppInfoDialog();
    }

    @Override
    public void onMasterDataSaveFailed() {

    }

    //SaveInitialDataInDbListener
    @Override
    public void onInitialDataSavedSuccessfully() {
        fetchBaazaarDetailsData();
    }

    @Override
    public void onInitialDataSaveFailed() {

    }

    //BaazaarListener
    @Override
    public void onBaazaarSelectListener(BazaarDetailsResponse bazaarDetail) {
        if (bazaarDetail != null) {
            switch (bazaarDetail.getBazaarId()) {
                case AppConstant.BAAZAAR_TYPE_KALYAN_OPEN:
                case AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE:
                case AppConstant.BAAZAAR_TYPE_MAIN_OPEN:
                case AppConstant.BAAZAAR_TYPE_MAIN_CLOSE:
                case AppConstant.BAAZAAR_TYPE_TIME_OPEN:
                case AppConstant.BAAZAAR_TYPE_TIME_CLOSE:
                case AppConstant.BAAZAAR_TYPE_MILAN_DAY_OPEN:
                case AppConstant.BAAZAAR_TYPE_MILAN_DAY_CLOSE:
                case AppConstant.BAAZAAR_TYPE_MILAN_NIGHT_OPEN:
                case AppConstant.BAAZAAR_TYPE_MILAN_NIGHT_CLOSE:
                    navigateToHomeScreen(bazaarDetail.getBazaarId(), bazaarDetail.getFinalNumber(),
                            (ArrayList<Integer>) bazaarDetail.getGameMap(), bazaarDetail.getLastResult());
                    break;
            }
        }
    }

    @Override
    public void onCustomGameSelectListener(CustomGamesResponse customGame) {

    }

    //ExpandableListView.OnGroupClickListener
    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {

        Intent intent;
        switch (navigationMenuList.get(groupPosition).getNavigationIntentType()) {
            case AppConstant.NAVIGATION_INTENT_PERSONAL_INFO:
                intent = new Intent(this, PersonalInfoScreenActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case AppConstant.NAVIGATION_INTENT_TYPE_WALLET_BALANCE:
                intent = new Intent(this, WalletInfoScreenActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case AppConstant.NAVIGATION_INTENT_TYPE_BAAZAR_HISTORY:
                /*intent = new Intent(this, DemoHistoryActivity.class);*/
                intent = new Intent(this, BaazaarHistoryScreenActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        return false;
    }

    //ExpandableListView.OnChildClickListener
    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

        List<NavigationMenuModel> mChildMenuList = childMenuList.get(navigationMenuList.get(groupPosition));

        if (mChildMenuList != null && !mChildMenuList.isEmpty()) {
            NavigationMenuModel navigationMenuModel = mChildMenuList.get(childPosition);
            switch (navigationMenuModel.getNavigationIntentType()) {
                case AppConstant.NAVIGATION_INTENT_TYPE_SINGLE_GAME_HISTORY:
                    navigateToGameHistoryScreen(AppConstant.GAME_TYPE_SINGLE);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case AppConstant.NAVIGATION_INTENT_TYPE_PAANA_GAME_HISTORY:
                    navigateToGameHistoryScreen(AppConstant.GAME_TYPE_PAANA);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case AppConstant.NAVIGATION_INTENT_TYPE_CP_GAME_HISTORY:
                    navigateToGameHistoryScreen(AppConstant.GAME_TYPE_CP);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case AppConstant.NAVIGATION_INTENT_TYPE_MOTOR_GAME_HISTORY:
                    navigateToGameHistoryScreen(AppConstant.GAME_TYPE_MOTOR);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case AppConstant.NAVIGATION_INTENT_TYPE_BRACKET_GAME_HISTORY:
                    navigateToGameHistoryScreen(AppConstant.GAME_TYPE_BRACKET);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case AppConstant.NAVIGATION_INTENT_TYPE_ALL_GAMES_HISTORY:
                    navigateToGameHistoryScreen(AppConstant.GAME_TYPE_ALL);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
        }

        return false;
    }

    //CustomDualButtonDialgoListener
    @Override
    public void onClickYesButtonListener() {
        if (customDualButtonDialogFragment != null) {
            if (customDualButtonDialogFragment.getTag() != null) {
                if (customDualButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_APP_EXIT)) {
                    dismissCustomDualButtonDialog();
                    finish();
                } else if (customDualButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_LOG_OUT)) {
                    dismissCustomDualButtonDialog();
                    clearAppData();
                    navigateToLoginScreen();
                }
            }
        }
    }

    @Override
    public void onClickNoButtonListener() {
        if (customDualButtonDialogFragment != null) {
            if (customDualButtonDialogFragment.getTag() != null) {
                if (customDualButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_APP_EXIT)) {
                    dismissCustomDualButtonDialog();
                    isBackBtnPressedOnce = false;
                } else if (customDualButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_LOG_OUT)) {
                    dismissCustomDualButtonDialog();
                }
            }
        }
    }

    @Override
    public void onNavigationMenuClick(int intentType) {
        drawerLayout.closeDrawer(GravityCompat.START);
        showLogOutConfirmationDialog();
//        downloadTestData();
//        String encryptedString = RSAUtility.encrypt("Hi", RSAUtility.publicKey);
//        LogUtils.e(TAG, encryptedString);
//        LogUtils.e(TAG, RSAUtility.decrypt(encryptedString, RSAUtility.privateKey));
    }

    //PermissionUtils.SettingListener
    @Override
    public void openSettingActivity() {
        navigateToAppSettingsScreen();
    }

    //CustomAppUpdateDialogListener
    @Override
    public void onClickOkay() {
        dismissAppUpdateInfoDialog();
        checkWriteStoragePermission();
    }

    //CustomSingleButtonDialogListener
    @Override
    public void onClickSingleButtonListener() {
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_APP_INFO)) {
                    dismissCustomSingleButtonDialog();
                }
            }
        } else {
            super.onClickSingleButtonListener();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewLeft:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }
}
