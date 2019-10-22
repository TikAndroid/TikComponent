package com.tik.android.component.bussiness.market;

/**
 * Created by jianglixuan on 2018/11/23
 */
public interface IChartView {
//    void setSymbol();

    void refreshChartView();

    void refreshChartView(String symbol);

    void refreshChartView(RefreshListener listener);

    void refreshChartView(String symbol, RefreshListener listener);

    interface RefreshListener {
        void onRefreshEnd();
    }
}
