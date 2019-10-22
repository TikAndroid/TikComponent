package com.tik.android.component.property.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.sharedpreferences.RxSharedPrefer;
import com.tik.android.component.property.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.tik.android.component.property.multitype.HeaderViewBinder.SHOULD_HIDE;

public class PropertyUtil {
    /**
     * 将double类型的数据转换成以3位逗号隔开的字符串，并且保留两位有效数字，不四舍五入
     * @param data
     * @return
     */
    public static String formatString(double data) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }

    /**
     * 将double类型的数据转换成以3位逗号隔开的字符串，保留两位有效数字，并且根据涨跌加上 "+","-"
     * @param data
     * @return
     */
    public static String formatStringPlusOrMinus(double data) {
        if (data > 0) {
            return "+" + formatString(data);
        } else {
            return formatString(data);
        }
    }

    /**
     * 以%的形式保留到小数点后两位
     *
     * @param data
     * @return
     */
    public static String formatPersentStringPlusOrMinus(double data) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        if (data > 0) {
            return "+" + nf.format(data);
        } else {
            return nf.format(data);
        }
    }

    /**
     * 以%的形式保留到小数点后两位
     *
     * @param data
     * @return
     */
    public static void setTextColorByRiseOrFall(Context context, TextView tv, double data) {
        if (data > 0) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.property_red_rise));
        } else if (data < 0) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.property_green_fall));
        }
    }

    public static boolean getShouldHideValue() {
        return RxSharedPrefer
                .builder()
                .context(BaseApplication.getAPPContext())
                .build()
                .read()
                .getBoolean(SHOULD_HIDE, false);
    }

    public static void putShouldHideValue(boolean shouldHide) {
        RxSharedPrefer.builder().context(BaseApplication.getAPPContext()).build()
                .edit().putBoolean(SHOULD_HIDE, shouldHide).apply();
    }
}
