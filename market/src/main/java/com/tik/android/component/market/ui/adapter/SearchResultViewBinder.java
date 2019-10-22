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

import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.util.Constant;

import org.qiyi.video.svg.Andromeda;
import org.qiyi.video.svg.event.Event;
import org.qiyi.video.svg.event.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/13.
 */
public class SearchResultViewBinder extends ItemViewBinder<MarketStockEntity, SearchResultViewBinder.ViewHolder> {
    private OnResultItemClick mResultItemClick;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.market_search_result_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final @NonNull MarketStockEntity item) {
        holder.setResultItemClick(mResultItemClick);
        holder.showResult(item);
    }

    public interface OnResultItemClick {
        void onItemClick(MarketStockEntity entity);
        void onFavorClick(boolean isFavor, MarketStockEntity item);
    }

    public void setResultItemClick(OnResultItemClick itemClick) {
        mResultItemClick = itemClick;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements EventListener {
        private Context context;
        private MarketStockEntity item;
        private OnResultItemClick mResultItemClick;
        @NonNull
        @BindView(R2.id.search_layout_result)
        View root;
        @NonNull
        @BindView(R2.id.search_stock_name)
        TextView stockName;
        @NonNull
        @BindView(R2.id.search_stock_symbol)
        TextView stockSymbol;
        @NonNull
        @BindView(R2.id.search_stock_currency)
        ImageView stockCurrency;
        @NonNull
        @BindView(R2.id.search_star)
        ImageView stockFavor;

        @OnClick(R2.id.search_star)
        public void updateFavor() {
            boolean newFavor = !item.isFavor();
            stockFavor.setSelected(newFavor);
            item.setFavor(newFavor);
            //数据完全更新前不能再次点击
            stockFavor.setEnabled(false);

            //Todo 自选服务器更新失败后刷回UI
            mResultItemClick.onFavorClick(newFavor, item);
        }

        @OnClick(R2.id.search_layout_result)
        public void skipStockChart() {
            // 跳转到个股详情界面
            mResultItemClick.onItemClick(item);
        }

        public void setResultItemClick(OnResultItemClick itemClick) {
            mResultItemClick = itemClick;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.root = itemView;
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            Andromeda.subscribe(Constant.EVENT_FAVOR_ENABLE, this);
        }

        public void showResult(MarketStockEntity item) {
            this.item = item;
            //Todo 区分中英文
            stockName.setText(item.getCname());
            stockSymbol.setText(item.getSymbol());
            stockFavor.setSelected(item.isFavor());
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_tag_hk);

            if (item.getCurrency().equals(Constant.USD_STOCK_CURRENCY)) {
                drawable = context.getResources().getDrawable(R.drawable.ic_tag_us);
            } else if (item.getCurrency().equals(Constant.HKD_STOCK_CURRENCY)) {
                drawable = context.getResources().getDrawable(R.drawable.ic_tag_hk);
            }

            stockCurrency.setImageDrawable(drawable);
        }

        @Override
        public void onNotify(Event event) {
            if (Constant.EVENT_FAVOR_ENABLE.equals(event.getName())) {
                stockFavor.setEnabled(true);
            }
        }
    }
}
