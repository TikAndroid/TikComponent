package com.tik.android.component.market.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

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
public class SearchResultHeadBinder extends ItemViewBinder<MarketStockEntity, SearchResultHeadBinder.ViewHolder> {
    private OnButtonClickListener mClickListener;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.market_search_result_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MarketStockEntity item) {
        holder.setButtonClickListener(mClickListener);
    }

    public void setButtonClickListener (OnButtonClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface OnButtonClickListener {
        void onButtonClick();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private OnButtonClickListener mClickListener;

        @OnClick(R2.id.tv_clear_history)
        public void clearHistory() {
            mClickListener.onButtonClick();
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setButtonClickListener (OnButtonClickListener clickListener) {
            mClickListener = clickListener;
        }
    }
}
