package com.tik.android.component;

import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/12/12.
 */
@Aspect
public class TikAndroidAspect {
    public static final String TAG = "TikAndroidAspect";
    public static boolean sPrintLog = false;
    public static boolean sNeedTrace = true;
    public static final long MS = 1000000L;

    // 方法调用前后
    public static final String POINTCUT_METHOD =
            "(" +
                    // 格式{注解? 修饰符? 返回值类型 类型声明?方法名(参数列表) 异常列表?}
                    // * 匹配任意数量字符（如：一级子包）
                    // .. 匹配任意数量字符的重复（如：所有子包）
                    // + 子类型
                    "execution(* *.*(..)) ||" +
                    "execution(* *(..))" + // 所有方法
                    ")";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotated(){}

    @Around("methodAnnotated()")
    public Object weaveJoinPoint(ProceedingJoinPoint point) throws Throwable{
        Object result = null;
        long time = 0;
        boolean needTrace = sNeedTrace;
        boolean needTime = sPrintLog;
        MethodSignature signature = (MethodSignature) point.getSignature();
        String className = signature.getDeclaringType().getName();
        String traceTag = String.format("m_%s_%s_%s",
                className.substring(className.lastIndexOf(".") + 1), // 类名
                signature.getName(), // 方法名
                point.getSourceLocation().getLine() // 行号
        );
        if(signature.getName().startsWith("access$")){
            needTrace = false;
        }
        if (needTime) {
            time = SystemClock.elapsedRealtimeNanos();
        }
        if (needTrace) {
            Trace.beginSection(traceTag);
        }
        result = point.proceed();
        if (needTrace) {
            Trace.endSection();
        }
        if (needTime) {
            time = SystemClock.elapsedRealtimeNanos() - time;
            String logString = String.format("%s: %s", traceTag, (time / (float)MS));
            if (time > 32 * MS){
                Log.e(TAG, logString);
            } else if (time > 16 * MS){
                Log.w(TAG, logString);
            } else if (time > 8 * MS) {
                Log.i(TAG, logString);
            } else if (time > 4 * MS) {
                Log.d(TAG, logString);
            } else {
                Log.v(TAG, logString);
            }
        }
        return result;
    }
}
