package com.pax.mvvm.http;


import android.support.annotation.NonNull;

import com.pax.mvvm.constants.Const;
import com.pax.mvvm.utils.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author ligq
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private static final String TAG = HttpLogger.class.getSimpleName();

    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(@NonNull String message) {
        if (message.startsWith(Const.STRING_POST)) {
            mMessage.setLength(0);
        }
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = Logger.formatJson(message);
        }
        mMessage.append(message.concat("\n"));
        if (message.startsWith(Const.STRING_END_HTTP)) {
            Logger.e(TAG, mMessage.toString());
        }
    }
}