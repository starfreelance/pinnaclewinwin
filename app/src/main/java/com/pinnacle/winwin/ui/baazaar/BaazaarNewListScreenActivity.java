package com.pinnacle.winwin.ui.baazaar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomAppUpdateDialogFragment;
import com.pinnacle.winwin.custom.CustomBonusDialogFragment;
import com.pinnacle.winwin.custom.CustomDownloadDialogFragment;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.data.local.db.SaveInitialDataInDbTask;
import com.pinnacle.winwin.data.local.db.SaveMasterDataInDbTask;
import com.pinnacle.winwin.listener.CustomAppUpdateDialogListener;
import com.pinnacle.winwin.listener.CustomBonusDialogListener;
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
import com.pinnacle.winwin.ui.baazaar.adapter.BaazaarParentAdapter;
import com.pinnacle.winwin.ui.baazaar.adapter.NavigationMenuExpandableAdapter;
import com.pinnacle.winwin.ui.baazaar.listener.BaazaarListener;
import com.pinnacle.winwin.ui.baazaar.listener.NavigationMenuListener;
import com.pinnacle.winwin.ui.baazaar.model.AllGamesModel;
import com.pinnacle.winwin.ui.baazaar.model.NavigationMenuModel;
import com.pinnacle.winwin.ui.baazaarhistory.BaazaarHistoryScreenActivity;
import com.pinnacle.winwin.ui.enquiry.EnquiryScreenActivity;
import com.pinnacle.winwin.ui.gamehistory.GameHistoryScreenActivity;
import com.pinnacle.winwin.ui.headtailgame.HeadTailGameScreenActivity;
import com.pinnacle.winwin.ui.home.HomeScreenActivity;
import com.pinnacle.winwin.ui.kyc.KycScreenActivity;
import com.pinnacle.winwin.ui.personalinfo.PersonalInfoScreenActivity;
import com.pinnacle.winwin.ui.rechargehistory.RechargeHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.fragment.CustomDualButtonDialogFragment;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.ui.walletinfo.WalletInfoScreenActivity;
import com.pinnacle.winwin.ui.withdrawalhistory.WithdrawalHistoryScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.PermissionUtils;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class BaazaarNewListScreenActivity extends ASOnlineBaseActivity implements
        SaveInitialDataInDbTask.SaveInitialDataInDbListener, SaveMasterDataInDbTask.SaveMasterDataListener,
        ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener, CustomDualButtonDialgoListener,
        NavigationMenuListener, CustomSingleButtonDialogListener, View.OnClickListener,
        BaazaarListener, CustomAppUpdateDialogListener, PermissionUtils.SettingListener, CustomBonusDialogListener {

    private static final String TAG = BaazaarNewListScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle,tvUserPoints;
    private AppCompatTextView textViewBaazaarNews;

    private ImageView imgViewLeft;
    private ImageView imgViewRight;

    private RecyclerView recyclerViewBaazaars;
    private BaazaarParentAdapter mAdapter;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private ExpandableListView navigationExpandableListView;
    private NavigationMenuExpandableAdapter expandableAdapter;

    private FrameLayout layoutBaazaarNews;

    private View parentLayout;

    private CustomAppUpdateDialogFragment customAppUpdateDialogFragment;
    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;
    private CustomDownloadDialogFragment customDownloadDialogFragment;
    private CustomBonusDialogFragment customBonusDialogFragment;

    private ArrayList<BazaarDetailsResponse> bazaarDetails;
    private ArrayList<CustomGamesResponse> customGames;
    private ArrayList<AllGamesModel> allGames = new ArrayList<>();

    private List<NavigationMenuModel> navigationMenuList = new ArrayList<>();
    private LinkedHashMap<NavigationMenuModel, List<NavigationMenuModel>> childMenuList = new LinkedHashMap<>();

    private boolean isBackBtnPressedOnce = false;
    private int serverDBVersion = -1;
    private boolean isServerDBVersionUpdated;
    private PermissionUtils permissionUtils;
    private boolean neverAskAgain = false;
    private int bonusAmount;
    private boolean inApproval = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baazaar_new_list_screen);

        permissionUtils = PermissionUtils.sharedInstance();
        initViews();

        callGetInitialDataApi();
        processNavigationMenuList();
        loadExpandableAdapter();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!isBackBtnPressedOnce) {
                isBackBtnPressedOnce = true;
                showAppExitConfirmationDialog();
            } else {
                super.onBackPressed();
            }
        }
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

        tvUserPoints = findViewById(R.id.tvUserPoints);
        tvUserPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        tvUserPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        layoutBaazaarNews = findViewById(R.id.layoutBaazaarNews);

        textViewBaazaarNews = findViewById(R.id.textViewBaazaarNews);
        textViewBaazaarNews.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewBaazaarNews.setSelected(true);

        recyclerViewBaazaars = findViewById(R.id.recyclerViewBaazaars);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBaazaars.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.toolbar);

        imgViewLeft = findViewById(R.id.imgViewLeft);
        imgViewLeft.setVisibility(View.GONE);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationExpandableListView = findViewById(R.id.navigationExpandableListView);
        navigationExpandableListView.setOnGroupClickListener(this);
        navigationExpandableListView.setOnChildClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int integer = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, -1);
        LogUtils.e(TAG,"Dashboard Points "+ integer);
        tvUserPoints.setText("My Points : "+integer);
    }

    private void fetchBaazaarDetailsData() {
        if (ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetails().isEmpty()) {
            callGetMasterDataApi();
        } else {
            fetchBaazaarDetailsFromDb();
            showAppInfoDialog();
        }
    }

    private void fetchBaazaarDetailsFromDb() {
        bazaarDetails = (ArrayList<BazaarDetailsResponse>) ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails();
        //Used to move the first element of bazaar to the last
        if (bazaarDetails.size() > 0) {
            Collections.rotate(bazaarDetails, -1);
            LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails().get(0).getGameMap().size()));
            LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetails().size()));
