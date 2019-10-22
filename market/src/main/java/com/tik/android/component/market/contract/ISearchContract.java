package com.tik.android.component.market.contract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import java.util.List;

public interface ISearchContract {

    interface View extends BaseView {
        void updateData(DiffUtil.DiffResult result);

        void firstFillUpData(List<MarketStockEntity> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getSearchResult(String srcText);

        void clearSearchHistory();

        void loadSearchHistory();

        /**
         * 更新用户自选列表,更新本地数据库
         * @param isFavor
         * @param bundle
         * @param view
         */
        void updateUserFavor(boolean isFavor, Bundle bundle, Object view);
    }
}
