package com.tik.android.component.market.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.libcommon.DecimalUtils;
import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.util.Constant;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by wangqiqi on 2018/11/24.
 */
public class MarketViewBinder extends ItemViewBinder<MarketStockEntity, MarketViewBinder.ViewHolder> {
    private OnResultItemClick resultItemClick;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.market_tab_item, parent, false);

        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MarketStockEntity item) {
        holder.setResultItemClick(resultItemClick);
        holder.showResult(item);
    }

    public void setResultItemClick(OnResultItemClick listener) {
        this.resultItemClick = listener;
    }

    public interface OnResultItemClick {
        void onItemClick(MarketStockEntity entity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private OnResultItemClick mListener;
        private MarketStockEntity mItem;

        @NonNull
        @BindView(R2.id.layout_market_tab_item)
        View root;
        @BindView(R2.id.market_stock_currency)
        ImageView currency;
        @BindView(R2.id.market_stock_name)
        TextView stockName;
        @BindView(R2.id.market_stock_symbol)
        TextView stockSymbol;
        @BindView(R2.id.market_price)
        TextView stockPrice;
        @BindView(R2.id.market_gains)
        TextView stockGains;
        @BindView(R2.id.market_gains_rate)
        TextView stockGainsRate;

        @OnClick(R2.id.layout_market_tab_item)
        public void skipStockChart() {
            // 跳转到个股详情界面
            mListener.onItemClick(mItem);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.root = itemView;
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void setResultItemClick(OnResultItemClick listener) {
            this.mListener = listener;
        }

        public void showResult(MarketStockEntity item) {
            if (item == null) return;
            this.mItem = item;
            stockName.setText(item.getCname());
            stockSymbol.setText(item.getSymbol());
            String gains = item.getGains();
            String gainsValue = item.getGainsValue();

            if (0 == item.getPrice()) {
                stockPrice.setText("-");
            } else {
                stockPrice.setText(String.valueOf(item.getPrice()));
            }

            if (null == item.getGains()) {
                stockGains.setText("-");
            } else {
                boolean isProfit = (Float.valueOf(DecimalUtils.getPercentNum(gains)) > 0);

                if (isProfit) {
                    stockGains.setTextColor(mContext.getResources().getColor(R.color.market_color_gains_up));
                    stockGains.setText("+" + gains);
                } else {
                    stockGains.setTextColor(mContext.getResources().getColor(R.color.market_color_gains_down));
                    stockGains.setText(gains);
                }
            }

            if (null == item.getGainsValue()) {
                stockGainsRate.setText("-");
            } else {
                boolean isProfit = (Float.valueOf(DecimalUtils.formatValue(gainsValue)) > 0);

                if (isProfit) {
                    stockGainsRate.setTextColor(mContext.getResources().getColor(R.color.market_color_gains_up));
                    stockGainsRate.setText("+" + gainsValue);
                } else {
                    stockGainsRate.setTextColor(mContext.getResources().getColor(R.color.market_color_gains_down));
                    stockGainsRate.setText(gainsValue);
                }
            }

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_tag_hk);

            if (Constant.USD_STOCK_CURRENCY.equals(item.getCurrency())) {
                drawable = mContext.getResources().getDrawable(R.drawable.ic_tag_us);
            } else if (Constant.HKD_STOCK_CURRENCY.equals(item.getCurrency())) {
                drawable = mContext.getResources().getDrawable(R.drawable.ic_tag_hk);
            }

            currency.setImageDrawable(drawable);
        }
    }
}
