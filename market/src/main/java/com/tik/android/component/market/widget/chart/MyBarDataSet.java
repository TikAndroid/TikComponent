package com.tik.android.component.market.widget.chart;

import android.text.TextUtils;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.tik.android.component.bussiness.market.bean.KLineData;

import java.util.List;

public class MyBarDataSet extends BarDataSet {
    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    /**
     * 获取k线图barChart柱状图中的颜色
     * @param index
     * @return
     */
    @Override
    public int getColor(int index) {
        boolean isUp = false;
        BarEntry barEntry = getEntryForIndex(index);
        if (barEntry != null) {
            KLineData data = (KLineData) barEntry.getData();
            String price = data.getPrice();
            if (TextUtils.isEmpty(price)) {
                isUp = Double.valueOf(data.getOpen()) >= Double.valueOf(data.getClose());
            } else {
                isUp = Double.valueOf(price) >= Double.valueOf(index > 0 ?
                        ((KLineData) getEntryForIndex(index - 1).getData()).getPrice() : data.getClose());
            }
        }
        if (mColors.size() > 1) {
            return mColors.get(isUp ? 0 : 1);
        } else {
            return super.getColor(index);
        }
    }

}
