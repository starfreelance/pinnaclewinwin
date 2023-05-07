package com.pinnacle.winwin.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.listener.CustomBonusDialogListener;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

public class CustomBonusDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = CustomBonusDialogFragment.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewAward;
    private AppCompatTextView textViewBonus;

    private Button btnOkay;

    private Activity mActivity;

    private String bonusAmount;

    private CustomBonusDialogListener customBonusDialogListener;

    public static CustomBonusDialogFragment newInstance(String bonusAmount) {

        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_BONUS_AMOUNT, bonusAmount);

        CustomBonusDialogFragment fragment = new CustomBonusDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            customBonusDialogListener = (CustomBonusDialogListener) mActivity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processBundleArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bonus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        updateBonusData();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);
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
            window.setLayout((int) (size.x * 0.55), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            LogUtils.e("DIALOG FRAGMENT", "Exception", e);
        }
    }

    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewAward = view.findViewById(R.id.textViewAward);
        textViewAward.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewAward.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewBonus = view.findViewById(R.id.textViewBonus);
        textViewBonus.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewBonus.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        btnOkay = view.findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(this);
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_BONUS_AMOUNT) &&
                    args.getString(AppConstant.KEY_BONUS_AMOUNT) != null) {
                bonusAmount = args.getString(AppConstant.KEY_BONUS_AMOUNT);
            }
        }
    }

    private void updateBonusData() {

        if (bonusAmount != null && !bonusAmount.isEmpty()) {
            textViewBonus.setText(String.format(getResources().getString(R.string.bonus_msg), bonusAmount));
        }
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnOkay) {
            if (customBonusDialogListener != null) {
                customBonusDialogListener.onClickPositiveButton();
            }
        }
    }
}
