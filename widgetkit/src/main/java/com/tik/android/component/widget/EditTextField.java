package com.tik.android.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;


import com.tik.android.component.widgetkit.R;

import java.util.ArrayList;
import java.util.List;


/**
 * EditTextField是在EditText的基础上添加了右侧浮动删除按钮的实现类
 */
public class EditTextField extends AppCompatEditText {

    public static final int NEVER = 0; // 不显示清空按钮
    public static final int ALWAYS = 1; // 始终显示清空按钮
    public static final int WHILEEDITING = 2; // 输入框内容不为空且有获得焦点
    public static final int UNLESSEDITING = 3; // 输入框内容不为空且没有获得焦点

    public static final int SHOW_PWD_DISABLED = 0; // 默认状态，密码以点显示
    public static final int SHOW_PWD_ENABLED = 1; // 显示密码为明文的状态


    private Context mContext;
    private Paint mPaint;

    private List<FloatButton> mFloatButtons = new ArrayList<>(2);
    //按钮显示方式
    private int mClearButtonMode;
    //原始输入框右内边距
    private int mOriginPaddingRight;

    private int mOriginInputType; // 记录原始的InputType输入类型
    private boolean mEnableSwitchPwd; // 是否显示切换密码显示状态的开关

    public EditTextField(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attributeSet) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.EditTextField);
        mClearButtonMode = typedArray.getInteger(R.styleable.EditTextField_clearButtonMode, WHILEEDITING); // 默认为WHILEEDITIN模式
        int clearButtonRes = typedArray.getResourceId(R.styleable.EditTextField_clearButtonDrawable, R.drawable.ic_clear);

        mEnableSwitchPwd = typedArray.getBoolean(R.styleable.EditTextField_enableSwitchPwd, false);
        int hidePwdBtnRes = typedArray.getResourceId(R.styleable.EditTextField_hidePwdButtonDrawable, R.drawable.ic_pwd_invisible);
        int showPwdBtnRes = typedArray.getResourceId(R.styleable.EditTextField_showPwdButtonDrawable, R.drawable.ic_pwd_visible);
        typedArray.recycle();

        int mFloatButtonMargin = dp2px(3);

        FloatButton clearButton = new FloatButton(false, mFloatButtonMargin, getDrawableCompat(clearButtonRes)) {
            @Override
            void onPreDraw(Canvas canvas) {
                switch (mClearButtonMode) {
                    case ALWAYS:
                        enable = true;
                        break;
                    case WHILEEDITING:
                        enable = hasFocus() && getText().length() > 0;
                        break;
                    case UNLESSEDITING:
                        enable = !hasFocus() && getText().length() > 0;
                        break;
                    default:
                        enable = false;
                        break;
                }
            }

            @Override
            public boolean onClick() {
                setError(null);
                setText("");
                return true;
            }
        };

        LevelListDrawable drawable = new LevelListDrawable();
        if (mEnableSwitchPwd) {
            drawable.addLevel(SHOW_PWD_DISABLED, SHOW_PWD_DISABLED, getDrawableCompat(hidePwdBtnRes));
            drawable.addLevel(SHOW_PWD_ENABLED, SHOW_PWD_ENABLED, getDrawableCompat(showPwdBtnRes));
        }
        FloatButton showPwdButton = new FloatButton(mEnableSwitchPwd, mFloatButtonMargin, drawable) {

            @Override
            void onPreDraw(Canvas canvas) {
                enable = mEnableSwitchPwd && isPasswordInputType(mOriginInputType);
            }

            @Override
            public boolean onClick() {
                int newLevel = (drawable.getLevel() + 1) % 2;
                drawable.setLevel(newLevel);
                if (newLevel == SHOW_PWD_ENABLED) {
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                setSelection(getText().length());
                requestFocus();
                return true;
            }
        };

        mFloatButtons.add(clearButton);
        mFloatButtons.add(showPwdButton);

        mOriginInputType = getInputType();
        mOriginPaddingRight = getPaddingRight();

        mPaint =new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mOriginPaddingRight = right;
        super.setPadding(left, top, calRealPaddingRight(), bottom);
    }

    private void drawFloatButtons(Canvas canvas) {
        super.setPadding(getPaddingLeft(), getPaddingTop(), calRealPaddingRight(), getPaddingBottom());

        Rect drawRect = new Rect();
        for (int i = mFloatButtons.size() - 1; i >= 0; i--) {
            FloatButton button = mFloatButtons.get(i);
            button.onPreDraw(canvas);
            if (!button.enable || button.drawable == null) continue;

            drawRect.set(button.rect);
            drawRect.offset(getScrollX(), 0); // draw时需要加上滚动偏移
            if (!drawRect.isEmpty()) {
                drawDrawable(canvas, button.drawable, drawRect);
            }
        }
    }

    private int calRealPaddingRight() {
        int right = getMeasuredWidth() - mOriginPaddingRight;
        for (int i = mFloatButtons.size() - 1; i >= 0; i--) {
            FloatButton button = mFloatButtons.get(i);
            if (!button.enable || button.drawable == null) continue;

            right -= button.margin; // 减去右边的边距
            int left = right - button.drawable.getIntrinsicWidth();
            int top = (getMeasuredHeight() - button.drawable.getIntrinsicWidth())/2;
            int bottom  = top + button.drawable.getIntrinsicWidth();
            button.rect.set(left, top, right, bottom);

            right = left - button.margin; // 减去左边的边距
        }

        return getMeasuredWidth() - right;
    }

    private void drawDrawable(Canvas canvas, Drawable drawable, Rect rect) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            canvas.drawBitmap(bitmap, null, rect, mPaint);
        } else {
            drawable.setBounds(rect);
            drawable.draw(canvas);
        }
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        mOriginInputType = type;
    }

    /**
     * 判断是否是密码类型，从TextView中拷贝的实现
     * @param inputType
     * @return
     */
    static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        drawFloatButtons(canvas);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (FloatButton button : mFloatButtons) {
                    if (button.enable && button.rect.contains(x, y) && button.onClick()) {
                        return true;
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }




    /**
     * 获取Drawable
     * @param resourseId  资源ID
     */
    private Drawable getDrawableCompat(int resourseId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(resourseId, mContext.getTheme());
        } else {
            return getResources().getDrawable(resourseId);
        }
    }

    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    static class FloatButton {
        boolean enable;
        int margin;
        Drawable drawable;
        Rect rect = new Rect();

        public FloatButton(boolean enable, int margin, Drawable drawable) {
            this.enable = enable;
            this.margin = margin;
            this.drawable = drawable;
        }

        /**
         * 可重载此方法在绘制button前更新状态
         */
        void onPreDraw(Canvas canvas) {}

        public boolean onClick() {
            return false;
        }
    }
}