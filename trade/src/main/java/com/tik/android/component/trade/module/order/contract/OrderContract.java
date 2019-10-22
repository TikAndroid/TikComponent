package com.tik.android.component.trade.module.order.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;

import java.util.List;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/23
 */
public class OrderContract {
    public interface View extends BaseView {
        void onTradeLoaded(String dataType, List<TradeInfo> data);
        void onOrdersLoaded(List<OrderInfo> orders);
        void onQueryFailed(int errorCode, String msg);
    }

    public interface Presenter extends BasePresenter<View> {
        void getOrders(int page, String keyWord, int direction);
        void getCurrentTrades(int page, String keyWord, int direction);
        void getHistoryTrades(int page, String keyWord, int direction);
        void getSucceedTrades(int page, String keyWord, int direction);
        void getClosedTrades(int page, String keyWord, int direction);
    }
}
