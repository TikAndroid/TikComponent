package com.tik.android.component.libcommon.sharedpreferences;

import android.content.Context;

import org.junit.Test;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/6.
 */
public class SharedPreferTest {
    @Test
    public void edit(Context context) throws Exception{
        RxSharedPrefer
                .builder()
                .context(context)
                .build()
                .edit()
                .putBoolean("aa", Boolean.TRUE)
                .apply();
    }
}
