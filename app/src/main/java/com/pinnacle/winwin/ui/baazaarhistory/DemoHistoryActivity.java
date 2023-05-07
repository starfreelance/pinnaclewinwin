package com.pinnacle.winwin.ui.baazaarhistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.custom.SyncHorizontalScrollView;

public class DemoHistoryActivity extends AppCompatActivity implements SyncHorizontalScrollView.OnScrollViewListener {

    private SyncHorizontalScrollView syncHorizontalHeaderView;
    private SyncHorizontalScrollView syncHorizontalDataView;

    private RecyclerView recyclerViewDates;
    private LinearLayoutManager layoutManagerDates;

    private RecyclerView recyclerViewBaazaarData;
    private LinearLayoutManager layoutManagerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_history);

        initViews();
    }

    private void initViews() {

        syncHorizontalHeaderView = findViewById(R.id.syncHorizontalHeaderView);
        syncHorizontalHeaderView.setOnScrollViewListener(this);
        syncHorizontalDataView = findViewById(R.id.syncHorizontalDataView);
        syncHorizontalDataView.setOnScrollViewListener(this);

        recyclerViewDates = findViewById(R.id.recyclerViewDates);
        layoutManagerDates = new LinearLayoutManager(this);
        layoutManagerDates.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDates.setLayoutManager(layoutManagerDates);

        DemoDateAdapter demoDateAdapter = new DemoDateAdapter();
        recyclerViewDates.setAdapter(demoDateAdapter);
        recyclerViewDates.addOnScrollListener(scrollListener);

        recyclerViewBaazaarData = findViewById(R.id.recyclerViewBaazaarData);
        layoutManagerData = new LinearLayoutManager(this);
        layoutManagerData.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBaazaarData.setLayoutManager(layoutManagerData);

        DemoBaazaarDataAdapter demoBaazaarDataAdapter = new DemoBaazaarDataAdapter();
        recyclerViewBaazaarData.setAdapter(demoBaazaarDataAdapter);
        recyclerViewBaazaarData.addOnScrollListener(scrollListener);
    }

    @Override
    public void onScrollChanged(SyncHorizontalScrollView syncHorizontalScrollView, int l, int t, int oldl, int oldt) {

        if (syncHorizontalScrollView == syncHorizontalHeaderView) {
            syncHorizontalDataView.scrollTo(l, t);
        } else if (syncHorizontalScrollView == syncHorizontalDataView) {
            syncHorizontalHeaderView.scrollTo(l, t);
        }

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

    private void scroll(RecyclerView recyclerView, int dx, int dy) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(scrollListener);
    }

    /*RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
            super.onScrollStateChanged(recyclerView, scrollState);

            if (recyclerViewDates == recyclerView && scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                draggingView = 1;
            } else if (recyclerViewBaazaarData == recyclerView && scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                draggingView = 2;
            }

        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (recyclerViewDates != recyclerView) {
                scroll(recyclerViewDates, dx, dy);
            } else {
                scroll(recyclerViewBaazaarData, dx, dy);
            }

            if (scrolledDistance > HIDE_THRESHOLD *//*&& controlsVisible*//*) {
//                hideFilterByView();
                *//*controlsVisible = false;*//*
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD *//*&& !controlsVisible*//*) {
//                showFilterByView();
                *//*controlsVisible = true;*//*
                scrolledDistance = 0;
            }

            *//*if ((controlsVisible && y > 0) || (!controlsVisible && y < 0)) {
                scrolledDistance += y;
            }*//*

        }
    };*/

    /*private void scroll(RecyclerView recyclerView, int dx, int dy) {
        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(onScrollListener);
    }*/

    private class DateViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewBaazaarResult;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewBaazaarResult = itemView.findViewById(R.id.textViewBaazaarResult);
        }
    }

    private class DemoDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            RecyclerView.ViewHolder viewHolder;
            View view = LayoutInflater.from(DemoHistoryActivity.this).inflate(R.layout.cell_layout_baazaar_history, viewGroup, false);
            viewHolder = new DateViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    private class BaazaarDataViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewBaazaarResult;

        public BaazaarDataViewHolder(@NonNull View itemView) {
            super(itemView);

            /*textViewBaazaarResult = itemView.findViewById(R.id.textViewBaazaarResult);*/
        }
    }

    private class DemoBaazaarDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            RecyclerView.ViewHolder viewHolder;
            View view = LayoutInflater.from(DemoHistoryActivity.this).inflate(R.layout.cell_column_header_baazaar_history, viewGroup, false);
            viewHolder = new BaazaarDataViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }
}
