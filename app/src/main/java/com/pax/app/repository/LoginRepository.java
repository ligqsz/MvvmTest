package com.pax.app.repository;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.pax.app.constant.Constants;
import com.pax.app.constant.States;
import com.pax.app.db.BaseUserDb;
import com.pax.app.db.DbUtils;
import com.pax.app.db.User;
import com.pax.app.db.UserDao;
import com.pax.mvvm.base.BaseRepository;
import com.pax.mvvm.rx.BaseRxObservable;
import com.pax.mvvm.rx.RxEvent;
import com.pax.mvvm.rx.RxSchedulers;

import java.util.List;
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

        Observable<Integer> insertDb = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                BaseUserDb db = DbUtils.getDataBase(BaseUserDb.class, "test_db");
                User user = new User(username, pwd);
                UserDao userDao = db.userDao();
                if (userDao.findByName(username, pwd) == null) {
                    userDao.insertAll(user);
                }
                emitter.onComplete();
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

        addDisposable(Observable.concat(checkInfo, insertDb, login)
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
                        try {
                            if (Integer.parseInt(message) == States.INVALID_PWD) {
                                sendState(States.INVALID_PWD);
                            } else if (Integer.parseInt(message) == States.INVALID_USER) {
                                sendState(States.INVALID_USER);
                            }
                        } catch (Exception e) {
                            Log.e("LoginRepository", "accept: ", e);
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

    public void queryAll() {
        Observable<List<User>> testDb = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) {
                List<User> all = DbUtils.getDataBase(BaseUserDb.class, "test_db")
                        .userDao()
                        .getAll();
                emitter.onNext(all);
                emitter.onComplete();
            }
        })
                .compose(RxSchedulers.<List<User>>ioMain());
        RxEvent.getDefault().post("TEST_DB", testDb);
        addDisposable(testDb
                .subscribeWith(new BaseRxObservable<List<User>>() {
                    @Override
                    protected void onSuccess(List<User> users) {
                        for (User user : users) {
                            Log.i("queryAll", "onSuccess: " + user.toString());
                        }
                    }
                }));

    }
}
