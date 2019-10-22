package com.tik.android.component.bussiness.webview.jsbridge.jsparams;

import com.tik.android.component.bussiness.webview.jsbridge.entity.JsCallback;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public interface WritableJsMap extends JsMap{

    void putNull(String key);

    void putBoolean(String key, boolean value);

    void putDouble(String key, double value);

    void putInt(String key, int value);

    void putLong(String key, long value);

    void putString(String key, String value);

    void putArray(String key, WritableJsArray value);

    void putMap(String key, WritableJsMap value);

    void putCallback(String key, JsCallback value);

    class Create extends JsMapImpl {
        public Create() {

        }
    }
}
