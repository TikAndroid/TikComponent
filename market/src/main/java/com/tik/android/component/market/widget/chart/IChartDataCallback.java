package com.tik.android.component.market.widget.chart;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by jianglixuan on 2018/11/23
 */
public interface IChartDataCallback {
    void onSuccess(List<Entry> lineEntries, List<BarEntry> barEntries, String symbol, int type);

    void onError();
}
