package com.tik.android.component.bussiness.webview.jsbridge.entity;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.text.TextUtils;

import com.tik.android.component.bussiness.webview.jsbridge.compile.ParserUtil;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public class JsCallbackImpl implements JsCallback {

    private String name;
    private JsMethod method;

    public JsCallbackImpl(@NonNull JsMethod method, @NonNull String name) {
        this.method = method;
        this.name = name;
    }

    @Override
    @UiThread
    public void apply(Object... args) {
        if (method == null || method.getModule() == null || method.getModule().getWebView() == null
                || TextUtils.isEmpty(name)) {
            return;
        }
        String callback = method.getCallback();
        final StringBuilder builder = new StringBuilder("javascript:");
        builder.append("if(" + callback + " && " + callback + "['" + name + "']){");
        builder.append("var callback = " + callback + "['" + name + "'];");
        builder.append("if (typeof callback === 'function'){callback(");
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                builder.append(ParserUtil.toJsObject(args[i]));
                if (i != args.length - 1) {
                    builder.append(",");
                }
            }
        }
        builder.append(")}else{console.error(callback + ' is not a function')}}");
        method.getModule().getWebView().loadUrl(builder.toString());
    }
}
