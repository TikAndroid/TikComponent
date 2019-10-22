package com.tik.android.component.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tik.android.component.widgetkit.R;


/**
 * @describe preview icon + title + arrow
 * @usage
 * the item previewIcon {@link #setIcon(Drawable)} or {@link #setIcon(int)}
 * the item name {@link #setTitle(String)}
 * the item status, usually to indicate a setting state, {@link #setSubtitle(String)}
 *
 * Setting Item: preview icon + title + arrow
 *
 * </p>
 * Created by tanlin on 2018/11/12
 */
public class SettingItem extends RelativeLayout {
    private static final String TAG = SettingItem.class.getName();
    protected ImageView mIcon;
    protected ImageView mArrow;
    protected TextView mTitle;
    protected TextView mSubtitle;

    // bottom line
    private Paint mBottomLinePaint;
    protected boolean mBottomLineVisible;
    private SettingItemStyle mBgStyle;
    private final int BOTTOM_LINE_WIDTH;
    private final int TITLE_LEFT_MARGIN;
    private final int SUBTITLE_RIGHT_MARGIN;

    public SettingItem(Context context) {
        this(context, null);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);
        View root = LayoutInflater.from(context).inflate(R.layout.setting_item, this, true);
        mIcon = (ImageView) root.findViewById(R.id.setting_item_icon);
        mTitle = (TextView) root.findViewById(R.id.setting_item_title);
        mSubtitle = (TextView) root.findViewById(R.id.setting_item_subtitle);
        mArrow = (ImageView) root.findViewById(R.id.setting_item_arrow);
        Resources res = getResources();
        BOTTOM_LINE_WIDTH = res.getDimensionPixelSize(R.dimen.setting_item_divider);
        TITLE_LEFT_MARGIN = res.getDimensionPixelSize(R.dimen.setting_item_horizon_gap);
        SUBTITLE_RIGHT_MARGIN = res.getDimensionPixelSize(R.dimen.setting_item_subtitle_margin_right);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingItem, defStyleAttr, 0);
        Drawable icon = attributes.getDrawable(R.styleable.SettingItem_icon);
        boolean arrowVisible = attributes.getBoolean(R.styleable.SettingItem_arrowVisible, true);
        String title = attributes.getString(R.styleable.SettingItem_title);
        String subtitle = attributes.getString(R.styleable.SettingItem_subtitle);
        int dividerColor = attributes.getInt(R.styleable.SettingItem_divider_color, res.getColor(R.color.divider_color_def));

        mBgStyle = SettingItemStyle.fromValue(attributes.getInt(R.styleable.SettingItem_style, 0));
        attributes.recycle();
        initLinePaintIfNeed(mBgStyle, dividerColor);

        setIcon(icon);
        setArrowVisible(arrowVisible);
        setTitle(title);
        setSubtitle(subtitle);
        // elevation dropped
    }

    public void setIcon(int drawableId) {
        mIcon.setImageResource(drawableId);
    }

    public void setIcon(Drawable drawable) {
        mIcon.setImageDrawable(drawable);
    }

    public void setTitle(int titleId) {
        mTitle.setText(titleId);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setSubtitle(int resId) {
        setSubtitle(getContext().getString(resId));
    }

    public void setSubtitle(String text) {
        if(!TextUtils.isEmpty(text)) {
            mSubtitle.setText(text);
            mSubtitle.setVisibility(View.VISIBLE);
        } else {
            mSubtitle.setVisibility(View.GONE);
        }
    }

    public void setArrowVisible(boolean visible) {
        mArrow.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void initStyle() {

        int styleId = R.drawable.setting_item_single_bg;
        switch (mBgStyle) {
            case TOP:
                styleId = R.drawable.setting_item_top_bg;
                break;
            case MID:
                styleId = R.drawable.setting_item_middle_bg;
                break;
            case BOTTOM:
                styleId = R.drawable.setting_item_bottom_bg;
                break;
            case SINGLE:
                styleId = R.drawable.setting_item_single_bg;
                break;
        }
        setBackgroundResource(styleId);
    }

    private void initLinePaintIfNeed(SettingItemStyle style, int dividerColor) {
        mBottomLineVisible = style == SettingItemStyle.TOP || style == SettingItemStyle.MID;
        if(mBottomLineVisible) {
            // draw the bottom line
            mBottomLinePaint = new Paint();
            mBottomLinePaint.setDither(true);
            mBottomLinePaint.setAntiAlias(true);
            mBottomLinePaint.setStyle(Paint.Style.FILL);
            mBottomLinePaint.setColor(dividerColor);
            //mBottomLinePaint.setColor(getResources().getColor(android.R.color.holo_red_dark));
            mBottomLinePaint.setStrokeWidth(BOTTOM_LINE_WIDTH);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mBottomLineVisible) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + BOTTOM_LINE_WIDTH);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int w = getWidth();
        int h = getHeight();
        int il = getPaddingLeft();
        int it = (h - mIcon.getHeight()) / 2;
        int ir = il + mIcon.getWidth();
        int ib = it + mIcon.getHeight();
        mIcon.layout(il, it, ir, ib);

        int tl = mIcon.getRight() + TITLE_LEFT_MARGIN;
        int tt = (h - mTitle.getHeight()) / 2;
        int tr = tl + mTitle.getWidth();
        int tb = tt + mTitle.getHeight();
        mTitle.layout(tl, tt, tr, tb);

        int al = w - mArrow.getWidth() - getPaddingRight();
        int at = (h - mArrow.getHeight()) / 2;
        int ar = al + mArrow.getWidth();
        int ab = at + mArrow.getHeight();
        mArrow.layout(al, at, ar, ab);

        int stl = mArrow.getLeft() - mSubtitle.getWidth();
        int stt = (h - mSubtitle.getHeight()) / 2;
        int str = stl + mSubtitle.getWidth();
        int stb = stt + mSubtitle.getHeight();
        mSubtitle.layout(stl, stt, str, stb);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBottomLineVisible) {
            int lineY = getHeight() - BOTTOM_LINE_WIDTH / 2;
            canvas.drawLine(0, lineY, getWidth(), lineY, mBottomLinePaint);
        }
    }
}
