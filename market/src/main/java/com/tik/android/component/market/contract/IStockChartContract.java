package com.tik.android.component.market.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;

public interface IStockChartContract {
    interface View extends BaseView {
        void onObtainPriceInfo(StockPriceInfo priceInfo, String symbol);
    }

    interface Presenter extends BasePresenter<View> {
        void updateCurrPriceInfo(String symbol);
    }
}
