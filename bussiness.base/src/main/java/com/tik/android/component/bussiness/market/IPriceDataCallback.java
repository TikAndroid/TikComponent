package com.tik.android.component.bussiness.market;

import com.tik.android.component.bussiness.market.bean.StockPriceInfo;

/**
 * Created by jianglixuan on 2018/11/23
 */
public interface IPriceDataCallback {
    void onSuccess(StockPriceInfo info, String symbol);

    void onError();
}
