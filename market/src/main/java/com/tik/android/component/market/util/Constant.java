package com.tik.android.component.market.util;

public class Constant {
    public static final int GAINS_SORT_FACTOR = 100;
    public static final String[] SOURCE_TIME_STRING = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
    public static final String TIME_SHARING_HH_MM = "HH:mm";
    public static final String TIME_SHARING_MM_DD = "MM-dd";
    public static final String TIME_SHARING_YY_MM = "yyyy-MM";
    public static final String TIME_TITLE_FORMAT = "%m/%d %H:%M";
    public static final String TIME_TITLE_FORMAT_BUSINESS = "yyyy-MM-dd HH:mm";
    public static final String TIME_LABEL_MARK_TIME = "MM-dd HH:mm";

    public static final String KEY_CHART_TYPE = "chart_type";
    public static final int TYPE_TIME_SHARING_M1 = 1;
    public static final int TYPE_TIME_SHARING_M5 = 2;
    public static final int TYPE_TIME_SHARING_M15 = 3;
    public static final int TYPE_TIME_SHARING_M30 = 4;
    public static final int TYPE_TIME_SHARING_M60 = 5;
    public static final int TYPE_TIME_SHARING_DAY = 6;
    public static final int TYPE_TIME_SHARING_WEEK = 7;
    public static final int TYPE_TIME_SHARING_MONTH = 8;

    public static final String EMPTY_DEFAULT_VALUE = "--";
    public static final String SIGN_STOCK_US_DOLLAR = "$";
    public static final String SIGN_STOCK_HK_DOLLAR = "HK$";
    public static final String KEY_STOCK_TYPE = "HKD";
    public static final String HKD_STOCK_CURRENCY = "HKD";
    public static final String USD_STOCK_CURRENCY = "USD";

    public static final float DEFAULT_CHART_LINE_COUNT = 100f; //k线默认初始显示条数
    public static final int MIN_CHART_LINE_COUNT = 40;  //k线缩放时至少显示条数
    public static final int DEFAULT_CHART_LINE_ANIM_TIME = 600; //k线动画时间
    public static final float DEFAULT_CHART_FRICTION_COEF = 0.2f; //k线滑动时阻尼，0-1之间，0时阻尼最大

    public static final long MILLI_SECOND_2_DAY = 48 * 60 * 60 * 1000l;
    public static final long MILLI_SECOND_1_YEAR = 365 * MILLI_SECOND_2_DAY;
    public static final int SEARCH_BUFFER_SIZE = 1; //搜索输入框缓存

    //DB
    public static final String DB_SUFFIX_NAME = "tik.android.db";
    public static final String COLUMN_CNAME = "cname";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_UPDATE_TIME = "time";
    public static final String COLUMN_HISTORY = "history";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_GAINS = "gains";
    public static final String COLUMN_GAINS_VALUE = "gainsValue";
    public static final String COLUMN_LOCAL = "local";
    public static final int QUERY_LIMIT_NUM = 10;

    public static final String MARKET_STOCK_TABLE = "market_stock";

    public static final int LIST_TYPE = 0;
    public static final int HEAD_TYPE = 1;
    public static final int NO_FAVOR = 2;
    public static final int NO_SEARCH_RESULT = 3;
    public static final String HISTORY_INSERT_DATA = "history_insert_data";
    public static final String USER_FAVOR_DATA = "user_favor_data";
    public static final String KEY_DB_INITED = "db_init";
    public static final int MARKET_TAB_FAVOR = 0;//自选
    public static final int MARKET_TAB_USD = 1;//美股
    public static final int MARKET_TAB_HKD = 2;//港股
    public static final String EVENT_FAVOR_ENABLE = "event_favor_enable";
    public static final String PRICE_ORDER = "price_order";
    public static final String GAINS_ORDER = "gains_order";

    public static final int ORIGIN_ORDER = 0;
    public static final int UP_ORDER = 1;
    public static final int DOWN_ORDER = 2;

    public static final int STATE_NONE = 0;
    public static final int STATE_OPEN = 1;
    public static final int STATE_LUNCH = 2;
    public static final int STATE_CLOSE = 3;
    public static final int STATE_WEEKEND = 4;
    public static final int STATE_HOLIDAY = 5;
    public static final int STATE_HOLIDAY_HALF = 6;
}
