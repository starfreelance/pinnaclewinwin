package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.ChartDetailsResponse;

import java.util.List;

@Dao
public interface ChartDetailsDao {

    @Insert
    void insertAll(List<ChartDetailsResponse> chartDetails);

    @Query("Select * from chart_details")
    List<ChartDetailsResponse> getChartDetails();

    @Query("Select DISTINCT game_no from chart_details")
    List<String> getGameNumbers();

    @Query("Select * from chart_details where game_no = :gameNumber")
    List<ChartDetailsResponse> getChartDetailsWithGameNumber(String gameNumber);

    @Query("Select paana_no from chart_details where chart_name = :chartName")
    List<String> getChartPaanas(String chartName);
}
