package com.pinnacle.winwin.ui.baazaar.model;

import java.util.List;

public class AllGamesModel<T> {

    private int genericId;
    private String headerTitle;
    private List<T> genericGameList;

    public AllGamesModel() {

    }

    public int getGenericId() {
        return genericId;
    }

    public void setGenericId(int genericId) {
        this.genericId = genericId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<T> getGenericGameList() {
        return genericGameList;
    }

    public void setGenericGameList(List<T> genericGameList) {
        this.genericGameList = genericGameList;
    }
}
