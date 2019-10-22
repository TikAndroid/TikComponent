package com.tik.android.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;

import com.tik.android.component.widgetkit.R;


/**
 * @describe An SettingItem, hold an little point to prompt an update available.
 *
 * @usage
 * specified an radius of dimen in layout xml file. {@link R.styleable.DotSettingItem dotColor}
 *
 * </p>
 * Created by tanlin on 2018/11/13
 */
public class DotSettingItem extends SettingItem {
    private static final String TAG = DotSettingItem.class.getName();
    private Paint mDotPaint;
    private Point mDotPosition;

    private int mDotColor;
    private int mDotRadius;
    private boolean mDotVisible;

    private static final int DOT_COLOR_DEFAULT = Color.RED;
    private final int dotLeftMargin;

    public DotSettingItem(Context context) {
        this(context, null);
    }

    public DotSettingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotSettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int dotRadiusDefault = getResources().getDimensionPixelSize(R.dimen.setting_item_tips_dot_radius);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DotSettingItem, defStyleAttr, 0);
        int dotColor = attributes.getColor(R.styleable.DotSettingItem_dotColor, DOT_COLOR_DEFAULT);
        int dotRadius = attributes.getDimensionPixelSize(R.styleable.DotSettingItem_dotRadius, dotRadiusDefault);
        attributes.recycle();
        init(dotColor, dotRadius);
        dotLeftMargin = getResources().getDimensionPixelOffset(R.dimen.setting_item_tips_dot_left_margin);
    }

    private void init(int dotColor, int dotRadius) {
        mDotColor = dotColor;
        mDotRadius = dotRadius;
        // paint
        mDotPaint = new Paint();
        mDotPaint.setDither(true);
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(mDotColor);

        // dot position
        mDotPosition = new Point();
        setWillNotDraw(false);
    }

    public void setDotVisible(boolean visible) {
        mDotVisible = visible;
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //the circle centerX & centerY
        mDotPosition.x = mTitle.getRight() + dotLeftMargin + mDotRadius;
        mDotPosition.y = mTitle.getTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDotVisible) {
            canvas.save();
            canvas.drawCircle(mDotPosition.x, mDotPosition.y, mDotRadius, mDotPaint);
            canvas.restore();
        }

    }
}
