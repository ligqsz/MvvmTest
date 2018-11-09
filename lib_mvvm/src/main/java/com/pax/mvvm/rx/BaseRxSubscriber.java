package com.pax.mvvm.rx;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author ligq
 * @date 2018/11/7 14:54
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseRxSubscriber<T> extends DisposableSubscriber<T> {

    @Override
    protected void onStart() {
        super.onStart();
        showLoading();
    }

    private void showLoading() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
