package com.pinnacle.winwin.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.listener.CustomHTCongratulationsDialogListener;
import com.pinnacle.winwin.network.model.HTResultData;
import com.pinnacle.winwin.utils.Utils;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class CustomHTCongratulationsDialogFragment extends DialogFragment implements View.OnClickListener {

    private static String TAG = CustomHTCongratulationsDialogFragment.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewResult;
    private AppCompatTextView textViewPoints;

    private Button btnOkay;

    private KonfettiView konfettiView;

    private Activity mActivity;
    private Handler konfettiHandler;

    private HTResultData htResultData;

    private CustomHTCongratulationsDialogListener customHTCongratulationsDialogListener;

    public static CustomHTCongratulationsDialogFragment newInstance(HTResultData htResultData) {

        Bundle args = new Bundle();
        args.putParcelable(AppConstant.KEY_HT_RESULT_DATA, htResultData);

        CustomHTCongratulationsDialogFragment fragment = new CustomHTCongratulationsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            customHTCongratulationsDialogListener = (CustomHTCongratulationsDialogListener) mActivity;
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
        return inflater.inflate(R.layout.dialog_ht_congratulations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        updateHTResultData();
        konfettiHandler = new Handler();
        konfettiHandler.postDelayed(konfettiRunnable, 500);
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
            window.setLayout((int) (size.x * 0.55), (int) (size.x * 0.35));
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        konfettiHandler.removeCallbacks(konfettiRunnable);
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

    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        textViewResult = view.findViewById(R.id.textViewResult);
        textViewResult.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        textViewPoints = view.findViewById(R.id.textViewPoints);
        textViewPoints.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        btnOkay = view.findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(this);

        konfettiView = view.findViewById(R.id.konfettiView);
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_HT_RESULT_DATA) &&
                    args.getParcelable(AppConstant.KEY_HT_RESULT_DATA) != null) {
                htResultData = args.getParcelable(AppConstant.KEY_HT_RESULT_DATA);
            }
        }
    }

    private void updateHTResultData() {

        if (htResultData != null) {
//            textViewResult.setText(String.format(getResources().getString(R.string.ht_result_msg), htResultData.getResult()));
//            textViewPoints.setText(String.format(getResources().getString(R.string.ht_you_won_msg), String.valueOf(htResultData.getWinningAmount())));
            textViewResult.append(getResources().getString(R.string.ht_result_msg));
            textViewResult.append(Utils.getCustomSpannableString(mActivity, " " + htResultData.getResult(),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mActivity), 1.5f, true));
            textViewPoints.append(getResources().getString(R.string.ht_you_won_msg));
            textViewPoints.append(Utils.getCustomSpannableString(mActivity, " " + String.valueOf(htResultData.getWinningAmount()),
                    getResources().getColor(android.R.color.white), Utils.getTypeFaceBodoni72(mActivity), 1.5f, true));

        }
    }

    private void showKonfettiView() {
        Size sizeArray[] = {new Size(10, 5f), new Size(10, 5f)};
        konfettiView.build()
                .addColors(Color.parseColor("#FFbe965d"), Color.parseColor("#FFbe965d"), Color.parseColor("#FFbe965d"), Color.parseColor("#FFbe965d"))
                .setDirection(0.0, 359.0)
                .setSpeed(4f, 7f)
                .setFadeOutEnabled(true)
                .setTimeToLive(3000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(sizeArray)
                .setPosition(konfettiView.getX() + konfettiView.getWidth() / 2f, konfettiView.getY() + konfettiView.getHeight() / 3f)
                .burst(150);
    }

    Runnable konfettiRunnable = new Runnable() {
        @Override
        public void run() {
            showKonfettiView();
        }
    };

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnOkay) {
            if (customHTCongratulationsDialogListener != null) {
                customHTCongratulationsDialogListener.onClickPositiveButton();
            }
        }
    }
}
