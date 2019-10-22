package com.tik.android.component.widget.placeholderview.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tik.android.component.widget.placeholderview.PlaceHolderView;
import com.tik.android.component.widget.util.Utils;

import java.util.Collection;
import java.util.HashMap;

/**
 * @describe : 这个ViewGroup的作用是
 *                  1. 第0个child是目标view, 第1个以后child是占位view : loading view, empty view啥啥啥的
 *                  2. 实现了detach监听，会自动释放资源，不需要手动调用
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public class PlaceHolderLayout extends FrameLayout {

    @NonNull private HashMap<View, PlaceHolder> children = new HashMap<>();
    @NonNull private OnAttachStateChangeListener listener = new OnAttachStateChangeListener() {

        /**
         * when call {@link PlaceHolderView#bind(View)} , the below will be work
         * @param v
         */
        @Override
        public void onViewAttachedToWindow(View v) {

        }

        /**
         * when call {@link IPlaceHolderManager#hidePlaceHolder()} , the below will be work
         * @param v
         */
        @Override
        public void onViewDetachedFromWindow(View v) {
            releaseAllRegisteredPlaceHolders();
        }
    };

    public PlaceHolderLayout(Context context) {
        this(context, null);
    }

    public PlaceHolderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaceHolderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addOnAttachStateChangeListener(listener);
    }

    public void addPlaceHolders(@NonNull Collection<Class<? extends PlaceHolder>> collection) {
        try {
            for (Class<? extends PlaceHolder> clz : collection) {
                PlaceHolder holder = clz.newInstance();
                holder.setContext(getContext());
                View child = holder.getPlaceHolder();
                children.put(child, holder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlaceHolder(@NonNull Class<? extends PlaceHolder> clz, @Nullable IPlaceHolderManager.IExpose expose) {
        hidePlaceHolder();
        for (PlaceHolder holder : children.values()) {
            if (holder.getClass().equals(clz)) {
                View child = Utils.getKeyByValue(children, holder);
                if (child != null) {
                    addView(child);
                    notifyChildAttach(holder, child);
                    if (expose != null) {
                        expose.expose(child);
                    }
                }
            }
        }
    }

    public void hidePlaceHolder() {
        int sum = getChildCount();
        if (sum > 1) {
            for (int i = 1; i < sum; i++) {
                View child = getChildAt(i);
                notifyChildDetach(child);
                removeView(child);
            }
        }
    }

    private void releaseAllRegisteredPlaceHolders() {
        if (!children.isEmpty()) {
            for (View child : children.keySet()) {
                notifyChildDetach(child);
                removeView(child);
            }
        }

    }

    private void notifyChildAttach(PlaceHolder holder, View child) {
        assureChildDetach(child);
        children.put(child, holder);
        holder.onAttach();
    }

    private void notifyChildDetach(View child) {
        if (child != null && children.containsKey(child)) {
            PlaceHolder holder = children.get(child);
            if (holder != null) {
                holder.onDetach();
            }
        }
    }

    private void assureChildDetach(View child) {
        if (children.containsKey(child)) {
            notifyChildDetach(child);
        }
    }
}
