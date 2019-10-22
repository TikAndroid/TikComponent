package com.tik.android.component.bussiness.webview.jsbridge.entity;

import android.support.annotation.UiThread;

/**
 * @describe : Js回调
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public interface JsCallback {
    @UiThread
    void apply(Object... args);
}
