package com.tik.android.component.bussiness.util;

import android.app.Application;
import android.content.Context;

import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.libcommon.AppUtil;
import com.tik.android.component.libcommon.LogUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 异常上报统一处理类
 *
 * @author baowei
 */
public class CrashAgent {

    private static final String APPID = "65fc39df29";
    private static volatile boolean init = false;
    private static final String KEY_CHANNEL = "APP_CHANNEL";

    public static void init(Application application, boolean debug) {
        if (debug) {
            LogUtil.e("debug mode can not init bugly.");
            return;
        }
        BuglyStrategy buglyStrategy = new BuglyStrategy();
        buglyStrategy.setAppChannel(AppUtil.getMetaDataByKey(application.getApplicationContext(), KEY_CHANNEL));
        buglyStrategy.setBuglyLogUpload(!debug);

        CrashReport.setIsDevelopmentDevice(application, debug);
        User userInfo = LocalAccountInfoManager.getInstance().getUser();
        setUserInfo(application, userInfo);
        Bugly.init(application, APPID, debug, buglyStrategy);

        init = true;
    }

    public static void onError(String tag, Throwable throwable) {
        if (!init) {
            LogUtil.e("bugly has not been initialed");
            return;
        }
        Throwable throwablePrint = new Exception(tag, throwable);
        CrashReport.postCatchedException(throwablePrint);
        LogUtil.e(tag, throwablePrint);
    }

    public static void onError(String tag, String info) {
        if (!init) {
            LogUtil.e("bugly has not been initialed");
            return;
        }
        Throwable throwable = new Exception("tag:" + tag + info);
        CrashReport.postCatchedException(throwable);
        LogUtil.e(tag, throwable);
    }

    public static void setUserInfo(Context context, User user) {
        if (user != null) {
            String userId = user.getId();
            CrashReport.setUserId(userId + ", " + user.getDisplayName());
        }
    }

}
