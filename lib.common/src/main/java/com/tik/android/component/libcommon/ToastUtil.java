package com.tik.android.component.libcommon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class ToastUtil {

    private static Toast mToast;

    public static void showToastShort(@NonNull String text) {
        custom(text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(@NonNull CharSequence text) {
        custom(text, Toast.LENGTH_LONG).show();
    }

    public static Toast custom(@NonNull CharSequence message, int duration) {
        return makeToast(message, duration);
    }

    private static boolean isOver810() {
        int result = compareVersion("8.1.0", android.os.Build.VERSION.RELEASE);
        return result == 0 || result == -1;
    }

    private static Toast makeToast(@NonNull CharSequence message, int duration) {
        Context context = BaseApplication.getAPPContext();
        if (isOver810()) {
            mToast = null;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, message, duration);
        } else {
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        return mToast;
    }

    /**
     * 这里reset是在UI基类里做的，并不是所有的Activity都继承了UI.但是mToast本身占用内存很小，暂时不处理。
     * 这里的context为全局，不会出现内存泄露情况
     */
    public static void resetToast() {
        if (mToast != null) {
            mToast = null;
        }
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }


    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        try {
            if (version1.equals(version2)) {
                return 0;
            }
            String[] version1Array = version1.split("\\.");
            String[] version2Array = version2.split("\\.");

            int index = 0;
            // 获取最小长度值
            int minLen = Math.min(version1Array.length, version2Array.length);
            int diff = 0;
            // 循环判断每位的大小
            while (index < minLen && (diff = Integer.parseInt(version1Array[index]) -
                    Integer.parseInt(version2Array[index])) == 0) {
                index++;
            }
            if (diff == 0) {
                // 如果位数不一致，比较多余位数
                for (int i = index; i < version1Array.length; i++) {
                    if (Integer.parseInt(version1Array[i]) > 0) {
                        return 1;
                    }
                }

                for (int i = index; i < version2Array.length; i++) {
                    if (Integer.parseInt(version2Array[i]) > 0) {
                        return -1;
                    }
                }
                return 0;
            } else {
                return diff > 0 ? 1 : -1;
            }
        } catch (Exception e) {
            return 0;
        }
    }

}
