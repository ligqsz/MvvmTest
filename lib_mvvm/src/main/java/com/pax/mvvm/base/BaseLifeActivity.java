package com.pax.mvvm.base;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pax.mvvm.constants.Const;
import com.pax.mvvm.event.LiveBus;
import com.pax.mvvm.utils.TUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ligq
 * @date 2018/11/7 13:51
 */
public abstract class BaseLifeActivity<V extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = "test";
    protected V mViewModel;
    protected B mDataBinding;
    protected List<Object> events;
    protected Observer observer;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        events = new ArrayList<>();
        mViewModel = vmProviders(this, (Class<V>) TUtil.getInstance(this, 0));
        observer = getObserver();
        Log.i(TAG, "onCreate: mViewModel=" + mViewModel);
        if (mViewModel != null && observer != null) {
            Object stateEventKey = getStateEventKey();
            String stateEventTag = getStateEventTag();
            events.add(stateEventKey + stateEventTag);
            LiveBus.getDefault().subscribe(stateEventKey, stateEventTag).observe(this, observer);
        }
    }

    /**
     * get observer
     *
     * @return observer
     */
    protected abstract Observer getObserver();

    /**
     * layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    protected String getStateEventTag() {
        return Const.STR_EMPTY;
    }

    protected Object getStateEventKey() {
        return getClass().getSimpleName();
    }


    @SuppressWarnings("unchecked")
    protected V vmProviders(AppCompatActivity activity, @NonNull Class modelClass) {
        return (V) ViewModelProviders.of(activity).get(modelClass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!events.isEmpty()) {
            for (Object event : events) {
                LiveBus.getDefault().clear(event);
            }
        }
    }
}
