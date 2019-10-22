package com.tik.android.component.trade.module.order.viewbinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.order.OrdersDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/24
 */
public class OrderDetailViewBinder extends ItemViewBinder<OrdersDetailFragment.ItemBean, OrderDetailViewBinder.DetailViewHolder> {

    public OrderDetailViewBinder() {
    }

    @NonNull
    @Override
    protected DetailViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View rootView = inflater.inflate(R.layout.trade_order_detail_item, parent, false);
        return new DetailViewHolder(rootView);
    }

    protected void onBindViewHolder(@NonNull DetailViewHolder holder, @NonNull OrdersDetailFragment.ItemBean item) {
        holder.setData(item);
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.trade_order_detail)
        TextView detailTitle;
        @BindView(R2.id.trade_order_detail_value)
        TextView detailValue;
        private View itemView;

        private Context resourceHolder;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            resourceHolder = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void setData(OrdersDetailFragment.ItemBean info) {
            detailTitle.setText(info.getTitle());
            detailValue.setText(info.getValue());
            if(info.getColor() > 0) {
                detailValue.setTextColor(resourceHolder.getResources().getColor(info.getColor()));
            }
        }
    }
}
