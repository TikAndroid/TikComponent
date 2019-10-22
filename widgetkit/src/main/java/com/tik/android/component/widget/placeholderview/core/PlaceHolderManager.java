package com.tik.android.component.widget.placeholderview.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tik.android.component.widget.placeholderview.PlaceHolderView;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public class PlaceHolderManager implements IPlaceHolderManager {

    @NonNull private PlaceHolderLayout layout;
    @NonNull private PlaceHolderView.Config config;
    @NonNull private WrapperContext wrapperContext;

    public PlaceHolderManager(@NonNull PlaceHolderView.Config config, @NonNull WrapperContext wrapperContext) {
        this.config = config;
        this.wrapperContext = wrapperContext;
        initPlaceHolderLayout();
    }

    private void initPlaceHolderLayout() {
        Context context = wrapperContext.getContext();
        View oldContent = wrapperContext.getOldContent();
        ViewGroup parent = wrapperContext.getParent();
        layout = new PlaceHolderLayout(context);
        layout.addPlaceHolders(config.getPlaceHolders());
        layout.addView(oldContent);
        if (parent != null && oldContent != null) {
            ViewGroup.LayoutParams oldLayoutParams = oldContent.getLayoutParams();
            parent.addView(layout, wrapperContext.getChildIndex(), oldLayoutParams);
        }
    }

    @Override
    public void showPlaceHolder(@NonNull Class<? extends PlaceHolder> clz) {
        showPlaceHolder(clz, null);
    }

    @Override
    public void showPlaceHolder(@NonNull Class<? extends PlaceHolder> clz,@Nullable IExpose expose) {
        layout.showPlaceHolder(clz, expose);
    }

    @Override
    public void hidePlaceHolder() {
        layout.hidePlaceHolder();
    }

    @Override
    public void reset() {
        ViewGroup parent = wrapperContext.getParent();
        View oldContent = wrapperContext.getOldContent();
        if (parent != null) {
            parent.removeView(layout);
            parent.addView(oldContent, wrapperContext.getChildIndex());
        }
    }
}
