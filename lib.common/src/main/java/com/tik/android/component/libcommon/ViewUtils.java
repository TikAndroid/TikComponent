package com.tik.android.component.libcommon;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/12/7.
 */
public class ViewUtils {

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void measure(View view, int maxWidth, int maxHeight) {
        int wSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST);
        int hSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST);
        view.measure(wSpec, hSpec);
    }

    public static void measureWithWindowSize(Context context, View view) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int wSpec = View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.AT_MOST);
        int hSpec = View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.AT_MOST);
        view.measure(wSpec, hSpec);
    }

    public static void requestFocus(View view) {
        if (view != null && view.isFocusable()) {
            view.requestFocus();
            if (view instanceof EditText) {
                EditText et = (EditText) view;
                et.setSelection(et.length());
            }
        }
    }
}
