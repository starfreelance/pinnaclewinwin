package com.pinnacle.winwin.ui.chartgame.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ChartTabNumber implements Parcelable {

    private String gameNo;
    private boolean isSelected;
    private List<ChartDetail> chartDetailList = null;
    private int selectedIndex = -1;
    private int combinationCount;
    private boolean isItemClickable = true;

    public ChartTabNumber() {

    }

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<ChartDetail> getChartDetailList() {
        return chartDetailList;
    }

    public void setChartDetailList(List<ChartDetail> chartDetailList) {
        this.chartDetailList = chartDetailList;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getCombinationCount() {
        return combinationCount;
    }

    public void setCombinationCount(int combinationCount) {
        this.combinationCount = combinationCount;
    }

    public boolean isItemClickable() {
        return isItemClickable;
    }

    public void setItemClickable(boolean itemClickable) {
        isItemClickable = itemClickable;
    }

    protected ChartTabNumber(Parcel in) {
        gameNo = in.readString();
        isSelected = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            chartDetailList = new ArrayList<ChartDetail>();
            in.readList(chartDetailList, ChartDetail.class.getClassLoader());
        } else {
            chartDetailList = null;
        }
        selectedIndex = in.readInt();
        combinationCount = in.readInt();
        isItemClickable = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameNo);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        if (chartDetailList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(chartDetailList);
        }
        dest.writeInt(selectedIndex);
        dest.writeInt(combinationCount);
        dest.writeByte((byte) (isItemClickable ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChartTabNumber> CREATOR = new Parcelable.Creator<ChartTabNumber>() {
        @Override
        public ChartTabNumber createFromParcel(Parcel in) {
            return new ChartTabNumber(in);
        }

        @Override
        public ChartTabNumber[] newArray(int size) {
            return new ChartTabNumber[size];
        }
    };
}
