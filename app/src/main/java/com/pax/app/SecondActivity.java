package com.pax.app;

import android.util.Log;

import com.pax.app.db.User;
import com.pax.mvvm.base.BaseBindingActivity;
import com.pax.mvvm.listener.RxEventListener;
import com.pax.mvvm.rx.RxEvent;
import com.pax.mvvmtest.R;
import com.pax.mvvmtest.databinding.ActivitySecondBinding;

import java.util.List;

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

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(String key, Object object) {
        List<User> userList = (List<User>) object;
        users = object.toString();
        mDataBinding.setSecond(this);
        mDataBinding.setFirst(userList.get(0).firstName);
        mDataBinding.setLast(userList.get(0).lastName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEvent.getDefault().unDisposable();
    }
}
