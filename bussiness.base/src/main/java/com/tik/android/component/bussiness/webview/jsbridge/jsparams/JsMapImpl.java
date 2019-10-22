package com.tik.android.component.bussiness.webview.jsbridge.jsparams;

import com.tik.android.component.bussiness.webview.jsbridge.entity.JsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public class JsMapImpl extends JSONObject implements WritableJsMap {
    JsMapImpl() {}

    @Override
    public boolean isEmpty() {
        return super.length() == 0;
    }

    @Override
    public boolean hasKey(String name) {
        return super.has(name);
    }

    @Override
    public boolean isNull(String name) {
        return super.isNull(name);
    }

    @Override
    public Object get(String name) {
        return super.opt(name);
    }

    @Override
    public boolean getBoolean(String name) {
        return optBoolean(name);
    }

    @Override
    public double getDouble(String name) {
        return optDouble(name);
    }

    @Override
    public int getInt(String name) {
        return optInt(name);
    }

    @Override
    public long getLong(String name) {
        return optLong(name);
    }

    @Override
    public String getString(String name) {
        Object obj = get(name);
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

    @Override
    public JsCallback getCallback(String name) {
        if (get(name) != null && get(name) instanceof JsCallback) {
            return ((JsCallback) get(name));
        }
        return null;
    }

    @Override
    public JsMap getJsMap(String name) {
        return (JsMap) get(name);
    }

    @Override
    public JsArray getJsArray(String name) {
        return (JsArray) get(name);
    }

    @Override
    public Set<String> keySet() {
        Set<String> sets = new HashSet<>();
        Iterator<String> iterator = super.keys();
        while (iterator.hasNext()) {
            sets.add(iterator.next());
        }
        return sets;
    }

    @Override
    public void putNull(String key) {
        putValue(key, null);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        putValue(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        putValue(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        putValue(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        putValue(key, value);
    }

    @Override
    public void putString(String key, String value) {
        putValue(key, value);
    }

    @Override
    public void putArray(String key, WritableJsArray value) {
        putValue(key, value);
    }

    @Override
    public void putMap(String key, WritableJsMap value) {
        putValue(key, value);
    }

    @Override
    public void putCallback(String key, JsCallback value) {
        putValue(key, value);
    }

    @Override
    public String convertJS() {
        return toString();
    }

    private void putValue(String key, Object value) {
        try {
            super.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
