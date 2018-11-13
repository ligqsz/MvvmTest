package com.pax.app;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.app.constant.Constants;
import com.pax.app.constant.States;
import com.pax.app.vm.LoginViewModel;
import com.pax.mvvm.base.BaseLifeActivity;
import com.pax.mvvmtest.LoginBinding;
import com.pax.mvvmtest.R;

/**
 * A login screen that offers login via email/password.
 *
 * @author ligq
 */
public class LoginActivity extends BaseLifeActivity<LoginViewModel, LoginBinding> {
    public static final String TAG = "mvvm";
    public String username;
    public String password;
    public TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                Log.d(TAG, "onEditorAction");
                doLogin();
                return true;
            }
            return false;
        }
    };

    protected void showLoading() {
        mDataBinding.loginProgress.setVisibility(View.VISIBLE);
        mDataBinding.loginForm.setVisibility(View.GONE);
    }

    protected void showError() {
        hideProgress();
        showToast("error");
    }

    protected void showFailed() {
        hideProgress();
        showToast("failed");
        doQueryAll();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showSuccess() {
        doQueryAll();
        hideProgress();
        startActivity(new Intent(this, SecondActivity.class));
    }

    private void doQueryAll() {
        mViewModel.queryAll();
    }

    protected void showElse(Integer integer) {
        hideProgress();
        if (integer == States.INVALID_PWD) {
            mDataBinding.password.requestFocus();
            mDataBinding.password.setError("invalid password!!!");
        } else if (integer == States.INVALID_USER) {
            mDataBinding.email.requestFocus();
            mDataBinding.email.setError("invalid email!!!");
        }

        mDataBinding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected Observer getObserver() {
        return new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) {
                    showError();
                    return;
                }
                if (States.SUCCESS == integer) {
                    showSuccess();
                } else if (integer == States.FAILED) {
                    showFailed();
                } else if (integer == States.LOADING) {
                    showLoading();
                } else if (integer != States.ERROR) {
                    showElse(integer);
                } else {
                    showError();
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected Object getStateEventKey() {
        return Constants.EVENT_KEY_LOGIN_STATE;
    }

    private void initView() {
        mDataBinding.password.setOnEditorActionListener(onEditorActionListener);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.email_sign_in_button) {
            doLogin();
        }
    }

    private void doLogin() {
        mViewModel.login(mDataBinding.email.getText().toString(), mDataBinding.password.getText().toString());
    }

    private void hideProgress() {
        mDataBinding.loginProgress.setVisibility(View.GONE);
        mDataBinding.loginForm.setVisibility(View.VISIBLE);
    }
}

