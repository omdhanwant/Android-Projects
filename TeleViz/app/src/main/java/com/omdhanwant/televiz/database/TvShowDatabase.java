package com.omdhanwant.televiz.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.omdhanwant.televiz.dao.TvShowDao;
import com.omdhanwant.televiz.models.TvShow;

@Database(entities = TvShow.class, version = 1, exportSchema = false)
public abstract class TvShowDatabase extends RoomDatabase {
    private static TvShowDatabase tvShowDatabase;

    public static synchronized TvShowDatabase  getTvShowDatabase(Context context) {
        if(tvShowDatabase == null) {
            tvShowDatabase = Room.databaseBuilder(
                    context,
                    TvShowDatabase.class,
                    "tv_show_db"
            ).build();
        }

        return tvShowDatabase;
    }

    public abstract TvShowDao tvShowDao();
}
