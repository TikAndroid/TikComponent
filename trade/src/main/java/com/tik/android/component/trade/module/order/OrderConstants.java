package com.tik.android.component.trade.module.order;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/24
 */
public class OrderConstants {
    public static final int TRADE_DIRECTION_ALL = 0;
    public static final int TRADE_DIRECTION_BUY = 1;
    public static final int TRADE_DIRECTION_SELL = 2;

    public static final String TRADE_TYPE_CURRENT = "current";
    public static final String TRADE_TYPE_DONE = "done";
    public static final String TRADE_TYPE_SUCCEED = "succeed";
    public static final String TRADE_TYPE_CLOSED = "closed";
    public static final String TRADE_TYPE_HISTORY = "history";
    public static final String TRADE_KEYWORD_ALL = "";

    public static final int PAGE_SIZE = 20;

    // -1: default/tab change, 0: fresh,  1: loadmore
    public static final int MODE_DEFAULT = -1;
    public static final int MODE_REFRESH = 0;
    public static final int MODE_LOAD_MORE = 1;


    public static final int TRADE_STATUS_ERROR = -1;

    public static final int TRADE_CONSIGNING_1 = 1; // consigning
    public static final int TRADE_CONSIGNING_2 = 2; // consigning
    public static final int TRADE_CONSIGN_FAILED = 3;// trade failed

    public static final int TRADING_PENDING = 4;    // wait for deal
    public static final int TRADE_WITHDRAWING = 5;  // withdrawing
    public static final int TRADE_PARTIAL_DEAL = 6; // just a part of this trade success
    public static final int TRADE_STATUS_DONE = 0;  // default status when query order list
    public static final int TRADE_DEAL_DONE = 7;    // all done with success
    public static final int TRADE_REVOKED = 8;      // order canceled
    public static final int TRADE_EXPIRED = 9;      // yingtou-platform time out

    // order detail info format constants
    public static final String MONEY_FORMAT_PATTERN = "###,##0.00";
    public static final String MONEY_EMPTY_STRING = "--";
    public static final String TIMESTAMP_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";


}