//        LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.amountDetailsDao().getAmountDetailsWithGameId(1).get(0).getAmountDisplay()));
            LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.paanaDetailsDao().getPaanaDetails().size()));
            LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.cpDetailsDao().getCPDetails().get(0).getPaanaNo()));
            LogUtils.e(TAG, String.valueOf(ASOnlineApplication.appDatabase.motorCombDetailsDao().getMotorCombDetails().get(0).getSpComb()));
        }
        if (!inApproval) {
            fetchCustomGamesFromDb();
            if (customGames != null && !customGames.isEmpty()) {
                AllGamesModel allGamesModel = new AllGamesModel();
                allGamesModel.setGenericId(AppConstant.GENERIC_TYPE_GAMES);
                allGamesModel.setHeaderTitle(getResources().getString(R.string.title_games));
                allGamesModel.setGenericGameList(customGames);

                allGames.add(allGamesModel);
            }
        }
        if (bazaarDetails != null && !bazaarDetails.isEmpty()) {
//            loadAdapter();
            AllGamesModel allGamesModel = new AllGamesModel();
            allGamesModel.setGenericId(AppConstant.GENERIC_TYPE_BAAZAAR);
            allGamesModel.setHeaderTitle(getResources().getString(R.string.title_baazaar));
            allGamesModel.setGenericGameList(bazaarDetails);

            allGames.add(allGamesModel);
        }
        if (allGames != null && !allGames.isEmpty()) {
            loadAdapter();
        }
    }

    private void fetchCustomGamesFromDb() {
        customGames = (ArrayList<CustomGamesResponse>) ASOnlineApplication.appDatabase.customGamesDao().getCustomGames();

        LogUtils.d(TAG, String.valueOf(customGames.size()));
        LogUtils.d(TAG, String.valueOf(ASOnlineApplication.appDatabase.customGameAmountDetailsDao().getCustomGameAmountDetails().size()));
    }

    private void toggleNavigationMenuBtn() {
        if (!inApproval) {
            imgViewLeft.setVisibility(View.VISIBLE);
            imgViewLeft.setOnClickListener(this);
        } else {
            imgViewLeft.setVisibility(View.GONE);
        }
    }

    private void loadAdapter() {
        if (mAdapter == null) {
//            mAdapter = new BaazaarParentAdapter(this, bazaarDetails);
            mAdapter = new BaazaarParentAdapter(this, allGames, inApproval);
            recyclerViewBaazaars.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void processNavigationMenuList() {

        NavigationMenuModel navigationMenuModel = new NavigationMenuModel();
        navigationMenuModel.setTitle(ASOnlinePreferenceManager.getString(this,
                AppConstant.KEY_USER_FIRST_NAME, "") + " " + ASOnlinePreferenceManager.getString(this,
                AppConstant.KEY_USER_LAST_NAME, ""));
        navigationMenuModel.setNavigationIntentType(AppConstant.NAVIGATION_INTENT_PERSONAL_INFO);

        navigationMenuList.add(navigationMenuModel);

        String[] groupTitleArray = getResources().getStringArray(R.array.navigation_menu_group);
        int[] intentTypeArray = {/*AppConstant.NAVIGATION_INTENT_PERSONAL_INFO,*/ AppConstant.NAVIGATION_INTENT_TYPE_RECHARGE,
                AppConstant.NAVIGATION_INTENT_TYPE_WITHDRAWAL, AppConstant.NAVIGATION_INTENT_TYPE_GAME_HISTORY,
                AppConstant.NAVIGATION_INTENT_TYPE_BAAZAR_HISTORY, AppConstant.NAVIGATION_INTENT_TYPE_WALLET_BALANCE,
                AppConstant.NAVIGATION_INTENT_TYPE_ENQUIRY};

        for (int i = 0; i < groupTitleArray.length; i++) {
            navigationMenuModel = new NavigationMenuModel();
            navigationMenuModel.setTitle(groupTitleArray[i]);
            navigationMenuModel.setNavigationIntentType(intentTypeArray[i]);
            if (navigationMenuModel.getNavigationIntentType() == AppConstant.NAVIGATION_INTENT_TYPE_GAME_HISTORY) {
                childMenuList.put(navigationMenuModel, getChildMenuGameHistoryList());
            }
            navigationMenuList.add(navigationMenuModel);
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

    private void loadExpandableAdapter() {
        if (expandableAdapter == null) {
            expandableAdapter = new NavigationMenuExpandableAdapter(this, navigationMenuList, childMenuList);
            navigationExpandableListView.setAdapter(expandableAdapter);
        } else {
            expandableAdapter.notifyDataSetChanged();
        }
    }

    private void navigateToGameHistoryScreen(int gameId) {
        Intent intent = new Intent(this, GameHistoryScreenActivity.class);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        startActivity(intent);
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

    private void navigateToHeadTailGameScreen(int gameId) {
        Intent intent = new Intent(this, HeadTailGameScreenActivity.class);
        intent.putExtra(AppConstant.KEY_GAME_ID, gameId);
        startActivity(intent);
    }

    private void navigateToPlayStore() {
        String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException activityNotFoundException) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void navigateToKycScreen() {
        Intent intent = new Intent(this, KycScreenActivity.class);
        startActivity(intent);
    }

    private void  navigateToPersonalInfoScreen() {
        Intent intent = new Intent(this, PersonalInfoScreenActivity.class);
        startActivity(intent);
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

    private void callGetMasterDataApi() {
        callAppServer(AppConstant.REQ_API_TYPE_GET_MASTER_DATA, null, true);
    }

    private void callSaveInitialDataInDbTask(MatkaInitialData matkaInitialData) {
        SaveInitialDataInDbTask saveInitialDataInDbTask = new
                SaveInitialDataInDbTask(matkaInitialData, this);
        saveInitialDataInDbTask.execute();
    }

    private void saveUserData(UserData userData) {
        if (userData != null) {
            bonusAmount = userData.getBonus();
            inApproval = userData.getMobileNo() != null && userData.getMobileNo().equals("9111111111");
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_FIRST_NAME, userData.getFirstName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_LAST_NAME, userData.getLastName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_MOBILE_NO, userData.getMobileNo());
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, userData.getPoints());
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_CUST_ID, userData.getCustId());
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_ADMIN_ID, userData.getAdminId());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ADMIN_NAME, userData.getAdminName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ADMIN_MOBILE_NO, userData.getAdminMobileNo());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_PROFILE_IMAGE, userData.getProfileImage());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ACCOUNT_NUMBER, userData.getAccountNumber());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_IFSC_CODE, userData.getIfscCode());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_BANK_NAME, userData.getBankName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ACCOUNT_NAME, userData.getAccountName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ACCOUNT_TYPE, userData.getAccountType());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_BRANCH_NAME, userData.getBranchName());
            ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_USER_IS_KYC_COMPLETED, userData.isKycCompleted());
        }
    }

    private void saveMinMaxRechargeData(String minRecharge, String maxRecharge) {
        if (minRecharge != null &&
                !minRecharge.isEmpty()) {
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_MIN_RECHARGE, Integer.parseInt(minRecharge));
        }
        if (maxRecharge != null &&
                !maxRecharge.isEmpty()) {
            ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_MAX_RECHARGE, Integer.parseInt(maxRecharge));
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

    private void showKycInfoDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.kyc_info_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_KYC_INFO);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    private void showBonusDialog() {
        dismissBonusDialog();
        customBonusDialogFragment = CustomBonusDialogFragment.newInstance(String.valueOf(bonusAmount));
        customBonusDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_BONUS);
    }

    private void dismissBonusDialog() {
        if (!isFinishing() && customBonusDialogFragment != null &&
                customBonusDialogFragment.isVisible()) {
            customBonusDialogFragment.dismissAllowingStateLoss();
            customBonusDialogFragment = null;
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

    /**
     *
     * @param response
     */
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
                    saveMinMaxRechargeData(matkaInitialData.getMinRecharge(), matkaInitialData.getMaxRecharge());
//                    inApproval = matkaInitialData.isInApproval();
                    toggleNavigationMenuBtn();
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

    /**
     *
     * @param response
     */
    @Override
    protected void onFailure(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.drawerLayout),
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.drawerLayout),
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, drawerLayout,
                    getResources().getString(R.string.something_went_wrong_error),
                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        }
    }

    /**
     *
     */
    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, drawerLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        fetchBaazaarDetailsFromDb();
    }

    /**
     * SaveInitialDataInDbListener
     */
    @Override
    public void onInitialDataSavedSuccessfully() {
        fetchBaazaarDetailsData();
    }

    @Override
    public void onInitialDataSaveFailed() {

    }

    /**
     * SaveMasterDataListener
     */
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

    /**
     * ExpandableListView.OnGroupClickListener
     * @param parent
     * @param view
     * @param groupPosition
     * @param id
     * @return
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
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
                intent = new Intent(this, BaazaarHistoryScreenActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case AppConstant.NAVIGATION_INTENT_TYPE_ENQUIRY:
                intent = new Intent(this, EnquiryScreenActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case AppConstant.NAVIGATION_INTENT_TYPE_RECHARGE:
                intent = new Intent(this, RechargeHistoryScreenActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case AppConstant.NAVIGATION_INTENT_TYPE_WITHDRAWAL:
                if (ASOnlinePreferenceManager.getBoolean(this, AppConstant.KEY_USER_IS_KYC_COMPLETED, false)) {
                    intent = new Intent(this, WithdrawalHistoryScreenActivity.class);
                    startActivity(intent);
                } else {
                    showKycInfoDialog();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        return false;
    }

    /**
     * ExpandableListView.OnChildClickListener
     * @param parent
     * @param view
     * @param groupPosition
     * @param childPosition
     * @param id
     * @return
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
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

    @Override
    public void onNavigationMenuClick(int intentType) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (intentType == AppConstant.NAVIGATION_INTENT_PERSONAL_INFO) {
            navigateToPersonalInfoScreen();
        } else {
            showLogOutConfirmationDialog();
        }
    }

    /**
     * CustomAppUpdateDialogListener
     */
    @Override
    public void onClickOkay() {
        dismissAppUpdateInfoDialog();
        if (BuildConfig.IS_PLAYSTORE_BUILD) {
            navigateToPlayStore();
        } else {
            checkWriteStoragePermission();
        }
    }

    /**
     * PermissionUtils.SettingListener
     */
    @Override
    public void openSettingActivity() {
        navigateToAppSettingsScreen();
    }

    /**
     * CustomDualButtonDialgoListener
     */
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

    /**
     * CustomSingleButtonDialogListener
     */
    @Override
    public void onClickSingleButtonListener() {
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_APP_INFO)) {
                    dismissCustomSingleButtonDialog();
                    if (bonusAmount > 0) {
                        showBonusDialog();
                    }
                } else if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_KYC_INFO)) {
                    dismissCustomSingleButtonDialog();
                    navigateToKycScreen();
                }
            }
        } else {
            super.onClickSingleButtonListener();
        }
    }

    /**
     * CustomBonusDialogListener
     */
    @Override
    public void onClickPositiveButton() {
        if (customBonusDialogFragment != null) {
            if (customBonusDialogFragment.getTag() != null) {
                if (customBonusDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_BONUS)) {
                    dismissBonusDialog();
                }
            }
        }
    }

    /**
     * BaazaarListener
     * @param bazaarDetail
     */
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
        navigateToHeadTailGameScreen(customGame.getGameId());
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewLeft:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }
}