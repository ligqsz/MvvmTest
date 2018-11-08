package com.pax.app.utils;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;


/**
 * @author ligq
 */
@SuppressWarnings({"unchecked", "unused"})
public class TUtil {
    public static <T> T getNewInstance(Object object, int i) {
        if(object!=null){
            try {
                return ((Class<T>) ((ParameterizedType) (Objects.requireNonNull(object.getClass()
                        .getGenericSuperclass()))).getActualTypeArguments()[i])
                        .newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }

        }
        return null;

    }

    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            return (T) ((ParameterizedType) Objects.requireNonNull(object.getClass()
                    .getGenericSuperclass()))
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
