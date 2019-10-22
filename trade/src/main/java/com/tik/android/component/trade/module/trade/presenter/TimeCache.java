package com.tik.android.component.trade.module.trade.presenter;

import android.support.annotation.NonNull;

import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.libcommon.LogUtil;

import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * @describe : 根据时间进行cache缓存的工具类
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/28.
 */
public class TimeCache<T> {
    private Callable<T> mCallable;
    private Flowable<T> mFlowable;
    private long mCacheTime;
    private T mDefaultValue;

    private long mLastTime;
    private T mData;

    public TimeCache(@NonNull Callable<T> callable, long cacheTime) {
        this(callable, null, cacheTime, null);
    }

    public TimeCache(@NonNull Callable<T> callable, long cacheTime, T defaultValue) {
        this(callable, null, cacheTime, defaultValue);
    }

    public TimeCache(@NonNull Flowable<T> flowable, long cacheTime) {
        this(null, flowable, cacheTime, null);
    }

    public TimeCache(@NonNull Flowable<T> flowable, long cacheTime, T defaultValue) {
        this(null, flowable, cacheTime, defaultValue);
    }

    public TimeCache(Callable<T> callable, Flowable<T> flowable, long cacheTime, T defaultValue) {
        mCallable = callable;
        mFlowable = flowable;
        mCacheTime = cacheTime;
        mDefaultValue = defaultValue;
    }

    public T get() {
        return get(false);
    }

    public T get(boolean invalidate) {
        T data = null;
        if (invalidate || shouldRefresh()) {
            try {
                if (mFlowable != null) {
                    data = mFlowable.blockingFirst();
                } else {
                    data = mCallable.call();
                }
            } catch (Exception e) {
                LogUtil.e("get error: " + e.getMessage());
                if (mDefaultValue != null) { // 优先使用默认数据
                    data = mDefaultValue;
                } else { // 使用上次的数据兜底
                    data = mData;
                }
            }
            updateCache(data);
        } else {
            data = mData;
        }

        return data;
    }

    public Flowable<T> getFlowable() {
        return getFlowable(false);
    }

    public Flowable<T> getFlowable(final boolean invalidate) {
        if (mFlowable != null) {
            if (invalidate || shouldRefresh()) {
                return  mFlowable.doOnNext(this::updateCache)
                        .firstOrError().toFlowable()
                        .onErrorResumeNext(throwable -> {
                            LogUtil.e("get flowable error: " + throwable.getMessage());
                            if (mDefaultValue != null) { // 发送默认数据
                                return Flowable.just(mDefaultValue);
                            } else if (mData != null) { // 发送上一次的数据
                                return Flowable.just(mData);
                            } else { // 发送空数据
                                return Flowable.empty();
                            }
                        })
                        .compose(RxUtils.rxSchedulerHelperForFlowable());
            } else {
                return Flowable.just(mData).compose(RxUtils.rxSchedulerHelperForFlowable());
            }
        } else {
            return Flowable.create((FlowableOnSubscribe<T>) emitter -> {
                T data = get(invalidate);
                if (data != null) {
                    emitter.onNext(data);
                }
                emitter.onComplete();
            }, BackpressureStrategy.ERROR)
                    .compose(RxUtils.rxSchedulerHelperForFlowable());
        }
    }

    private boolean shouldRefresh() {
        return mData == null || System.currentTimeMillis() - mLastTime > mCacheTime;
    }

    private void updateCache(T data) {
        mData = data;
        mLastTime = System.currentTimeMillis();
    }

}
