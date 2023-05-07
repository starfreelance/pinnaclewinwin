package com.pinnacle.winwin.ui.paanagame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.ui.paanagame.adapter.PaanaTypeGroupAdapter;
import com.pinnacle.winwin.ui.paanagame.listener.PaanaGroupCheckBoxListener;
import com.pinnacle.winwin.ui.paanagame.model.PaanaGroupModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class PaanaGroupPagerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = PaanaGroupPagerFragment.class.getSimpleName();

    private AppCompatTextView textViewPaanaNumber;

    private AppCompatCheckBox checkboxSP;
    private AppCompatCheckBox checkboxDP;

    private RecyclerView recyclerViewPaanaType;
    public PaanaTypeGroupAdapter paanaTypeGroupAdapter;

    private Activity mActivity;
    private PaanaGroupCheckBoxListener paanaGroupCheckBoxListener;
    private ArrayList<PaanaGroupModel> paanaGroupList;
    private int selectedPaanaNumber;

    public static PaanaGroupPagerFragment newInstance(ArrayList<PaanaGroupModel> paanaGroupList, int selectedPaanaNumber) {
        
        Bundle args = new Bundle();
        args.putParcelableArrayList(AppConstant.KEY_PAANA_TYPE_GROUP_LIST, paanaGroupList);
        args.putInt(AppConstant.KEY_SELECTED_PAANA_NUMBER, selectedPaanaNumber);
        
        PaanaGroupPagerFragment fragment = new PaanaGroupPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            paanaGroupCheckBoxListener = (PaanaGroupCheckBoxListener) mActivity;
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
        return inflater.inflate(R.layout.fragment_paana_group_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
//        paanaGroupList = getPaanaGroupList();
        if (paanaGroupList != null && !paanaGroupList.isEmpty()) {
            loadPaanaTypeGroupAdapter();
        }
    }

    private void initViews(View view) {

        /*checkboxSP = view.findViewById(R.id.checkboxSP);
        checkboxSP.setOnClickListener(this);
        checkboxSP.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        checkboxSP.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        checkboxDP = view.findViewById(R.id.checkboxDP);
        checkboxDP.setOnClickListener(this);
        checkboxDP.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        checkboxDP.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        textViewPaanaNumber = view.findViewById(R.id.textViewPaanaNumber);
        textViewPaanaNumber.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewPaanaNumber.setText(String.format(getResources().getString(R.string.lbl_paana_number), String.valueOf(selectedPaanaNumber)));

        recyclerViewPaanaType = view.findViewById(R.id.recyclerViewPaanaType);
        recyclerViewPaanaType.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_PAANA_TYPE_GROUP_LIST) &&
                    args.getParcelableArrayList(AppConstant.KEY_PAANA_TYPE_GROUP_LIST) != null) {
                paanaGroupList = args.getParcelableArrayList(AppConstant.KEY_PAANA_TYPE_GROUP_LIST);
            }
            if (args.containsKey(AppConstant.KEY_SELECTED_PAANA_NUMBER)) {
                selectedPaanaNumber = args.getInt(AppConstant.KEY_SELECTED_PAANA_NUMBER, -1);
            }
        }
    }

    /*private ArrayList<PaanaGroupModel> getPaanaGroupList() {
        ArrayList<PaanaGroupModel> paanaGroupList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            PaanaGroupModel paanaGroupModel = new PaanaGroupModel();
            if (i == 0) {
                paanaGroupModel.setPaanaType(AppConstant.PAANA_TYPE_SP);
            } else {
                paanaGroupModel.setPaanaType(AppConstant.PAANA_TYPE_DP);
            }
            paanaGroupList.add(paanaGroupModel);
        }

        return paanaGroupList;
    }*/

    public void loadPaanaTypeGroupAdapter() {
        if (paanaTypeGroupAdapter == null) {
            paanaTypeGroupAdapter = new PaanaTypeGroupAdapter(mActivity, paanaGroupList);
            recyclerViewPaanaType.setAdapter(paanaTypeGroupAdapter);
        } else {
            paanaTypeGroupAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.checkboxSP:
                checkboxSP.setChecked(checkboxSP.isChecked());
                paanaGroupCheckBoxListener.onSPCheckedChangeListener(checkboxSP.isChecked());
                break;
            case R.id.checkboxDP:
                if (!mActivity.isFinishing()) {
                    PaanaNumberSwipeableScreenActivity paanaNumberSwipeableScreenActivity = (PaanaNumberSwipeableScreenActivity) mActivity;
                    if (paanaNumberSwipeableScreenActivity.totalSPPoints > 0) {
                        checkboxDP.setChecked(checkboxDP.isChecked());
                        paanaGroupCheckBoxListener.onDPCheckedChangeListener(checkboxDP.isChecked());
                    }
                }
                break;
        }*/
    }
}
