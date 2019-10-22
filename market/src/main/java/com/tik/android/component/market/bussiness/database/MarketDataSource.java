package com.tik.android.component.market.bussiness.database;
import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by wangqiqi on 2018/11/23.
 */
public interface MarketDataSource {
    List<Long> insertDatas(List<MarketStockEntity> entities);
    Flowable<List<MarketStockEntity>> getMarketStocks();

    Long insertData(MarketStockEntity searchEntity);
    Flowable<List<MarketStockEntity>> getFavorStocks();
    Single<List<MarketStockEntity>> getHistory();
    void clearSearchData();
    Flowable<List<MarketStockEntity>> getAllStocks();
}
