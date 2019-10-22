package com.tik.android.component.widget.placeholderview.core;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public abstract class PlaceHolder implements Serializable {

    private static final String TAG = PlaceHolder.class.getName();

    private View placeHolder;
    private Context context;

    @NonNull public View getPlaceHolder() {
        @LayoutRes int resId = onCreateView();
        if (placeHolder == null) {
            if (context == null) {
                Log.e(TAG, "call setContext() before getPlaceHolder()");
            }
            placeHolder = View.inflate(context, resId, null);
        }
        onViewCreate(context, placeHolder);
        return placeHolder;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @return PlaceHolder Layout Id
     */
    @CheckResult
    @LayoutRes
    public abstract int onCreateView();

    /**
     * 可以初始化PlaceHolder内部的一些控件
     * @param context
     * @param view 传入的 PlaceHolder Layout
     */
    public abstract void onViewCreate(Context context, View view);

    public abstract void onAttach();

    /**
     * 释放所有资源
     */
    public abstract void onDetach();
}
