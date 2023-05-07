package com.pinnacle.winwin.ui.paanagame.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.network.model.PaanaDetailsResponse;
import com.pinnacle.winwin.ui.paanagame.PaanaGroupPagerFragment;
import com.pinnacle.winwin.ui.paanagame.PaanaIndividualPagerFragment;
import com.pinnacle.winwin.ui.paanagame.model.PaanaGroupModel;

import java.util.ArrayList;

public class PaanaNumberPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<PaanaDetailsResponse> paanaDetails;
    private ArrayList<PaanaGroupModel> paanaGroupList;
    private int selectedPaanaNumber;

    public PaanaNumberPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<PaanaDetailsResponse> paanaDetails,
                                   ArrayList<PaanaGroupModel> paanaGroupList, int selectedPaanaNumber) {
        super(fm, behavior);
        this.paanaDetails = paanaDetails;
        this.paanaGroupList = paanaGroupList;
        this.selectedPaanaNumber = selectedPaanaNumber;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return PaanaGroupPagerFragment.newInstance(paanaGroupList, selectedPaanaNumber);
        } else {
            return PaanaIndividualPagerFragment.newInstance(paanaDetails);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return AppConstant.PAANA_GAME_GROUP_TAB;
        } else {
            return AppConstant.PAANA_GAME_INDIVIDUAL_TAB;
        }
    }

    public void updateData(ArrayList<PaanaDetailsResponse> paanaDetails, ArrayList<PaanaGroupModel> paanaGroupList) {
        this.paanaDetails = paanaDetails;
        this.paanaGroupList = paanaGroupList;
        notifyDataSetChanged();
    }
}
