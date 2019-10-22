package com.tik.android.component.trade.module.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tik.android.component.trade.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by jianglixuan on 2018/11/27
 */
public class AnimChartViewBinder extends ItemViewBinder<String, AnimChartViewBinder.ViewHolder>  {
    private final AnimChartView.OnAnimChartListener mListener;

    public AnimChartViewBinder(AnimChartView.OnAnimChartListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.trade_anim_chart_view, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String symbol) {
        holder.updateSymbol(symbol);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AnimChartView animChartView = (AnimChartView) itemView;
            animChartView.setOnAnimChartListener(mListener);
        }

        public void updateSymbol(String symbol) {
            AnimChartView animChartView = (AnimChartView) itemView;
            animChartView.refresh(symbol);
        }

        public void refresh() {
            AnimChartView animChartView = (AnimChartView) itemView;
            animChartView.refresh();
        }
    }
}
