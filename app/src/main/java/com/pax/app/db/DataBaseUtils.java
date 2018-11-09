package com.pax.app.db;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.pax.app.utils.Utils;

/**
 * @author ligq
 * @date 2018/11/8 17:54
 */
public class DataBaseUtils {
    public static <T extends RoomDatabase> T getDataBase(Class<T> clazz, String dbName) {
        return Room.databaseBuilder(Utils.getApp(), clazz, dbName).build();
    }
}
