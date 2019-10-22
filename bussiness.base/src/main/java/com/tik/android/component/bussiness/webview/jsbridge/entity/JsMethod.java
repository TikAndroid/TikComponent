package com.tik.android.component.bussiness.webview.jsbridge.entity;

import android.support.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @describe : the method which annotated by {@link com.tik.android.component.bussiness.webview.jsbridge.annotation.JsInterface}
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
public class JsMethod {

    /**
     * 对应的java函数
     */
    private Method method;
    /**
     * 所属的 {@link JsModule}
     */
    private JsModule module;
    /**
     * java函数名称
     */
    private String methodName;
    /**
     * 和JavaScript约定的
     */
    private String protocol;

    /**
     * 参数
     */
    private List<Integer> parameterType = new ArrayList<>();

    private boolean hasReturn;

    private boolean isStatic;

    @NonNull public static JsMethod create(JsModule module, Method method, String methodName,
                                           List<Integer> parameterTypeList, boolean hasReturn, String protocol) {
        JsMethod jsMethod = new JsMethod();
        jsMethod.setMethod(method);
        jsMethod.setMethodName(methodName);
        jsMethod.setModule(module);
        jsMethod.setParameterType(parameterTypeList);
        jsMethod.setHasReturn(hasReturn);
        jsMethod.setProtocol(protocol);
        return jsMethod;
    }

    public Object invoke(Object... args) throws Exception {
        if (method != null) {
            method.setAccessible(true);
            return method.invoke(getModule(), args);
        }
        return null;
    }

    /**
     * 注入的JS代码
     *
     * @return
     */
    public String getInjectJs() {
        StringBuilder builder = new StringBuilder();
        builder.append(getMethodName() + ":function(){");
        builder.append("if(!" + getCallback() + ")" + getCallback() + "={};");
        builder.append(String.format(Locale.getDefault(), "return _method(%s,arguments,%d,'%s','%s')",
                getCallback(), 0,
                getModule().getModuleName(), getMethodName()));
        builder.append("}");
        builder.append(",");
        return builder.toString();
    }

    public String getCallback() {
        return String.format("%s.%s.%sCallback", getProtocol(),
                getModule().getModuleName(), getMethodName());
    }

    /* getter and setter*/

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public JsModule getModule() {
        return module;
    }

    public void setModule(JsModule module) {
        this.module = module;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @NonNull public List<Integer> getParameterType() {
        return parameterType;
    }

    public void setParameterType(List<Integer> parameters) {
        this.parameterType = parameters;
    }

    public boolean hasReturn() {
        return hasReturn;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
}
