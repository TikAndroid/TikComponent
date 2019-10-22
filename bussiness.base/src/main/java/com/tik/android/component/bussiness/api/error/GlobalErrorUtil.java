package com.tik.android.component.bussiness.api.error;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.bussiness.api.error.exception.ConnectFailedException;
import com.tik.android.component.bussiness.api.error.exception.ResponseException;
import com.tik.android.component.bussiness.api.error.retry.RetryConfig;
import com.tik.android.component.libcommon.LogUtil;

import java.net.ConnectException;

import io.reactivex.Observable;
import io.reactivex.Single;

public class GlobalErrorUtil {

    private static final int SUCCESS_CODE = 0;

    public static <T> GlobalErrorTransformer<T> handleGlobalError() {
        return new GlobalErrorTransformer<T>(
                (it) -> {
                    if (it instanceof Result) {
                        Result result = (Result) it;
                        if (result.getCode() != SUCCESS_CODE) {
                            return Observable.error(new ResponseException(result.getMessage(), result.getCode()));
                        }
                    }
                    return Observable.just(it);
                },
                (error) -> {
                    if (error instanceof ConnectException) {
                        return Observable.error(new ConnectFailedException());
                    }
                    return Observable.error(error);
                },
                (error) -> {
                    if (error instanceof ConnectFailedException) {
                        return new RetryConfig(() -> Single.just(true)
                        );
                    }
                    return new RetryConfig(0);
                },
                (throwable) -> {
                    LogUtil.e("异常:" + throwable.toString());
                }
        );
    }
}
