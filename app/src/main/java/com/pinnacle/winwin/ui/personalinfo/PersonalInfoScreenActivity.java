package com.pinnacle.winwin.ui.personalinfo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomCircleImageView;
import com.pinnacle.winwin.custom.CustomEditDialogFragment;
import com.pinnacle.winwin.custom.CustomListDialogFragment;
import com.pinnacle.winwin.fileutils.FileOperation;
import com.pinnacle.winwin.listener.CustomEditDialogListener;
import com.pinnacle.winwin.listener.CustomListDialogListener;
import com.pinnacle.winwin.network.model.CustomerDetailsUpdateRequest;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.UpdateProfileImageData;
import com.pinnacle.winwin.network.model.UpdateProfileImageRequest;
import com.pinnacle.winwin.network.model.UpdateProfileImageResponse;
import com.pinnacle.winwin.ui.changepassword.ChangePasswordScreenActivity;
import com.pinnacle.winwin.ui.kyc.KycScreenActivity;
import com.pinnacle.winwin.ui.personalinfo.model.SelectImageOption;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.PermissionUtils;
import com.pinnacle.winwin.utils.Utils;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class PersonalInfoScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, CustomEditDialogListener, CustomListDialogListener, PermissionUtils.SettingListener {

    private static final String TAG = PersonalInfoScreenActivity.class.getSimpleName();
    private static final int REQ_IMAGE_CAMERA = 1;
    private static final int REQ_IMAGE_GALLERY = 2;

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewMobileNo;
    private AppCompatTextView textViewPoints;
    private AppCompatTextView lblName;
    private AppCompatTextView textViewName;
    private AppCompatTextView lblProfilePhoto;
    private AppCompatTextView lblPassword;
    private AppCompatTextView lblKyc;
    private AppCompatTextView textViewChangeKyc;
    private AppCompatTextView textViewChangePhoto;
    private AppCompatTextView textViewChangeName;
    private AppCompatTextView textViewChangePassword;

    private ConstraintLayout layoutName;
    private ConstraintLayout layoutProfilePhoto;
    private ConstraintLayout layoutPassword;
    private ConstraintLayout layoutKyc;

    /*private ImageView imgViewProfile;*/
    private CustomCircleImageView imgViewProfile;

    private View parentLayout;

    private CustomEditDialogFragment customEditDialogFragment;
    private CustomListDialogFragment customListDialogFragment;

    private ArrayList<SelectImageOption> selectImageOptions;

    private PermissionUtils permissionUtils;
    private boolean neverAskAgain = false;
    private boolean neverAskCameraAgain = false;
    private Uri imageUri;
    private FileOperation fileOperation;
    private Bitmap mBitmap;
    private String encodedImageString = "";
    private CustomerDetailsUpdateRequest customerDetailsUpdateRequest;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_screen_new);

        permissionUtils = PermissionUtils.sharedInstance();
        fileOperation = new FileOperation(this);
        mobileNumber = ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_MOBILE_NO, "");
        initViews();
        updateProfileName();
        updateProfilePic();
        selectImageOptions = getSelectImageOptions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionUtils.REQUEST_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        permissionUtils.requestStoragePermission(false, this,
                                parentLayout, this);
                    } else {
                        neverAskAgain = true;
                        permissionUtils.requestStoragePermission(neverAskAgain, this, parentLayout, this);
                    }
                }
                break;
            case PermissionUtils.REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            permissionUtils.requestCameraPermission(false, this,
                                    parentLayout, this);
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            permissionUtils.requestStoragePermission(false, this,
                                    parentLayout, this);
                        } else {
                            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                                neverAskCameraAgain = true;
                                permissionUtils.requestCameraPermission(neverAskCameraAgain, this,
                                        parentLayout, this);
                            } else if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                                neverAskAgain = true;
                                permissionUtils.requestStoragePermission(neverAskAgain, this, parentLayout, this);
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_IMAGE_CAMERA && resultCode == RESULT_OK) {

            if (imageUri != null) {

                String imageFilePath = Utils.getRealPathFromURI(imageUri);

                if (imageFilePath != null) {
                    File file = new File(imageFilePath);
                    String fileName = file.getName();
                    try {
                        mBitmap = fileOperation.getCompressedBitmap(imageFilePath);
                        processBitmap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtils.e("FILE NAME", fileName);
                }
            }

        } else if (requestCode == REQ_IMAGE_GALLERY && resultCode == RESULT_OK) {

            // Do work with photo saved at fullPhotoUri
            Uri fullPhotoUri = null;
            String imageFilePath = null;
            if (data != null) {
                fullPhotoUri = data.getData();
                imageFilePath = fileOperation.getPath(fullPhotoUri);
            }


            if (imageFilePath != null && !imageFilePath.isEmpty()) {
                File file = new File(imageFilePath);
                String fileName = file.getName();

                try {
                    mBitmap = fileOperation.getCompressedBitmap(imageFilePath);
                    processBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.e("FILE NAME", fileName);
            }


        } else if (requestCode == AppConstant.REQ_CODE_APP_SETTINGS) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    //Local Methods
    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        imgViewProfile = findViewById(R.id.imgViewProfile);

        textViewMobileNo = findViewById(R.id.textViewMobileNo);
        textViewMobileNo.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewMobileNo.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        textViewMobileNo.setText("");
        textViewMobileNo.append(getResources().getString(R.string.lbl_mobile_no));
        textViewMobileNo.append(" " + mobileNumber);

        textViewPoints = findViewById(R.id.textViewPoints);
        textViewPoints.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewPoints.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        textViewPoints.setText("");
        textViewPoints.append(getResources().getString(R.string.lbl_my_points));
        textViewPoints.append(" " + ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0));

        lblName = findViewById(R.id.lblName);
        lblName.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblName.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewName = findViewById(R.id.textViewName);
        textViewName.setTypeface(Utils.getTypeFaceBodoni72(this));

        lblProfilePhoto = findViewById(R.id.lblProfilePhoto);
        lblProfilePhoto.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblProfilePhoto.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewChangePhoto = findViewById(R.id.textViewChangePhoto);
        textViewChangePhoto.setTypeface(Utils.getTypeFaceBodoni72(this));
        /*textViewChangePhoto.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        /*textViewChangeName = findViewById(R.id.textViewChangeName);
        textViewChangeName.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewChangeName.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        textViewChangeName.setText("");
        textViewChangeName.append(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_FIRST_NAME, ""));
        textViewChangeName.append(" " + ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_LAST_NAME, ""));*/

        lblPassword = findViewById(R.id.lblPassword);
        lblPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblPassword.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewChangePassword = findViewById(R.id.textViewChangePassword);
        textViewChangePassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        /*textViewChangePassword.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        /*layoutName = findViewById(R.id.layoutName);
        layoutName.setOnClickListener(this);

        layoutPassword = findViewById(R.id.layoutPassword);
        layoutPassword.setOnClickListener(this);*/

        layoutName = findViewById(R.id.layoutName);
        layoutName.setOnClickListener(this);

        layoutProfilePhoto = findViewById(R.id.layoutProfilePhoto);
        layoutProfilePhoto.setOnClickListener(this);

        layoutPassword = findViewById(R.id.layoutPassword);
        layoutPassword.setOnClickListener(this);

        layoutKyc = findViewById(R.id.layoutKyc);
        layoutKyc.setOnClickListener(this);

        lblKyc = findViewById(R.id.lblKyc);
        lblKyc.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblKyc.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewChangeKyc = findViewById(R.id.textViewChangeKyc);
        textViewChangeKyc.setTypeface(Utils.getTypeFaceBodoni72(this));

    }

    private void updateProfilePic() {
        String profilePic = ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_PROFILE_IMAGE, "");

        if (profilePic != null && !profilePic.isEmpty()) {
            Glide.with(this)
                    .load(BuildConfig.BASE_URL + /*"uploads/" +*/ profilePic)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_profile)
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                    .into(imgViewProfile);
        }

    }

    private void updateProfileName() {
        String name = ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_FIRST_NAME, "") +
                " " + ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_LAST_NAME, "");
        textViewName.setText(name);
        /*textViewName.setText("");
        textViewName.append(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_FIRST_NAME, ""));
        textViewName.append(" " + ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_LAST_NAME, ""));*/
    }

    /*private void navigateToForgotPasswordScreen(int changePasswordType) {
        Intent intent = new Intent(this, ForgotPasswordScreenActiviy.class);
        intent.putExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE, changePasswordType);
        startActivity(intent);
    }*/

    private void navigateToChangePasswordScreen(int changePasswordType) {
        Intent intent = new Intent(this, ChangePasswordScreenActivity.class);
        intent.putExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE, changePasswordType);
        intent.putExtra(AppConstant.KEY_MOBILE_NUMBER, mobileNumber);
        startActivity(intent);
    }

    private void navigateToKycScreen() {
        Intent intent = new Intent(this, KycScreenActivity.class);
        startActivity(intent);
    }

    private ArrayList<SelectImageOption> getSelectImageOptions() {

        ArrayList<SelectImageOption> selectImageOptions = new ArrayList<>();

        String[] selectImageTitleArray = getResources().getStringArray(R.array.select_image_title);
        int[] selectImageTypeArray = {AppConstant.IMAGE_TYPE_CAMERA, AppConstant.IMAGE_TYPE_GALLERY};

        for (int i = 0; i < selectImageTitleArray.length; i++) {
            SelectImageOption selectImageOption = new SelectImageOption();

            selectImageOption.setSelectImageOption(selectImageTypeArray[i]);
            selectImageOption.setSelectImageTitle(selectImageTitleArray[i]);
            selectImageOption.setSelected(false);

            selectImageOptions.add(selectImageOption);
        }

        return selectImageOptions;
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestCameraPermission(neverAskCameraAgain, this,
                        parentLayout, this);
            } else {
                dispatchTakePictureIntent();
            }

        } else {
            dispatchTakePictureIntent();
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestStoragePermission(neverAskAgain, this,
                        parentLayout, this);
            } else {
                selectImage();
            }

        } else {
            selectImage();
        }
    }

    private void dispatchTakePictureIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQ_IMAGE_CAMERA);
        }
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQ_IMAGE_GALLERY);
        }
    }

    private void processBitmap() {
        showProgressDialog(true, "Please Wait...");
        new Thread() {
            @Override
            public void run() {
                encodedImageString = Utils.convertBitmapToBase64(mBitmap);
                messageHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void callUpdateProfileImageApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_UPDATE_PROFILE_IMAGE_REQUEST, getUpdateProfileImageRequest());
        callAppServer(AppConstant.REQ_API_TYPE_UPDATE_PROFILE_IMAGE, intent, true);
    }

    private UpdateProfileImageRequest getUpdateProfileImageRequest() {
        UpdateProfileImageRequest updateProfileImageRequest = new UpdateProfileImageRequest();
        updateProfileImageRequest.setProfileImage(encodedImageString);
//        updateProfileImageRequest.setProfileImage("data:image/png;base64,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");
        updateProfileImageRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));

        return updateProfileImageRequest;
    }

    private void callUpdateCustomerDetailsApi(CustomerDetailsUpdateRequest customerDetailsUpdateRequest) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_UPDATE_CUSTOMER_NAME_REQUEST, customerDetailsUpdateRequest);
        callAppServer(AppConstant.REQ_API_TYPE_UPDATE_CUSTOMER_NAME, intent, false);
    }

    private void showEditNameDialog() {
        if (!isFinishing()) {
            dismissCustomEditDialog();
            customEditDialogFragment = CustomEditDialogFragment.newInstance(getResources().getString(R.string.title_edit_profile_name));
            customEditDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_EDIT_NAME);
        }

    }

    private void dismissCustomEditDialog() {
        if (!isFinishing() && customEditDialogFragment != null &&
                customEditDialogFragment.isVisible()) {
            customEditDialogFragment.dismissAllowingStateLoss();
            customEditDialogFragment = null;
        }
    }

    private void showSelectImageOptionDialog() {
        if (!isFinishing()) {
            dismissCustomListDialog();
            customListDialogFragment = CustomListDialogFragment.newInstance(getResources().getString(R.string.title_choose_option),
                    AppConstant.SELECT_IMAGE_TYPE_DIALOG, selectImageOptions);
            customListDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_SELECT_IMAGE);
        }
    }

    private void dismissCustomListDialog() {
        if (!isFinishing() && customListDialogFragment != null &&
                customListDialogFragment.isVisible()) {
            customListDialogFragment.dismissAllowingStateLoss();
            customListDialogFragment = null;
        }
    }

    private Handler messageHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            callUpdateProfileImageApi();
        }
    };

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (customEditDialogFragment != null) {
                customEditDialogFragment.toggleProgressBtn(false);
            }
            /*if (genericResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                LogUtils.e(TAG, "Name Updated Successfully!!");
            }*/
            if (genericResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                if (customerDetailsUpdateRequest != null) {
                    ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_FIRST_NAME,
                            customerDetailsUpdateRequest.getFirstName());
                    ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_LAST_NAME,
                            customerDetailsUpdateRequest.getLastName());
                    dismissCustomEditDialog();
                    updateProfileName();
                    Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                            getResources().getString(R.string.name_updated_successful_msg),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                }
            }
        } else if (response instanceof UpdateProfileImageResponse) {
            UpdateProfileImageResponse updateProfileImageResponse = (UpdateProfileImageResponse) response;
            if (updateProfileImageResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                UpdateProfileImageData updateProfileImageData = updateProfileImageResponse.getUpdateProfileImageData();
                if (updateProfileImageData != null &&
                        updateProfileImageData.getProfileImage() != null && !updateProfileImageData.getProfileImage().isEmpty()) {
                    ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_PROFILE_IMAGE, updateProfileImageData.getProfileImage());
                    updateProfilePic();
                }
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        dismissCustomEditDialog();
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
    }

    //CustomEditDialogListener
    @Override
    public void onClickDoneButtonListener(Object data) {
        /*dismissCustomEditDialog();*/
        if (customEditDialogFragment != null) {
            customEditDialogFragment.toggleProgressBtn(true);
            if (data instanceof CustomerDetailsUpdateRequest) {
                customerDetailsUpdateRequest = (CustomerDetailsUpdateRequest) data;
                callUpdateCustomerDetailsApi((customerDetailsUpdateRequest));
            }
        }
    }

    @Override
    public void onClickCancelButtonListener() {
        dismissCustomEditDialog();
    }

    //CustomListDialogListener
    @Override
    public void onListItemSelected(Object data, int dialogType/*, int selectedPosition*/) {
        if (dialogType == AppConstant.SELECT_IMAGE_TYPE_DIALOG) {
            if (data instanceof SelectImageOption) {
                SelectImageOption selectImageOption = (SelectImageOption) data;
                switch (selectImageOption.getSelectImageOption()) {
                    case AppConstant.IMAGE_TYPE_CAMERA:
                        checkCameraPermission();
                        break;
                    case AppConstant.IMAGE_TYPE_GALLERY:
                        checkStoragePermission();
                        break;
                }
            }
        }
    }

    //PermissionUtils.SettingListener
    @Override
    public void openSettingActivity() {
        navigateToAppSettingsScreen();
    }

    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutPassword:
                /*navigateToForgotPasswordScreen(-1);*/
                navigateToChangePasswordScreen(-1);
                break;
            case R.id.layoutName:
                showEditNameDialog();
                break;
            case R.id.layoutProfilePhoto:
                showSelectImageOptionDialog();
                break;
            case R.id.layoutKyc:
                navigateToKycScreen();
                break;
        }
    }
}
