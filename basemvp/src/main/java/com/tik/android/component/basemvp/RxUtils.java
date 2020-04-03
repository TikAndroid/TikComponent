package com.tik.android.component.basemvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.LogUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    public static <T> LifecycleTransformer<T> bindToLifecycle(Object view) {
        if (view == null) {
            throw new NullPointerException("BaseView is null");
        }
        if (view instanceof RxActivity) {
            return ((RxActivity) view).bindToLifecycle();
        } else if (view instanceof RxFragment) {
            return ((RxFragment) view).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }

    public static Context getContext(@NonNull BaseView view) {
        if (view instanceof RxActivity) {
            return (RxActivity) view;
        } else if (view instanceof RxFragment) {
            return ((RxFragment) view).getActivity();
        } else {
            return BaseApplication.getAPPContext();
        }
    }


    /**
     * 适用于计算性工作
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxComputationSchedulerHelper() { //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> rxComputationSchedulerHelperForFlowable() { //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 适用于io操作
     * 统一线程处理,在子线程处理，在主线程接收
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() { //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> rxSchedulerHelperForFlowable() { //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //计时器
    public static Observable<Integer> countdown(int time, TimeUnit unit) {
        if (time < 0)
            time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .map(increaseTime -> countTime - increaseTime.intValue())
                .take(countTime + 1);
    }

    public static Observable<Integer> countdown(int time) {
        return countdown(time, TimeUnit.SECONDS);
    }

    //新增
    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static <T> void async(final Object view, final Work<T> work, final Main<T> main) {
        Observable.create((ObservableOnSubscribe<T>) e -> {
            T data = work.get();
            if (data == null) {
                e.onComplete();
                return;
            }
            e.onNext(data);
            e.onComplete();
        })
                .compose(RxUtils.bindToLifecycle(view))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(main::doOnMain, e -> LogUtil.e("", e));
    }

    //新增
    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static <T> void async(final Work<T> work, final Main<T> main) {
        Observable.create((ObservableOnSubscribe<T>) e -> {
            T data = work.get();
            if (data == null) {
                e.onComplete();
                return;
            }
            e.onNext(data);
            e.onComplete();
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(main::doOnMain, e -> LogUtil.e("", e));
    }

    /**
     * NewCacheThreadPool
     *
     * @param runnable
     */
    public static Disposable runIo(Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return Schedulers.io().createWorker().schedule(runnable, delay, unit);
    }
    public static Disposable runIo(Runnable runnable) {
        return Schedulers.io().createWorker().schedule(runnable);
    }

    /**
     * NewFixedThreadPool
     *
     * @param runnable
     */
    public static Disposable runComputation(Runnable runnable) {
        return Schedulers.computation().createWorker().schedule(runnable);
    }

    public static Disposable runComputation(Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return Schedulers.computation().createWorker().schedule(runnable, delay, unit);
    }

    /**
     * 随机抢占cpu资源
     * @param runnable
     */
    public static Disposable runNewThread(Runnable runnable) {
        return Schedulers.newThread().createWorker().schedule(runnable);
    }

    public static Disposable runNewThread(Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return Schedulers.newThread().createWorker().schedule(runnable, delay, unit);
    }

    /**
     * 单线程顺序执行
     * @param runnable
     */
    public static Disposable runSingle(Runnable runnable) {
        return Schedulers.single().createWorker().schedule(runnable);
    }

    public static Disposable runSingle(Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return Schedulers.single().createWorker().schedule(runnable, delay, unit);
    }

    public static Disposable runUI(Runnable runnable) {
        return AndroidSchedulers.mainThread().createWorker().schedule(runnable);
    }

    public static Disposable runUI(Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return AndroidSchedulers.mainThread().createWorker().schedule(runnable, delay, unit);
    }

    /**
     * 绑定生命周期， 无需考虑资源释放问题
     * @param view
     * @param runnable
     * @param delay
     * @param unit
     */
    public static void runSafeUI(final Object view, Runnable runnable, long delay, @NonNull TimeUnit unit) {
        Completable.fromRunnable(runnable)
                .compose(RxUtils.bindToLifecycle(view))
                .delay(delay, unit)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public interface Work<T> {
        T get();
    }

    public interface Main<T> {
        void doOnMain(T t);
    }

}
