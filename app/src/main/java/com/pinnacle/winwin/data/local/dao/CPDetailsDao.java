package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.CPDetailsResponse;

import java.util.List;

@Dao
public interface CPDetailsDao {

    @Insert
    void insertAll(List<CPDetailsResponse> cpDetails);

    @Query("Select * from cp_paana_details")
    List<CPDetailsResponse> getCPDetails();
}
