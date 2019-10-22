package com.tik.android.component.trade.module.order.viewbinder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.order.OrderUtils;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

import static com.tik.android.component.trade.module.TradeConstants.STOCK_CURRENCY_HK;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/30
 */
public class OrderDetailStatusViewBinder extends ItemViewBinder<OrderInfo, OrderDetailStatusViewBinder.StatusViewHolder> {

    private StatusClickListener mStatusClickListener;

    public interface StatusClickListener {
        void onClick(View itemView, Object info);
    }

    public OrderDetailStatusViewBinder(StatusClickListener listener) {
        mStatusClickListener = listener;
    }

    @NonNull
    @Override
    protected StatusViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new StatusViewHolder(inflater.inflate(R.layout.trade_order_detail_status, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull StatusViewHolder holder, @NonNull OrderInfo item) {
        holder.setData(item, mStatusClickListener);
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R2.id.trade_stock_currency)
        ImageView stockCurrency;
        @BindView(R2.id.trade_stock_symbol)
        TextView stockSymbol;
        @BindView(R2.id.trade_stock_full_name)
        TextView stockFullName;
        @BindView(R2.id.trade_status)
        TextView tradeStatus;

        private Context resourceHolder;
        private OrderInfo mOrderInfo;
        private StatusClickListener mClickListener;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceHolder = itemView.getContext();
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(OrderInfo orderInfo, StatusClickListener listener) {
            setData(orderInfo.getTradeInfo(), listener);
        }

        public void setData(TradeInfo tradeInfo, StatusClickListener listener) {
            this.mClickListener = listener;

            if (STOCK_CURRENCY_HK.equals(tradeInfo.getCurrency())) {
                stockCurrency.setImageDrawable(getDrawable(R.drawable.ic_tag_hk));
            } else {
                stockCurrency.setImageDrawable(getDrawable(R.drawable.ic_tag_us));
            }
            stockSymbol.setText(tradeInfo.getStockSymbol());
            stockFullName.setText(OrderUtils.buildStockDisplayName(tradeInfo));
            tradeStatus.setText(tradeInfo.getStatusStr(resourceHolder));
        }

        public void release() {
            if (mClickListener != null) {
                mClickListener = null;
            }
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onClick(v, mOrderInfo);
            }
        }

        private Drawable getDrawable(int drawableId) {
            return ContextCompat.getDrawable(resourceHolder, drawableId);
        }
    }

    @Override
    protected void onViewDetachedFromWindow(@NonNull StatusViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.release();
    }
}
