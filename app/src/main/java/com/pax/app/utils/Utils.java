package com.pax.app.utils;

import android.app.Application;

/**
 * @author ligq
 * @date 2018/11/8 14:03
 */
public class Utils {
    public static Application app;

    public static void init(Application application) {
        app = application;
    }

    public static Application getApp() {
        return app;
    }
}
