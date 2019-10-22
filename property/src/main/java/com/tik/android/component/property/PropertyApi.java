package com.tik.android.component.property;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.bussiness.property.bean.CoinInfo;
import com.tik.android.component.bussiness.property.bean.CoinAssets;
import com.tik.android.component.bussiness.property.bean.Rate;
import com.tik.android.component.bussiness.property.bean.StockAssets;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface PropertyApi {
    /**
     *
     * 获取当前汇率
     *
     * @return
     */
    @GET("rate")
    Flowable<Result<Rate>> getRate();

    /**
     * 获取当前的数字货币信息
     * @return
     */
    @GET("coin_info_list")
    Flowable<Result<List<CoinInfo>>> getCoinInfo();

    /**
     * 获取用户数字货币资产
     *
     * @return
     */
    @GET("asset/coin")
    Flowable<Result<List<CoinAssets>>> getCoin();

    /**
     * 获取用户股票资产
     *
     * @return
     */
    @GET("asset/stock")
    Flowable<Result<List<StockAssets>>> getStock();
}
