package com.pax.mvvm.rx;

import com.pax.mvvm.listener.RxEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * @author ligq
 * @date 2018/11/13 14:27
 */
public class RxEvent {
    private Map<String, Observable<?>> eventMap;
    private static RxEvent rxEvent;
    private CompositeDisposable mCompositeDisposable;
    private Map<String, DisposableObserver<?>> mDisposableObserverMap;

    private RxEvent() {
        eventMap = new HashMap<>();
        mDisposableObserverMap = new HashMap<>();
    }

    public static RxEvent getDefault() {
        synchronized (RxEvent.class) {
            if (rxEvent == null) {
                rxEvent = new RxEvent();
            }
            return rxEvent;
        }
    }

    public void post(String key, Observable<?> observable) {
        if (!eventMap.containsKey(key)) {
            eventMap.put(key, observable);
        }
    }

    @SuppressWarnings("RedundantCollectionOperation")
    private void unRegister(String key) {
        if (eventMap.containsKey(key)) {
            eventMap.remove(key);
        }
        deleteDisposable(mDisposableObserverMap.get(key));
        mDisposableObserverMap.remove(key);
    }

    private Observable<?> getEvent(String key) {
        return eventMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public void doEvent(final String key, final RxEventListener listener) {
        Observable<?> event = getEvent(key);
        if (event == null) {
            return;
        }
        BaseRxObservable<Object> disposable = event
                .compose(RxSchedulers.ioMain())
                .subscribeWith(new BaseRxObservable<Object>() {
                    @Override
                    protected void onSuccess(Object o) {
                        listener.onSuccess(key, o);
                        unRegister(key);
                    }
                });
        mDisposableObserverMap.put(key, disposable);
        addDisposable(disposable);
    }

    public void clear() {
        eventMap.clear();
        mDisposableObserverMap.clear();
        unDisposable();
    }

    private void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    private void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    private void deleteDisposable(Disposable disposable) {
        if (mCompositeDisposable != null && disposable != null) {
            mCompositeDisposable.delete(disposable);
        }
    }

    public <T> Observable<T> createEventData(T data) {
        return Observable.just(data);
    }
}
