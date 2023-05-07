package com.pinnacle.winwin.data.local.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinnacle.winwin.network.model.AmountDetailsResponse;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromAmountDetails(List<AmountDetailsResponse> amountDetails) {
        if (amountDetails == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AmountDetailsResponse>>() {}.getType();
        return gson.toJson(amountDetails, type);
    }

    @TypeConverter
    public List<AmountDetailsResponse> toAmountDetails(String amountDetailsString) {
        if (amountDetailsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AmountDetailsResponse>>() {}.getType();
        return gson.fromJson(amountDetailsString, type);
    }

    @TypeConverter
    public String fromGameMap(List<Integer> gameMap) {
        if (gameMap == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.toJson(gameMap, type);
    }

    @TypeConverter
    public List<Integer> toGameMap(String gameMapString) {
        if (gameMapString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(gameMapString, type);
    }
}
