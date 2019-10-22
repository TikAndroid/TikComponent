package com.tik.android.component.mine;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.mine.bean.VersionInfo;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @describe : check app version
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/14
 */
public interface VersionApi {
    @GET("app_version")
    public Flowable<Result<VersionInfo>> getAppVersion(
            @Query("version") String curVersion,
            @Query("type") String type);
}
