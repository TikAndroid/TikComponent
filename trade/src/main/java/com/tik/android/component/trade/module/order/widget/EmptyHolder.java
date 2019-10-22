package com.tik.android.component.trade.module.order.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.widget.placeholderview.core.PlaceHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/28
 */
public class EmptyHolder extends PlaceHolder {

    @BindView(R2.id.trade_list_empty_img)
    ImageView emptyImageView;
    @BindView(R2.id.trade_empty_type_tips)
    TextView mEmptyTypeTipsView;
    @BindView(R2.id.trade_empty_action)
    TextView mEmptyActionView;

    @Override
    public int onCreateView() {
        return R.layout.trade_empty_view;
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
