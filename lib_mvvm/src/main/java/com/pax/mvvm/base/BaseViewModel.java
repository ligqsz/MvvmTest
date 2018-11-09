package com.pax.mvvm.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.pax.mvvm.utils.TUtil;

/**
 * @author ligq
 * @date 2018/11/7 13:42
 */
public class BaseViewModel<T extends BaseRepository> extends AndroidViewModel {
    public T mRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = TUtil.getNewInstance(this, 0);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepository != null) {
            mRepository.unDisposable();
        }
    }
}
