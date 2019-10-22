package com.tik.android.component.account.transPassword;

import com.tik.android.component.basemvp.Result;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by yangtao on 2018/11/29
 */
public interface TransactionPasswordApi {


    /**
     * 设置交易密码
     *
     * @param assetPassword 交易密码
     * @return
     */
    @FormUrlEncoded
    @POST("asset_password")
    Flowable<Result> setPassword(@Field("assetPassword") String assetPassword);

    /**
     * 重置交易密码
     *
     * @param assetPassword 交易密码
     * @param ticket
     * @return
     */
    @FormUrlEncoded
    @PUT("asset_password")
    Flowable<Result> resetPassword(
            @Field("assetPassword") String assetPassword,
            @Field("ticket") String ticket
    );

    /**
     * 交易密码校验
     *
     * @param assetPassword 交易密码
     * @return
     */
    @FormUrlEncoded
    @POST("asset_password/verify")
    Flowable<Result> verifyPassword(@Field("assetPassword") String assetPassword);

    /**
     * 检查是否在授权时间内
     *
     * @return
     */
    @GET("asset_password/check")
    Flowable<Result> checkPasswordWithinTime();

}
