package com.pinnacle.winwin.ui.chartgame.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pinnacle.winwin.ui.chartgame.NumberPagerFragment;
import com.pinnacle.winwin.ui.chartgame.model.ChartTabNumber;

import java.util.ArrayList;

public class NumberPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ChartTabNumber> chartTabNumbers;

    public NumberPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<ChartTabNumber> chartTabNumbers) {
        super(fm, behavior);
        this.chartTabNumbers = chartTabNumbers;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return NumberPagerFragment.newInstance(chartTabNumbers.get(position));
    }

    @Override
    public int getCount() {
        return chartTabNumbers.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return chartTabNumbers.get(position).getGameNo();
    }

    public void updateData(ArrayList<ChartTabNumber> chartTabNumbers) {
        this.chartTabNumbers = chartTabNumbers;
        notifyDataSetChanged();
    }
}
