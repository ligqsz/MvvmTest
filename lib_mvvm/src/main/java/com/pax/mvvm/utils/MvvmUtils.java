package com.pax.mvvm.utils;

import android.app.Application;

/**
 * @author ligq
 * @date 2018/11/9 10:46
 */
public class MvvmUtils {
    public static Application app;

    public static void init(Application application) {
        app = application;
    }

    public static Application getApp() {
        return app;
    }
}
