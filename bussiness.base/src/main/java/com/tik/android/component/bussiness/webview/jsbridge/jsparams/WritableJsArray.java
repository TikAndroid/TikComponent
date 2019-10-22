package com.tik.android.component.bussiness.webview.jsbridge.jsparams;

import com.tik.android.component.bussiness.webview.jsbridge.entity.JsCallback;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public interface WritableJsArray extends JsArray {
    void pushNull();

    void pushBoolean(boolean value);

    void pushDouble(double value);

    void pushInt(int value);

    void pushLong(long value);

    void pushString(String value);

    void pushArray(WritableJsArray value);

    void pushMap(WritableJsMap value);

    void pushCallback(JsCallback value);

    class Create extends JsArrayImpl {
        public Create() {
        }
    }
}
