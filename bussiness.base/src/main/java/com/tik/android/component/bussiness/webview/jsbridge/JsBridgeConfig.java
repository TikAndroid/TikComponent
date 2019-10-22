package com.tik.android.component.bussiness.webview.jsbridge;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tik.android.component.bussiness.webview.jsbridge.entity.JsModule;
import com.tik.android.component.libcommon.Constants;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
public class JsBridgeConfig {

    /**
     * 当前注册的{@link JsModule}
     */
    private List<Class<? extends JsModule>> modules = new LinkedList<>();
    /**
     * 与JavaScript约定的协议头
     */
    private String protocol;

    @NonNull public List<Class<? extends JsModule>> getModules() {
        return modules;
    }

    public void registerDefaultModule(Class<? extends JsModule>... inputs) {
        if (inputs != null && inputs.length > 0) {
            modules.addAll(Arrays.asList(inputs));
        }
    }

    @NonNull public String getProtocol() {
        return TextUtils.isEmpty(protocol) ? Constants.WebView.DEFAULT_JSBRIDGE_PROTOCOL : protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
