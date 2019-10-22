package com.tik.android.component.bussiness.webview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @describe : 1. 基础WebView封装
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/11.
 */
public class HoxWebView extends WebView {


    public HoxWebView(Context context) {
        this(context, null);
    }

    public HoxWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoxWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void removeJavascriptInterface(@NonNull String name) {
        super.removeJavascriptInterface(name);
    }

    private void init(Context context) {
        initWebSettings(context);
        initWebChromeClient();
        initWebViewClient();
    }

    private void initWebViewClient() {
        setWebViewClient(new WebViewClient()
//        {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                JsBridgeImpl.getInstance().injectJs(view);
//            }
//        }
        );

    }

    private void initWebChromeClient() {
        setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//                if (JsBridgeImpl.getInstance().callJsPrompt(message, result)) {
//                    return true;
//                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    private void initWebSettings(Context context) {
        WebSettings webSettings = getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(context.getDir("cache", Context.MODE_PRIVATE).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //移除部分系统JavaScript接口
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }
}
