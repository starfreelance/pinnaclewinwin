package com.pinnacle.winwin.ui.paanagame;

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
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.network.model.PaanaDetailsResponse;
import com.pinnacle.winwin.ui.singlegame.adapter.GenericNumberAdapter;

import java.util.ArrayList;

public class PaanaIndividualPagerFragment extends Fragment {

    private static final String TAG = PaanaIndividualPagerFragment.class.getSimpleName();

    private RecyclerView recyclerViewNumbers;
    private GridLayoutManager gridLayoutManager;

    public GenericNumberAdapter genericNumberAdapter;

    private Activity mActivity;
    private ArrayList<PaanaDetailsResponse> paanaDetails;

    public static PaanaIndividualPagerFragment newInstance(ArrayList<PaanaDetailsResponse> paanaDetails) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(AppConstant.KEY_PAANA_NUMBER_LIST, paanaDetails);

        PaanaIndividualPagerFragment fragment = new PaanaIndividualPagerFragment();
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
        return inflater.inflate(R.layout.fragment_paana_individual_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        if (paanaDetails != null && !paanaDetails.isEmpty()) {
            loadGenericNumberAdapter();
        }
    }

    private void initViews(View view) {

        recyclerViewNumbers = view.findViewById(R.id.recyclerViewNumbers);
        gridLayoutManager = new GridLayoutManager(mActivity, 5);
        recyclerViewNumbers.setLayoutManager(gridLayoutManager);
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_PAANA_NUMBER_LIST) &&
                    args.getParcelableArrayList(AppConstant.KEY_PAANA_NUMBER_LIST) != null) {
                paanaDetails = args.getParcelableArrayList(AppConstant.KEY_PAANA_NUMBER_LIST);
            }
        }
    }

    public void loadGenericNumberAdapter() {
        if (genericNumberAdapter == null) {
            genericNumberAdapter = new GenericNumberAdapter(mActivity, paanaDetails, AppConstant.GAME_TYPE_PAANA_NUMBER);
            recyclerViewNumbers.setAdapter(genericNumberAdapter);
        } else {
            genericNumberAdapter.notifyDataSetChanged();
        }
    }
}
