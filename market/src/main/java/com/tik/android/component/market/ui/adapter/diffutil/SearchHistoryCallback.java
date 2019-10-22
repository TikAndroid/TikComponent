package com.tik.android.component.market.ui.adapter.diffutil;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import java.util.List;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/15.
 */
public class SearchHistoryCallback extends DiffUtil.Callback {

    private final List<MarketStockEntity> oldList;
    private final List<MarketStockEntity> newList;

    public SearchHistoryCallback(@NonNull List<MarketStockEntity> oldList, @NonNull List<MarketStockEntity> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getCname() == newList.get(newItemPosition).getCname();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final MarketStockEntity oldItem = oldList.get(oldItemPosition);
        final MarketStockEntity newItem = newList.get(newItemPosition);
        return newItem.equals(oldItem);
    }
}
