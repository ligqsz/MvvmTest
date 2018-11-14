package com.pax.app;

import android.util.Log;
import android.widget.Toast;

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
    private static final String TAG = SecondActivity.class.getSimpleName();
    public String users;

    @Override
    protected void init() {
        RxEvent.getDefault().doEvent("TEST_DB", this);
        RxEvent.getDefault().doEvent("TEST_DATA", this);
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
        Log.d(TAG, "onSuccess: key=" + key);
        if ("TEST_DB".equals(key)) {
            List<User> userList = (List<User>) object;
            users = object.toString();
            mDataBinding.setSecond(this);
            mDataBinding.setFirst(userList.get(0).firstName);
            mDataBinding.setLast(userList.get(0).lastName);
        } else if ("TEST_DATA".equals(key)) {
            Toast.makeText(this, object.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
