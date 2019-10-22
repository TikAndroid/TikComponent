package com.tik.android.component.trade.module.trade.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.trade.ui.TradeFragment.TradeHeaderBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/12/1
 */
public class TradeHeaderViewBinder extends ItemViewBinder<TradeHeaderBean, TradeHeaderViewBinder.TradeHeaderViewHolder> {

    private HeaderClickListener mClickListener;

    public interface HeaderClickListener {
        void onClick(View v);

        void onCheckChange(CompoundButton buttonView, boolean isChecked);
    }

    public TradeHeaderViewBinder() {
    }

    public TradeHeaderViewBinder(HeaderClickListener listener) {
        mClickListener = listener;
    }

    @NonNull
    @Override
    protected TradeHeaderViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new TradeHeaderViewHolder(inflater.inflate(R.layout.trade_list_header_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull TradeHeaderViewHolder holder, @NonNull TradeHeaderBean item) {
        holder.setData(item);
    }

    public static class TradeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {
        HeaderClickListener mCallback;

        @BindView(R2.id.trade_list_hide_other)
        Switch mSwitch;
        @BindView(R2.id.trade_list_hide_other_tv)
        TextView mSwitchText;
        @BindView(R2.id.trade_list_header_show_all)
        TextView mShowAll;
        @BindView(R2.id.trade_list_header_show_all_img)
        ImageView mShowAllImg;
        @BindView(R2.id.trade_list_header_title)
        TextView mTitle;

        public TradeHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mSwitch.setOnCheckedChangeListener(this);
            mShowAll.setOnClickListener(this);
            mShowAllImg.setOnClickListener(this);
        }

        private void setData(TradeHeaderBean headerBean) {
            mTitle.setText(R.string.trade_list_header_title);
            mSwitchText.setText(R.string.trade_list_header_hide_other);
            mShowAll.setText(R.string.trade_list_header_show_all);
            mSwitch.setChecked(headerBean.isHideOthers());
        }

        private void setListener(HeaderClickListener listener) {
            mCallback = listener;
        }

        private void release() {
            if (mCallback != null) {
                mCallback = null;
            }
        }

        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onClick(v);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mCallback != null) {
                mCallback.onCheckChange(buttonView, isChecked);
            }
        }
    }

    @Override
    protected void onViewAttachedToWindow(@NonNull TradeHeaderViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setListener(mClickListener);
    }

    @Override
    protected void onViewDetachedFromWindow(@NonNull TradeHeaderViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.release();
    }
}
