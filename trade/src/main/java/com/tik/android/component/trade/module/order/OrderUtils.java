package com.tik.android.component.trade.module.order;

import android.content.Context;

import com.tik.android.component.trade.R;
import com.tik.android.component.trade.module.order.bean.TradeInfo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.tik.android.component.trade.module.order.OrderConstants.MONEY_FORMAT_PATTERN;
import static com.tik.android.component.trade.module.order.OrderConstants.TIMESTAMP_FORMAT_PATTERN;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/28
 */
public class OrderUtils {

    /**
     * date short & time medium: 2018/11/27 10:10:26
     * will format with your pattern 'yyyy-MM-dd HH:mm:ss'
     */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(TIMESTAMP_FORMAT_PATTERN, Locale.US);
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat(MONEY_FORMAT_PATTERN);

    /**
     * stock-name of the item view
     *
     * @param tradeInfo the trade info
     * @return string to display
     */
    public static String buildStockDisplayName(TradeInfo tradeInfo) {
        return "(" + tradeInfo.getStockName() + ")";
    }

    /**
     * default is yyyy-MM-dd HH:mm:ss
     *
     * @param timeAt
     * @return
     */
    public static String getDateString(long timeAt) {
        return SIMPLE_DATE_FORMAT.format(timeAt);
    }

    /**
     * convert number to a format money string
     * result like: 1,000,200.00    0.00   999.99
     *
     * @param number maybe big decimal
     * @return s a format string
     */
    public static String getMoneyString(Number number) {
        return MONEY_FORMAT.format(number);
    }

    public static String getMoneyString(String pattern, Number number) {
        DecimalFormat special = new DecimalFormat(pattern);
        special.applyPattern(pattern);
        return special.format(number);
    }

    /**
     * @param context for get resource
     * @return the message of status
     */
    public static String getStatusStr(Context context, int status) {
        String str = null;
        switch (status) {
            case OrderConstants.TRADE_STATUS_ERROR:
                str = context.getString(R.string.trade_status_consign_submit);
                break;
            case OrderConstants.TRADE_CONSIGNING_1:
            case OrderConstants.TRADE_CONSIGNING_2:
                str = context.getString(R.string.trade_status_consigning);
                break;
            case OrderConstants.TRADE_CONSIGN_FAILED:
                str = context.getString(R.string.trade_status_consign_failed);
                break;
            case OrderConstants.TRADING_PENDING:
                str = context.getString(R.string.trade_status_pending);
                break;
            case OrderConstants.TRADE_WITHDRAWING:
                str = context.getString(R.string.trade_status_order_withdrawing);
                break;
            case OrderConstants.TRADE_PARTIAL_DEAL:
                str = context.getString(R.string.trade_status_partial_deal);
                break;
            case OrderConstants.TRADE_STATUS_DONE:
            case OrderConstants.TRADE_DEAL_DONE:
                str = context.getString(R.string.trade_status_deal_done);
                break;
            case OrderConstants.TRADE_REVOKED:
                str = context.getString(R.string.trade_status_revoked);
                break;
            case OrderConstants.TRADE_EXPIRED:
                str = context.getString(R.string.trade_status_expired);
                break;
            default:
                break;
        }
        return str;
    }
}
