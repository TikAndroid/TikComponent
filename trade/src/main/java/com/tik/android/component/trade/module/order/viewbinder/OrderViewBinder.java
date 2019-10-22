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

import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.order.OrderUtils;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

import static com.tik.android.component.trade.module.TradeConstants.STOCK_CURRENCY_HK;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_DIRECTION_BUY;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_DIRECTION_SELL;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/23
 */
public class OrderViewBinder extends ItemViewBinder<OrderInfo, OrderViewBinder.ViewHolder> {

    private ItemClickListener mItemClickListener;
    public interface ItemClickListener {
        void onClick(View itemView, OrderInfo info);
    }

    public OrderViewBinder(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        // replaced by trade_item_view_relative
        return new ViewHolder(inflater.inflate(R.layout.trade_item_view_relative, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull OrderInfo data) {
        holder.setData(data);
    }

    /**
     * maybe used by trade page
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context resourceHolder;
        // first line
        @BindView(R2.id.trade_stock_currency)
        ImageView stockCurrency;
        @BindView(R2.id.trade_stock_symbol)
        TextView stockSymbol;
        @BindView(R2.id.trade_stock_full_name)
        TextView stockFullName;
        @BindView(R2.id.trade_status)
        TextView tradeStatus;

        // second line
        @BindView(R2.id.trade_orientation)
        TextView tradeOrientation;
        @BindView(R2.id.trade_average_price)
        TextView tradeAveragePrice; // final price
        @BindView(R2.id.trade_number_ratio)
        TextView tradeNumber;


        private OrderInfo mOrderInfo;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            resourceHolder = itemView.getContext();
        }

        void setData(OrderInfo orderInfo) {
            mOrderInfo = orderInfo;
            setData(orderInfo.getTradeInfo());
        }

        private void setData(TradeInfo tradeInfo) {
            if(tradeInfo == null) {
                LogUtil.e("error: item trade info is null");
                return;
            }
            if (STOCK_CURRENCY_HK.equals(tradeInfo.getCurrency())) {
                stockCurrency.setImageDrawable(getDrawable(R.drawable.ic_tag_hk));
            } else {
                stockCurrency.setImageDrawable(getDrawable(R.drawable.ic_tag_us));
            }
            stockSymbol.setText(tradeInfo.getStockSymbol());
            stockFullName.setText(OrderUtils.buildStockDisplayName(tradeInfo));
            tradeStatus.setText(tradeInfo.getStatusStr(resourceHolder));

            // second line
            String type = null;
            int color = 0xffffff;
            switch (tradeInfo.getType()) {
                case TRADE_DIRECTION_BUY:
                    type = resourceHolder.getResources().getString(R.string.trade_order_direction_buy);
                    color = resourceHolder.getResources().getColor(R.color.trade_direction_buy);
                    break;
                case TRADE_DIRECTION_SELL:
                    type = resourceHolder.getResources().getString(R.string.trade_order_direction_sell);
                    color = resourceHolder.getResources().getColor(R.color.trade_direction_sell);
                    break;
            }
            tradeOrientation.setText(type);
            tradeOrientation.setTextColor(color);
            tradeAveragePrice.setText(tradeInfo.getPriceStr(resourceHolder));
            tradeNumber.setText(tradeInfo.getDealNumStr(resourceHolder));
        }
        public void setListener(ItemClickListener listener) {
            itemClickListener = listener;
        }
        public void release() {
            itemClickListener = null;
        }

        private Drawable getDrawable(int drawableId) {
            return ContextCompat.getDrawable(resourceHolder, drawableId);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onClick(v, mOrderInfo);
            }
        }
    }

    @Override
    protected void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setListener(mItemClickListener);
    }

    @Override
    protected void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.release();
    }
}
