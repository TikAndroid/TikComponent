package com.tik.android.component.market.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.tik.android.component.libcommon.AssetUtils;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.JsonUtil;
import com.tik.android.component.market.R;
import com.tik.android.component.market.bean.MarketBusinessHours;

public class ResourceUtils {
    private static String mStrPriceHigh;
    private static String mStrPriceLow;
    private static String mStrPriceClose;
    private static String mStrPriceOpen;
    private static String mStrPriceVolume;
    private static String mStrSpace = " ";
    private static String mStrColon = ": ";
    private static String mStrMarketOpen ;
    private static String mStrMarketClose ;
    private static String mStrCurrPrice ;
    private static String mStrUserFavor;
    private static String mStrCurrencyUSD;
    private static String mStrCurrencyHKD;

    private static int mDimTitleSize;
    private static int mDimTitleSizeLite;
    private static int mDimPriceValueSize;
    private static volatile MarketBusinessHours mMarketBusiness;
    private static final String MARKET_BUSINESS_JSON_FILE = "markets_business.json";
    private static int mColorRed;
    private static int mColorGreen;
    private static String mStrMarketLunch;
    private static String mStrMarketHoliday;
    private static String mStrMarketWeekend;

    public static void initResource(Context context) {
        Resources res = context.getResources();
        mStrPriceHigh = res.getString(R.string.market_string_price_value_high);
        mStrPriceLow = res.getString(R.string.market_string_price_value_low);
        mStrPriceClose = res.getString(R.string.market_string_price_value_close);
        mStrPriceOpen = res.getString(R.string.market_string_price_value_open);
        mStrPriceVolume = res.getString(R.string.market_string_price_value_volume);
        mStrMarketOpen = res.getString(R.string.market_string_stock_market_open);
        mStrMarketClose = res.getString(R.string.market_string_stock_market_close);
        mStrMarketLunch = res.getString(R.string.market_string_stock_market_lunch);
        mStrMarketHoliday = res.getString(R.string.market_string_stock_market_holiday);
        mStrMarketWeekend = res.getString(R.string.market_string_stock_market_weekend);
        mStrCurrPrice = res.getString(R.string.market_string_price_value_curr);
        mDimTitleSize = res.getDimensionPixelSize(R.dimen.market_chart_title_name_size);
        mDimTitleSizeLite = res.getDimensionPixelSize(R.dimen.market_chart_title_name_size_lite);
        mDimPriceValueSize = res.getDimensionPixelSize(R.dimen.market_chart_price_curr_value_size);

        mStrUserFavor = res.getString(R.string.market_string_user_favor);
        mStrCurrencyUSD = res.getString(R.string.market_string_currency_usd);
        mStrCurrencyHKD = res.getString(R.string.market_string_currency_hkd);

        mColorRed = res.getColor(R.color.highlight_red);
        mColorGreen = res.getColor(R.color.highlight_green);
    }

    public static String getStrUserFavor() {
        checkResourceEmpty();
        return mStrUserFavor;
    }

    public static String getStrCurrencyUSD() {
        checkResourceEmpty();
        return mStrCurrencyUSD;
    }

    public static String getStrCurrencyHKD() {
        checkResourceEmpty();
        return mStrCurrencyHKD;
    }

    public static String getStrPriceHigh() {
        checkResourceEmpty();
        return mStrPriceHigh;
    }

    private static void checkResourceEmpty() {
        if (TextUtils.isEmpty(mStrPriceHigh)) {
            initResource(BaseApplication.getAPPContext());
        }
    }

    public static String getStrPriceLow() {
        checkResourceEmpty();
        return mStrPriceLow;
    }

    public static String getStrPriceClose() {
        checkResourceEmpty();
        return mStrPriceClose;
    }

    public static String getStrPriceOpen() {
        checkResourceEmpty();
        return mStrPriceOpen;
    }

    public static String getStrPriceVolume() {
        checkResourceEmpty();
        return mStrPriceVolume;
    }

    public static String getStrSpace() {
        checkResourceEmpty();
        return mStrSpace;
    }

    public static String getStrColon() {
        checkResourceEmpty();
        return mStrColon;
    }

    public static String getStrMarketOpen() {
        checkResourceEmpty();
        return mStrMarketOpen;
    }

    public static String getStrMarketClose() {
        checkResourceEmpty();
        return mStrMarketClose;
    }

    public static String getStrMarketState(int index) {
        checkResourceEmpty();
        String state;
        switch (index) {
            case Constant.STATE_OPEN:
                state = mStrMarketOpen;
                break;
            case Constant.STATE_LUNCH:
                state = mStrMarketLunch;
                break;
            case Constant.STATE_CLOSE:
                state = mStrMarketClose;
                break;
            case Constant.STATE_WEEKEND:
                state = mStrMarketWeekend;
                break;
            case Constant.STATE_HOLIDAY:
            case Constant.STATE_HOLIDAY_HALF:
                state = mStrMarketHoliday;
                break;
            default:
                state = "";
        }
        return state;
    }

    public static String getStrCurrPrice() {
        checkResourceEmpty();
        return mStrCurrPrice;
    }

    public static int getDimTitleSize() {
        checkResourceEmpty();
        return mDimTitleSize;
    }

    public static int getDimTitleSizeLite() {
        checkResourceEmpty();
        return mDimTitleSizeLite;
    }

    public static int getDimPriceValueSize() {
        checkResourceEmpty();
        return mDimPriceValueSize;
    }

    public static int getColorRed() {
        checkResourceEmpty();
        return mColorRed;
    }

    public static int getColorGreen() {
        checkResourceEmpty();
        return mColorGreen;
    }

    public static synchronized MarketBusinessHours getMarketBusiness(Context context) {
        if (mMarketBusiness == null) {
            String marketBusinessJson = AssetUtils.getAssetJson(MARKET_BUSINESS_JSON_FILE, context);
            if (marketBusinessJson != null) {
                mMarketBusiness = JsonUtil.json2object(marketBusinessJson, MarketBusinessHours.class);
            }
        }
        return mMarketBusiness;
    }

}
