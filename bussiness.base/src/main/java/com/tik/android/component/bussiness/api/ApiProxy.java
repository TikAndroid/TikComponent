package com.tik.android.component.bussiness.api;

import android.text.TextUtils;

import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.libcommon.AppUtil;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProxy {

    private static final boolean PRINT_LOG = LogUtil.isEnableDebug();
    public static final String HEADER_AUTH = "authorization";
    public static final String HEADER_APPID = "appid";
    private static final String KEY_CHANNEL = "APP_CHANNEL";
    private static final String KEY_SERVER_URL = "SERVER_URL";
    private static final String CHANNEL_TEST = "Test";
    private static String DOMAIN_URL;

    private static final HashMap<String, Retrofit> RETROFITS = new HashMap(4);

    static {
        DOMAIN_URL = AppUtil.getMetaDataByKey(BaseApplication.getAPPContext(), KEY_SERVER_URL);
    }

    public static final String API_URL = DOMAIN_URL + BaseApplication.getVersionName() + "/";

    static class InstanceHolder {
        final static ApiProxy instance = new ApiProxy();
    }

    public static ApiProxy getInstance() {
        return ApiProxy.InstanceHolder.instance;
    }

    private ApiProxy() {
        Retrofit retrofit = initRetrofit(API_URL);
        RETROFITS.put(API_URL, retrofit);
    }

    private Retrofit initRetrofit(final String baseUri) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor((message) -> {
            LogUtil.i("retrofit = " + message);
        });

        if (PRINT_LOG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        Interceptor baseInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppUtil.isNetWorkAvailable(BaseApplication.getAPPContext())) {
                    /**
                     * 离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间
                     */
                    int maxStale = 60 * 60 * 24 * 56; // 离线时缓存保存8周,单位:秒
                    CacheControl tempCacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(maxStale, TimeUnit.SECONDS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(tempCacheControl)
                            .build();
                    LogUtil.i("intercept:no network ");
                }
                return chain.proceed(request);
            }
        };

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String authorization = LocalAccountInfoManager.getInstance().getAuthorization();
                Request.Builder builder = chain.request().newBuilder();
                if (!TextUtils.isEmpty(authorization)) {
                    builder.addHeader(HEADER_AUTH, authorization);
                }

                builder.addHeader(HEADER_APPID, String.valueOf(3001));

                Response response = chain.proceed(builder.build());
                return response;
            }
        };

        //只有 网络拦截器环节 才会写入缓存,在有网络的时候 设置缓存时间
        Interceptor rewriteCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response originalResponse = chain.proceed(request);
                int maxAge = 3; // 在线缓存在1分钟内可读取 单位:秒
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }
        };

        //设置缓存路径 内置存储
        Cache cache = null;
        if (BaseApplication.getAPPContext() != null) {
            File httpCacheDirectory = new File(BaseApplication.getAPPContext().getCacheDir(), "responses");
            //外部存储
//        File httpCacheDirectory = new File(SunApplication.getInstance().getExternalCacheDir(), "responses");
            //设置缓存 100M
            int cacheSize = 200 * 1024 * 1024;
            cache = new Cache(httpCacheDirectory, cacheSize);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
//                .addInterceptor(baseInterceptor)
                .addInterceptor(headerInterceptor)
                .addNetworkInterceptor(rewriteCacheControlInterceptor)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUri)
                .build();
    }

    private Retrofit getRetrofit(final String baseUri) {
        if (baseUri == null || baseUri.length() == 0) {
            // return the default
            return RETROFITS.get(API_URL);
        }
        Retrofit specialRetrofit = RETROFITS.get(baseUri);
        if (specialRetrofit == null) {
            specialRetrofit = initRetrofit(baseUri);
            RETROFITS.put(baseUri, specialRetrofit);
        }
        return specialRetrofit;
    }

    /**
     * 每个模块自己定义自己的API
     *
     * @param service
     * @return
     */
    public <T> T getApi(final Class<T> service) {
        return getApi(service, API_URL);
    }


    /**
     * @param service
     * @param baseUri
     * @param <T>
     * @return
     */
    public <T> T getApi(final Class<T> service, String baseUri) {
        return getRetrofit(baseUri).create(service);
    }

}
