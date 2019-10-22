package com.tik.android.component.libcommon.sharedpreferences;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/15.
 */
public interface IRxSharedPrefer {

    @CheckResult
    @NonNull
    SharedPreferences read();

    @CheckResult
    @NonNull
    SharedPreferences.Editor edit();


    @CheckResult
    @NonNull
    Flowable<SharedPreferences> readAsFlowable();

    @CheckResult
    @NonNull
    Flowable<SharedPreferences.Editor> editAsFlowable();

    /**
     * 回调默认在线程中， 调用者无需关心当前环境是否位于主线程中
     * @param consumer
     */
    void readAsAsyncFlowable(Consumer<SharedPreferences> consumer);

    /**
     * 回调默认在线程中， 调用者无需关心当前线程环境
     * 直接调用 {@link SharedPreferences.Editor#commit()}
     * @param consumer
     */
    void editAsAsyncFlowable(Consumer<SharedPreferences.Editor> consumer);

    public interface Consumer<T> {
        /**
         * 若当前位于主线程， 则在{@link Schedulers#computation()}
         * 若当前位于子线程， 则在当前线程
         * @param t
         */
        void accept(T t);

        void error(String msg);
    }
}
