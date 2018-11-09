package com.pax.app.db;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.pax.mvvm.utils.MvvmUtils;

/**
 * @author ligq
 * @date 2018/11/8 17:54
 */
public class DbUtils {
    public static <T extends RoomDatabase> T getDataBase(Class<T> clazz, String dbName) {
        return Room.databaseBuilder(MvvmUtils.getApp(), clazz, dbName).build();
    }
}
