package com.tik.android.component.trade.module.trade.viewbinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tik.android.component.libcommon.ViewUtils;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/12/7.
 */
public class TradeInfoPopWindow extends PopupWindow {

    @BindView(R2.id.trade_tv_popup_fee)
    public TextView mTvFee;
    @BindView(R2.id.trade_tv_popup_rate)
    public TextView mTvRate;

    private int mWidth;
    private int mHeight;

    private int mMargin;

    public TradeInfoPopWindow(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.trade_popup_info, null),
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View content = getContentView();
        ButterKnife.bind(this, content);

        ViewUtils.measureWithWindowSize(context, content);
        mWidth = content.getMeasuredWidth();
        mHeight = content.getMeasuredHeight();
        mMargin = ViewUtils.dp2px(context, 8);

        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void show(View anchor, CharSequence fee, CharSequence rate) {
        mTvFee.setText(fee);
        mTvRate.setText(rate);
        showAsDropDown(anchor, 0, - anchor.getHeight() - mHeight - mMargin);
    }
}
