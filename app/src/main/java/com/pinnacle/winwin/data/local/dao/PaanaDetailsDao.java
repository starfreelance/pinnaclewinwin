package com.pinnacle.winwin.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pinnacle.winwin.network.model.PaanaDetailsResponse;

import java.util.List;

@Dao
public interface PaanaDetailsDao {

    @Insert
    void insertAll(List<PaanaDetailsResponse> paanaDetails);

    @Query("Select * from paana_details")
    List<PaanaDetailsResponse> getPaanaDetails();

    @Query("Select * from paana_details where single_value = :singleValue")
    List<PaanaDetailsResponse> getPaanaDetailsWithSingleValue(int singleValue);

    @Query("Select * from paana_details where paana_no = :paanaNumber")
    PaanaDetailsResponse getPaanaDetailWithPaanaNumber(String paanaNumber);

    @Query("Select * from paana_details where single_value = :singleValue AND paana_type = :paanaType")
    List<PaanaDetailsResponse> getPaanaDetailsWithSingleValueAndPaanaType(int singleValue, String paanaType);
}
