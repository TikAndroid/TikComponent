package com.tik.android.component.account.login;


import com.tik.android.component.basemvp.Result;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/16
 */
public interface ThirdPartApi {

    /**
     * @param type wechat/google/fb/
     * @param code the code from third-part
     * @return
     */
    @FormUrlEncoded
    @POST("oauth/login")
    Flowable<Result<Map>> getThirdPartToken(
            @Field("type") String type,
            @Field("code") String code
    );
}
