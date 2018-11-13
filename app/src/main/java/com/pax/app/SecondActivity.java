package com.pax.app;

import android.util.Log;

import com.pax.mvvm.base.BaseBindingActivity;
import com.pax.mvvm.listener.RxEventListener;
import com.pax.mvvm.rx.RxEvent;
import com.pax.mvvmtest.R;
import com.pax.mvvmtest.databinding.ActivitySecondBinding;

/**
 * @author ligq
 */
public class SecondActivity extends BaseBindingActivity<ActivitySecondBinding> implements RxEventListener {
    public String users;

    @Override
    protected void init() {
        RxEvent.getDefault().doEvent("TEST_DB", this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }

    public void testClick() {
        Log.e("Second", "testClick: ");
    }

    @Override
    public void onSuccess(String key, Object object) {
        users = object.toString();
        mDataBinding.setSecond(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEvent.getDefault().unDisposable();
    }
}
