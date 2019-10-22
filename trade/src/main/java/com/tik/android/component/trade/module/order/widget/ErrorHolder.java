package com.tik.android.component.trade.module.order.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.widget.placeholderview.core.PlaceHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/12/10
 */
public class ErrorHolder extends PlaceHolder {

    @BindView(R2.id.trade_list_error_img)
    ImageView errorImageView;
    @BindView(R2.id.trade_error_type_tips)
    TextView mErrorTypeTipsView;

    @Override
    public int onCreateView() {
        return R.layout.trade_error_view;
    }

    @Override
    public void onViewCreate(Context context, View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
