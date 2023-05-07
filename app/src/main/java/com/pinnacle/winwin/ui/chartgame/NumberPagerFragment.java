package com.pinnacle.winwin.ui.chartgame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.network.model.ChartDetailsResponse;
import com.pinnacle.winwin.ui.chartgame.model.ChartDetail;
import com.pinnacle.winwin.ui.chartgame.model.ChartTabNumber;
import com.pinnacle.winwin.ui.singlegame.adapter.GenericNumberAdapter;
import com.pinnacle.winwin.ui.singlegame.model.SingleGameNumberModel;
import com.pinnacle.winwin.utils.LogUtils;

import java.util.ArrayList;

public class NumberPagerFragment extends Fragment {

    private static final String TAG = NumberPagerFragment.class.getSimpleName();

    private RecyclerView recyclerViewNumbers;
    private GridLayoutManager gridLayoutManager;

    public GenericNumberAdapter genericNumberAdapter;

    private Activity mActivity;
    private ArrayList<SingleGameNumberModel> singleGameNumberList;
    private ArrayList<ChartDetailsResponse> chartDetails;
    private ArrayList<ChartDetail> chartDetailList;
    private ArrayList<ChartDetail> updatedChartDetailList;
    private ChartTabNumber chartTabNumber;
    private String gameNumber = "0";

    public static NumberPagerFragment newInstance(ChartTabNumber chartTabNumber) {
        
        Bundle args = new Bundle();
        args.putParcelable(AppConstant.KEY_CHART_TAB_NUMBER, chartTabNumber);

        NumberPagerFragment fragment = new NumberPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
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
        return inflater.inflate(R.layout.fragment_number_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        /*singleGameNumberList = getSingleGameNumberList();
        if (singleGameNumberList != null && !singleGameNumberList.isEmpty()) {
            loadGenericNumberAdapter();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) {
            LogUtils.d(TAG, chartTabNumber.getGameNo());
            /*gameNumber = chartTabNumber.getGameNo();
            chartDetails = fetchChartDetailsFromDb();
            if (chartDetails != null && !chartDetails.isEmpty()) {
                LogUtils.d(TAG, String.valueOf(chartDetails.size()));
                chartDetailList = new ArrayList<>();
                for (ChartDetailsResponse chartDetailsResponse : chartDetails) {
                    ChartDetail chartDetail = new ChartDetail();
                    chartDetail.setGameNo(chartDetailsResponse.getGameNo());
                    chartDetail.setSingleValue(chartDetailsResponse.getSingleValue());
                    chartDetail.setChartName(chartDetailsResponse.getChartName());

                    chartDetailList.add(chartDetail);
                }

                Set<ChartDetail> uniqueChartDetails = new LinkedHashSet<>(chartDetailList);

                updatedChartDetailList = new ArrayList<>(uniqueChartDetails);

                LogUtils.d(TAG, String.valueOf(updatedChartDetailList.size()) + updatedChartDetailList.get(0).getChartName());
                if (!updatedChartDetailList.isEmpty()) {
                    loadGenericNumberAdapter();
                }
            }*/
            updatedChartDetailList = (ArrayList<ChartDetail>) chartTabNumber.getChartDetailList();
            if (!updatedChartDetailList.isEmpty()) {
                loadGenericNumberAdapter();
                if (!chartTabNumber.isItemClickable()) {
                    genericNumberAdapter.isItemClickable = false;
                }
            }
        }
    }

    private void initViews(View view) {

        recyclerViewNumbers = view.findViewById(R.id.recyclerViewNumbers);
        gridLayoutManager = new GridLayoutManager(mActivity, 5);
        recyclerViewNumbers.setLayoutManager(gridLayoutManager);
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_CHART_TAB_NUMBER) &&
                    args.getParcelable(AppConstant.KEY_CHART_TAB_NUMBER) != null) {
                chartTabNumber = args.getParcelable(AppConstant.KEY_CHART_TAB_NUMBER);
            }
        }
    }

    private ArrayList<ChartDetailsResponse> fetchChartDetailsFromDb() {
        return (ArrayList<ChartDetailsResponse>) ASOnlineApplication.appDatabase.chartDetailsDao().getChartDetailsWithGameNumber(gameNumber);
    }

    private ArrayList<SingleGameNumberModel> getSingleGameNumberList() {

        ArrayList<SingleGameNumberModel> singleGameNumberList = new ArrayList<>();

        String[] singleGameNumberArray = getResources().getStringArray(R.array.game_number_array);

        for (int i = 0; i < singleGameNumberArray.length; i++) {
            SingleGameNumberModel singleGameNumberModel = new SingleGameNumberModel();
            singleGameNumberModel.setNumber(singleGameNumberArray[i]);
            singleGameNumberModel.setPointsValue(0);
            singleGameNumberModel.setSelected(false);

            singleGameNumberList.add(singleGameNumberModel);
        }

        return singleGameNumberList;
    }

    public void loadGenericNumberAdapter() {
        if (genericNumberAdapter == null) {
            genericNumberAdapter = new GenericNumberAdapter(mActivity, updatedChartDetailList, AppConstant.GAME_TYPE_CHART);
            recyclerViewNumbers.setAdapter(genericNumberAdapter);
        } else {
            genericNumberAdapter.notifyDataSetChanged();
        }
    }
}
