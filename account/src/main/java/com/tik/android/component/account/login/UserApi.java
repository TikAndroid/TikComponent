package com.tik.android.component.account.login;

import android.support.annotation.Nullable;

import com.tik.android.component.account.binding.bean.BindArgs;
import com.tik.android.component.account.binding.bean.VerifiCodeArgs;
import com.tik.android.component.account.login.bean.LoginInfo;
import com.tik.android.component.account.login.bean.RegisterArgs;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.bussiness.account.bean.User;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    /**
     * 获取ticket，相当于将三个参数合并为一个提交给服务器验证
     * @param challenge 通过请求极验验证码获取的challenge
     * @param validate 通过请求极验验证码获取的validate
     * @param seccode 通过请求极验验证码获取的seccode
     * @return
     */
    @GET("gt/ticket")
    Flowable<Result<Map>> getTicket(
            @Query("challenge") String challenge,
            @Query("validate") String validate,
            @Query("seccode") String seccode
    );

    /**
     * 通过邮件注册
     *
     * @return
     */
    @POST("register")
    Flowable<Result<LoginInfo>> register(@Body RegisterArgs args);

    /**
     * 通过手机号注册
     *
     * @return
     */
    @POST("m/register")
    Flowable<Result<LoginInfo>> registerByMobile(@Body RegisterArgs args);

    /**
     * 登录
     *
     * @param challenge 通过请求极验验证码获取的challenge
     * @param validate 通过请求极验验证码获取的validate
     * @param seccode 通过请求极验验证码获取的seccode
     * @param email 用户名，即邮件地址
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Flowable<Result> login(
            @Field("challenge") String challenge,
            @Field("validate") String validate,
            @Field("seccode") String seccode,
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * 手机登录
     *
     * @param challenge 通过请求极验验证码获取的challenge
     * @param validate 通过请求极验验证码获取的validate
     * @param seccode 通过请求极验验证码获取的seccode
     * @param countryCode 区号
     * @param mobile 手机号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("m/login")
    Flowable<Result> loginByMobile(
            @Field("challenge") String challenge,
            @Field("validate") String validate,
            @Field("seccode") String seccode,
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    /**
     * 发送验证码到邮件
     *
     * @param language 语言，非必需默认为'en'
     * @param email 邮件地址
     * @param type 邮件类型，非必需，默认'regmail'
     * @return
     */
    @FormUrlEncoded
    @POST("send_verify_code")
    Flowable<Result> sendVerifyCode(
            @Field("language") @Nullable String language,
            @Field("email") String email,
            @Field("type") @Nullable String type
    );

    /**
     * 发送短信验证码
     *
     * @param countryCode 区号
     * @param mobile 手机号
     * @return
     */
    @FormUrlEncoded
    @POST("send_sms_code")
    Flowable<Result> sendSmsCode(
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile
    );

    /**
     * 获取User信息
     *
     * @return
     */
    @GET("user/detail")
    Flowable<Result<User>> getUser();

    /**
     * 检查邮箱账号是否被使用
     *
     * @param email 邮箱账号
     * @return
     */
    @FormUrlEncoded
    @POST("user/exists")
    Flowable<Result> checkEmailExists(
            @Field("email") String email
    );

    /**
     * 检查手机号是否被使用
     *
     * @param countryCode 区号
     * @param mobile 手机号
     * @return
     */
    @FormUrlEncoded
    @POST("user/mobile_exists")
    Flowable<Result> checkMobileExists(
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile
    );

    /**
     * 检查账号是否绑定指定类型的第三方账号
     *
     * @param email 邮箱地址
     * @param countryCode 区号
     * @param mobile 手机号
     * @param type 第三方类型google, facebook, wechat
     * @return
     */
    @POST("is_bind_oauth")
    Flowable<Result> isBindOauth(@Body VerifiCodeArgs args);

    /**
     * 单独验证手机和验证码
     *
     * @param countryCode 区号
     * @param mobile 手机号
     * @param code 验证码
     * @return
     */
    @FormUrlEncoded
    @POST("check_sms_code")
    Flowable<Result> checkSmsCode(
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile,
            @Field("code") String code
    );

    /**
     * 单独校验邮件和验证码
     *
     * @param email 邮箱账户
     * @param code 验证码
     * @return
     */
    @FormUrlEncoded
    @POST("check_email_code")
    Flowable<Result> checkEmailCode(
            @Field("email") String email,
            @Field("code") String code
    );

    /**
     * 根据第三方uuid绑定手机账号、补充资料
     *
     * @param args 参考BindArgs类
     * @return
     */
    @POST("oauth/bind_mobile")
    Flowable<Result<Map>> bindMobile(@Body BindArgs args);

    /**
     * 根据第三方uuid绑定邮箱账号、补充资料
     *
     * @param args 参考BindArgs类
     * @return
     */
    @POST("oauth/bind_email")
    Flowable<Result<Map>> bindEmail(@Body BindArgs args);


}
