package com.tik.android.component.property.multitype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tik.android.component.bussiness.service.IAppService;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.CollectionUtils;
import com.tik.android.component.property.R;
import com.tik.android.component.property.R2;
import com.tik.android.component.property.bean.PropertyData;
import com.tik.android.component.property.util.PropertyUtil;

import org.qiyi.video.svg.Andromeda;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

public class HeaderViewBinder extends ItemViewBinder<PropertyData, HeaderViewBinder.ViewHolder> {

    public static final String SHOULD_HIDE = "should_hide";
    public static final String HIDE_TEXT = "****";

    private boolean mShouldHide;
    private OnVisibleClickListener mOnVisibleClickListener;

    public HeaderViewBinder(OnVisibleClickListener onVisibleClickListener) {
        this.mOnVisibleClickListener = onVisibleClickListener;
        this.mShouldHide = PropertyUtil.getShouldHideValue();
    }

    public interface OnVisibleClickListener {
        void onVisibleClick();
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.property_header_property, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PropertyData data) {
        holder.showData(data, mShouldHide);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R2.id.tv_total_assets)
        TextView tvTotalAssets;
        @Nullable
        @BindView(R2.id.tv_stock_assets)
        TextView tvStockAssets;
        @Nullable
        @BindView(R2.id.tv_total_profit)
        TextView tvProfit;
        @Nullable
        @BindView(R2.id.tv_total_profit_rate)
        TextView tvProfitRate;
        @Nullable
        @BindView(R2.id.tv_buy_power)
        TextView tvBuyPower;
        @Nullable
        @BindView(R2.id.tv_freeze_assets)
        TextView tvFreezeAssets;
        @Nullable
        @BindView(R2.id.tv_hk_assets)
        TextView tvHkStockAssets;
        @Nullable
        @BindView(R2.id.tv_us_assets)
        TextView tvUsStockAssets;
        @Nullable
        @BindView(R2.id.tv_assets_details)
        TextView tvAssetsDetails;
        @Nullable
        @BindView(R2.id.ll_no_assets)
        LinearLayout llNoAssets;

        PropertyData data;

        @OnClick(R2.id.tv_choose_assets)
        public void goSelect(View view) {
            IAppService appService = Andromeda.getLocalService(IAppService.class);
            if (appService != null) {
                // 需根据主页进行调整
                appService.switchToTab(IAppService.TAB_INDEX_MARKET, null);
            }
        }

        @OnClick(R2.id.image_visible)
        public void setImageVisible(ImageView imageView) {
            if (!mShouldHide) {
                imageView.setImageDrawable(ContextCompat.getDrawable(BaseApplication.getAPPContext(), R.drawable.icon_invisible));
                PropertyUtil.putShouldHideValue(true);
                showData(data, true);
                mShouldHide = true;
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(BaseApplication.getAPPContext(), R.drawable.icon_visible));
                PropertyUtil.putShouldHideValue(false);
                showData(data, false);
                mShouldHide = false;
            }
            mOnVisibleClickListener.onVisibleClick();
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void showData(PropertyData data, boolean shouldHide) {
            this.data = data;
            tvTotalAssets.setText(shouldHide ? HIDE_TEXT : PropertyUtil.formatString(data.getTotalAssets()));
            tvStockAssets.setText(shouldHide ? HIDE_TEXT : PropertyUtil.formatString(data.getStockAssets()));
            tvProfit.setText(shouldHide ? HIDE_TEXT : PropertyUtil.formatStringPlusOrMinus(data.getProfit()));
            tvProfitRate.setText(shouldHide ? HIDE_TEXT : PropertyUtil.formatPersentStringPlusOrMinus(data.getProfitRatio()));
            tvBuyPower.setText(shouldHide ? HIDE_TEXT : "$ " + PropertyUtil.formatString(data.getBuyPower()));
            tvFreezeAssets.setText(shouldHide ? HIDE_TEXT : "$ " + PropertyUtil.formatString(data.getFreezeAssets()));
            tvHkStockAssets.setText(shouldHide ? HIDE_TEXT : PropertyUtil.formatString(data.getHkStockAssets()));
            tvUsStockAssets.setText(shouldHide ? HIDE_TEXT : PropertyUtil.formatString(data.getUsStockAssets()));

            if (shouldHide) {
                tvProfit.setTextColor(ContextCompat.getColor(tvProfit.getContext(), R.color.property_text_black));
                tvProfitRate.setTextColor(ContextCompat.getColor(tvProfitRate.getContext(), R.color.property_text_black));
            } else {
                PropertyUtil.setTextColorByRiseOrFall(tvProfit.getContext(), tvProfit, data.getProfit());
                PropertyUtil.setTextColorByRiseOrFall(tvProfitRate.getContext(), tvProfitRate, data.getProfitRatio());
            }

            if (CollectionUtils.isNotBlank(data.getStock())) {
                llNoAssets.setVisibility(View.GONE);
                tvAssetsDetails.setVisibility(View.VISIBLE);
            } else {
                llNoAssets.setVisibility(View.VISIBLE);
                tvAssetsDetails.setVisibility(View.GONE);
            }
        }
    }
}
