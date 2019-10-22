package com.tik.android.component.market.bussiness.database;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by wangqiqi on 2018/11/23.
 */
public class LocalMarketDataSource implements MarketDataSource {
    private MarketStockDao stockDao;

    public LocalMarketDataSource(MarketStockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public List<Long> insertDatas(List<MarketStockEntity> entities) {
        return stockDao.insertDatas(entities);
    }

    @Override
    public Flowable<List<MarketStockEntity>> getMarketStocks() {
        return stockDao.getMarketStocks();
    }

    @Override
    public Long insertData(MarketStockEntity searchEntity) {
        return stockDao.insertData(searchEntity);
    }

    @Override
    public Flowable<List<MarketStockEntity>> getFavorStocks() {
        return stockDao.getFavorStocks();
    }

    @Override
    public Single<List<MarketStockEntity>> getHistory() {
        return stockDao.getHistory();
    }

    @Override
    public void clearSearchData() {
        stockDao.clearSearchData();
    }

    @Override
    public Flowable<List<MarketStockEntity>> getAllStocks() {
        return stockDao.getAllStocks();
    }
}
