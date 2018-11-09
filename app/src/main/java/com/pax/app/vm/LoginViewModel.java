package com.pax.app.vm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.pax.app.repository.LoginRepository;
import com.pax.mvvm.base.BaseViewModel;

/**
 * @author ligq
 * @date 2018/11/7 14:04
 */
public class LoginViewModel extends BaseViewModel<LoginRepository> {
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String username, String password) {
        mRepository.doLogin(username, password);
    }

    public void queryAll() {
        mRepository.queryAll();
    }
}
