package com.pinnacle.winwin.ui.kyc;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomListDialogFragment;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.listener.CustomListDialogListener;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.IfscResponse;
import com.pinnacle.winwin.network.model.KycRequest;
import com.pinnacle.winwin.network.model.KycResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.kyc.model.AccountType;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class KycScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, CustomListDialogListener {

    private static final String TAG = KycScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewBankInfo;

    private AppCompatEditText editTextAccountName;
    private AppCompatEditText editTextAccountNumber;
    private AppCompatEditText editTextConfirmAccountNumber;
    private AppCompatEditText editTextIfsc;
    private AppCompatEditText editTextAccountType;

    private AppCompatImageView imgViewEdit;

    private Button btnSubmit;

    private View parentLayout;

    private CustomListDialogFragment customListDialogFragment;
    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private boolean isKycCompleted;
    private ArrayList<AccountType> accountTypes;
    private int requestType = -1;
    private IfscResponse ifscResponse;
    private boolean isIfscValid;
    private boolean isEditEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_screen);

        isKycCompleted = ASOnlinePreferenceManager.getBoolean(this, AppConstant.KEY_USER_IS_KYC_COMPLETED, false);
        initViews();
        toggleKycForm();
        accountTypes = getAccountTypeList();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        imgViewEdit = findViewById(R.id.imgViewEdit);
        imgViewEdit.setOnClickListener(this);

        editTextAccountName = findViewById(R.id.editTextAccountName);
        editTextAccountName.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextAccountName.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextAccountNumber = findViewById(R.id.editTextAccountNumber);
        editTextAccountNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextAccountNumber.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextConfirmAccountNumber = findViewById(R.id.editTextConfirmAccountNumber);
        editTextConfirmAccountNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextConfirmAccountNumber.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextIfsc = findViewById(R.id.editTextIfsc);
        editTextIfsc.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextIfsc.addTextChangedListener(ifscTextWatcher);
        editTextIfsc.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextAccountType = findViewById(R.id.editTextAccountType);
        editTextAccountType.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextAccountType.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        editTextAccountType.setOnClickListener(this);

        textViewBankInfo = findViewById(R.id.textViewBankInfo);
        textViewBankInfo.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnSubmit.setOnClickListener(this);

    }

    private void toggleKycForm() {
        if (isKycCompleted) {
            updateKycDetails();
            imgViewEdit.setEnabled(true);
            editTextConfirmAccountNumber.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        } else {
            imgViewEdit.setEnabled(false);
            editTextConfirmAccountNumber.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void toggleImageViewEdit(boolean isEnabled) {
        if (isEnabled) {
            imgViewEdit.setImageResource(R.drawable.ic_cancel);
            editTextConfirmAccountNumber.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            imgViewEdit.setImageResource(R.drawable.ic_edit_kyc);
            editTextConfirmAccountNumber.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    private void updateKycDetails() {
        editTextAccountName.setText(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_ACCOUNT_NAME, ""));
        editTextAccountNumber.setText(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_ACCOUNT_NUMBER, ""));
        editTextConfirmAccountNumber.setText(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_ACCOUNT_NUMBER, ""));
        editTextIfsc.setText(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_IFSC_CODE, ""));
        editTextAccountType.setText(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_ACCOUNT_TYPE, ""));
    }

    private ArrayList<AccountType> getAccountTypeList() {

        ArrayList<AccountType> accountTypes = new ArrayList<>();

        String[] accountTypeTitleArray = getResources().getStringArray(R.array.account_type_title);
        int[] accountTypeArray = {AppConstant.ACCOUNT_TYPE_SAVINGS, AppConstant.ACCOUNT_TYPE_CURRENT};

        for (int i = 0; i < accountTypeTitleArray.length; i++) {
            AccountType mAccountType = new AccountType();
            mAccountType.setTitle(accountTypeTitleArray[i]);
            mAccountType.setType(accountTypeArray[i]);
            mAccountType.setSelected(false);

            accountTypes.add(mAccountType);
        }

        return accountTypes;
    }

    private boolean validateKycForm() {
        if (TextUtils.isEmpty(editTextAccountName.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_account_name_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextAccountNumber.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_account_number_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextConfirmAccountNumber.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.confirm_account_number_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!editTextAccountNumber.getText().toString().equals(editTextConfirmAccountNumber.getText().toString())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.account_number_match_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextIfsc.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_ifsc_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextAccountType.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.select_account_type_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!isIfscValid) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.invalid_ifsc_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private void updateBankInfo(IfscResponse ifscResponse) {
        String ifscString = "";
        if (isIfscValid && ifscResponse != null) {
            ifscString = ifscResponse.getBank() + " " + ifscResponse.getBranch();
        } else {
            ifscString = getResources().getString(R.string.invalid_ifsc_error);
        }
        textViewBankInfo.setVisibility(View.VISIBLE);
        textViewBankInfo.setText(ifscString);
    }

    private void saveKycData(UserData userData) {
        if (userData != null) {
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ACCOUNT_NUMBER, userData.getAccountNumber());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_IFSC_CODE, userData.getIfscCode());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_BANK_NAME, userData.getBankName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ACCOUNT_NAME, userData.getAccountName());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_ACCOUNT_TYPE, userData.getAccountType());
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_BRANCH_NAME, userData.getBranchName());
            ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_USER_IS_KYC_COMPLETED, userData.isKycCompleted());
        }
    }

    private void showAccountTypeDialog() {
        if (!isFinishing()) {
            dismissCustomListDialog();
            customListDialogFragment = CustomListDialogFragment.newInstance(getResources().getString(R.string.title_choose_option),
                    AppConstant.SELECT_ACCOUNT_TYPE_DIALOG, accountTypes);
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

    private void showKycUpdatedDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.kyc_updated_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_KYC_UPDATED);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    private void callUpdateKycApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_UPDATE_KYC_REQUEST, getKycRequest());
        callAppServer(AppConstant.REQ_API_TYPE_UPDATE_KYC, intent, true);
    }

    private KycRequest getKycRequest() {
        KycRequest kycRequest = new KycRequest();
        kycRequest.setAccountName(editTextAccountName.getText().toString().trim());
        kycRequest.setAccountNo(editTextAccountNumber.getText().toString().trim());
        kycRequest.setAccountType(editTextAccountType.getText().toString().trim());
        kycRequest.setBankName(ifscResponse.getBank());
        kycRequest.setBranchName(ifscResponse.getBranch());
        kycRequest.setIfscCode(editTextIfsc.getText().toString().trim());
        kycRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));

        return kycRequest;
    }

    private void callGetBankDetailsApi(String ifsc) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_BANK_DETAILS_URL, AppConstant.IFSC_INFO_URL + ifsc);
        requestType = AppConstant.REQ_API_TYPE_GET_BANK_DETAILS;
        callAppServer(AppConstant.REQ_API_TYPE_GET_BANK_DETAILS, intent, false);
    }

    private TextWatcher ifscTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String ifscString = charSequence.toString();
            if (ifscString.length() == 11) {
                callGetBankDetailsApi(ifscString);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof KycResponse) {
            KycResponse kycResponse = (KycResponse) response;
            if (kycResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                saveKycData(kycResponse.getUserData());
                toggleKycForm();
                showKycUpdatedDialog();
            }


        } else if (response instanceof IfscResponse) {
            ifscResponse = (IfscResponse) response;
            isIfscValid = true;
            LogUtils.d(TAG, ifscResponse.getBranch());
            updateBankInfo(ifscResponse);
        }
    }

    @Override
    protected void onFailure(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            if (requestType == AppConstant.REQ_API_TYPE_GET_BANK_DETAILS) {
                isIfscValid = false;
                updateBankInfo(null);
                LogUtils.d(TAG, "IFSC INVALID");
            } else {
                Utils.showCustomSnackBarMessageView(this, parentLayout,
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        }
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    @Override
    public void onListItemSelected(Object data, int dialogType) {
        if (dialogType == AppConstant.SELECT_ACCOUNT_TYPE_DIALOG) {
            if (data instanceof AccountType) {
                AccountType accountType = (AccountType) data;
                editTextAccountType.setText(accountType.getTitle());
            }
        }
    }

    @Override
    public void onClickSingleButtonListener() {
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_KYC_UPDATED)) {
                    dismissCustomSingleButtonDialog();
                    finish();
                }
            }
        } else {
            super.onClickSingleButtonListener();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmit) {
            if (validateKycForm()) {
                callUpdateKycApi();
            }
        } else if (id == R.id.editTextAccountType) {
            showAccountTypeDialog();
        } else if (id == R.id.imgViewEdit) {
            toggleImageViewEdit(isEditEnabled);
            isEditEnabled = !isEditEnabled;
        }
    }
}