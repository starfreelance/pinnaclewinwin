package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.MotorCombDetailsResponse;

import java.util.List;

@Dao
public interface MotorCombDetailsDao {

    @Insert
    void insertAll(List<MotorCombDetailsResponse> motorCombDetails);

    @Query("Select * from motor_comb_details")
    List<MotorCombDetailsResponse> getMotorCombDetails();
}
