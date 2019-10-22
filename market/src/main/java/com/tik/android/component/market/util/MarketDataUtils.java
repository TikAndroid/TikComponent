package com.tik.android.component.market.util;

import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.market.bean.StockSearchResult;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import java.util.List;

/**
 * Created by wangqiqi on 2018/11/30.
 */
public class MarketDataUtils {
    public static boolean areFavorTheSame(List<String> oldFavor, List<String> newFavor) {
        if (oldFavor.size() != newFavor.size()) {
            return false;
        }

        for (String s : oldFavor) {
            if (!newFavor.contains(s)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 将服务返回的 StockPriceInfo 转为 MarketStockEntity
     * @param info
     * @return
     */
    public static MarketStockEntity convertPriceEntity(StockPriceInfo info) {
        MarketStockEntity entity = new MarketStockEntity();
        entity.setName(info.getName());
        entity.setCname(info.getCname());
        entity.setSymbol(info.getSymbol());
        entity.setGains(info.getGains());
        entity.setGainsValue(info.getGainsValue());
        entity.setPrice(info.getPrice());
        entity.setCurrency(info.getCurrency());

        boolean isLocal = UserDataManager.getInstance().getLocalSymbols().contains(entity.getSymbol());

        if (isLocal) {
            entity.setLocal(true);
        } else {
            entity.setLocal(false);
        }

        return entity;
    }

    public static MarketStockEntity convertSearchEntity(StockSearchResult searchResult) {
        MarketStockEntity entity = new MarketStockEntity();
        entity.setName(searchResult.getName());
        entity.setCname(searchResult.getCname());
        entity.setSymbol(searchResult.getSymbol());
        entity.setGains(searchResult.getGains());
        entity.setGainsValue(searchResult.getGainsValue());
        entity.setPrice(searchResult.getPrice());
        entity.setCurrency(searchResult.getCurrency());
        return entity;
    }

}
