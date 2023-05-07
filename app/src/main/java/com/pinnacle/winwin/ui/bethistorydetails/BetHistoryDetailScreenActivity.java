package com.pinnacle.winwin.ui.bethistorydetails;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.BetClaimRequest;
import com.pinnacle.winwin.network.model.BetClaimResponse;
import com.pinnacle.winwin.network.model.BetHistoryData;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.utils.Utils;

public class BetHistoryDetailScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private static final String TAG = BetHistoryDetailScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewBetId;
    private AppCompatTextView textViewBaazaarName;
    private AppCompatTextView textViewBaazaarResult;
    private AppCompatTextView textViewGameName;
    private AppCompatTextView textViewBetDate;
    private AppCompatTextView textViewBetTiming;
    private AppCompatTextView textViewBetNo;
    private AppCompatTextView textViewPointsPerPaana;
    private AppCompatTextView textViewTotalPoints;
    private AppCompatTextView textViewPaanaType;
    private AppCompatTextView textViewWinningAmount;

    private Button btnClaim;

    private View parentLayout;

    private BetHistoryData betHistoryData;
    private boolean isClaimBetSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_history_detail_screen);

        processIntentData();
        initViews();
        updateBetDetailsView();
        toggleBtnClaim();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (isClaimBetSuccessful) {
            navigateToGameHistoryScreen();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_bet_details));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewBetId = findViewById(R.id.textViewBetId);
        textViewBetId.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewBaazaarName = findViewById(R.id.textViewBaazaarName);

        textViewBaazaarResult = findViewById(R.id.textViewBaazaarResult);
        textViewBaazaarResult.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewGameName = findViewById(R.id.textViewGameName);
        textViewGameName.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewBetDate = findViewById(R.id.textViewBetDate);
        textViewBetDate.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewBetTiming = findViewById(R.id.textViewBetTiming);
        textViewBetTiming.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewBetNo = findViewById(R.id.textViewBetNo);
        textViewBetNo.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewPointsPerPaana = findViewById(R.id.textViewPointsPerPaana);
        textViewPointsPerPaana.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewTotalPoints = findViewById(R.id.textViewTotalPoints);
        textViewTotalPoints.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewPaanaType = findViewById(R.id.textViewPaanaType);
        textViewPaanaType.setTypeface(Utils.getTypeFaceBodoni72(this));

        textViewWinningAmount = findViewById(R.id.textViewWinningAmount);
        textViewWinningAmount.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnClaim = findViewById(R.id.btnClaim);
        btnClaim.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnClaim.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        btnClaim.setOnClickListener(this);

    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_BET_HISTORY_DATA) &&
                    getIntent().getParcelableExtra(AppConstant.KEY_BET_HISTORY_DATA) != null) {
                betHistoryData = getIntent().getParcelableExtra(AppConstant.KEY_BET_HISTORY_DATA);
            }
        }
    }

    private void updateBetDetailsView() {
        if (betHistoryData != null) {
            textViewBetId.setText("");
            textViewBetId.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_bet_id),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewBetId.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getBetId(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            textViewBaazaarName.setText("");
            textViewBaazaarName.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_baazar_name),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewBaazaarName.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getBazaarName(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            textViewBaazaarResult.setText("");
            textViewBaazaarResult.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_baazar_result),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            if (betHistoryData.getBazaarResult() != null &&
                    !betHistoryData.getBazaarResult().isEmpty()) {
                textViewBaazaarResult.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getBazaarResult(),
                        getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            } else {
                textViewBaazaarResult.append(Utils.getCustomSpannableString(this, " " + getResources().getString(R.string.final_number_empty),
                        getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            }

            textViewGameName.setText("");
            textViewGameName.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_bet_game_name),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewGameName.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getGameName(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            textViewBetDate.setText("");
            textViewBetDate.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_date),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewBetDate.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getBookingTime(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            textViewBetTiming.setText("");
            textViewBetTiming.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_time),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewBetTiming.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getBookingTime(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            textViewBetNo.setText("");
            textViewBetNo.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_bet_no),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewBetNo.append(Utils.getCustomSpannableString(this, " "  + betHistoryData.getSelectedPaana(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            if (betHistoryData.getGameId() == AppConstant.GAME_TYPE_MOTOR ||
                    betHistoryData.getGameId() == AppConstant.GAME_TYPE_CP) {
                textViewPointsPerPaana.setVisibility(View.VISIBLE);
                textViewPointsPerPaana.setText("");
                textViewPointsPerPaana.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_points_per_paana),
                        getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
                textViewPointsPerPaana.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getAmountPerPaana(),
                        getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            } else {
                textViewPointsPerPaana.setVisibility(View.GONE);
            }

            textViewTotalPoints.setText("");
            textViewTotalPoints.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_total_points),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            textViewTotalPoints.append(Utils.getCustomSpannableString(this, " "  + betHistoryData.getTotalAmount(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));

            if (betHistoryData.getGameId() == AppConstant.GAME_TYPE_PAANA ||
                    betHistoryData.getGameId() == AppConstant.GAME_TYPE_MOTOR) {
                textViewPaanaType.setVisibility(View.VISIBLE);
                textViewPaanaType.setText("");
                textViewPaanaType.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_paana_type),
                        getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
                textViewPaanaType.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getPaanaType(),
                        getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            } else {
                textViewPaanaType.setVisibility(View.GONE);
            }
        }
    }

    private void toggleBtnClaim() {
        if (betHistoryData.getBetStatus().equalsIgnoreCase(AppConstant.BET_STATUS_WON_UNCLAIMED)) {
            btnClaim.setVisibility(View.VISIBLE);
            textViewWinningAmount.setVisibility(View.GONE);
        } else if (betHistoryData.getBetStatus().equalsIgnoreCase(AppConstant.BET_STATUS_WON_CLAIMED)) {
            btnClaim.setVisibility(View.GONE);
            textViewWinningAmount.setVisibility(View.VISIBLE);

            textViewWinningAmount.setText("");
            textViewWinningAmount.append(Utils.getCustomSpannableStringWithGradient(this, getResources().getString(R.string.lbl_you_won),
                    getResources().getColor(R.color.colorAccent), Utils.getTypeFaceBodoni72(this)));
            if (betHistoryData.getWinningAmount() != 0) {
                textViewWinningAmount.append(Utils.getCustomSpannableString(this, " " + betHistoryData.getWinningAmount(),
                        getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            } else {
                textViewWinningAmount.append(Utils.getCustomSpannableString(this, " " + getResources().getString(R.string.final_number_empty),
                        getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(this)));
            }
        } else {
            btnClaim.setVisibility(View.GONE);
            textViewWinningAmount.setVisibility(View.GONE);
        }
    }

    private void navigateToGameHistoryScreen() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_BET_HISTORY_CLAIM_DATA, betHistoryData);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void callClaimBetApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_CLAIM_BET_REQUEST, getBetClaimRequest());
        callAppServer(AppConstant.REQ_API_TYPE_CLAIM_BET, intent, true);
    }

    private BetClaimRequest getBetClaimRequest() {
        BetClaimRequest betClaimRequest = new BetClaimRequest();
        betClaimRequest.setBetId(betHistoryData.getBetId());

        return betClaimRequest;
    }

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof BetClaimResponse) {
            BetClaimResponse betClaimResponse = (BetClaimResponse) response;
            if (betClaimResponse.getBetHistoryData() != null) {
                isClaimBetSuccessful = true;
                betHistoryData = betClaimResponse.getBetHistoryData();
                ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, betHistoryData.getUpdatedPoints());
                toggleBtnClaim();
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
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClaim:
                callClaimBetApi();
                break;

        }

    }
}
