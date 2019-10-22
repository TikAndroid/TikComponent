package com.tik.android.component.trade.module.trade;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.trade.module.trade.bean.OrderSubmitResult;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TradeApi {

    @FormUrlEncoded
    @POST("trade/buy")
    Flowable<Result<OrderSubmitResult>> buy(@Field("symbol") String symbol,
                                            @Field("totalSupply") int totalSupply,
                                            @Field("price") float price,
                                            @Field("coinSymbol") String coinSymbol);

    @FormUrlEncoded
    @POST("trade/sell")
    Flowable<Result<OrderSubmitResult>> sell(@Field("symbol") String symbol,
                                             @Field("totalSupply") int totalSupply,
                                             @Field("price") float price,
                                             @Field("coinSymbol") String coinSymbol);
}
