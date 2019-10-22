package com.tik.android.component.market.service;

import android.os.Bundle;

import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.service.market.ISearchService;
import com.tik.android.component.market.bussiness.database.MarketOperate;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.util.Constant;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangqiqi on 2018/11/21.
 */
public class SearchServiceImpl implements ISearchService {
    @Override
    public void insertSearchDB(Bundle bundle, Object view) {
        MarketStockEntity entity = bundle.getParcelable(Constant.HISTORY_INSERT_DATA);
        entity.setTime(System.currentTimeMillis());

        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> emitter) throws Exception {
                MarketOperate.getInstance().insertData(entity);
                emitter.onNext(new Object());
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.bindToLifecycle(view))
                .subscribe();
    }
}
