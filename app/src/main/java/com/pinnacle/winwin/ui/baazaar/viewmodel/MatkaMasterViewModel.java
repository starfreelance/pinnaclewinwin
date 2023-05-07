package com.pinnacle.winwin.ui.baazaar.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.pinnacle.winwin.network.model.BazaarDetailsResponse;

import java.util.List;

public class MatkaMasterViewModel extends AndroidViewModel {

    private LiveData<List<BazaarDetailsResponse>> bazaarDetails;

    public MatkaMasterViewModel(@NonNull Application application) {
        super(application);
    }

    /*public LiveData<List<BazaarDetailsResponse>> getBazaarDetails() {
        return ASOnlineApplication.appDatabase.baazaarDetailsDao().getBazaarDetails();
    }*/


}
