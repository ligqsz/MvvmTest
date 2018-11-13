package com.pax.mvvm.rx;

import com.pax.mvvm.listener.RxEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author ligq
 * @date 2018/11/13 14:27
 */
@SuppressWarnings("TypeParameterExplicitlyExtendsObject")
public class RxEvent {
    private Map<String, Observable<? extends Object>> eventMap;
    private static RxEvent rxEvent;
    private CompositeDisposable mCompositeDisposable;

    private RxEvent() {
        eventMap = new HashMap<>();
    }

    public static RxEvent getDefault() {
        synchronized (RxEvent.class) {
            if (rxEvent == null) {
                rxEvent = new RxEvent();
            }
            return rxEvent;
        }
    }

    public void post(String key, Observable<? extends Object> observable) {
        if (!eventMap.containsKey(key)) {
            eventMap.put(key, observable);
        }
    }

    @SuppressWarnings("RedundantCollectionOperation")
    private void unRegister(String key) {
        if (eventMap.containsKey(key)) {
            eventMap.remove(key);
        }
    }

    private Observable<? extends Object> getEvent(String key) {
        return eventMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public void doEvent(final String key, final RxEventListener listener) {
        Observable<? extends Object> event = getEvent(key);
        if (event == null) {
            return;
        }
        addDisposable(event
                .compose(RxSchedulers.ioMain())
                .subscribeWith(new BaseRxObservable<Object>() {
                    @Override
                    protected void onSuccess(Object o) {
                        listener.onSuccess(key, o);
                        unRegister(key);
                    }
                }));
    }

    public void clear() {
        eventMap.clear();
    }

    private void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
