package com.tik.android.component.property;

import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.error.GlobalErrorUtil;
import com.tik.android.component.bussiness.property.bean.CoinAssets;
import com.tik.android.component.bussiness.property.bean.CoinInfo;
import com.tik.android.component.bussiness.property.bean.Rate;
import com.tik.android.component.bussiness.property.bean.StockAssets;
import com.tik.android.component.bussiness.service.property.IPropertyService;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/27.
 */
public class PropertyServiceImpl implements IPropertyService {
    @Override
    public Flowable<List<CoinAssets>> getCoinAssets() {
        return observeApi(ApiProxy.getInstance().getApi(PropertyApi.class).getCoin().map(listResult -> {
            if (listResult == null || listResult.getData() == null) {
                throw new RuntimeException("PropertyServiceImpl getCoinAssets result is null");
            }

            return listResult.getData();
        }));
    }

    @Override
    public Flowable<List<StockAssets>> getStockAssets() {
        return observeApi(ApiProxy.getInstance().getApi(PropertyApi.class).getStock().map(listResult -> {
            if (listResult == null || listResult.getData() == null) {
                throw new RuntimeException("PropertyServiceImpl getStockAssets result is null");
            }

            return listResult.getData();
        }));
    }

    @Override
    public Flowable<Rate> getRate() {
        return observeApi(ApiProxy.getInstance().getApi(PropertyApi.class).getRate().map(rateResult -> {
            if (rateResult == null || rateResult.getData() == null) {
                throw new RuntimeException("PropertyServiceImpl getRate result is null");
            }

            return rateResult.getData();
        }));
    }

    @Override
    public Flowable<List<CoinInfo>> getCoinInfo() {
        return observeApi(ApiProxy.getInstance().getApi(PropertyApi.class).getCoinInfo().map(listResult -> {
            if (listResult == null || listResult.getData() == null) {
                throw new RuntimeException("PropertyServiceImpl getRate result is null");
            }

            return listResult.getData();
        }));
    }


    private  <T> Flowable<T> observeApi(Flowable<T> observable) {
        return observable
                .compose(GlobalErrorUtil.handleGlobalError())
                .compose(RxUtils.rxSchedulerHelperForFlowable());
    }
}
