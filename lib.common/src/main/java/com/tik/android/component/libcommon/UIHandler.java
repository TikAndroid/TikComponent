package com.tik.android.component.libcommon;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

/**
 * 全局Handler处理类
 * <p>
 * warn:针对个别需要用到的场景，尽量都用rxjava的线程切换处理
 */
public class UIHandler {
    private static final Handler sHandler;

    static {
        sHandler = new Handler(Looper.getMainLooper());
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static boolean post(Runnable r) {
        return sHandler.post(r);
    }

    public static boolean postDelayed(Runnable r, long delayMillis) {
        return sHandler.postDelayed(r, delayMillis);
    }

    public static void removeCallbacks(Runnable r) {
        sHandler.removeCallbacks(r);
    }

    public static void postIdle(Runnable runnable, long timeout) {
        post(new IdleRunnable(runnable, timeout));
    }

    private static class IdleRunnable implements Runnable {

        private long timeout;
        private Runnable runnable;

        private Runnable idleRunnable = new Runnable() {
            @Override
            public void run() {
                Looper.myQueue().removeIdleHandler(idleHandler);
                if (runnable != null) {
                    runnable.run();
                }
            }
        };
        private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                removeCallbacks(idleRunnable);
                if (runnable != null) {
                    runnable.run();
                }
                return false;
            }
        };

        IdleRunnable(Runnable runnable, long timeout) {
            this.timeout = timeout;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            Looper.myQueue().addIdleHandler(idleHandler);
            postDelayed(idleRunnable, timeout);
        }

    }
}
