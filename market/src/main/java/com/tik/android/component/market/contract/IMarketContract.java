package com.tik.android.component.market.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import java.util.List;

/**
 * Created by wangqiqi on 2018/11/22.
 */
public interface IMarketContract {
    interface View extends BaseView {
        void showMarketStocksPrice(List<MarketStockEntity> entityList);
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 根据股票代码获取股票信息
         * @param marketStocks 股票symbol
         * @param type 股票类型
         */
        void getMarketStocksPrice(String marketStocks, String type);

        /**
         * 获取用户自选列表
         */
        void getUserFavors();

        /**
         * 按照type进行排序，刷新UI
         * @param type 排序类型
         * @param index 展示列表类型
         * @param order 顺序类型 降序、升序、原始
         */
        void sortStock(String type, int index, int order);

        /**
         * 离线情况下，从数据库获取行情信息
         * 区分：联网状态下使用 getMarketPriceFromDB
         * @param type 股票类型
         */
        void getMarketLocalFromDB(String type);

        /**
         * 离线情况下，从数据库获取用户自选列表
         */
        void getLocalUserFavor();

        /**
         * 联网情况下，从数据库获取需要查询的股票，通过服务器获取这些股票的最新信息
         * @param type 股票类型
         */
        void getMarketPriceFromDB(String type);
    }
}
