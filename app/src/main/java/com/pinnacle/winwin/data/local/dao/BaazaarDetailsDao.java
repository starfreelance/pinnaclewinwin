package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.BazaarDetailsResponse;

import java.util.List;

@Dao
public interface BaazaarDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BazaarDetailsResponse> bazaarDetails);

    @Query("Select * from baazaar_details WHERE status = 1 ORDER BY strftime('%H-%M-%S', bazaar_timing) ASC")
    List<BazaarDetailsResponse> getBazaarDetails();
}
