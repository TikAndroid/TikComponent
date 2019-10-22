package com.tik.android.component.market.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Time;

import com.tik.android.component.libcommon.LogUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {
    private static DecimalFormat df = new DecimalFormat("#.##");

    public static String formatValue(double value) {
        return df.format(value);
    }

    /**
     * 格式化交易量volume，以超过千、百万的以***K/M显示
     *
     * @param volume 交易量
     * @return
     */
    public static String formatVolume(String volume) {
        String unit = "";
        try {
            Double value = Double.valueOf(volume);
            if (value >= 1000000) {
                value /= 1000000;
                unit = "M";
            } else if (value >= 1000) {
                value /= 1000;
                unit = "K";
            }
            return df.format(value) + unit;
        } catch (Exception e) {
        }
        return volume;
    }

    /**
     * 格式化double类型的数字字符串为保留2位小数的字符串
     *
     * @param value
     * @return
     */
    public static String formatValue(String value) {
        try {
            return df.format(Double.valueOf(value));
        } catch (Exception e) {
        }
        return value;
    }

    /**
     *  带有%的百分数String字符串保留2位小数
     * @param data
     * @return
     */
    public static String formatPercentString(String data) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        try {
            Number number = nf.parse(data);
            nf.setMinimumFractionDigits(2);
            return nf.format(number);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 讲时间字符串转换为date
     *
     * @param string
     * @return Date 日期
     */
    public static Date formatStringToTime(String string, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String changedDateFormat(@NonNull Date string, String desPattern) {
        SimpleDateFormat format = new SimpleDateFormat(desPattern);
        return format.format(string);
    }

    /**
     * 确定时间数据格式类型
     *
     * @param time
     * @return 0: "yyyy-MM-dd HH:mm:ss"
     * 1： "yyyy-MM-dd"
     * -1：后端返回时间格式有问题
     */
    public static int getFormatDateType(String time) {
        for (int i = 0; i < Constant.SOURCE_TIME_STRING.length; i++) {
            Date dateMin = formatStringToTime(time, Constant.SOURCE_TIME_STRING[i]);
            if (dateMin != null) {
                return i;
            }
        }
        LogUtil.e("chart date has other type, you should update. data  = " + time);
        return 1;
    }

    /**
     * @param string
     * @return 返回格式为MM/dd HH:mm的时间字符串
     */
    public static String formatTime3339(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        Time time = new Time();
        boolean parse = time.parse3339(string);
        if (parse) {
            string = time.format(Constant.TIME_TITLE_FORMAT);
        }
        return string;
    }
}
