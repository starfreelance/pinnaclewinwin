package com.pinnacle.winwin.ui.baazaar.listener;

import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.network.model.CustomGamesResponse;

public interface BaazaarListener {

    void onBaazaarSelectListener(BazaarDetailsResponse bazaarDetail);
    void onCustomGameSelectListener(CustomGamesResponse customGame);
}
