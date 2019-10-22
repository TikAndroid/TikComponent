package com.tik.android.component.mine.utils;

import android.text.TextUtils;

import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.AppUtil;
import com.tik.android.component.mine.bean.VersionInfo;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/14
 */
public class AppVersionCheckUtil {

    public static final int NO_VERSION_NEW = -1;
    public static final int VERSION_LATEST = 0;
    public static final int HAS_VERSION_NEW = 1;

    private AppVersionCheckUtil() {
    }

    /**
     *
     * @param newVersionInfo new version info from server
     * @return 1: has new app version
     *         0: the current app version is the latest
     *        -1: current version is upper than server, should not be happen.
     */
    public static int needUpdate(VersionInfo newVersionInfo) {
        if(newVersionInfo == null || TextUtils.isEmpty(newVersionInfo.getCurrentVersion())) {
            return NO_VERSION_NEW;
        }
        String curVersion = AppUtil.getProjectVersionName(BaseApplication.getAPPContext());
        return newVersionInfo.getCurrentVersion().compareTo(curVersion);
    }
}
