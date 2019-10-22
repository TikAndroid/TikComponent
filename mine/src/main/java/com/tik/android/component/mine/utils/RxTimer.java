package com.tik.android.component.mine.utils;

import android.content.Context;

import com.tik.android.component.basemvp.RxUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @describe : schedule a async task
 * @usage : {@link #shotOnce(long, Context, IRxNext)} will call the {@link IRxNext#doNext(long)}
 *      first: implement the {@link IRxNext#doNext(long)}
 *        sec: do your business on {@link IRxNext#doNext(long)}
 *
 * </p>
 * Created by tanlin on 2018/11/15
 */
public class RxTimer {

    public static void shotOnce(long milliDelay, Context view, final RxTimer.IRxNext next) {
        Observable.timer(milliDelay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(view))
                .safeSubscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (next != null) {
                            next.doNext(aLong);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface IRxNext {
        void doNext(long number);
    }
}
