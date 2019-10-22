package com.tik.android.component.widget.placeholderview;

import android.app.Activity;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tik.android.component.widget.placeholderview.core.PlaceHolderManager;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public interface IPlaceHolderView {

    /**
     * 支持绑定activity
     *
     * @param activity
     */
    @CheckResult
    @NonNull
    PlaceHolderManager bind(@NonNull Activity activity);

    /**
     * 支持绑定fragment
     *
     * @param fragment
     */
    @CheckResult
    @NonNull
    PlaceHolderManager bind(@NonNull Fragment fragment);

    /**
     * 支持绑定单个view
     *
     * @param root
     */
    @CheckResult
    @NonNull
    PlaceHolderManager bind(@NonNull View root);
}
