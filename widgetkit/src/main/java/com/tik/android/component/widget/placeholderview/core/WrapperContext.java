package com.tik.android.component.widget.placeholderview.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public class WrapperContext {

    private Context context;
    /**
     * 填充占位符的view的父布局
     */
    private ViewGroup parent;
    /**
     * 需要填充占位符的view
     */
    private View oldContent;
    /**
     * 在父布局中的index
     */
    private int childIndex;

    public WrapperContext(@NonNull Context context, @Nullable ViewGroup parent, @Nullable View oldContent, int childIndex) {
        this.context = context;
        this.parent = parent;
        this.oldContent = oldContent;
        this.childIndex = childIndex;
    }

    @NonNull public Context getContext() {
        return context;
    }

    @Nullable  public ViewGroup getParent() {
        return parent;
    }

    @Nullable public View getOldContent() {
        return oldContent;
    }

    public int getChildIndex() {
        return childIndex;
    }
}
