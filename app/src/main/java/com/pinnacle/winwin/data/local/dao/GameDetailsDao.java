package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.GameDetailsResponse;

import java.util.List;

@Dao
public interface GameDetailsDao {

    @Insert
    void insertAll(List<GameDetailsResponse> gameDetails);

    @Query("Select * from game_details")
    List<GameDetailsResponse> getGameDetails();

    @Query("Select * from game_details where game_id = :gameId")
    GameDetailsResponse getGameDetailWithGameId(int gameId);
}
