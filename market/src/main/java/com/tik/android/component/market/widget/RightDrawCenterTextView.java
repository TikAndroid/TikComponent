package com.tik.android.component.market.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * 文字和drawableRight一起居中的TextView
 * ******gravity设置为right时才会生效
 *
 * Created by jianglixuan on 2018/11/16
 */
public class RightDrawCenterTextView extends android.support.v7.widget.AppCompatTextView {
    public RightDrawCenterTextView(Context context) {
        this(context, null);
    }

    public RightDrawCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable = drawables[2];
            if (drawable != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawable.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((bodyWidth - getWidth()) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }

}
