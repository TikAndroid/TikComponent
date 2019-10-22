package com.tik.android.component.bussiness.webview.jsbridge.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.webkit.WebView;

import com.tik.android.component.bussiness.webview.jsbridge.compile.ParserUtil;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public abstract class JsListenerModule extends JsModule {

    /**
     * 执行 JS 回调方法
     * @param callPath
     * @param webView
     * @param args
     */
    @UiThread
    protected static final void callJsListener(@NonNull String callPath, @NonNull WebView webView,
                                               @Nullable Object...args) {
        if (TextUtils.isEmpty(callPath) || webView == null) {
            return;
        }
        ParserUtil.callJsMethod(callPath, webView, args);
    }
}
