package com.tik.android.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.tik.android.component.widgetkit.R;


/**
 * @describe : 验证码输入框
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/16.
 */
public class VerificationCodeInput extends AppCompatEditText {

    private int mCodeLength;
    private Paint mEmptyPaint;
    private Paint mBgPaint;
    private boolean mIsPassword;
    private int mBgSize; // Background box size
    private int mAvailableWidth;
    private int mAvailableHeight;
    private RectF mBgRectF;

    public VerificationCodeInput(Context context) {
        this(context, null);
    }

    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs); // 调用super的构造方法，才能应用默认的style到EditText
        init(context, attrs, 0);
    }

    public VerificationCodeInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInput);
        mCodeLength = typedArray.getInteger(R.styleable.VerificationCodeInput_code_length, 6); // 默认长度为6
        mIsPassword = typedArray.getBoolean(R.styleable.VerificationCodeInput_is_password, false); //默认为false
        typedArray.recycle();

        mEmptyPaint = new Paint();
        mEmptyPaint.setStrokeWidth(dp2px(2));
        mEmptyPaint.setAntiAlias(true);
        mEmptyPaint.setDither(true);
        mEmptyPaint.setColor(Color.LTGRAY);

        mBgSize = dp2px(1);
        mBgRectF = new RectF();
        mBgPaint = new Paint();

        if (mIsPassword) {
            mBgPaint.setStyle(Paint.Style.STROKE);
            mBgPaint.setStrokeWidth(dp2px(1));
            mBgPaint.setAntiAlias(true);
            mBgPaint.setDither(true);
            mBgPaint.setColor(getResources().getColor(R.color.account_password_rectangle_bg));
        }

        setPadding(0, 0, 0, 0);
        setFilters(new InputFilter[] { new InputFilter.LengthFilter(mCodeLength) });
        setSingleLine(true);
        setCursorVisible(false);
        setTextIsSelectable(false);
        setLongClickable(false);
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mAvailableHeight = getHeight();
        mAvailableWidth = getWidth();
        mBgRectF.set(mBgSize,mBgSize,mAvailableWidth - mBgSize,
                mAvailableHeight - mBgSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText() != null ? getText().toString() : "";
        TextPaint paint = getPaint();
        paint.setTextAlign(Paint.Align.LEFT);

        float numberWidth = paint.measureText("0");
        float padding = dp2px(2); // 每个数字留出2dp的padding距离
        float width = numberWidth + padding * 2; // 加上两端的padding宽度
        float space = Math.max(0, mAvailableWidth - width * mCodeLength) / (mCodeLength - 1);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (mAvailableHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        int bottom = baseline + fontMetrics.bottom;

        int currentLen = text.length();
        float x = 0;
        float x1 = 0;
        float width1 = mAvailableWidth / mCodeLength;

        if (mIsPassword) {
            // Draw a rectangular background, you can also use RoundRectShape
            canvas.drawRoundRect(mBgRectF,dp2px(6),dp2px(6),mBgPaint);
        }

        for (int i = 0; i < mCodeLength; i++) {
            if (i < currentLen) { // 绘制数字
                if (mIsPassword) {
//                    canvas.drawText("*", x1 + width1/2 - paint.measureText("*")/2 , getHeight() - paint.measureText("*")/2 , paint);
                    canvas.drawCircle(x1 + width1/2,  mAvailableHeight/2, dp2px(6), paint);
                } else {
                    canvas.drawText(text.subSequence(i, i + 1).toString(), x + padding, baseline, paint);
                }
            } else { // 绘制下划线
                if (!mIsPassword) {
                    canvas.drawLine(x, bottom, x + width, bottom, mEmptyPaint);
                }
            }
            // Draw vertical lines
            if (mIsPassword && i < mCodeLength-1) {
                canvas.drawLine(x1 + width1, mBgSize, x1 + width1,
                        mAvailableHeight - mBgSize, mBgPaint);
            }
            x += width + space;
            x1 += width1;
        }
    }

    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
