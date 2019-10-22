package com.tik.android.component.basemvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/21.
 */
public class RouteUtil {

    /**
     * 以startActivity的方式将fragment启动到新的activity
     * @param toFragment 目标fragment，需保证有默认构造方法
     * @param args fragment的参数
     * @param activityOptions startActivity时的可选参数
     */
    public static void startWithActivity(@NonNull BasicFragment from, @NonNull Class<? extends BasicFragment> toFragment,
                                         @Nullable Bundle args, @Nullable Bundle activityOptions) {
        from.startActivity(getIntentWithStartAsActivity(from, toFragment, WrapperActivity.class, args), activityOptions);
    }

    /**
     * 启动其他进程activity
     * @param from
     * @param toFragment
     * @param args
     * @param activityOptions
     */
    public static void startRemoteActivity(@NonNull BasicFragment from, @NonNull Class<? extends BasicFragment> toFragment,
                                         @Nullable Bundle args, @Nullable Bundle activityOptions) {
        from.startActivity(getIntentWithStartAsActivity(from, toFragment, RemoteActivity.class, args), activityOptions);
    }

    /**
     * 以startActivityForResult的方式将fragment启动到新的activity
     * @param toFragment 目标fragment，需保证有默认构造方法
     * @param args fragment的参数
     * @param requestCode startActivityForResult的请求码
     * @param activityOptions startActivity时的可选参数
     */
    public static void startForResultWithActivity(@NonNull BasicFragment from, @NonNull Class<? extends BasicFragment> toFragment,
                                                  @Nullable Bundle args, int requestCode, @Nullable Bundle activityOptions) {
        from.startActivityForResult(getIntentWithStartAsActivity(from, toFragment, WrapperActivity.class, args), requestCode, activityOptions);
    }

    private static Intent getIntentWithStartAsActivity(@NonNull BasicFragment from, @NonNull Class<? extends BasicFragment> toFragment,
                                                       @NonNull Class<? extends BasicActivity> host, @Nullable Bundle args) {
        Intent intent = new Intent(from.getActivity(), host);
        intent.putExtra(WrapperActivity.EXTRA_FRAGMENT, toFragment.getName());
        intent.putExtra(WrapperActivity.EXTRA_FRAGMENT_ARGS, args);
        return intent;
    }
}
