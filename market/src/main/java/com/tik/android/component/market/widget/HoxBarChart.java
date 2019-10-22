package com.tik.android.component.market.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.market.widget.chart.BarAXisValueFormatter;
import com.tik.android.component.market.widget.chart.BarXAxisRenderer;

/**
 * Created by jianglixuan on 2018/11/16
 */
public class HoxBarChart extends BarChart implements BarXAxisRenderer.IXAxisRendererCallback {
    public HoxBarChart(Context context) {
        super(context);
    }

    public HoxBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HoxBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        setXAxisRenderer(new BarXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer, this));
    }

    @Override
    public String getDateForHighlight(Highlight highlight) {
        if (mData != null) {
            Entry entry = mData.getEntryForHighlight(highlight);
            if (entry != null && entry.getData() instanceof KLineData) {
                KLineData data = (KLineData) entry.getData();
                IAxisValueFormatter valueFormatter = mXAxis.getValueFormatter();
                if (valueFormatter instanceof BarAXisValueFormatter) {
                    return ((BarAXisValueFormatter) valueFormatter).formatLabelTime((int) highlight.getX(), data);
                }
                return data.getTime();
            }
        }
        return "";
    }
}
