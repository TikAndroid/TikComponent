package com.tik.android.component.market.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.util.Constant;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianglixuan on 2018/11/20
 */
public class PopupChartSelectWindow extends PopupWindow {
    private boolean mIsClickItem;
    private ChartPopupListener mChartPopupListener;
    @BindViews({R2.id.selection_5_minute, R2.id.selection_15_minute, R2.id.selection_30_minute, R2.id.selection_60_minute})
    List<TextView> mViews;

    public PopupChartSelectWindow(Context context) {
        this(context, null);
    }

    public PopupChartSelectWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupChartSelectWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.market_popup_chart_selection, null);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, view);
        setBackgroundDrawable(new ColorDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        setWidth(context.getResources().getDimensionPixelSize(R.dimen.market_popup_window_width));
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mChartPopupListener != null) {
                    mChartPopupListener.onDismiss();
                    if (!mIsClickItem) {
                        mChartPopupListener.onCancel();
                        return;
                    }
                    mIsClickItem = false;
                }
            }
        });
    }

    @OnClick({R2.id.selection_5_minute, R2.id.selection_15_minute, R2.id.selection_30_minute, R2.id.selection_60_minute})
    public void onItemClick(View view) {
        if (mChartPopupListener != null) {
            int type = getTypeByViewId(view.getId());
            CharSequence text = ((TextView) view).getText();
            if (text != null) {
                mIsClickItem = true;
                mChartPopupListener.onItemClick(type, text.toString());
            }
        }
        dismiss();
    }

    private int getTypeByViewId(int id) {
        int type = 0;
        if (id == R.id.selection_5_minute) {
            type = Constant.TYPE_TIME_SHARING_M5;
        } else if (id == R.id.selection_15_minute) {
            type = Constant.TYPE_TIME_SHARING_M15;
        } else if (id == R.id.selection_30_minute) {
            type = Constant.TYPE_TIME_SHARING_M30;
        } else if (id == R.id.selection_60_minute) {
            type = Constant.TYPE_TIME_SHARING_M60;
        }
        return type;
    }

    public void setChartPopupListener(ChartPopupListener listener) {
        mChartPopupListener = listener;
    }

    public void setSelectItem(int tmpM5Type) {
        //mViews的第一个textview对应索引为2
        if (tmpM5Type - 2 > mViews.size() && tmpM5Type - 2 < 0) {
            return;
        }
        for (int i = 0; i < mViews.size(); i++) {
            mViews.get(i).setSelected(tmpM5Type - 2 == i);
        }
    }

    public interface ChartPopupListener {
        void onItemClick(int type, String text);

        void onCancel();

        void onDismiss();
    }

}
