package com.tik.android.component.market.widget.chart;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.FormatUtils;

import java.util.Date;

/**
 * 用于格式化横轴时间
 *
 * Created by jianglixuan on 2018/11/16
 */
public abstract class BarAXisValueFormatter implements IAxisValueFormatter {
    //横轴显示的时间格式：3种
    // "HH:mm"，"MM-dd"，"yyyy-MM"
    public String mDisplayTimeFormat = Constant.TIME_SHARING_YY_MM;

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return calculateFormattedValue((int) value);
    }

    public abstract String calculateFormattedValue(int index);

    public abstract void needUpdateValueRange();

    public void setDisplayTimeFormat(String displayTimeFormat) {
        this.mDisplayTimeFormat = displayTimeFormat;
    }

    public String formatLabelTime(int index, @NonNull KLineData kline) {
        String formatTime = "";
        Date date = kline.getDate();
        if (!TextUtils.isEmpty(kline.getTime())) {
            switch (mDisplayTimeFormat) {
                case Constant.TIME_SHARING_YY_MM:
                    formatTime = FormatUtils.changedDateFormat(date, Constant.SOURCE_TIME_STRING[1]);
                    break;
                case Constant.TIME_SHARING_HH_MM:
                    formatTime = FormatUtils.changedDateFormat(date, Constant.TIME_LABEL_MARK_TIME);
                    break;
                case Constant.TIME_SHARING_MM_DD:
                    int formatDateType = FormatUtils.getFormatDateType(kline.getTime());
                    formatTime = FormatUtils.changedDateFormat(date, formatDateType == 0 ? Constant.TIME_LABEL_MARK_TIME : Constant.SOURCE_TIME_STRING[1]);
                    break;
            }
        }
        if (TextUtils.isEmpty(formatTime)) {
            formatTime = calculateFormattedValue(index);
        }
        return formatTime;
    }
}
