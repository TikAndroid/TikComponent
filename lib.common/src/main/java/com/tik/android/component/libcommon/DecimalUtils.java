package com.tik.android.component.libcommon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * 小数处理工具，保留2位小数
 * Created by jianglixuan on 2018/11/26
 */
public class DecimalUtils {
    private static DecimalFormat df = new DecimalFormat("#.##");

    public static String formatValue(double value) {
        return df.format(value);
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
            e.printStackTrace();
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
     *  带有%的百分数String字符串保留2位小数
     * @param data
     * @return
     */
    public static String getPercentNum(String data) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        try {
            Number number = nf.parse(data);
            return number.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 使用逗号分隔的格式
     * @param number
     * @return
     */
    public static String formatDivideNumber(Number number, int decimalCount) {
        return getDivideNumberFormat(decimalCount).format(number);
    }

    public static Number parseDividerNumber(String value, int decimalCount) {
        try {
            return getDivideNumberFormat(decimalCount).parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static DecimalFormat getDivideNumberFormat(int decimalCount) {
        return getDivideNumberFormat(decimalCount, false);
    }

    /**
     * 获取使用逗号分隔的格式
     * @param decimalCount 小数位数
     * @param fixed 小数是否固定位数显示
     * @return
     */
    public static DecimalFormat getDivideNumberFormat(int decimalCount, boolean fixed) {
        StringBuilder format = new StringBuilder(",##0");
        if (decimalCount > 0) {
            String placeholder = fixed ? "0" : "#";
            format.append(".");
            while (decimalCount-- > 0) {
                format.append(placeholder);
            }
        }

        return new DecimalFormat(format.toString());
    }
}
