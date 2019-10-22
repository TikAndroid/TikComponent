package com.tik.android.component.market.presenter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;

import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.bussiness.market.IPriceDataCallback;
import com.tik.android.component.libcommon.SpannableStringFormat;
import com.tik.android.component.market.R;
import com.tik.android.component.market.bussiness.ChartDataRequester;
import com.tik.android.component.market.contract.IStockChartContract;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.FormatUtils;
import com.tik.android.component.market.util.ResourceUtils;

public class StockChartPresenter extends RxPresenter<IStockChartContract.View> implements IStockChartContract.Presenter {
    @Override
    public void updateCurrPriceInfo(final String symbol) {
        ChartDataRequester.getStockPrice(symbol, mView, new IPriceDataCallback() {
            @Override
            public void onSuccess(StockPriceInfo info, String symbol) {
                mView.onObtainPriceInfo(info,symbol);
            }

            @Override
            public void onError() {
                mView.onObtainPriceInfo(null,symbol);
            }
        });
    }

    public SpannableString getSuitableText(int viewId, StockPriceInfo info) {
        SpannableString spannableString = new SpannableString(info.getCname());
        if (viewId == R.id.stock_title_name) {
            String name = info.getCname();
            String symbol = info.getSymbol();
            String marketStatus = ResourceUtils.getStrMarketState(info.getStatus());
            String text = name + "(" + symbol + ")" + "\n" + marketStatus
                    + ResourceUtils.getStrSpace() + FormatUtils.formatTime3339(info.getLastTime());
            spannableString = SpannableStringFormat.createSpannableBuild(text)
                    .textColorSize(name.length() + symbol.length() + 2, text.length(), Color.parseColor("#AEB2C2"), ResourceUtils.getDimTitleSizeLite(), false)
                    .textColorStyleSize(0, name.length() + symbol.length() + 2, Color.parseColor("#23262F"), Typeface.BOLD, ResourceUtils.getDimTitleSize(), false)
                    .build();
        } else if (viewId == R.id.stock_curr_sign_and_price) {
            String price = FormatUtils.formatValue(info.getPrice());
            boolean isProfit = Double.valueOf(info.getGainsValue()) >= 0;
            String prevStr = info.getCurrency().equals(Constant.KEY_STOCK_TYPE) ?
                    Constant.SIGN_STOCK_HK_DOLLAR : Constant.SIGN_STOCK_US_DOLLAR + ResourceUtils.getStrSpace();
            String text = prevStr + price;
            spannableString = SpannableStringFormat.createSpannableBuild(text)
                    .textColorStyle(0, text.length(), isProfit ? ResourceUtils.getColorRed() : ResourceUtils.getColorGreen(), Typeface.BOLD)
                    .textSize(0, prevStr.length(), 18, true)
                    .textSize(prevStr.length(), text.length(), 32, true)
                    .build();
        } else if (viewId == R.id.stock_gains_and_value) {
            Double value = Double.valueOf(info.getGainsValue());
            String valueStr = FormatUtils.formatValue(value);
            boolean isProfit = value >= 0;
            String extraStr = isProfit ? "+" : "";
            String text = extraStr + valueStr + "\n" + extraStr + FormatUtils.formatPercentString(info.getGains());
            spannableString = SpannableStringFormat.createSpannableBuild(text)
                    .textColorSize(0, text.length(), isProfit ? ResourceUtils.getColorRed() : ResourceUtils.getColorGreen(), 12, true)
                    .build();
        } else if (viewId == R.id.stock_high_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceHigh(), FormatUtils.formatValue(info.getHigh()));
        } else if (viewId == R.id.stock_open_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceOpen(), FormatUtils.formatValue(info.getOpen()));
        } else if (viewId == R.id.stock_close_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceClose(), FormatUtils.formatValue(info.getClose()));
        } else if (viewId == R.id.stock_low_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceLow(), FormatUtils.formatValue(info.getLow()));
        }
        return spannableString;
    }

    /**
     * 获取最高/最低/今开/昨收 对应的text的SpannableString
     *
     * @param prevStr
     * @param laterStr
     * @return
     */
    @NonNull
    private SpannableString getSpannable(String prevStr, String laterStr) {
        String string = prevStr + "\n" + laterStr;
        int preLength = prevStr.length();
        return SpannableStringFormat.createSpannableBuild(string).textColorSize(0, preLength, Color.parseColor("#AEB2C2"), 10, true)
                .textColorSize(preLength, string.length(), Color.parseColor("#6F7385"), 12, true)
                .build();
    }

    public SpannableString getSuitableText(int viewId, String symbol) {
        SpannableString spannableString = new SpannableString(symbol);
        if (viewId == R.id.stock_title_name) {
            String text = symbol + "\n" + Constant.EMPTY_DEFAULT_VALUE + ResourceUtils.getStrSpace() + Constant.EMPTY_DEFAULT_VALUE;
            spannableString = SpannableStringFormat.createSpannableBuild(text)
                    .textColorStyleSize(0, symbol.length(), Color.parseColor("#23262F"), Typeface.BOLD, ResourceUtils.getDimTitleSize(), false)
                    .textColorSize(symbol.length(), text.length(), Color.parseColor("#AEB2C2"), ResourceUtils.getDimTitleSizeLite(), false)
                    .build();
        } else if (viewId == R.id.stock_curr_sign_and_price) {
            String text = Constant.EMPTY_DEFAULT_VALUE + " " + Constant.EMPTY_DEFAULT_VALUE;
            spannableString = SpannableStringFormat.createSpannableBuild(text)
                    .textStyleSize(0, text.length(), Typeface.BOLD, 32, true)
                    .build();
        } else if (viewId == R.id.stock_gains_and_value) {
            String text = Constant.EMPTY_DEFAULT_VALUE + "\n" + Constant.EMPTY_DEFAULT_VALUE;
            spannableString = SpannableStringFormat.createSpannableBuild(text)
                    .textSize(0, text.length(), 12, true)
                    .build();
        } else if (viewId == R.id.stock_high_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceHigh(), Constant.EMPTY_DEFAULT_VALUE);
        } else if (viewId == R.id.stock_open_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceOpen(), Constant.EMPTY_DEFAULT_VALUE);
        } else if (viewId == R.id.stock_close_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceClose(), Constant.EMPTY_DEFAULT_VALUE);
        } else if (viewId == R.id.stock_low_value) {
            spannableString = getSpannable(ResourceUtils.getStrPriceLow(), Constant.EMPTY_DEFAULT_VALUE);
        }
        return spannableString;
    }
}
