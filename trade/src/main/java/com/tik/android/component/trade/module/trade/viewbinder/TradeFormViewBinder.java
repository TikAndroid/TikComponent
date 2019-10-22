package com.tik.android.component.trade.module.trade.viewbinder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.LoginState;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.libcommon.DecimalUtils;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.SpannableBuilder;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.TradeConstants;
import com.tik.android.component.trade.module.trade.presenter.TradePresenter;
import com.tik.android.component.trade.module.trade.ui.TradeFragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/26.
 */
public class TradeFormViewBinder extends ItemViewBinder<StockPriceInfo, TradeFormViewBinder.ViewHolder> {

    private TradePresenter mPresenter;
    private TradeFragment mTradeFragment;
    private OnConfirmClickListener mOnConfirmClickListener;
    private ViewHolder mViewHolder;

    public TradeFormViewBinder(@NonNull TradePresenter presenter, @NonNull TradeFragment tradeFragment, @NonNull OnConfirmClickListener listener) {
        this.mPresenter = presenter;
        this.mTradeFragment = tradeFragment;
        this.mOnConfirmClickListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder(inflater.inflate(R.layout.trade_form, parent, false));
        }
        if (mViewHolder.itemView.getParent() instanceof ViewGroup) {
            ((ViewGroup) mViewHolder.itemView.getParent()).removeView(mViewHolder.itemView);
        }
        return mViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StockPriceInfo item) {
        holder.setStockInfo(item);
        holder.tradeFormInputPrice.clearFocus();
        holder.tradeFormInputNumber.clearFocus();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.trade_form_tab_buy)
        TextView tradeFormTabBuy;
        @BindView(R2.id.trade_form_tab_sale)
        TextView tradeFormTabSale;
        @BindView(R2.id.trade_form_tv_price)
        TextView tradeFormTvPrice;
        @BindView(R2.id.trade_form_btn_price_minus)
        ImageView tradeFormBtnPriceMinus;
        @BindView(R2.id.trade_form_btn_price_plus)
        ImageView tradeFormBtnPricePlus;
        @BindView(R2.id.trade_form_input_price)
        EditText tradeFormInputPrice;
        @BindView(R2.id.trade_form_tv_number)
        TextView tradeFormTvNumber;
        @BindView(R2.id.trade_form_btn_number_minus)
        ImageView tradeFormBtnNumberMinus;
        @BindView(R2.id.trade_form_btn_number_plus)
        ImageView tradeFormBtnNumberPlus;
        @BindView(R2.id.trade_form_input_number)
        EditText tradeFormInputNumber;
