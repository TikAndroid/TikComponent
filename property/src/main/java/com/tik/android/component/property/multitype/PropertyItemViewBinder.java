package com.tik.android.component.property.multitype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.bussiness.property.bean.StockAssets;
import com.tik.android.component.bussiness.service.trade.ITradeService;
import com.tik.android.component.property.R;
import com.tik.android.component.property.R2;
import com.tik.android.component.property.util.PropertyUtil;

import org.qiyi.video.svg.Andromeda;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

public class PropertyItemViewBinder extends ItemViewBinder<StockAssets, PropertyItemViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.property_item_property, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StockAssets stock) {
        holder.setData(stock);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R2.id.image_tag)
        ImageView mImageTag;
        @Nullable
        @BindView(R2.id.tv_stockName)
        TextView mTvStockName;
        @Nullable
        @BindView(R2.id.tv_symbol)
        TextView mTvSymbol;
        @Nullable
        @BindView(R2.id.tv_volume)
        TextView mTvVolume;
        @Nullable
        @BindView(R2.id.tv_currentTotalPrice)
        TextView mTvCurrentTotalPrice;
        @Nullable
        @BindView(R2.id.tv_current_price)
        TextView mTvCurrentPrice;
        @Nullable
        @BindView(R2.id.tv_holder_price)
        TextView mTvHolderPrice;
        @Nullable
        @BindView(R2.id.tv_profit)
        TextView mTvProfit;
        @Nullable
        @BindView(R2.id.tv_profit_rate)
        TextView mTvProfitRate;
        StockAssets stock;

        @OnClick(R2.id.container)
        public void onItemClick() {
            ITradeService service = Andromeda.getLocalService(ITradeService.class);
            if (service != null) {
                service.navigateTrade(stock.getSymbol());
            }
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(StockAssets stock) {
            this.stock = stock;
            mTvStockName.setText(stock.getStockName());
            mTvSymbol.setText("(" + stock.getSymbol() + ")");
            mTvVolume.setText(stock.getVolume() + "");
            mTvCurrentTotalPrice.setText(PropertyUtil.formatString(stock.getTotalCurrentPrice()));
            mTvCurrentPrice.setText(PropertyUtil.formatString(stock.getCurrentPrice()));
            mTvHolderPrice.setText(PropertyUtil.formatString(stock.getHoldPrice()));
            PropertyUtil.setTextColorByRiseOrFall(mTvProfitRate.getContext(), mTvProfitRate, stock.getProfitRatio());
            mTvProfitRate.setText(PropertyUtil.formatPersentStringPlusOrMinus(stock.getProfitRatio()));
            PropertyUtil.setTextColorByRiseOrFall(mTvProfit.getContext(), mTvProfit, stock.getProfit());
            mTvProfit.setText(PropertyUtil.formatStringPlusOrMinus(stock.getProfit()));

            if (stock.getCurrency().equals("HKD")) {
                mImageTag.setImageDrawable(ContextCompat.getDrawable(mImageTag.getContext(), R.drawable.ic_tag_hk));
            } else {
                 mImageTag.setImageDrawable(ContextCompat.getDrawable(mImageTag.getContext(), R.drawable.ic_tag_us));
            }
        }
    }
}
