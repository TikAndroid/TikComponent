package com.tik.android.component.trade.module.trade.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.trade.module.order.bean.TradeInfo;

import java.util.List;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.trade.module.trade.bean.OrderSubmitResult;

import io.reactivex.Flowable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/28.
 */
public interface TradeContract {

    interface View extends BaseView {
        void onTradeLoaded(String dataType, List<TradeInfo> data);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 查询可用的货币资产，一般是指USDT
         * @param foreNetwork 强制请求网络结果而不走缓存
         * @return
         */
        Flowable<Double> queryCoinBalance(boolean foreNetwork);

        /**
         * 查询可用的股票资产
         * @param stockSymbol 需要查询的股票
         * @param foreNetwork 强制请求网络结果而不走缓存
         * @return
         */
        Flowable<Integer> queryStockBalance(String stockSymbol, boolean foreNetwork);

        /**
         * 查询USD买入USDT汇率
         * @param foreNetwork 强制请求网络结果而不走缓存
         * @return
         */
        Flowable<Double> queryUsdtRate(boolean foreNetwork);

        /**
         * 查询HKD汇率
         * @param foreNetwork 强制请求网络结果而不走缓存
         * @return
         */
        Flowable<Double> queryHkdRate(boolean foreNetwork);

        /**
         * @see com.tik.android.component.trade.module.order.OrderApi#getTrades(String, int, String, int)
         * @param page
         * @param keyWord
         * @param direction
         */
        void getCurrentTrades(int page, String keyWord, int direction);

        /**
         * 请求买入委托
         * @param stockSymbol 股票代号
         * @param totalSupply 买入数量
         * @param price 委托买入价格
         * @param coinSymbol 买入时使用的数字货币代号，一般时USDT
         * @return
         */
        Flowable<Result<OrderSubmitResult>> buy(String stockSymbol, int totalSupply, float price, String coinSymbol);

        /**
         * 请求卖出委托
         * @param stockSymbol 股票代号
         * @param totalSupply 卖出数量
         * @param price 委托卖出价格
         * @param coinSymbol 卖出时使用的数字货币代号，一般时USDT
         * @return
         */
        Flowable<Result<OrderSubmitResult>> sell(String stockSymbol, int totalSupply, float price, String coinSymbol);
    }
}
