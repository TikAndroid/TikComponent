package com.tik.android.component.bussiness.webview.jsbridge;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
public interface IJsBridge {

    /**
     * initialize
     */
    public void init();

    /**
     * inject JavaScripts when {@link android.webkit.WebViewClient#onPageFinished(WebView, String)}
     * Call in Main Thread
     * @param webView
     */
    public void injectJs(@NonNull WebView webView);

    /**
     * invoke this when {@link android.webkit.WebChromeClient#onJsPrompt(WebView, String, String, String, JsPromptResult)}
     * @param methodArgs
     * @param result
     * @return
     */
    public boolean callJsPrompt(@NonNull String methodArgs, @NonNull JsPromptResult result);

    /**
     * release sources in {@link Activity#onDestroy()}
     */
    public void release();
}
