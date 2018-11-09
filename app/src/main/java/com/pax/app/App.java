package com.pax.app;

import android.app.Application;

import com.pax.app.utils.Utils;
import com.pax.mvvm.utils.MvvmUtils;

/**
 * @author ligq
 * @date 2018/11/8 14:03
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        MvvmUtils.init(this);
    }
}