//        @BindView(R2.id.quick_number_1_of_4)
//        TextView quickNumber1Of4;
//        @BindView(R2.id.quick_number_1_of_3)
//        TextView quickNumber1Of3;
//        @BindView(R2.id.quick_number_1_of_2)
//        TextView quickNumber1Of2;
//        @BindView(R2.id.quick_number_all)
//        TextView quickNumberAll;
        @BindView(R2.id.trade_label_real_amounts)
        TextView tradeLabelRealAmounts;
        @BindView(R2.id.trade_real_amounts)
        TextView tradeRealAmounts;
        @BindView(R2.id.trade_label_usdt_amounts)
        TextView tradeLabelUsdtAmounts;
        @BindView(R2.id.trade_usdt_amounts)
        TextView tradeUsdtAmounts;
        @BindView(R2.id.trade_usable_property)
        TextView tradeUsableProperty;
        @BindView(R2.id.trade_btn_confirm)
        TextView tradeBtnConfirm;

        private Context mContext;
        private StockPriceInfo mPriceInfo;

        DecimalFormat mDecimalFormat = DecimalUtils.getDivideNumberFormat(4);

        private double mUsdtRate = 1.0;
        private double mHkdRate = 1.0;
        private double mUsableCoin;
        private int mUsableStock;

        private LoginState mLoginState = LoginState.NOT_LOGIN;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mContext = view.getContext();

            Observable.merge(
                    RxTextView.textChanges(tradeFormInputPrice),
                    RxTextView.textChanges(tradeFormInputNumber))
                    .skip(1, TimeUnit.SECONDS)
                    .subscribe(sequence -> updateAmounts(false));

            onTabClicked(tradeFormTabBuy);
        }

        void setStockInfo(StockPriceInfo info) {
            tradeFormInputPrice.setText(String.valueOf(info.getPrice()));
            mPriceInfo = info;
            updateConfirmButton();
            updateAmounts(true);
        }

        @OnClick({R2.id.trade_form_tab_buy, R2.id.trade_form_tab_sale})
        public void onTabClicked(View view) {
            int i = view.getId();
            if (i == R.id.trade_form_tab_buy) {
                setTabMode(tradeFormTabBuy, true);
                setTabMode(tradeFormTabSale, false);
            } else if (i == R.id.trade_form_tab_sale) {
                setTabMode(tradeFormTabBuy, false);
                setTabMode(tradeFormTabSale, true);
            }
            updateConfirmButton();
            updateAmounts(false);
        }

        private void setTabMode(TextView tab, boolean checked) {
            tab.setSelected(checked);
            // 字体设置粗体或者正常
            String text = String.valueOf(tab.getText());
            if (checked) {
                tab.setText(SpannableBuilder.create(text)
                        .span(0, text.length(), new StyleSpan(Typeface.BOLD))
                        .build());
            } else {
                tab.setText(text);
            }
        }

        private void updateConfirmButton() {
            CharSequence text = "";
            int bgRes = 0;
            mLoginState = LocalAccountInfoManager.getInstance().getLoginState();
            switch (mLoginState) {
                case NOT_LOGIN:
                    text = mContext.getString(R.string.trade_confirm_login);
                    bgRes = R.drawable.bg_round_rect_highlight;
                    break;
                case NOT_BIND:
                case NO_ASSET_PWD:
                    text = mContext.getString(R.string.trade_confirm_unlock);
                    bgRes = R.drawable.bg_round_rect_highlight;
                    break;
                case FULL_LOGIN:
                    if (tradeFormTabBuy.isSelected()) {
                        text = tradeFormTabBuy.getText();
                        bgRes = R.drawable.bg_round_rect_highlight_green;
                    } else {
                        text = tradeFormTabSale.getText();
                        bgRes = R.drawable.bg_round_rect_highlight_red;
                    }
                    break;
            }

            if (mLoginState != LoginState.FULL_LOGIN) {
                tradeBtnConfirm.setEnabled(true);
            }
            tradeBtnConfirm.setText(text);
            tradeBtnConfirm.setBackgroundResource(bgRes);
        }

        private void updateAmounts(boolean refreshData) {
            boolean isBuy = tradeFormTabBuy.isSelected();
            String symbol = mPriceInfo != null ? mPriceInfo.getSymbol() : "";
            double price = parseDivideNumber(String.valueOf(tradeFormInputPrice.getText())).doubleValue();
            int number = parseDivideNumber(String.valueOf(tradeFormInputNumber.getText())).intValue();
            String currency = mPriceInfo != null ? mPriceInfo.getCurrency() : TradeConstants.STOCK_CURRENCY_US;
            // 金额计算
            double amount = price * number;
            tradeRealAmounts.setText(getAmountsString(amount, TradeConstants.DECIMALS_REAL_AMOUNTS, currency));
            // 应付/可得计算
            double usdtAmount = calCoinAmount(amount, currency, isBuy);
            tradeUsdtAmounts.setText(getAmountsString(usdtAmount, TradeConstants.DECIMALS_COIN_AMOUNTS, TradeConstants.COIN_CURRENCY_USDT));

            if (!refreshData) {
                if (isBuy) {
                    // 设置应付标签
                    tradeLabelUsdtAmounts.setText(R.string.trade_form_tv_pay);
                    // 计算可用币额
                    String usableInfo = mContext.getResources().getString(R.string.trade_form_usable_coin);
                    CharSequence text = usableInfo + getAmountsString(mUsableCoin, TradeConstants.DECIMALS_COIN_AMOUNTS, TradeConstants.COIN_CURRENCY_USDT);
                    text = SpannableBuilder.create(text.toString())
                            .span(usableInfo.length(), text.length(),
                                    new ForegroundColorSpan(mContext.getResources().getColor(R.color.highlight_red)))
                            .build();
                    tradeUsableProperty.setText(text);
                    if (mLoginState == LoginState.FULL_LOGIN) {
                        tradeBtnConfirm.setEnabled(usdtAmount > 0 && mUsableCoin >= usdtAmount);
                    }
                } else {
                    // 设置可得标签
                    tradeLabelUsdtAmounts.setText(R.string.trade_form_tv_income);
                    // 计算可用股票持仓
                    CharSequence usableInfo = mContext.getResources().getString(R.string.trade_form_usable_stock);
                    CharSequence text = "" + usableInfo + mUsableStock;
                    text = SpannableBuilder.create(text.toString())
                            .span(usableInfo.length(), text.length(),
                                    new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_primary)))
                            .build();
                    tradeUsableProperty.setText(text);
                    if (mLoginState == LoginState.FULL_LOGIN) {
                        tradeBtnConfirm.setEnabled(number > 0 && mUsableStock >= number);
                    }
                }
            } else {
                Flowable.zip(
                        mPresenter.queryUsdtRate(true),
                        mPresenter.queryHkdRate(true),
                        mPresenter.queryCoinBalance(true),
                        mPresenter.queryStockBalance(symbol, true),
                        (usdtRate, hkdRate, usableCoin, usableStock) -> {
                            mUsdtRate = usdtRate;
                            mHkdRate = hkdRate;
                            mUsableCoin = usableCoin;
                            mUsableStock = usableStock;
                            return true;
                        }
                )
                        .compose(RxUtils.bindToLifecycle(mTradeFragment))
                        .safeSubscribe(new NormalSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                updateAmounts(false);
                            }
                        });
            }
        }

        private double calCoinAmount(double amount, String currency, boolean buy) {
            if (TradeConstants.STOCK_CURRENCY_HK.equals(currency)) { // 港元先兑换成美元
                amount /= mHkdRate;
            }
            if (buy) { // 美元换算成USDT
                amount *= mUsdtRate;
            }

            return amount;
        }

        @OnClick({R2.id.trade_form_btn_price_minus, R2.id.trade_form_btn_price_plus, R2.id.trade_form_btn_number_minus, R2.id.trade_form_btn_number_plus})
        public void onMinusPlusClicked(View view) {
            if (view == tradeFormBtnPriceMinus) {
                adjustNumberWithClick(tradeFormInputPrice, -0.01);
            } else if (view == tradeFormBtnPricePlus) {
                adjustNumberWithClick(tradeFormInputPrice, 0.01);
            } else if (view == tradeFormBtnNumberMinus) {
                adjustNumberWithClick(tradeFormInputNumber, -1);
            } else if (view == tradeFormBtnNumberPlus) {
                adjustNumberWithClick(tradeFormInputNumber, 1);
            }
        }

        private void adjustNumberWithClick(EditText editText, double delta) {
            String text = String.valueOf(editText.getText());
            try {
                DecimalFormat format = DecimalUtils.getDivideNumberFormat(editText == tradeFormInputPrice ? TradeConstants.DECIMALS_PRICE : 0);
                Double number = TextUtils.isEmpty(text) ? 0 : format.parse(text).doubleValue();
                number += delta;
                text = number >= 0 ? format.format(number) : "";
            } catch (ParseException e) {
                text = "";
            }

            editText.setText(text);
        }

        private Number parseDivideNumber(String text) {
            Number number = 0;
            if (!TextUtils.isEmpty(text)) {
                try {
                    number = mDecimalFormat.parse(text);
                } catch (ParseException e) {
                    LogUtil.e(e.getMessage());
                }
            }
            return number;
        }

        private String formatDivideNumber(Number number, int decimals) {
            return DecimalUtils.getDivideNumberFormat(decimals).format(number);
        }

        private String getAmountsString(Number amounts, int decimals, String currency) {
            return mDecimalFormat.format(amounts) + (TextUtils.isEmpty(currency) ? "" : " " + currency);
        }
