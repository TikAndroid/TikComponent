package com.tik.android.component.bussiness.service.property;

import com.tik.android.component.bussiness.property.bean.CoinAssets;
import com.tik.android.component.bussiness.property.bean.CoinInfo;
import com.tik.android.component.bussiness.property.bean.Rate;
import com.tik.android.component.bussiness.property.bean.StockAssets;

import java.util.List;

import io.reactivex.Flowable;

public interface IPropertyService {
    /**
     * 获取数字货币资产
     */
    Flowable<List<CoinAssets>> getCoinAssets();

    /**
     * 获取股票资产
     */
    Flowable<List<StockAssets>> getStockAssets();

    /**
     * 获取汇率
     */
    Flowable<Rate> getRate();

    Flowable<List<CoinInfo>> getCoinInfo();
}
