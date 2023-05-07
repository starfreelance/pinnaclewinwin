package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.AmountDetailsResponse;

import java.util.List;

@Dao
public interface AmountDetailsDao {

    @Insert
    void insertAll(List<AmountDetailsResponse> amountDetails);

    @Insert
    void insert(AmountDetailsResponse amountDetail);

    @Query("Select * from amount_details")
    List<AmountDetailsResponse> getAmountDetails();

    @Query("Select * from amount_details where game_id = :gameId")
    List<AmountDetailsResponse> getAmountDetailsWithGameId(int gameId);
}
