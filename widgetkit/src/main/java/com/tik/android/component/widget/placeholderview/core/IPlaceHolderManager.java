package com.tik.android.component.widget.placeholderview.core;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public interface IPlaceHolderManager {

    public interface IExpose {
        void expose(@NonNull View placeHolder);
    }

    /**
     * 显示单个PlaceHolder
     *
     * @param clz
     */
    @MainThread
    public void showPlaceHolder(@NonNull Class<? extends PlaceHolder> clz);


    /**
     * 暴露接口，用于外部点击事件的传递
     *
     * @param clz
     * @param expose
     */
    @MainThread
    public void showPlaceHolder(@NonNull Class<? extends PlaceHolder> clz, @Nullable IExpose expose);

    /**
     * 隐藏整个PlaceHolderLayout
     */
    @MainThread
    public void hidePlaceHolder();

    /**
     * 恢复之前的布局层级，
     * 把增加的PlaceHolderLayout 给删除掉
     * 调用这个方法后， 就不能再次showPlaceHolder了
     */
    @MainThread
    public void reset();

}
