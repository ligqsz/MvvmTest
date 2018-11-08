package com.pax.app;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.app.constant.Constants;
import com.pax.app.constant.States;
import com.pax.app.vm.LoginViewModel;
import com.pax.mvvmtest.LoginBinding;
import com.pax.mvvmtest.R;

/**
 * A login screen that offers login via email/password.
 *
 * @author ligq
 */
public class LoginActivity extends BaseActivity<LoginViewModel, LoginBinding> {
    public static final String TAG = "mvvm";
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

    @Override
    protected void showLoading() {
        mDataBinding.loginProgress.setVisibility(View.VISIBLE);
        mDataBinding.loginForm.setVisibility(View.GONE);
    }

    @Override
    protected void showError() {
        hideProgress();
        showToast("error");
    }

    @Override
    protected void showFailed() {
        hideProgress();
        showToast("failed");
        doQueryAll();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void showSuccess() {
        hideProgress();
        showToast("success");
        doQueryAll();
    }

    private void doQueryAll() {
        mViewModel.queryAll();
    }

    @Override
    protected void showElse(Integer integer) {
        hideProgress();
        if (integer == States.INVALID_PWD) {
            mDataBinding.password.requestFocus();
            mDataBinding.password.setError("invalid password!!!");
        } else if (integer == States.INVALID_USER) {
            mDataBinding.email.requestFocus();
            mDataBinding.email.setError("invalid email!!!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
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

