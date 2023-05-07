package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.CustomGamesResponse;

import java.util.List;

@Dao
public interface CustomGamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CustomGamesResponse> customGames);

    @Query("Select * from custom_games")
    List<CustomGamesResponse> getCustomGames();
}
