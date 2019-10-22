package com.tik.android.component.market.bussiness.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tik.android.component.market.util.Constant;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by wangqiqi on 2018/11/23.
 */
@Dao
public interface MarketStockDao {
    /**
     * 插入多项股票信息
     * @param entities
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertDatas(List<MarketStockEntity> entities);

    /**
     * 获取本地美股和港股信息（不包含 自选 和 搜索）
     * @return
     */
    @Query("select * from market_stock where local = 1")
    Flowable<List<MarketStockEntity>> getMarketStocks();

    /**
     * 获取本地自选信息
     * @return
     */
    @Query("select * from market_stock where favorite = 1")
    Flowable<List<MarketStockEntity>> getFavorStocks();

    /**
     * 插入一项搜索记录
     * @param searchEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertData(MarketStockEntity searchEntity);

    /**
     * 获取搜索记录
     * @return
     */
    @Query("select * from market_stock where history = 1 order by time desc limit " + Constant.QUERY_LIMIT_NUM)
    Single<List<MarketStockEntity>> getHistory();

    //Todo 更新history值为0!
    @Query("delete from market_stock where history = 1")
    void clearSearchData();

    @Query("select * from market_stock")
    Flowable<List<MarketStockEntity>> getAllStocks();
}
