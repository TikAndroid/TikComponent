package com.tik.android.component.market.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tik.android.component.market.R;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by wangqiqi on 2018/12/14.
 */
public class NoSearchResultViewBinder extends ItemViewBinder<MarketStockEntity, NoSearchResultViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.market_search_no_result, parent,false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MarketStockEntity item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
