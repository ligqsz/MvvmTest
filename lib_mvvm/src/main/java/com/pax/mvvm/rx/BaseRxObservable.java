package com.pax.mvvm.rx;

import android.util.Log;

import io.reactivex.observers.DisposableObserver;

/**
 * @author ligq
 * @date 2018/11/7 14:54
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseRxObservable<T> extends DisposableObserver<T> {
    private static final String TAG = BaseRxObservable.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onNext(T t) {
        Log.d(TAG, "onNext: ");
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, "onError: ", t);
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: ");
    }
}
