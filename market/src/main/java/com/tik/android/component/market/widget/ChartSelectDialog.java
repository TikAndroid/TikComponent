package com.tik.android.component.market.widget;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.util.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChartSelectDialog extends Dialog {

    private ChartDialogListener mChartDialogListener;

    public ChartSelectDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ChartSelectDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.market_bottom_dialog_style);
        init(context);
    }

    public ChartSelectDialog(@NonNull Context context, ChartDialogListener listener) {
        this(context);
        mChartDialogListener = listener;
    }

    public void init(Context context) {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.market_dialog_animation);

        View rootView = View.inflate(context, R.layout.market_popup_chart_selection, null);
        window.setContentView(rootView);
        ButterKnife.bind(this);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R2.id.selection_5_minute, R2.id.selection_15_minute, R2.id.selection_30_minute, R2.id.selection_60_minute})
    public void onItemClick(View view) {
        int id = view.getId();
        if (id == R.id.selection_cancel) {
            cancel();
        } else {
            if (mChartDialogListener != null) {
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
                CharSequence text = ((TextView) view).getText();
                if (text == null) {
                    throw new RuntimeException("this text must  has value !");
                }
                mChartDialogListener.onItemClick(type, text.toString());
            }
            dismiss();
        }
    }

    public void setChartDialogListener(ChartDialogListener listener) {
        mChartDialogListener = listener;
    }

    interface ChartDialogListener {
        void onItemClick(int type, String text);

        void onCancel();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mChartDialogListener != null) {
            mChartDialogListener.onCancel();
        }
    }
}
