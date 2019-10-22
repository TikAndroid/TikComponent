package com.tik.android.component;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mmkv.MMKV;
import com.tik.android.component.bussiness.util.CrashAgent;
import com.tik.android.component.libcommon.ActivityStackManager;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.widget.Config;
import com.tik.android.component.widget.placeholderview.PlaceHolderView;
import com.tik.android.component.widget.placeholderview.placeholder.EmptyPlaceHolder;
import com.tik.android.component.widget.placeholderview.placeholder.ErrorPlaceHolder;
import com.tik.android.component.widget.placeholderview.placeholder.LoadingPlaceHolder;

import org.qiyi.video.svg.Andromeda;

import java.net.SocketException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import me.jessyan.autosize.AutoSizeConfig;
import me.yokeyword.fragmentation.Fragmentation;

public class TikAndroidApplication extends Application {

    private static TikAndroidApplication sInstance;

    public static TikAndroidApplication getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
        BaseApplication.initContext(base);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this); // multi processes support to do
        CrashAgent.init(this, BuildConfig.DEBUG);
        Andromeda.init(this);
        registerActivityLifecycleCallbacks(ActivityStackManager.getInstance());
//        JsBridgeImpl.getInstance()
//                .registerDefaultModule()
//                .setProtocol(Constants.WebView.DEFAULT_JSBRIDGE_PROTOCOL)
//                .init();
        // 内存注册全局通用的place holder;
        new PlaceHolderView.Config()
                .addPlaceHolder(ErrorPlaceHolder.class, EmptyPlaceHolder.class, LoadingPlaceHolder.class)
                .install();
        initDebugComponents();

        setRxJavaErrorHandler();

        LogUtil.i("configAutoSize:" + BuildConfig.OPEN_AUTO_SIZE);
        if (BuildConfig.OPEN_AUTO_SIZE) {
            configAutoSize();
        }
    }

    private void configAutoSize() {
        Config.OPEN_AUTOSIZE = true;
        AutoSizeConfig.getInstance().setLog(BuildConfig.DEBUG);
        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    private void initDebugComponents() {
        // 通过fragment构建app, 需要fragment栈进行调试
        Fragmentation.builder()
                .debug(BuildConfig.DEBUG)
                .stackViewMode(Fragmentation.BUBBLE)
                .install();
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyDialog() //弹出违规提示对话框
                    .penaltyLog() //在Logcat 中打印违规异常信息
                    .penaltyFlashScreen() //API等级11
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects() //API等级11
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
        }
    }


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            return new MaterialHeader(context);
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20));
    }

    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IndexOutOfBoundsException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            LogUtil.e("Undeliverable exception received, not sure what to do", e);
        });
    }
}
