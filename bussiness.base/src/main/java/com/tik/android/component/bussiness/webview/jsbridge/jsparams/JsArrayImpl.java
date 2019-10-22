package com.tik.android.component.bussiness.webview.jsbridge.jsparams;

import com.tik.android.component.bussiness.webview.jsbridge.compile.ParserUtil;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsArgumentType;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsCallback;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public class JsArrayImpl extends JSONArray implements WritableJsArray {

    JsArrayImpl() {}

    @Override
    public int size() {
        return super.length();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isNull(int index) {
        return get(index) == null;
    }

    @Override
    public boolean getBoolean(int index) {
        return optBoolean(index);
    }

    @Override
    public double getDouble(int index) {
        return optDouble(index);
    }

    @Override
    public int getInt(int index) {
        return optInt(index);
    }

    @Override
    public long getLong(int index) {
        return optLong(index);
    }

    @Override
    public String getString(int index) {
        Object obj = get(index);
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

    @Override
    public JsMap getMap(int index) {
        return (JsMap) get(index);
    }

    @Override
    public JsArray getArray(int index) {
        return (JsArray) get(index);
    }

    @Override
    public JsCallback getCallback(int index) {
        return (JsCallback) get(index);
    }

    @Override
    public
    @JsArgumentType.Type
    int getType(int index) {
        if (get(index) != null) {
            return ParserUtil.transformType(get(index).getClass());
        }
        return JsArgumentType.TYPE_UNDEFINE;
    }

    @Override
    public Object get(int index) {
        return super.opt(index);
    }

    @Override
    public void pushNull() {
        super.put(null);
    }

    @Override
    public void pushBoolean(boolean value) {
        put(value);
    }

    @Override
    public void pushDouble(double value) {
        try {
            put(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pushInt(int value) {
        put(value);
    }

    @Override
    public void pushLong(long value) {
        put(value);
    }

    @Override
    public void pushString(String value) {
        put(value);
    }

    @Override
    public void pushArray(WritableJsArray value) {
        put(value);
    }

    @Override
    public void pushMap(WritableJsMap value) {
        put(value);
    }

    @Override
    public void pushCallback(JsCallback value) {
        put(value);
    }

    @Override
    public String convertJS() {
        return toString();
    }
}
