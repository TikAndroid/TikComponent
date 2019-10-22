package com.tik.android.component.widget.util;

import java.util.Map;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/27.
 */
public class Utils {


    /**
     * 通过value获取key值
     * @param map
     * @param value
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
