package com.pax.app.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * @author ligq
 * @date 2018/11/8 13:56
 */
@Database(entities = {User.class}, version = 1)
public abstract class BaseDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
