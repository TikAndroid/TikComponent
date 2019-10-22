package com.tik.android.component.market.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.FormatUtils;
import com.tik.android.component.market.util.ResourceUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkInfoView extends MarkerView {
    DecimalFormat df = new DecimalFormat(".00");
    @BindView(R2.id.mark_view_root)
    TextView mText;

    public MarkInfoView(Context context) {
        super(context, R.layout.market_view_mark_stock_info);
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        KLineData data = (KLineData) e.getData();
        SpannableString string;
        if (data != null) {
            String price = data.getPrice();
            if (TextUtils.isEmpty(price)) {
                string = new SpannableString(data.getTime() + "\n\n"
                        + ResourceUtils.getStrPriceHigh() + ResourceUtils.getStrColon() + FormatUtils.formatValue(data.getHight()) + "\n"
                        + ResourceUtils.getStrPriceLow() + ResourceUtils.getStrColon() + FormatUtils.formatValue(data.getLow()) + "\n"
                        + ResourceUtils.getStrPriceOpen() + ResourceUtils.getStrColon() + FormatUtils.formatValue(data.getOpen()) + "\n"
                        + ResourceUtils.getStrPriceClose() + ResourceUtils.getStrColon() + FormatUtils.formatValue(data.getClose()) + "\n"
                        + ResourceUtils.getStrPriceVolume() + ResourceUtils.getStrColon() + data.getVolume());
            } else {
                string = new SpannableString(data.getTime() + "\n\n"
                        + ResourceUtils.getStrCurrPrice() + ResourceUtils.getStrColon() + df.format(Double.valueOf(price)) + "\n"
                        + ResourceUtils.getStrPriceVolume() + ResourceUtils.getStrColon() + data.getVolume());
            }
        } else {
            string = getDefaultSpannableString();
        }
        mText.setText(string);
        super.refreshContent(e, highlight);
    }

    @NonNull
    private SpannableString getDefaultSpannableString() {
        return new SpannableString(Constant.EMPTY_DEFAULT_VALUE + ":" + Constant.EMPTY_DEFAULT_VALUE + "\n\n"
                + ResourceUtils.getStrPriceHigh() + ResourceUtils.getStrColon() + Constant.EMPTY_DEFAULT_VALUE + "\n"
                + ResourceUtils.getStrPriceLow() + ResourceUtils.getStrColon() + Constant.EMPTY_DEFAULT_VALUE + "\n"
                + ResourceUtils.getStrPriceOpen() + ResourceUtils.getStrColon() + Constant.EMPTY_DEFAULT_VALUE + "\n"
                + ResourceUtils.getStrPriceClose() + ResourceUtils.getStrColon() + Constant.EMPTY_DEFAULT_VALUE + "\n"
                + ResourceUtils.getStrPriceVolume() + ResourceUtils.getStrColon() + Constant.EMPTY_DEFAULT_VALUE);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 20);
    }
}
