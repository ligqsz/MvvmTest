package com.pax.app.utils;

import android.support.annotation.NonNull;

import com.pax.app.vm.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;


/**
 * @author ligq
 */
@SuppressWarnings({"unchecked", "unused"})
public class TUtil {
    /**
     * getInstance by object{@link com.pax.app.vm.BaseViewModel}
     *
     * @param object obj{@link com.pax.app.vm.BaseViewModel}
     * @param i      i
     * @param <T>    {@link BaseViewModel#mRepository}
     * @return T instance not class
     */
    public static <T> T getNewInstance(Object object, int i) {
        if (object != null) {
            try {
                return ((Class<T>) getInstance(object, i)).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }

        }
        return null;

    }

    /**
     * getInstance by object{@link com.pax.app.vm.BaseViewModel}
     *
     * @param object obj{@link com.pax.app.vm.BaseViewModel}
     * @param i      i
     * @param <T>    {@link BaseViewModel#mRepository}
     * @return T class not instance
     */
    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            return (T) ((ParameterizedType) Objects.requireNonNull(object.getClass().getGenericSuperclass()))
                    .getActualTypeArguments()[i];
        }
        return null;

    }

    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