//
//        @OnClick({R2.id.quick_number_1_of_4, R2.id.quick_number_1_of_3, R2.id.quick_number_1_of_2, R2.id.quick_number_all})
//        public void onQuickNumberClicked(View view) {
//            double ratio = 1.0;
//            int i = view.getId();
//            if (i == R.id.quick_number_1_of_4) {
//                ratio = 1.0 / 4;
//            } else if (i == R.id.quick_number_1_of_3) {
//                ratio = 1.0 / 3;
//            } else if (i == R.id.quick_number_1_of_2) {
//                ratio = 1.0 / 2;
//            } else if (i == R.id.quick_number_all) {
//                ratio = 1.0;
//            }
//
//            if (tradeFormTabBuy.isSelected()) {
//                double money = mUsableCoin / mUsdtRate;
//                String currency = mPriceInfo != null ? mPriceInfo.getCurrency() : TradeConstants.STOCK_CURRENCY_US;
//                if (TradeConstants.STOCK_CURRENCY_HK.equals(currency)) { // 港元先兑换成美元
//                    money *= mHkdRate;
//                }
//                double price = parseDivideNumber(String.valueOf(tradeFormInputPrice.getText())).doubleValue();
//
//                tradeFormInputNumber.setText(getAmountsString((int) (money * ratio / price), 0, ""));
//            } else {
//                tradeFormInputNumber.setText(getAmountsString((int) (mUsableStock * ratio), 0, ""));
//            }
//        }

        @OnClick(R2.id.trade_btn_confirm)
        public void onConfirmClicked() {
            if (mPriceInfo != null) {
                boolean isBuy = tradeFormTabBuy.isSelected();
                double price = parseDivideNumber(String.valueOf(tradeFormInputPrice.getText())).doubleValue();
                int number = parseDivideNumber(String.valueOf(tradeFormInputNumber.getText())).intValue();
                double amounts = price * number;
                double coinAmounts = calCoinAmount(amounts, mPriceInfo.getCurrency(), isBuy);
                StockPriceInfo info = new StockPriceInfo();
                info.setSymbol(mPriceInfo.getSymbol());
                info.setName(mPriceInfo.getName());
                info.setCname(mPriceInfo.getCname());
                info.setPrice(price);
                info.setCurrency(mPriceInfo.getCurrency());
                mOnConfirmClickListener.onConfirmClick(info, number, isBuy, amounts, coinAmounts);
            }
        }

        private TradeInfoPopWindow mPopupWindow;
        @OnClick(R2.id.trade_form_btn_detail)
        public void onInfoDetailClick(View view) {
            if (mPopupWindow == null) {
               mPopupWindow = new TradeInfoPopWindow(mContext);
            }

            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                String oldFee = String.format("%s%%", mDecimalFormat.format(TradeConstants.FEE_OLD * 100));
                String text = String.format("%s %s%%", oldFee, mDecimalFormat.format(TradeConstants.FEE * 100));
                CharSequence feeText = SpannableBuilder.create(text, 0, oldFee.length())
                        .span(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_deactive)))
                        .span(new StrikethroughSpan())
                        .build();
                double rate = 1 / mUsdtRate;
                String currency = TradeConstants.STOCK_CURRENCY_US;
                if (mPriceInfo != null && TradeConstants.STOCK_CURRENCY_HK.equals(mPriceInfo.getCurrency())) { // 港元先兑换成美元
                    rate *= mHkdRate;
                    currency = TradeConstants.STOCK_CURRENCY_HK;
                }

                CharSequence rateText = String.format("1 USDT = %s %s", mDecimalFormat.format(rate), currency);
                mPopupWindow.show(view, feeText, rateText);
            }
        }

    }


    public interface OnConfirmClickListener {
        void onConfirmClick(StockPriceInfo priceInfo, int number, boolean buy, double amounts, double coinAmounts);
    }
}
