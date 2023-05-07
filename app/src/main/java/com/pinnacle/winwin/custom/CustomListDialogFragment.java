package com.pinnacle.winwin.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.listener.CustomListDialogListener;
import com.pinnacle.winwin.ui.kyc.model.AccountType;
import com.pinnacle.winwin.ui.personalinfo.adapter.GenericListDialogAdapter;
import com.pinnacle.winwin.ui.personalinfo.listener.GenericListAdapterListener;
import com.pinnacle.winwin.ui.personalinfo.model.SelectImageOption;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class CustomListDialogFragment<T> extends DialogFragment implements GenericListAdapterListener {

    private static final String TAG = CustomListDialogFragment.class.getSimpleName();

    private AppCompatTextView textViewTitle;

    private RecyclerView recyclerViewItems;
    private GenericListDialogAdapter mAdapter;

    private Activity mActivity;

    private String title;
    private int dialogType;
    private ArrayList<T> dataList;

    private CustomListDialogListener customListDialogListener;

    public static <T> CustomListDialogFragment newInstance(String title, int dialogType, ArrayList<T> dataList) {

        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_DIALOG_TITLE, title);
        args.putInt(AppConstant.KEY_DIALOG_TYPE, dialogType);
        if (dataList != null && !dataList.isEmpty()) {
            args.putParcelableArrayList(AppConstant.KEY_DIALOG_LIST, (ArrayList<? extends Parcelable>) dataList);
        }

        CustomListDialogFragment fragment = new CustomListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomListDialogFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity = (Activity) context;
            customListDialogListener = (CustomListDialogListener) mActivity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processBundleArgs(getArguments());
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
            window.setLayout((int) (size.x * 0.50), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        initViews(view);
        loadAdapter();
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

    //Local Methods
    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        textViewTitle.setText(title);

        recyclerViewItems = view.findViewById(R.id.recyclerViewItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItems.setLayoutManager(layoutManager);
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_DIALOG_TITLE) &&
                    args.getString(AppConstant.KEY_DIALOG_TITLE) != null) {
                title = args.getString(AppConstant.KEY_DIALOG_TITLE);
            }
            if (args.containsKey(AppConstant.KEY_DIALOG_TYPE)) {
                dialogType = args.getInt(AppConstant.KEY_DIALOG_TYPE, -1);
            }
            if (args.containsKey(AppConstant.KEY_DIALOG_LIST) &&
                    args.getParcelableArrayList(AppConstant.KEY_DIALOG_LIST) != null) {
                dataList = (ArrayList<T>) args.getParcelableArrayList(AppConstant.KEY_DIALOG_LIST);
            }
        }
    }

    private void loadAdapter() {
        if (mAdapter == null) {
            mAdapter = new GenericListDialogAdapter(mActivity, dataList, dialogType);
            mAdapter.setAdapterListener(this);
            recyclerViewItems.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    //Event Handlers
    //GenericListAdapterListener
    @Override
    public void onAdapterItemSelected(int index) {

        if (dataList != null) {
            if (dataList.get(0) instanceof SelectImageOption) {

                ArrayList<SelectImageOption> selectImageOptions = (ArrayList<SelectImageOption>) dataList;
                for (SelectImageOption mSelectImageOption : selectImageOptions) {
                    if (mSelectImageOption.isSelected()) {
                        mSelectImageOption.setSelected(false);
                    }
                }

                SelectImageOption selectImageOption = (SelectImageOption) dataList.get(index);
                selectImageOption.setSelected(true);
                selectImageOptions.set(index, selectImageOption);
                dataList = (ArrayList<T>) selectImageOptions;
                mAdapter.updateData(dataList);
                if (customListDialogListener != null) {
                    dismissAllowingStateLoss();
                    customListDialogListener.onListItemSelected(selectImageOption, AppConstant.SELECT_IMAGE_TYPE_DIALOG);
                }
            } else if (dataList.get(0) instanceof AccountType) {

                ArrayList<AccountType> accountTypes = (ArrayList<AccountType>) dataList;
                for (AccountType mAccountType : accountTypes) {
                    if (mAccountType.isSelected()) {
                        mAccountType.setSelected(false);
                    }
                }

                AccountType accountType = (AccountType) dataList.get(index);
                accountType.setSelected(true);
                accountTypes.set(index, accountType);
                dataList = (ArrayList<T>) accountTypes;
                mAdapter.updateData(dataList);
                if (customListDialogListener != null) {
                    dismissAllowingStateLoss();
                    customListDialogListener.onListItemSelected(accountType, AppConstant.SELECT_ACCOUNT_TYPE_DIALOG);
                }
            }
        }
    }
}
