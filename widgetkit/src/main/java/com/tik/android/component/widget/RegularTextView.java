package com.tik.android.component.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RegularTextView extends android.support.v7.widget.AppCompatTextView {
    public RegularTextView(Context context) {
        super(context);
        setFont();
    }
    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public RegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
