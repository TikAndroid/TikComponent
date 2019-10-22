package com.tik.android.component.widget.placeholderview;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tik.android.component.widget.placeholderview.core.PlaceHolder;
import com.tik.android.component.widget.placeholderview.core.PlaceHolderManager;
import com.tik.android.component.widget.placeholderview.core.WrapperContext;
import com.tik.android.component.widget.placeholderview.core.WrapperContextTransformer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @describe :
 *         ErrorView,EmptyView,LoadingView以及其他一些公共占位符的统一封装
 *         主要原理， 就是在目标view在parent的位置，增加一个包含所有占位view的PlaceHolderLayout用于展示各式各样的站位view
 * @usage :
 * 1.注册 ：
 *      new PlaceHolderView.Config()
 *              .addPlaceHolder(ErrorPlaceHolder.class, EmptyPlaceHolder.class, LoadingPlaceHolder.class)
 *              .install();
 *
 * 2.在具体的View/Activity/fragment上绑定 ：
 *      PlaceHolderView.getDefault().bind(View)
 *
 * 3.通过PlaceHolderManager来控制Show/Hide。
 *      PlaceHolderManager.showPlaceHolder(EmptyPlaceHolder.class);
 *      et...
 *
 * 4.可以在View/Activity/fragment生命周期结束时，释放
 *      PlaceHolderManager.release();
 * <p>
 * </p>
 * Created by caixi on 2018/11/23.
 */
public class PlaceHolderView implements IPlaceHolderView {

    @NonNull
    private Config config;
    private static volatile PlaceHolderView sInstance;

    public static PlaceHolderView getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("call PlaceHolderView.Config().install() first");
        }
        return sInstance;
    }

    public PlaceHolderView(@NonNull Config config) {
        this.config = config;
    }

    @NonNull
    @Override
    public PlaceHolderManager bind(@NonNull Activity activity) {
        WrapperContext context = new WrapperContextTransformer().build(activity);
        return new PlaceHolderManager(config, context);
    }

    @NonNull
    @Override
    public PlaceHolderManager bind(@NonNull Fragment fragment) {
        WrapperContext context = new WrapperContextTransformer().build(fragment.getActivity());
        return new PlaceHolderManager(config, context);
    }

    @NonNull
    @Override
    public PlaceHolderManager bind(@NonNull View root) {
        WrapperContext context = new WrapperContextTransformer().build(root);
        return new PlaceHolderManager(config, context);
    }


    public static class Config {
        @NonNull
        private HashSet<Class<? extends PlaceHolder>> placeHolders = new HashSet<>();

        public Config addPlaceHolder(@NonNull Class<? extends PlaceHolder>... holders) {
            placeHolders.addAll(Arrays.asList(holders));
            return this;
        }

        @NonNull
        public HashSet<Class<? extends PlaceHolder>> getPlaceHolders() {
            return placeHolders;
        }

        @MainThread
        public PlaceHolderView build() {
            return new PlaceHolderView(this);
        }

        /**
         * @return 单例类
         */
        @MainThread
        public PlaceHolderView install() {
            if (PlaceHolderView.sInstance == null) {
                synchronized (PlaceHolderView.class) {
                    if (PlaceHolderView.sInstance == null) {
                        PlaceHolderView.sInstance = new PlaceHolderView(this);
                    }
                }
            }
            return PlaceHolderView.sInstance;
        }
    }

}
