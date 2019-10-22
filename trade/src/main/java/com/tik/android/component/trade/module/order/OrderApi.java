package com.tik.android.component.trade.module.order;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;
import com.tik.android.component.bussiness.api.PageBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderApi {

    /**
     * @param type      eg. {@link OrderConstants#TRADE_TYPE_CURRENT}
     * @param page      query page number
     * @param keyWord   query key when filter list
     * @param direction buy or sell, {@link OrderConstants#TRADE_DIRECTION_BUY}
     * @return PageBean contains trade info (or order-info)
     */
    @GET("trade")
    Flowable<Result<PageBean<TradeInfo>>> getTrades(
            @Query("type") String type,
            @Query("page") int page,
            @Query("keyword") String keyWord,
            @Query("direction") int direction
    );

    /**
     * maybe the same as trade:type=history
     *
     * @see #getTrades(String, int, String, int) ;
     */
    @GET("trade/history")
    Flowable<Result<PageBean<TradeInfo>>> getHistoryTrades(
            @Query("page") int page,
            @Query("keyword") String keyWord,
            @Query("direction") int direction
    );

    /**
     * cancel trade
     *
     * @param tradeId string tradeId from TradeInfo
     * @return
     */
    @PUT("trade_cancel")
    Flowable<Result<List<TradeInfo>>> cancelTrade(
            @Body String tradeId
    );

    @GET("order")
    Flowable<Result<PageBean<OrderInfo>>> getOrders(
            @Query("page") int page,
            @Query("keyword") String keyWord,
            @Query("direction") int direction
    );
}
