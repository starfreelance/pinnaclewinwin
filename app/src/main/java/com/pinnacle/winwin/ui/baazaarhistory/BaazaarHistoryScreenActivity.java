package com.pinnacle.winwin.ui.baazaarhistory;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.SyncHorizontalScrollView;
import com.pinnacle.winwin.network.model.BaazaarHistoryData;
import com.pinnacle.winwin.network.model.BaazaarHistoryDataResponse;
import com.pinnacle.winwin.network.model.BaazaarHistoryRequest;
import com.pinnacle.winwin.network.model.BaazaarHistoryResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.ui.baazaarhistory.adapter.CustomTableCellDataAdapter;
import com.pinnacle.winwin.ui.baazaarhistory.adapter.CustomTableRowDateAdapter;
import com.pinnacle.winwin.ui.baazaarhistory.listener.CustomTableCellListener;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class BaazaarHistoryScreenActivity extends ASOnlineBaseActivity implements
        SyncHorizontalScrollView.OnScrollViewListener, CustomTableCellListener {

    private static final String TAG = BaazaarHistoryScreenActivity.class.getSimpleName();
    private static final int DATA_PER_PAGE = 25;

    private AppCompatTextView textViewTitle;

    private AppCompatTextView textViewHeaderDate;
    private AppCompatTextView textViewHeaderKOKC;
    /*private AppCompatTextView textViewHeaderKC;*/
    private AppCompatTextView textViewHeaderMOMC;
    private AppCompatTextView textViewHeaderTOTC;
    private AppCompatTextView textViewHeaderMDOMDC;
    private AppCompatTextView textViewHeaderMNOMNC;
    /*private AppCompatTextView textViewHeaderMC;*/

    private SyncHorizontalScrollView syncHorizontalHeaderView;
    private SyncHorizontalScrollView syncHorizontalDataView;

    private RecyclerView recyclerViewDates;
    private CustomTableRowDateAdapter rowDateAdapter;

    private RecyclerView recyclerViewBaazaarData;
    private CustomTableCellDataAdapter cellDataAdapter;

    private View parentLayout;

    /*private TableView tableViewBaazaarHistory;
    private TableViewAdapter tableViewAdapter;*/

    /*private ArrayList<BazaarDetailsResponse> bazaarDetails;*/

    private ArrayList<BaazaarHistoryData> baazaarHistoryList;
    private ArrayList<String> baazaarResultDates;

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baazaar_history_screen);

        initViews();
        callGetBaazaarHistoryApi(true);
        /*fetchBaazaarDetailsFromDb();*/

        /*tableViewAdapter = new TableViewAdapter(BaazaarHistoryScreenActivity.this);
        tableViewBaazaarHistory.setAdapter(tableViewAdapter);
        tableViewAdapter.setAllItems(getColumnHeaders(), getRowHeaders(), getCellList());*/
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        CustomTableCellDataAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    //Local Methods
    private void initViews() {

        /*tableViewBaazaarHistory = findViewById(R.id.tableViewBaazaarHistory);*/

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_baazaar_history));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewHeaderDate = findViewById(R.id.textViewHeaderDate);
        textViewHeaderDate.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderDate.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderKOKC = findViewById(R.id.textViewHeaderKOKC);
        textViewHeaderKOKC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderKOKC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        /*textViewHeaderKC = findViewById(R.id.textViewHeaderKC);
        textViewHeaderKC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderKC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        textViewHeaderMOMC = findViewById(R.id.textViewHeaderMOMC);
        textViewHeaderMOMC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderMOMC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderTOTC = findViewById(R.id.textViewHeaderTOTC);
        textViewHeaderTOTC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderTOTC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderMDOMDC = findViewById(R.id.textViewHeaderMDOMDC);
        textViewHeaderMDOMDC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderMDOMDC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewHeaderMNOMNC = findViewById(R.id.textViewHeaderMNOMNC);
        textViewHeaderMNOMNC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderMNOMNC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        /*textViewHeaderMC = findViewById(R.id.textViewHeaderMC);
        textViewHeaderMC.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewHeaderMC.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

        syncHorizontalHeaderView = findViewById(R.id.syncHorizontalHeaderView);
        syncHorizontalHeaderView.setOnScrollViewListener(this);

        syncHorizontalDataView = findViewById(R.id.syncHorizontalDataView);
        syncHorizontalDataView.setOnScrollViewListener(this);

        recyclerViewDates = findViewById(R.id.recyclerViewDates);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDates.setLayoutManager(layoutManager);
        recyclerViewDates.addOnScrollListener(scrollListener);

        recyclerViewBaazaarData = findViewById(R.id.recyclerViewBaazaarData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBaazaarData.setLayoutManager(linearLayoutManager);
        recyclerViewBaazaarData.addOnScrollListener(scrollListener);

    }

    private void loadRowDateAdapter() {
        if (rowDateAdapter == null) {
            rowDateAdapter = new CustomTableRowDateAdapter(this, baazaarResultDates, AppConstant.TABLE_TYPE_BAAZAAR_HISTORY);
            recyclerViewDates.setAdapter(rowDateAdapter);
        } else {
            rowDateAdapter.notifyDataSetChanged();
        }
    }

    private void loadCellDataAdapter() {
        if (cellDataAdapter == null) {
            cellDataAdapter = new CustomTableCellDataAdapter(this, baazaarHistoryList);
            recyclerViewBaazaarData.setAdapter(cellDataAdapter);
        } else {
            cellDataAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<String> getBaazaarResultDates(ArrayList<BaazaarHistoryData> baazaarHistoryDataList) {
        ArrayList<String> baazaarResultDates = new ArrayList<>();

        if (baazaarHistoryDataList != null && !baazaarHistoryDataList.isEmpty()) {
            for (BaazaarHistoryData baazaarHistoryData : baazaarHistoryDataList) {
                baazaarResultDates.add(baazaarHistoryData.getResultDate());
            }
        }

        return baazaarResultDates;
    }

    /*private void fetchBaazaarDetailsFromDb() {
        bazaarDetails = (ArrayList<BazaarDetailsResponse>) ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails();
    }

    private List<ColumnHeader> getColumnHeaders() {
        List<ColumnHeader> columnHeaders =  new ArrayList<>();

        if (bazaarDetails != null && !bazaarDetails.isEmpty()) {
            for (BazaarDetailsResponse  bazaarDetailsResponse : bazaarDetails) {
                ColumnHeader columnHeader = new ColumnHeader(String.valueOf(bazaarDetailsResponse.getBazaarId()), bazaarDetailsResponse.getBazaarName());
                columnHeaders.add(columnHeader);
            }
        }
        *//*for (int i = 0; i < 20; i++) {
            ColumnHeader columnHeader = new ColumnHeader(String.valueOf(i));
            columnHeaders.add(columnHeader);
        }*//*

        return columnHeaders;
    }

    private List<RowHeader> getRowHeaders() {
        List<RowHeader> rowHeaders = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            RowHeader rowHeader = new RowHeader(String.valueOf(i), "01-06-2019");
            rowHeaders.add(rowHeader);
        }

        return rowHeaders;
    }

    private List<List<Cell>> getCellList() {
        List<List<Cell>> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < bazaarDetails.size(); j++) {
                Cell cell = new Cell(String.valueOf(j), String.valueOf(j));
                cellList.add(cell);
            }

            list.add(cellList);
        }

        return list;
    }*/

    private void callGetBaazaarHistoryApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_BAAZAAR_HISTORY_REQUEST, getBaazaarHistoryRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_BAAZAAR_HISTORY, intent, isProgressVisible);
    }

    private BaazaarHistoryRequest getBaazaarHistoryRequest() {
        BaazaarHistoryRequest baazaarHistoryRequest = new BaazaarHistoryRequest();
        baazaarHistoryRequest.setCurrentPage(currentPage);
        baazaarHistoryRequest.setDataPerPage(DATA_PER_PAGE);

        return baazaarHistoryRequest;
    }

    private void scroll(RecyclerView recyclerView, int dx, int dy) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(scrollListener);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (recyclerViewDates != recyclerView) {
                scroll(recyclerViewDates, dx, dy);
            } else {
                scroll(recyclerViewBaazaarData, dx, dy);
            }
        }
    };

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof BaazaarHistoryResponse) {
            BaazaarHistoryResponse baazaarHistoryResponse = (BaazaarHistoryResponse) response;
            BaazaarHistoryDataResponse baazaarHistoryDataResponse = baazaarHistoryResponse.getBaazaarHistoryDataResponse();
            if (baazaarHistoryDataResponse != null) {
                if (baazaarHistoryDataResponse.getBaazaarHistoryList() != null &&
                        !baazaarHistoryDataResponse.getBaazaarHistoryList().isEmpty()) {
                    LogUtils.e(TAG, String.valueOf(baazaarHistoryDataResponse.getBaazaarHistoryList().size()));
                    if (baazaarHistoryList == null || baazaarHistoryList.isEmpty()) {
                        baazaarHistoryList = (ArrayList<BaazaarHistoryData>) baazaarHistoryDataResponse.getBaazaarHistoryList();
                        baazaarResultDates = getBaazaarResultDates(baazaarHistoryList);
                    } else {
                        baazaarHistoryList.addAll(baazaarHistoryDataResponse.getBaazaarHistoryList());
                        baazaarResultDates.addAll(getBaazaarResultDates((ArrayList<BaazaarHistoryData>) baazaarHistoryDataResponse.getBaazaarHistoryList()));
                    }
                    if (baazaarHistoryDataResponse.getTotalData() == baazaarHistoryList.size()) {
                        CustomTableCellDataAdapter.isEntireListLoaded = true;
                    }
                    loadCellDataAdapter();
                    if (baazaarResultDates != null) {
                        loadRowDateAdapter();
                    }
                }
            }
            /*if (baazaarHistoryResponse.getBaazaarHistoryList() != null &&
                    !baazaarHistoryResponse.getBaazaarHistoryList().isEmpty()) {
                baazaarHistoryList = (ArrayList<BaazaarHistoryData>) baazaarHistoryResponse.getBaazaarHistoryList();
                baazaarResultDates = getBaazaarResultDates();
                loadCellDataAdapter();
                if (baazaarResultDates != null) {
                    loadRowDateAdapter();
                }
            }*/
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

    //Event Handlers
    //SyncHorizontalScrollView.OnScrollViewListener
    @Override
    public void onScrollChanged(SyncHorizontalScrollView syncHorizontalScrollView, int l, int t, int oldl, int oldt) {
        if (syncHorizontalScrollView == syncHorizontalHeaderView) {
            syncHorizontalDataView.scrollTo(l, t);
        } else if (syncHorizontalScrollView == syncHorizontalDataView) {
            syncHorizontalHeaderView.scrollTo(l, t);
        }
    }

    /**
     * CustomTableCellListener
     */
    @Override
    public void onLoadMoreData() {
        currentPage++;
        callGetBaazaarHistoryApi(false);
    }
}
