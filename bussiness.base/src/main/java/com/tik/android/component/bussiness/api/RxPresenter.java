package com.tik.android.component.bussiness.api;

import android.support.annotation.Nullable;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.error.GlobalErrorUtil;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {
    @Nullable
    protected T mView;
    protected String TAG = this.getClass().getSimpleName();

    @Override
    public void attachView(T view) {
        this.mView = view;
        registerEvent();
    }

    protected void registerEvent() {

    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    /**
     * 适用于多个信号需要链式调用，不会切换回主线程
     *
     * @param observable
     * @param <T>
     * @return
     */
    protected <T> Flowable<T> observeOnIO(Flowable<T> observable) {
        return observable
                .compose(GlobalErrorUtil.handleGlobalError())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    protected <T> Flowable<T> observe(Flowable<T> observable) {
        return observable
                .compose(GlobalErrorUtil.handleGlobalError())
                .compose(RxUtils.<T>rxSchedulerHelperForFlowable());
    }

}
