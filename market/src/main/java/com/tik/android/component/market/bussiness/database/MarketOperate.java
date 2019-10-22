package com.tik.android.component.market.bussiness.database;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by wangqiqi on 2018/11/23.
 */
public class MarketOperate {
    private final MarketDataSource mMarketDataSource;
    private static volatile MarketOperate INSTANCE;

    public MarketOperate() {
        HoxDatabase hoxDatabase = HoxDatabase.getInstance();
        mMarketDataSource = new LocalMarketDataSource(hoxDatabase.marketStockDao());
    }

    public static MarketOperate getInstance() {
        if (null == INSTANCE) {
            synchronized (MarketOperate.class) {
                INSTANCE = new MarketOperate();
            }
        }

        return INSTANCE;
    }

    public void reset() {
        if (null != INSTANCE) {
            INSTANCE = null;
        }
    }

    public List<Long> insertDatas(List<MarketStockEntity> entities) {
        return mMarketDataSource.insertDatas(entities);
    }

    public Flowable<List<MarketStockEntity>> getMarketStocks() {
        return mMarketDataSource.getMarketStocks();
    }

    public Long insertData(MarketStockEntity searchEntity) {
        return mMarketDataSource.insertData(searchEntity);
    }

    public Flowable<List<MarketStockEntity>> getFavorStocks() {
        return mMarketDataSource.getFavorStocks();
    }

    public Single<List<MarketStockEntity>> getHistory() {
        return mMarketDataSource.getHistory();
    }

    public void clearSearchData() {
        mMarketDataSource.clearSearchData();
    }

    public Flowable<List<MarketStockEntity>> getAllStocks() {
        return mMarketDataSource.getAllStocks();
    }
}
