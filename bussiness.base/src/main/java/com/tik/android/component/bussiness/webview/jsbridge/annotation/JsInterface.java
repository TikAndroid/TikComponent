package com.tik.android.component.bussiness.webview.jsbridge.annotation;

import android.webkit.JavascriptInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @describe : Annotation that allows exposing methods to JavaScript ，
 *             the same use as {@link JavascriptInterface}
 *
 * @usage :
 *          JavaScript调用规则 ： <a href='javascript:${protocol}.${JsModule}.${JsMethod}();'>
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface JsInterface {
    public String methodName() default "";
}
