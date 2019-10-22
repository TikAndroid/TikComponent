package com.tik.android.component.bussiness.webview.jsbridge.compile;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.webkit.WebView;

import com.tik.android.component.bussiness.webview.jsbridge.annotation.JsInterface;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsArgumentType;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsCallbackImpl;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsMethod;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsModule;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.tik.android.component.bussiness.webview.jsbridge.entity.JsRunMethodFactory.*;
import com.tik.android.component.bussiness.webview.jsbridge.jsparams.JsArray;
import com.tik.android.component.bussiness.webview.jsbridge.entity.JsCallback;
import com.tik.android.component.bussiness.webview.jsbridge.jsparams.JsMap;
import com.tik.android.component.bussiness.webview.jsbridge.jsparams.JsObject;
import com.tik.android.component.bussiness.webview.jsbridge.jsparams.WritableJsArray;
import com.tik.android.component.bussiness.webview.jsbridge.jsparams.WritableJsMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
public class ParserUtil {

    private static List<Class> validParameterList = Arrays.asList(Integer.class, Float.class, Double.class, String.class,
            Boolean.class, JsCallback.class, JsMap.class, JsArray.class,
            int.class, float.class, double.class, boolean.class);

    /**
     * 获取所有JsModule下的注解JsMethod
     * @param module
     * @param protocol
     * @return
     */
    public static HashMap<String, JsMethod> parseAllJsMethods(JsModule module, String protocol) {
        HashMap<String, JsMethod> methods = new HashMap<>();
        Class clz = module.getClass();
        Method[] declaredMethods = clz.getDeclaredMethods();
        if (declaredMethods == null || declaredMethods.length == 0) {
            return methods;
        }
        for (Method method : declaredMethods) {
            String name = method.getName();
            int modifiers = method.getModifiers();
            // 静态函数， 抽象函数直接略过
            if (TextUtils.isEmpty(name) || Modifier.isAbstract(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            // 是否是标记让javascript调用的native函数
            JsInterface annotation = method.getAnnotation(JsInterface.class);
            if (annotation == null) {
                continue;
            }
            if (!TextUtils.isEmpty(annotation.methodName())) {
                name = annotation.methodName();
            }
            boolean hasReturn = !"void".equals(method.getReturnType().getName());
            Class[] parameters = method.getParameterTypes();
            List<Integer> parameterTypeList = new ArrayList<>();
            for (Class cls : parameters) {
                if (!parameterIsValid(cls)) {
                    throw new IllegalArgumentException("Method " + method.getName() + " parameter is not Valid");
                }
                parameterTypeList.add(transformType(cls));
            }
            JsMethod jsMethod = JsMethod.create(module, method, name, parameterTypeList, hasReturn, protocol);
            methods.put(name, jsMethod);
        }

        return methods;
    }

    public static String getUtilMethods(@NonNull String protocol) {
        StringBuilder builder = new StringBuilder();
        String loadReadyMethod = String.format("on%sReady", protocol);
        AbsJsRunMethod[] methods = new AbsJsRunMethod[]{new GetType(), new ParseFunction(),
                new OnJsBridgeReady(loadReadyMethod), new CallJava(),
                new Printer(), new CallMethod()
        };
        for (AbsJsRunMethod method : methods) {
            builder.append(method.getMethod());
        }
        return builder.toString();
    }

    public static Object parseToObject(@JsArgumentType.Type int type, JsArgumentParser.Parameter parameter,
                                       JsMethod method) {
        if (type == JsArgumentType.TYPE_INT || type == JsArgumentType.TYPE_FLOAT
                || type == JsArgumentType.TYPE_DOUBLE) {
            if (parameter.getType() != JsArgumentType.TYPE_NUMBER) {
                return new RuntimeException("parameter error, expect <number>");
            }
            try {
                switch (type) {
                    case JsArgumentType.TYPE_INT:
                        return Integer.parseInt(parameter.getValue());
                    case JsArgumentType.TYPE_FLOAT:
                        return Float.parseFloat(parameter.getValue());
                    case JsArgumentType.TYPE_DOUBLE:
                        return Double.parseDouble(parameter.getValue());
                    case JsArgumentType.TYPE_LONG:
                        return Long.parseLong(parameter.getValue());
                }
            } catch (NumberFormatException e) {
                return new RuntimeException(e.getMessage());
            }
        } else if (type == JsArgumentType.TYPE_STRING) {
            return parameter.getValue();
        } else if (type == JsArgumentType.TYPE_FUNCTION) {
            if (parameter.getType() != JsArgumentType.TYPE_FUNCTION) {
                return new RuntimeException("parameter error, expect <function>");
            }
            return new JsCallbackImpl(method, parameter.getName());
        } else if (type == JsArgumentType.TYPE_OBJECT) {
            if (parameter.getType() != JsArgumentType.TYPE_OBJECT) {
                return new RuntimeException("parameter error, expect <object>");
            }
            try {
                JSONObject jsonObject = new JSONObject(parameter.getValue());
                return parseObjectLoop(jsonObject, method);
            } catch (JSONException e) {
                return null;
            }
        } else if (type == JsArgumentType.TYPE_ARRAY) {
            if (parameter.getType() != JsArgumentType.TYPE_ARRAY) {
                return new RuntimeException("parameter error, expect <array>");
            }
            try {
                JSONArray jsonArray = new JSONArray(parameter.getValue());
                return parseObjectLoop(jsonArray, method);
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 转化为 Js 对象
     * @param javaObject
     * @return
     */
    public static String toJsObject(Object javaObject) {
        if (javaObject == null || javaObject instanceof Integer || javaObject instanceof Float
                || javaObject instanceof Double || javaObject instanceof Long
                || javaObject instanceof Boolean) {
            return "" + javaObject;
        } else if (javaObject instanceof JsObject) {
            return ((JsObject) javaObject).convertJS();
        } else {
            String format = javaObject.toString().replaceAll("'", "\\\\\'").replace("\"","\\\"");
            return "'" + format + "'";
        }
    }

    /**
     * parse object loop
     *
     * @param parser
     * @param method
     * @return
     */
    private static Object parseObjectLoop(Object parser, JsMethod method) {
        if (parser != null) {
            if (parser instanceof BigDecimal) {
                return ((BigDecimal) parser).doubleValue();
            } else if (parser instanceof String) {
                String str = (String) parser;
                if (str.startsWith("[Function]::")) {
                    String[] function = str.split("::");
                    if (function.length == 2 && !TextUtils.isEmpty(function[1])) {
                        return new JsCallbackImpl(method, function[1]);
                    }
                }
            } else if (parser instanceof JSONObject) {
                WritableJsMap writableJsMap = new WritableJsMap.Create();
                JSONObject jsonObject = (JSONObject) parser;
                for (Iterator<String> iterator = jsonObject.keys(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    Object ret = parseObjectLoop(jsonObject.opt(key), method);
                    if (ret == null) {
                        writableJsMap.putNull(key);
                        continue;
                    }
                    if (ret instanceof Integer) {
                        writableJsMap.putInt(key, (Integer) ret);
                    } else if (ret instanceof Double) {
                        writableJsMap.putDouble(key, (Double) ret);
                    } else if (ret instanceof Long) {
                        writableJsMap.putLong(key, (Long) ret);
                    } else if (ret instanceof String) {
                        writableJsMap.putString(key, (String) ret);
                    } else if (ret instanceof Boolean) {
                        writableJsMap.putBoolean(key, (Boolean) ret);
                    } else if (ret instanceof WritableJsArray) {
                        writableJsMap.putArray(key, (WritableJsArray) ret);
                    } else if (ret instanceof WritableJsMap) {
                        writableJsMap.putMap(key, (WritableJsMap) ret);
                    } else if (ret instanceof JsCallback) {
                        writableJsMap.putCallback(key, (JsCallback) ret);
                    } else {
                        writableJsMap.putNull(key);
                    }
                }
                return writableJsMap;
            } else if (parser instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) parser;
                WritableJsArray writableJBArray = new WritableJsArray.Create();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object ret = parseObjectLoop(jsonArray.opt(i), method);
                    if (ret == null) {
                        writableJBArray.pushNull();
                        continue;
                    }
                    if (ret instanceof Integer) {
                        writableJBArray.pushInt((Integer) ret);
                    } else if (ret instanceof Double) {
                        writableJBArray.pushDouble((Double) ret);
                    } else if (ret instanceof Long) {
                        writableJBArray.pushLong((Long) ret);
                    } else if (ret instanceof String) {
                        writableJBArray.pushString((String) ret);
                    } else if (ret instanceof Boolean) {
                        writableJBArray.pushBoolean((Boolean) ret);
                    } else if (ret instanceof WritableJsArray) {
                        writableJBArray.pushArray((WritableJsArray) ret);
                    } else if (ret instanceof WritableJsMap) {
                        writableJBArray.pushMap((WritableJsMap) ret);
                    } else if (ret instanceof JsCallback) {
                        writableJBArray.pushCallback((JsCallback) ret);
                    } else {
                        writableJBArray.pushNull();
                    }
                }
                return writableJBArray;
            }
        }
        return parser;
    }

    /**
     * 将native参数映射到js
     *
     * @param cls
     * @return
     */
    public static @JsArgumentType.Type int transformType(Class cls) {
        if (cls.equals(Integer.class) || cls.equals(int.class)) {
            return JsArgumentType.TYPE_INT;
        } else if (cls.equals(Float.class) || cls.equals(float.class)) {
            return JsArgumentType.TYPE_FLOAT;
        } else if (cls.equals(Double.class) || cls.equals(double.class)) {
            return JsArgumentType.TYPE_DOUBLE;
        } else if (cls.equals(Long.class) || cls.equals(long.class)) {
            return JsArgumentType.TYPE_LONG;
        } else if (cls.equals(Boolean.class) || cls.equals(boolean.class)) {
            return JsArgumentType.TYPE_BOOL;
        } else if (cls.equals(String.class)) {
            return JsArgumentType.TYPE_STRING;
        } else if (cls.equals(JsArray.class)) {
            return JsArgumentType.TYPE_ARRAY;
        } else if (cls.equals(JsMap.class)) {
            return JsArgumentType.TYPE_OBJECT;
        } else if (cls.equals(JsCallback.class)) {
            return JsArgumentType.TYPE_FUNCTION;
        }
        return JsArgumentType.TYPE_UNDEFINE;
    }

    /**
     * 执行js回调
     * @param methodName
     * @param webView
     * @param args
     */
    @UiThread
    public static void callJsMethod(@NonNull String methodName, @NonNull final WebView webView,
                                    @Nullable Object... args) {
        if (TextUtils.isEmpty(methodName) || webView == null) {
            return;
        }
        final StringBuilder builder = new StringBuilder("javascript:");
        builder.append("try{");
        builder.append("var callback=");
        builder.append(methodName);
        builder.append(";");
        builder.append("if (callback && typeof callback === 'function'){callback(");
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                builder.append(ParserUtil.toJsObject(args[i]));
                if (i != args.length - 1) {
                    builder.append(",");
                }
            }
        }
        builder.append(")}");
        builder.append("}catch(e){}");
        webView.loadUrl(builder.toString());
    }

    /**
     * native 注册方法参数是否符合要求
     *
     * @param cls
     * @return
     */
    public static boolean parameterIsValid(Class cls) {
        return validParameterList.contains(cls);
    }
}
