package com.tik.android.component.trade.module.order.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @describe :
 * @usage :
 *
 * @deprecated use place holder {@link com.tik.android.component.widget.placeholderview.core.PlaceHolder}
 *
 * </p>
 * Created by tanlin on 2018/11/26
 */
public class EmptyViewBinder extends ItemViewBinder<EmptyViewBinder.EmptyData, EmptyViewBinder.EmptyViewHolder> {

    public EmptyViewBinder() {
    }

    @NonNull
    @Override
    protected EmptyViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new EmptyViewHolder(inflater.inflate(R.layout.trade_empty_view, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull EmptyViewHolder holder, @NonNull EmptyData item) {
        holder.setData(item);
        LogUtil.d("onBindViewHolder.." + item);
        if(getAdapter().getItemCount() <= 1) {
            holder.setVisible(View.VISIBLE);
        } else {
            holder.setVisible(View.GONE);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.trade_list_empty_img)
        ImageView emptyImageView;
        @BindView(R2.id.trade_empty_type_tips)
        TextView mEmptyTypeTipsView;
        @BindView(R2.id.trade_empty_action)
        TextView mEmptyActionView;

        private EmptyData mData;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(EmptyData data) {
            mData = data;
            mEmptyTypeTipsView.setText(data.tips);
            mEmptyActionView.setText(data.actionText);
        }

        private void setVisible(int visible) {
            emptyImageView.setVisibility(visible);
            mEmptyTypeTipsView.setVisibility(visible);
            mEmptyActionView.setVisibility(visible);
        }

        @OnClick(R2.id.trade_empty_action)
        public void doAction() {
            if(mData.action != null) {
                mData.action.doEmptyAction(mData.type);
            }
        }
    }

    public static class EmptyData {
        String type;
        String tips;
        String actionText;
        private EmptyAction action;

        public EmptyData() {
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public void setAction(EmptyAction action) {
            if(this.action != action) {
                this.action = action;
            }
        }

        public void setActionText(String actionText) {
            this.actionText = actionText;
        }

        @Override
        public String toString() {
            return "EmptyData{" +
                    "type='" + type + '\'' +
                    ", tips='" + tips + '\'' +
                    ", actionText='" + actionText + '\'' +
                    ", action=" + action +
                    '}';
        }
    }

    public interface EmptyAction {
        void doEmptyAction(String type);
    }
}
