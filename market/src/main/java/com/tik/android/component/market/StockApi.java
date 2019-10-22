package com.tik.android.component.market;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.market.bean.CoinInfo;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.market.bean.StockFullList;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.market.bean.StockSearchResult;
import com.tik.android.component.market.bean.UserFavorParam;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface StockApi {
    @GET("stock_info_list")
    Flowable<Result<List<StockFullList>>> getStockFullList();

    @GET("coin_info_list")
    Flowable<Result<List<CoinInfo>>> getCoinInfo();

    @GET("stock/hisData")
    Flowable<Result<List<KLineData>>> getChartData(@Query("symbol") String symbol, @Query("type") int type);

    /**
     * 根据symbo查询股票价格
     * @param symbol 单个symbol
     * @return
     */
    @GET("stock/price")
    Flowable<Result<StockPriceInfo>> getCurrPriceInfo(@Query("symbol") String symbol);

    /**
     * 根据symbo查询股票价格
     * @param symbols 多个或者单个symbol,多个则用逗号分开。 eg: "BABA,YY,AMZN" 中间不需要空格
     * @return data返回StockPriceInfo数组
     */
    @GET("stock/price")
    Flowable<Result> getCurrPriceInfoArray(@Query("symbol") String symbols);

    @GET("stock/search")
    Flowable<Result<List<StockSearchResult>>> getSearchResult(@Query("keyword") String keyword);

    /**
     * 获取用户自选列表
     * @return data返回用户自选列表symbol字符串数组
     */
    @GET("user/favs")
    Flowable<Result<List<String>>> getUserFavor();

    /**
     * 更新用户自选列表
     * @param favor 自选列表symbol数组
     * @return
     */
    @PUT("user/favs")
    Flowable<Result> updateUserFavor(@Body UserFavorParam favor);

}
