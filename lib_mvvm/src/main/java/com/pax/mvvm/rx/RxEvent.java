package com.pax.mvvm.rx;

import com.pax.mvvm.listener.RxEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * @author ligq
 * @date 2018/11/13 14:27
 */
public class RxEvent {
    private Map<String, Observable<?>> eventMap;
    private static RxEvent rxEvent;
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
        if (observable == null) {
            observable = createEventData(0);
        }
        if (!eventMap.containsKey(key)) {
            eventMap.put(key, observable);
        }
    }

    @SuppressWarnings("RedundantCollectionOperation")
    private void unRegister(String key) {
        if (eventMap.containsKey(key)) {
            eventMap.remove(key);
        }
        disposableMap(key);
    }

    private void disposableMap(String key) {
        DisposableObserver<?> disposableObserver = mDisposableObserverMap.get(key);
        if (disposableObserver != null) {
            if (!disposableObserver.isDisposed()) {
                disposableObserver.dispose();
            }
            mDisposableObserverMap.remove(key);
        }
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
    }

    public void clear() {
        eventMap.clear();
        mDisposableObserverMap.clear();
    }


    public <T> Observable<T> createEventData(T data) {
        return Observable.just(data);
    }
}
