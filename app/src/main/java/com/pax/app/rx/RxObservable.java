package com.pax.app.rx;

import io.reactivex.observers.DisposableObserver;

/**
 * @author ligq
 * @date 2018/11/7 14:54
 */
public abstract class RxObservable<T> extends DisposableObserver<T> {

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
