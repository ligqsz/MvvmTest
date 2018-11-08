package com.pax.app.repository;

import android.os.SystemClock;
import android.text.TextUtils;

import com.pax.app.constant.Constants;
import com.pax.app.constant.States;
import com.pax.app.rx.RxSchedulers;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author ligq
 * @date 2018/11/7 14:06
 */
public class LoginRepository extends BaseRepository {
    public void doLogin(final String username, final String pwd) {
        Observable<Integer> checkInfo = Observable.create(
                new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) {
                        if (TextUtils.isEmpty(username) || checkIf(username)) {
                            emitter.onError(new Throwable(String.valueOf(States.INVALID_USER)));
                        } else if (TextUtils.isEmpty(pwd) || checkIf(pwd)) {
                            emitter.onError(new Throwable(String.valueOf(States.INVALID_PWD)));
                        } else {
                            emitter.onComplete();
                        }
                    }
                });

        Observable<Integer> login = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                SystemClock.sleep(2000);
                int i = new Random().nextInt(10);
                emitter.onNext(i % 2 == 0 ? States.SUCCESS : States.FAILED);
                emitter.onComplete();
            }
        });

//        addDisposable(Observable.concat(checkInfo, login)
//                .compose(RxSchedulers.<Integer>ioMain())
//                .subscribeWith(new RxObservable<Integer>() {
//                    @Override
//                    protected void onSuccess(Integer integer) {
//                        //...
//                    }
//                }));

        addDisposable(Observable.concat(checkInfo, login)
                .compose(RxSchedulers.<Integer>ioMain())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        sendState(States.LOADING);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        sendState(integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        String message = throwable.getMessage();
                        if (Integer.parseInt(message) == States.INVALID_PWD) {
                            sendState(States.INVALID_PWD);
                        } else if (Integer.parseInt(message) == States.INVALID_USER) {
                            sendState(States.INVALID_USER);
                        } else {
                            sendState(States.ERROR);
                        }

                    }
                }));
    }

    private boolean checkIf(String str) {
        return str.length() >= 10 || str.length() <= 4;
    }

    private void sendState(Integer integer) {
        sendData(Constants.EVENT_KEY_LOGIN_STATE, integer);
    }
}
