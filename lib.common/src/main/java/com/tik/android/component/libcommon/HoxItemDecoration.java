package com.tik.android.component.libcommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HoxItemDecoration extends RecyclerView.ItemDecoration{
    private Paint mPaint;
    //分割线
    private Drawable mDivider;
    //分割线高度
    private int mDividerHeight = 1;
    //列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private int mOrientation;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     *
     * 默认分割线：高度为1px，颜色为灰色
     * 获取属性值，
     *
     * @param context
     * @param orientation  列表方向
     */

    public HoxItemDecoration(Context context, int orientation){
        mOrientation = orientation;
        final TypedArray array = context.obtainStyledAttributes(ATTRS);
        mDivider = array.getDrawable(0);
        array.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public HoxItemDecoration(Context context, int orientation, int drawableId) {
        mOrientation = orientation;
        mDivider = ContextCompat.getDrawable(context, drawableId);
    }

    /**
     * 自定义分割线
     *
     * @param orientation 列表方向
     * @param drawable  分割线颜色对应的xml
     */
    public HoxItemDecoration(int orientation, Drawable drawable) {
        mOrientation = orientation;
        mDivider = drawable;
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }

        outRect.set(0, 0, 0, mDividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVerticalLine(c, parent);
        } else {
            drawHorizontalLine(c, parent);
        }
    }

    //为横方向item, 画分割线
    private void drawHorizontalLine(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //为竖方向item, 画分割线
    private void drawVerticalLine(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}