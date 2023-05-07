package com.pinnacle.winwin.data.local.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.data.local.dao.AmountDetailsDao;
import com.pinnacle.winwin.data.local.dao.BaazaarDetailsDao;
import com.pinnacle.winwin.data.local.dao.CPDetailsDao;
import com.pinnacle.winwin.data.local.dao.ChartDetailsDao;
import com.pinnacle.winwin.data.local.dao.CustomGameAmountDetailsDao;
import com.pinnacle.winwin.data.local.dao.CustomGamesDao;
import com.pinnacle.winwin.data.local.dao.GameDetailsDao;
import com.pinnacle.winwin.data.local.dao.MotorCombDetailsDao;
import com.pinnacle.winwin.data.local.dao.PaanaDetailsDao;
import com.pinnacle.winwin.network.model.AmountDetailsResponse;
import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.network.model.CPDetailsResponse;
import com.pinnacle.winwin.network.model.ChartDetailsResponse;
import com.pinnacle.winwin.network.model.CustomGameAmountResponse;
import com.pinnacle.winwin.network.model.CustomGamesResponse;
import com.pinnacle.winwin.network.model.GameDetailsResponse;
import com.pinnacle.winwin.network.model.MotorCombDetailsResponse;
import com.pinnacle.winwin.network.model.PaanaDetailsResponse;

@Database(entities = {BazaarDetailsResponse.class, GameDetailsResponse.class,
        AmountDetailsResponse.class, PaanaDetailsResponse.class, CPDetailsResponse.class,
        MotorCombDetailsResponse.class, ChartDetailsResponse.class, CustomGamesResponse.class,
        CustomGameAmountResponse.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BaazaarDetailsDao baazaarDetailsDao();
    public abstract GameDetailsDao gameDetailsDao();
    public abstract AmountDetailsDao amountDetailsDao();
    public abstract PaanaDetailsDao paanaDetailsDao();
    public abstract CPDetailsDao cpDetailsDao();
    public abstract MotorCombDetailsDao motorCombDetailsDao();
    public abstract ChartDetailsDao chartDetailsDao();
    public abstract CustomGamesDao customGamesDao();
    public abstract CustomGameAmountDetailsDao customGameAmountDetailsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppConstant.APP_DB_NAME)
                            .allowMainThreadQueries()
                            /*.addMigrations(MIGRATION_1_2)*/
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    /*private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE game_details ADD COLUMN imageResourceName TEXT");
        }
    };*/
}
