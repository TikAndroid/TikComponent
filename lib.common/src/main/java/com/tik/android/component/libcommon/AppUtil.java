package com.tik.android.component.libcommon;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;


public class AppUtil {

    public static boolean isNetWorkAvailable(@NonNull Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String getMetaDataByKey(@NonNull Context context, String key) {
        ApplicationInfo appInfo;
        try {
            appInfo = context
                    .getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("getMetaDataByKey key:" + key + ", value:empty");
        return "";
    }

    @NonNull
    public static String getProjectVersionName(@NonNull Context context) {
        String versionName = null;
        try {
            PackageManager pkMgr = context.getPackageManager();
            if (pkMgr != null) {
                PackageInfo pkInfo = pkMgr.getPackageInfo(context.getPackageName(), 0);
                if (pkInfo != null) {
                    versionName = pkInfo.versionName;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(versionName) ? Constants.APP.DEFAULT_VERSION_NAME : versionName;
    }

    public static boolean isInBackgroundThread() {
        return Looper.getMainLooper() != Looper.myLooper();
    }

}
