package com.tik.android.component.bussiness.api;

import com.google.gson.JsonParseException;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.api.error.exception.ResponseException;
import com.tik.android.component.bussiness.base.R;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.LogUtil;

import org.qiyi.video.svg.Andromeda;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * features:
 * <p>
 * 1.A common entrance to RxJava.
 * 2.handle auto show/dismiss loading dialog.
 */
public abstract class NormalSubscriber<T> extends DisposableSubscriber<T> {

    public static final int STATUS_TOKEN_INVALID = 1001401002;//token失效
    public static final int JSON_PARSE_EXCEPTION = -1000;//json解析异常
    public static final int UNKOWN = -1001;


    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("onError:" + e.toString());
        if (e instanceof ResponseException) {
            if (((ResponseException) e).getCode() == STATUS_TOKEN_INVALID) {
//                UIHandler.post(() -> ToastUtil.showToastShort(BaseApplication.getAPPContext().getString(com.tap4fun.hox.lib.style.R.string.token_invalid)));
                LocalAccountInfoManager manager = LocalAccountInfoManager.getInstance();
                IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
                if (accountService != null && !manager.isThirdUser()) {
                    accountService.logout();
                }
            }
            onError(((ResponseException) e).getCode(), e.getMessage());
        } else if (e instanceof JsonParseException) {
            onError(JSON_PARSE_EXCEPTION, e.getMessage());
        } else {
            onError(UNKOWN, BaseApplication.getAPPContext().getString(R.string.unknown));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onError(int errorCode, String errorMsg) {
    }
}
