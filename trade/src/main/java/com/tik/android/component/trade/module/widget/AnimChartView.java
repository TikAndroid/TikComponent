package com.tik.android.component.trade.module.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tik.android.component.bussiness.market.IChartView;
import com.tik.android.component.bussiness.market.IPriceDataCallback;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.libcommon.Constants.Market;
import com.tik.android.component.libcommon.DecimalUtils;
import com.tik.android.component.libcommon.SpannableStringFormat;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianglixuan on 2018/11/23
 */
public class AnimChartView extends LinearLayout {
    @BindView(R2.id.trade_chart_name)
    protected TextView mNameTextView;
    private View mStockChartView;
    @BindView(R2.id.trade_chart_value)
    protected TextView mValueTextView;
    @BindView(R2.id.trade_chart_btn)
    protected ImageView mBtn;
    @BindView(R2.id.trade_chart_tag)
    protected ImageView mTag;
    private int mLowHeight;
    private int mHighHeight;
    private String mSymbol = Market.EMPTY_DEFAULT_VALUE;
    private String mCName = Market.EMPTY_DEFAULT_VALUE;
    private OnAnimChartListener mChartListener;
    private boolean mIsOpenedState;

    public AnimChartView(Context context) {
        this(context, null);
    }

    public AnimChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View inflateView = LayoutInflater.from(context).inflate(R.layout.trade_anim_view_top, this, true);
        ButterKnife.bind(inflateView);
        setDefaultText();
        mNameTextView.setOnClickListener(v -> startHeightenAnim());
        mValueTextView.setOnClickListener(v -> startHeightenAnim());
        mBtn.setOnClickListener(v -> startHeightenAnim());
    }

    private void refreshPrice() {
        if (mSymbol.equals(Market.EMPTY_DEFAULT_VALUE)) {
            return;
        }
        MarketUtils.getStockPrice(mSymbol, getContext(), new IPriceDataCallback() {
            @Override
            public void onSuccess(StockPriceInfo info, String symbol) {
                if (mChartListener != null) {
                    mChartListener.onPriceUpdate(info);
                }
                updateTextViews(info, symbol);
            }

            @Override
            public void onError() {
                // TODO: 2018/11/26 数据获取不到时使用默认样式还是上一次的样式？？
                setDefaultText();
            }
        });
    }

    /**
     * 下拉刷新时调用
     */
    public void refresh() {
        refreshPrice();
        refreshChartView();
    }

    /**
     * 刷新为指定symbol股票的交易页面
     *
     * @param symbol
     */
    public void refresh(final String symbol) {
        mSymbol = symbol;
        mCName = Market.EMPTY_DEFAULT_VALUE;
        refresh();
    }

    private void refreshChartView() {
        if (mStockChartView == null || mSymbol == Market.EMPTY_DEFAULT_VALUE) {
            return;
        }
        ((IChartView) mStockChartView).refreshChartView(mSymbol, () -> {
            if (mChartListener != null) {
                mChartListener.onRefreshEnd();
            }
        });
    }

    private void loadChartViewIfNeed() {
        if (mStockChartView == null) {
            IChartView chartView = MarketUtils.getStockChartView(getContext());
            mStockChartView = (View) chartView;
            mStockChartView.setPadding(0, -getResources().getDimensionPixelSize(R.dimen.dimen_12), 0, getResources().getDimensionPixelSize(R.dimen.dimen_8));
            addView(mStockChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((IChartView) mStockChartView).refreshChartView(mSymbol, () -> {
                if (mChartListener != null) {
                    mChartListener.onRefreshEnd();
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHighHeight == mLowHeight && mStockChartView != null && mHighHeight != 0) {
            int measuredHeight = getMeasuredHeight();
            if (measuredHeight > mLowHeight) {
                mHighHeight = getMeasuredHeight();
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = mLowHeight;
        }
    }

    /**
     * 展开和收缩动画
     *
     * @return 返回true时将要做伸展动画，false时做收回动画
     */
    public boolean startHeightenAnim() {
        loadChartViewIfNeed();
        int height = getHeight();
        mIsOpenedState = height < mHighHeight / 2;
        int end = mIsOpenedState ? mHighHeight : mLowHeight;
        if (height == end) {
            return false;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(height, end);
        valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = value;
            setLayoutParams(layoutParams);
            if (mChartListener != null) {
                mChartListener.onChartHeightChanged(value);
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
        mBtn.setSelected(mIsOpenedState);
        return mIsOpenedState;
    }

    /**
     * 默认展示的价格信息
     */
    private void setDefaultText() {
        String textName = mSymbol + " " + mCName;
        SpannableString spannableName = SpannableStringFormat.createSpannableBuild(textName)
                .textColorStyleSize(0, mSymbol.length(), Color.parseColor("#53627C"), Typeface.BOLD, 18, true)
                .textColorSize(mSymbol.length(), textName.length(), Color.parseColor("#A6AEBC"), 16, true)
                .build();

        String textValue = Market.EMPTY_DEFAULT_VALUE + "   " + Market.EMPTY_DEFAULT_VALUE + " " + Market.EMPTY_DEFAULT_VALUE;
        SpannableString spannableValue = SpannableStringFormat.createSpannableBuild(textValue)
                .textColorStyleSize(0, Market.EMPTY_DEFAULT_VALUE.length(), Color.parseColor("#53627C"), Typeface.BOLD, 24, true)
                .textColorSize(Market.EMPTY_DEFAULT_VALUE.length(), textValue.length(), Color.parseColor("#A6AEBC"), 17, true)
                .build();
        mNameTextView.setText(spannableName);
        mValueTextView.setText(spannableValue);
    }

    private void updateTextViews(StockPriceInfo info, String symbol) {
        boolean isProfit = Double.valueOf(info.getGainsValue()) > 0;
        String extraStr = isProfit ? "+" : "";

        String textName = symbol + " " + info.getCname();
        mCName = info.getCname();
        String textValue = DecimalUtils.formatValue(info.getPrice()) + "   " + extraStr + DecimalUtils.formatValue(info.getGainsValue()) + " "
                + extraStr + DecimalUtils.formatPercentString(info.getGains());
        SpannableString nameSpannable = SpannableStringFormat.createSpannableBuild(textName)
                .textColorStyleSize(0, symbol.length(), Color.parseColor("#53627C"), Typeface.BOLD, 18, true)
                .textColorSize(symbol.length(), textName.length(), Color.parseColor("#A6AEBC"), 16, true)
                .build();

        SpannableString valueSpannable = SpannableStringFormat.createSpannableBuild(textValue)
                .textColorStyleSize(0, +(info.getPrice() + "").length(), Color.parseColor("#53627C"), Typeface.BOLD, 24, true)
                .textColorSize((info.getPrice() + "").length(), textValue.length(), Color.parseColor(isProfit ? "#ff6666" : "#4EC286"), 17, true)
                .build();
        mNameTextView.setText(nameSpannable);
        mTag.setBackgroundResource(info.getCurrency().equals(Market.SIGN_STOCK_CURRENCY_HKD) ? R.drawable.ic_tag_hk : R.drawable.ic_tag_us);
        mValueTextView.setText(valueSpannable);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mLowHeight == 0) {
            mLowHeight = getMeasuredHeight();
            mHighHeight = mLowHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStockChartView == null) {
            post(() -> loadChartViewIfNeed());
        }
    }

    public void setOnAnimChartListener(OnAnimChartListener listener) {
        mChartListener = listener;
    }

    public interface OnAnimChartListener {
        void onRefreshEnd();

        void onPriceUpdate(StockPriceInfo info);

        void onChartHeightChanged(int value);
    }

}


