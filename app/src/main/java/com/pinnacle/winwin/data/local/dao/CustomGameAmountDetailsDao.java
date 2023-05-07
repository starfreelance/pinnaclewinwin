package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.CustomGameAmountResponse;

import java.util.List;

@Dao
public interface CustomGameAmountDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomGameAmountResponse customGameAmountDetail);

    @Query("Select * from custom_game_amount_details")
    List<CustomGameAmountResponse> getCustomGameAmountDetails();

    @Query("Select * from custom_game_amount_details where game_id = :gameId")
    List<CustomGameAmountResponse> getCustomGameAmountDetailsWithGameId(int gameId);
}
