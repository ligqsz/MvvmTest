package com.pax.app;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pax.app.constant.States;
import com.pax.app.event.LiveBus;
import com.pax.app.utils.TUtil;
import com.pax.app.vm.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ligq
 * @date 2018/11/7 13:51
 */
public abstract class BaseActivity<T extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = "test";
    protected T mViewModel;
    protected B mDataBinding;
    protected List<Object> events;
    protected Observer observer = new Observer<Integer>() {
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

    protected abstract void showLoading();

    @SuppressWarnings("unused")
    protected void showElse(Integer integer) {

    }

    protected abstract void showError();

    protected abstract void showFailed();

    protected abstract void showSuccess();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        events = new ArrayList<>();
        mViewModel = vmProviders(this, (Class<T>) TUtil.getInstance(this, 0));
        Log.i(TAG, "onCreate: mViewModel=" + mViewModel);
        if (mViewModel != null) {
            Object stateEventKey = getStateEventKey();
            String stateEventTag = getStateEventTag();
            events.add(stateEventKey + stateEventTag);
            LiveBus.getDefault().subscribe(stateEventKey, stateEventTag).observe(this, observer);
        }
    }

    protected abstract int getLayoutId();

    protected String getStateEventTag() {
        return "";
    }

    protected abstract Object getStateEventKey();


    @SuppressWarnings("unchecked")
    protected T vmProviders(AppCompatActivity activity, @NonNull Class modelClass) {
        return (T) ViewModelProviders.of(activity).get(modelClass);
    }
}
