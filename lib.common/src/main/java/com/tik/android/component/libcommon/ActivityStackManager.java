package com.tik.android.component.libcommon;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

public class ActivityStackManager implements Application.ActivityLifecycleCallbacks {
    /**
     * TAG
     */
    private final String TAG = "ActivityStackManager";

    /**
     * 窗口栈。
     */
    private LinkedList<Activity> mActivityStack = new LinkedList<>();

    private int mActivityCount;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 得到指定activity前一个activity的实例
     *
     * @param curActivity 当前activity
     * @return 可能为null
     */
    public Activity getPreviousActivity(Activity curActivity) {
        final LinkedList<Activity> activities = mActivityStack;
        Activity preActivity = null;
        for (int cur = activities.size() - 1; cur >= 0; cur--) {
            Activity activity = activities.get(cur);
            if (activity == curActivity) {
                int pre = cur - 1;
                if (pre >= 0) {
                    preActivity = activities.get(pre);
                }
            }
        }

        return preActivity;
    }

    /**
     * 从栈管理队列中移除该Activity。
     *
     * @param activity Activity。
     */
    private void removeFromStack(Activity activity) {
        mActivityStack.remove(activity);

//        printActivityStack();
    }

    /**
     * 将Activity加入栈管理队列中。
     *
     * @param activity Activity。
     */
    private void addToStack(Activity activity) {
        mActivityStack.add(activity);

//        printActivityStack();
    }

    /**
     * 清除栈队列中的所有Activity。
     */
    public void clearActivityStack() {
        int size = mActivityStack.size();
        if (size > 0) {
            Activity[] activities = new Activity[size];
            mActivityStack.toArray(activities);

            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }

    /**
     * 获得Activity栈
     */
    public Activity[] getActivityStack() {
        Activity[] activities = new Activity[mActivityStack.size()];
        return mActivityStack.toArray(activities);
    }

    /**
     * 打印出当前activity stack的信息
     */
    private void printActivityStack() {
        int size = mActivityStack.size();
        if (size > 0) {
            LogUtil.i("Activity stack begin ======== ");
            LogUtil.i("    The activity stack: ");
            for (int i = size - 1; i >= 0; --i) {
                Activity activity = mActivityStack.get(i);
                LogUtil.i("   Activity" + (i + 1) + " = " + activity.getClass().getSimpleName());
            }
            LogUtil.i("Activity stack end ========== ");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addToStack(activity);
    }


    @Override
    public void onActivityStarted(Activity activity) {
        mActivityCount++;
        if (mActivityCount == 1) {
            LogUtil.i("从后台切到前台");
            if (foregroundStatusListener != null) {
                foregroundStatusListener.isForeground(true);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActivityCount--;
        if (mActivityCount == 0) {
            LogUtil.i("从前台切到后台");

            if (foregroundStatusListener != null) {
                foregroundStatusListener.isForeground(false);
            }
        }
    }

    /**
     * 监听是否进入后台
     */
    public interface OnForegroundStatusListener {
        void isForeground(boolean foreground);
    }

    private OnForegroundStatusListener foregroundStatusListener;

    public void setForegroundStatusListener(OnForegroundStatusListener listener) {
        foregroundStatusListener = listener;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        removeFromStack(activity);
    }

    private static class Holder {
        private static ActivityStackManager INSTANCE = new ActivityStackManager();
    }

    public boolean isForeground() {
        if (mActivityCount > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Activity getCurrentActivity() {
        int size = mActivityStack.size();
        if (size > 0) {
            return mActivityStack.get(size - 1);
        }
        return null;
    }

    /**
     * 清除栈队列中的所有Activity，除了特定的一个Activity。
     */
    public void finishActivitiesWithoutOne(String specialClassName) {
        int size = mActivityStack.size();
        if (size > 0) {
            Activity[] activities = new Activity[size];
            mActivityStack.toArray(activities);

            for (Activity activity : activities) {
                if (!activity.getClass().getName().equals(specialClassName)) {
                    activity.finish();
                }
            }
        }
    }
}
