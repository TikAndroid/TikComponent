package com.tik.android.component.market.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by wangqiqi on 2018/12/3.
 */
public class NoFavorViewBinder extends ItemViewBinder<MarketStockEntity, NoFavorViewBinder.ViewHolder> {
    private OnButtonClick listener;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.market_nofavor, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MarketStockEntity item) {
        holder.setOnButtonClickListener(listener);
    }

    public interface OnButtonClick {
        void onItemClick();
    }

    public void setOnButtonClickListener(OnButtonClick clickListener) {
        this.listener = clickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private OnButtonClick listener;

        @OnClick(R2.id.add_favor)
        void goToSearch() {
            listener.onItemClick();
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnButtonClickListener(OnButtonClick clickListener) {
            this.listener = clickListener;
        }
    }
}
