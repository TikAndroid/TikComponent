package com.tik.android.component.account.verify;

import com.tik.android.component.account.verify.bean.Ticket;
import com.tik.android.component.basemvp.Result;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface VerifyApi {

    /**
     * 发送手机验证码
     *
     * @param countryCode
     * @param mobile
     * @return
     */
    @FormUrlEncoded
    @POST("send_sms_code")
    Flowable<Result> sendSmsCode(
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile
    );

    /**
     * 登录后发送邮件
     *
     * @param language Default value : ‘en’
     * @param type     Available values : changeEmail, changeMobile, changePassword, turnOnGoogle, turnOffGoogle,
     *                 turnOnEmail, turnOffEmail, turnOffSMS, turnOnSMS, cashOut, resetAssetPassword, setPassword,
     *                 unbindFacebook, unbindWechat, unbindGoogle, bindEmail
     * @return
     */
    @FormUrlEncoded
    @POST("user_send_mail")
    Flowable<Result> userSendMail(
            @Field("language") String language,
            @Field("type") String type
    );

    /**
     * 获取ticket
     *
     * @param op          授权行为 Available values : changeEmail, changeMobile, forgetPassword, changePassword,
     *                    setPassword, changAssetPassword, cashout, turnOffAuthenticator, turnOnAuthenticator, unbindOauth
     * @param countryCode
     * @param mobile
     * @param email
     * @param smsCode
     * @param emailCode
     * @param googleCode
     * @return
     */
    @GET("ticket")
    Flowable<Result<Ticket>> getTicket(
            @Query("op") String op,
            @Query("countryCode") String countryCode,
            @Query("mobile") String mobile,
            @Query("email") String email,
            @Query("smsCode") String smsCode,
            @Query("emailCode") String emailCode,
            @Query("googleCode") String googleCode
    );

    /**
     * 根据 ticket 动态执行 get 请求
     *
     * @param url 请求的url
     * @return
     */
    @FormUrlEncoded
    @GET
    Flowable<Result> doGetRequestByTicket(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 根据 ticket 动态执行 post 请求
     *
     * @param url 请求的 url
     * @param map 封装的数据
     * @return
     */
    @FormUrlEncoded
    @POST
    Flowable<Result> doPostRequestByTicket(@Url String url, @FieldMap Map<String, String> map);
}
