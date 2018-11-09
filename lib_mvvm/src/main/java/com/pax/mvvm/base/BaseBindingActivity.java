package com.pax.mvvm.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author ligq
 * @date 2018/11/9 11:24
 */
public abstract class BaseBindingActivity<B extends ViewDataBinding> extends AppCompatActivity {
    protected B mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    /**
     * layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();
}
