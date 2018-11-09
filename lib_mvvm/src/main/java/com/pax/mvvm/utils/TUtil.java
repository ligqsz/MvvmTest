package com.pax.mvvm.utils;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;


/**
 * @author ligq
 */
@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public class TUtil {
    /**
     * getInstance by object
     *
     * @param object obj
     * @param i      i
     * @param <T>    T
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
     * getInstance by object
     *
     * @param object obj
     * @param i      i
     * @param <T>    T
     * @return T class not instance
     */
    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            return (T) ((ParameterizedType) Objects.requireNonNull(object.getClass().getGenericSuperclass()))
                    .getActualTypeArguments()[i];
        }
        return null;

    }

    @NonNull
    public static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
