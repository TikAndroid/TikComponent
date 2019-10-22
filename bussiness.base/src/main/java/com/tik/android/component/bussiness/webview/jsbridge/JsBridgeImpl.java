package com.tik.android.component.bussiness.webview.jsbridge;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.tik.android.component.bussiness.webview.jsbridge.annotation.JsInterface;
import com.tik.android.component.bussiness.webview.jsbridge.compile.JsArgumentParser;
import com.tik.android.component.bussiness.webview.jsbridge.compile.ParserUtil;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsArgumentType;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsMethod;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsModule;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsStaticModule;
import com.tik.android.component.libcommon.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @describe :
 *           1. 定义{@link JsModule} 并且 注册 {@link JsInterface}
 *           2. 在Application里面注册 :
 *           JsBridgeImpl.getInstance()
 *                 .registerDefaultModule(AModule.class, BModule.class) //Module按业务划分， 管理js调用native的函数
 *                 .setProtocol(PROTOCOL) // 与JavaScript约定的协议头
 *                 .init();
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
public class JsBridgeImpl implements IJsBridge {

    @NonNull
    private JsBridgeConfig config;
    private final Map<JsModule, HashMap<String, JsMethod>> exposedMethods;
    private final String className;
    private String preLoad;


    private static class SingletonHolder {
        private static final JsBridgeImpl INSTANCE = new JsBridgeImpl();
    }

    public JsBridgeImpl() {
        config = new JsBridgeConfig();
        exposedMethods = new HashMap<>();
        className = Integer.toHexString(hashCode());
    }

    public static JsBridgeImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public JsBridgeImpl registerDefaultModule(Class<? extends JsModule>... inputs) {
        config.registerDefaultModule(inputs);
        return this;
    }

    public JsBridgeImpl setProtocol(String protocol) {
        config.setProtocol(protocol);
        return this;
    }

    @Override
    public void init() {
        try {
            for (Class<? extends JsModule> clz : config.getModules()) {
                JsModule module = clz.newInstance();
                if (module != null && !TextUtils.isEmpty(module.getModuleName())) {
                    HashMap<String, JsMethod> methods = ParserUtil.parseAllJsMethods(module, config.getProtocol());
                    exposedMethods.put(module, methods);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        preLoad = getInjectJsString();
    }

    @Override
    public void injectJs(final @NonNull WebView webView) {
        evaluateJavascript(webView, preLoad);
    }

    /**
     * 执行JS回调
     * @param methodArgs
     * @param result
     * @return
     */
    @Override
    public boolean callJsPrompt(@NonNull String methodArgs, @NonNull JsPromptResult result) {
        if (TextUtils.isEmpty(methodArgs) || result == null) {
            return false;
        }
        JsArgumentParser argumentParser = JsArgumentParser.parse(methodArgs);
        if (argumentParser != null ){
            String module = argumentParser.getModule();
            String method = argumentParser.getMethod();
            if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(method)) {
                JsModule findModule = getModule(module);
                if (findModule != null) {
                    HashMap<String, JsMethod> methodHashMap = exposedMethods.get(findModule);
                    if (methodHashMap != null && !methodHashMap.isEmpty() && methodHashMap.containsKey(method)) {
                        JsMethod jsMethod = methodHashMap.get(method);
                        List<JsArgumentParser.Parameter> parameters = argumentParser.getParameters();
                        int length = jsMethod.getParameterType().size();
                        Object[] invokeArgs = new Object[length];
                        for (int i = 0; i < length; ++i) {
                            @JsArgumentType.Type int type = jsMethod.getParameterType().get(i);
                            if (parameters != null && parameters.size() >= i + 1) {
                                JsArgumentParser.Parameter param = parameters.get(i);
                                Object parseObject = ParserUtil.parseToObject(type, param, jsMethod);
                                if (parseObject != null && parseObject instanceof RuntimeException) {
                                    setJsPromptResult(result, false, parseObject.toString());
                                    return true;
                                }
                                invokeArgs[i] = parseObject;
                            }
                            if (invokeArgs[i] == null) {
                                switch (type) {
                                    case JsArgumentType.TYPE_NUMBER:
                                        invokeArgs[i] = 0;
                                        break;
                                    case JsArgumentType.TYPE_BOOL:
                                        invokeArgs[i] = false;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        try {
                            Object ret = jsMethod.invoke(invokeArgs);
                            setJsPromptResult(result, true, ret == null ? "" : ret);
                        } catch (Exception e) {
                            setJsPromptResult(result, false, "Error: " + e.toString());
                            LogUtil.e("Call JsMethod <" + jsMethod.getMethodName() + "> Error", e);
                        }
                        return true;
                    }
                }
            }
            setJsPromptResult(result, false, "JBArgument Parse error");
            return true;
        }
        return false;
    }

    /**
     * native call js method
     * @param jsCode
     */
    private void evaluateJavascript(@NonNull WebView webView, String jsCode) {
        webView.loadUrl("javascript:" + jsCode);
    }

    @Override
    public void release() {
        exposedMethods.clear();
    }

    @Nullable private JsModule getModule(@NonNull String moduleName) {
        for (JsModule module : exposedMethods.keySet()) {
            if (moduleName.equals(module.getModuleName())) {
                return module;
            }
        }
        return null;
    }

    private String getInjectJsString() {
        StringBuilder builder = new StringBuilder();
        builder.append("var " + className + " = function () {");
        // 注入通用方法
        builder.append(ParserUtil.getUtilMethods(config.getProtocol()));
        // 注入默认方法
        for (JsModule module: exposedMethods.keySet()) {
            HashMap<String, JsMethod> methods = exposedMethods.get(module);
            if (methods == null || methods.keySet() == null) {
                continue;
            }
            if (module instanceof JsStaticModule) {
                for (String method : methods.keySet()) {
                    JsMethod jsMethod = methods.get(method);
                    builder.append(jsMethod.getInjectJs());
                }
            } else {
                builder.append(className + ".prototype." + module.getModuleName() + " = {");
                if (methods != null && methods.keySet() != null) {
                    for (String method : methods.keySet()) {
                        JsMethod jsMethod = methods.get(method);
                        builder.append(jsMethod.getInjectJs());
                    }
                }
                builder.append("};");
            }
        }
        builder.append("};");
        builder.append("window." + config.getProtocol() + " = new " + className + "();");
        builder.append(config.getProtocol() + ".OnJsBridgeReady();");
        return builder.toString();
    }

    private void setJsPromptResult(JsPromptResult promptResult, boolean success, Object msg) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("success", success);
            ret.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        promptResult.confirm(ret.toString());
    }
}
