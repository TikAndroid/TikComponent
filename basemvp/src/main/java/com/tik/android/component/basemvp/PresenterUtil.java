package com.tik.android.component.basemvp;

import com.tik.android.component.libcommon.ReflectUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class PresenterUtil {

    public static boolean isParameterizedType(Class cls) {
        if (cls == null) return false;
        Type type = cls.getGenericSuperclass();
        return type != null ? ParameterizedType.class.isAssignableFrom(type.getClass()) : false;
    }

    public static <T> T getBasePresenter(Class cls) {
        if (isParameterizedType(cls)) {
            try {
                Type[] types = ((ParameterizedType) cls.getGenericSuperclass()).getActualTypeArguments();
                if (types != null && types.length > 0) {
                    Class<T> clazz = (Class<T>) ReflectUtil.getClass(types[0]);
                    if (clazz == null) return null;
                    if (clazz != null && BasePresenter.class.isAssignableFrom(clazz)) {
                        return clazz.newInstance();
                    } else {
                        throw new Exception("需要设置对应的presenter");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            if (cls == null || !BaseView.class.isAssignableFrom(cls)) return null;
            return getBasePresenter(cls.getSuperclass());
        }
        return null;
    }

}
