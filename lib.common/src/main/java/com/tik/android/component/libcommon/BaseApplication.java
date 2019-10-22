package com.tik.android.component.libcommon;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 应用状态与销毁通知
 * 全局Application对象保存，
 */
public class BaseApplication {
    //Todo :更完整更合理的销毁流程会在后续添加
    private static Context mAppContext;
    private static boolean isDebugMode = true;
    private static String sVersionName;

    public static void initContext(Context base) {
        mAppContext = base;
        sVersionName = AppUtil.getProjectVersionName(base);
    }

    public static boolean isDebugMode(){
        return isDebugMode;
    }

    public static void onCreate(boolean debugMode) {
        isDebugMode = debugMode;
    }

    @NonNull public static Context getAPPContext() {
        return mAppContext;
    }

    @NonNull public static String getVersionName() {
        return sVersionName;
    }
}
