package com.tik.android.component.widget.placeholderview.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/26.
 */
public class WrapperContextTransformer {

    @NonNull
    public WrapperContext build(@NonNull Activity activity) {
        ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        int childIndex = 0;
        View oldContent = null;
        if (contentParent != null) {
            oldContent = contentParent.getChildAt(0);
            contentParent.removeView(oldContent);
        }
        return new WrapperContext(activity, contentParent, oldContent, childIndex);
    }

    @NonNull public WrapperContext build(@NonNull View view) {
        Context context = view.getContext();
        ViewGroup contentParent = (ViewGroup) view.getParent();
        int childIndex = 0, childCount = 0;
        if (contentParent != null) {
            childCount = contentParent.getChildCount();
        }
        for (int i = 0; i < childCount; i++) {
            if (contentParent.getChildAt(i) == view) {
                childIndex = i;
                break;
            }
        }
        if (contentParent != null) {
            contentParent.removeView(view);
        }
        return new WrapperContext(context, contentParent, view, childIndex);
    }
}
