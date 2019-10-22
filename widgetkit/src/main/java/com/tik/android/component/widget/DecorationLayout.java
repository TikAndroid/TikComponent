package com.tik.android.component.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tik.android.component.widgetkit.R;


public class DecorationLayout extends RelativeLayout {

    public static final int DECOR_ALIGN_START = -1;
    public static final int DECOR_ALIGN_END = -2;

    private int mInitPaddingStart;
    private int mInitPaddingEnd;

    private int mLastStartChild = 0;
    private int mLastEndChild = 0;

    private OnLayoutChangeListener mChildLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            setBaseViewPadding();
        }
    };

    public DecorationLayout(Context context) {
        this(context, null);
    }

    public DecorationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DecorationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        View baseView = getChildAt(0);
        int fixHeightSpec = MeasureSpec.makeMeasureSpec(baseView.getMeasuredHeight(), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, fixHeightSpec);
    }

    private int getViewWidth(View view) {
        int width = view.getMeasuredWidth();
        if (view.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
            width += params.leftMargin + params.rightMargin;
        }

        return width;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!(params instanceof LayoutParams)) {
            params = generateLayoutParams(params);
        }

        addViewInner(child, (LayoutParams) params);
    }

    private void addViewInner(View child, LayoutParams params) {
        int index = getChildCount();
        if (index == 0) {
            mLastStartChild = mLastEndChild = 0;
            mInitPaddingStart = child.getPaddingStart();
            mInitPaddingEnd = child.getPaddingEnd();
        } else {
            switch (params.decorAlign) {
                case DECOR_ALIGN_START:
                    if (mLastStartChild == 0) {
                        params.addRule(ALIGN_START, getChildAt(mLastStartChild).getId());
                    } else {
                        params.addRule(END_OF, getChildAt(mLastStartChild).getId());
                    }
                    mLastStartChild = index;
                    break;
                case DECOR_ALIGN_END:
                    if (mLastEndChild == 0) {
                        params.addRule(ALIGN_END, getChildAt(mLastEndChild).getId());
                    } else {
                        params.addRule(START_OF, getChildAt(mLastEndChild).getId());
                    }
                    mLastEndChild = index;
                    break;
            }

            child.setClickable(true);
            child.addOnLayoutChangeListener(mChildLayoutChangeListener);
        }


        if (child.getId() == NO_ID) {
            child.setId(generateViewId());
        }
        super.addView(child, index, params);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        view.removeOnLayoutChangeListener(mChildLayoutChangeListener);
    }

    @Override
    public void removeViewInLayout(View view) {
        super.removeViewInLayout(view);
        view.removeOnLayoutChangeListener(mChildLayoutChangeListener);
    }

    @Override
    public void removeViewAt(int index) {
        View view = getChildAt(index);
        if (view != null) {
            view.removeOnLayoutChangeListener(mChildLayoutChangeListener);
        }
        super.removeViewAt(index);
    }

    private void setBaseViewPadding() {
        View baseView = getChildAt(0);
        int leftPadding = mInitPaddingStart;
        int rightPadding = mInitPaddingEnd;
        for (int i = 1; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (params.decorAlign == DECOR_ALIGN_START) {
                leftPadding += getViewWidth(child);
            } else if (params.decorAlign == DECOR_ALIGN_END) {
                rightPadding += getViewWidth(child);
            }
        }

        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) { // RTL布局则交换padding
            leftPadding = leftPadding ^ rightPadding;
            rightPadding = leftPadding ^ rightPadding;
            leftPadding = leftPadding ^ rightPadding;
        }

        if (leftPadding != baseView.getPaddingLeft() || rightPadding != baseView.getPaddingRight()) {
            baseView.setPadding(leftPadding, baseView.getPaddingTop(), rightPadding, baseView.getPaddingBottom());
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(super.generateLayoutParams(lp));
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {
        // 默认对其左边
        public int decorAlign = DECOR_ALIGN_START;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.DecorationLayout);
            decorAlign = array.getInteger(R.styleable.DecorationLayout_layout_decorAlign, DECOR_ALIGN_START);
            array.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }
    }
}
