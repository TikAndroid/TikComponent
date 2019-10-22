package com.tik.android.component.trade.module.trade.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.libcommon.Constants;
import com.tik.android.component.libcommon.DecimalUtils;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.SpannableBuilder;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.TradeConstants;
import com.tik.android.component.trade.module.order.OrderConstants;
import com.tik.android.component.trade.module.order.OrdersFragment;
import com.tik.android.component.trade.module.trade.bean.OrderSubmitResult;
import com.tik.android.component.trade.module.trade.contract.TradeSubmitContract;
import com.tik.android.component.trade.module.trade.presenter.TradeSubmitPresenter;

import org.qiyi.video.svg.Andromeda;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by xiangning on 2018/11/29
 */
public class OrderSubmitFragment extends BaseMVPFragment<TradeSubmitPresenter> implements TradeSubmitContract.View {
    public static final String TAG = "OrderSubmitFragment";

    public static final String EXTRA_STOCK_PRICE = "stock_price";
    public static final String EXTRA_BOOLEAN_OPERATE_BUY = "operate_buy";
    public static final String EXTRA_INTEGER_STOCK_NUMBER = "stock_number";
    public static final String EXTRA_DOUBLE_AMOUNTS = "amounts";
    public static final String EXTRA_DOUBLE_COIN_AMOUNTS = "coin_amounts";

    @BindView(R2.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.toolbar)
    FrameLayout toolbar;
    @BindView(R2.id.stock_tag)
    ImageView stockTag;
    @BindView(R2.id.stock)
    TextView stock;
    @BindView(R2.id.operate)
    TextView operate;
    @BindView(R2.id.price)
    TextView price;
    @BindView(R2.id.number)
    TextView number;
    @BindView(R2.id.amounts)
    TextView amounts;
    @BindView(R2.id.coin_label)
    TextView coinLabel;
    @BindView(R2.id.coin_amounts)
    TextView coinAmounts;
    @BindView(R2.id.btn_submit)
    TextView btnSubmit;

    private StockPriceInfo mPriceInfo;
    private int mNumber;
    private boolean mBuy;
    private double mAmounts;
    private double mCoinAmounts;

    public static OrderSubmitFragment newInstance(StockPriceInfo priceInfo, int number, boolean buy, double amounts, double coinAmounts) {
        Bundle extra = new Bundle();
        extra.putParcelable(EXTRA_STOCK_PRICE, priceInfo);
        extra.putInt(EXTRA_INTEGER_STOCK_NUMBER, number);
        extra.putBoolean(EXTRA_BOOLEAN_OPERATE_BUY, buy);
        extra.putDouble(EXTRA_DOUBLE_AMOUNTS, amounts);
        extra.putDouble(EXTRA_DOUBLE_COIN_AMOUNTS, coinAmounts);
        OrderSubmitFragment fragment = new OrderSubmitFragment();
        fragment.setArguments(extra);
        return fragment;
    }

    @Override
    public int initLayout() {
        return R.layout.trade_submit_fragment;
    }

    @Override
    protected void init(View view) {
        if (getArguments() == null || getArguments().getParcelable(EXTRA_STOCK_PRICE) == null) {
            pop();
            return;
        }

        Bundle args = getArguments();

        mPriceInfo = args.getParcelable(EXTRA_STOCK_PRICE);
        mNumber = args.getInt(EXTRA_INTEGER_STOCK_NUMBER);
        mBuy = args.getBoolean(EXTRA_BOOLEAN_OPERATE_BUY);
        mAmounts = args.getDouble(EXTRA_DOUBLE_AMOUNTS);
        mCoinAmounts = args.getDouble(EXTRA_DOUBLE_COIN_AMOUNTS);

        Resources res = getResources();
        stockTag.setImageResource(TradeConstants.STOCK_CURRENCY_HK.equals(mPriceInfo.getCurrency()) ? R.drawable.ic_tag_hk : R.drawable.ic_tag_us);

        String name = mPriceInfo.getName();
        String chineseName = "(" + mPriceInfo.getCname() + ")";
        CharSequence nameText = SpannableBuilder.create(name + chineseName)
                .span(name.length(), name.length() + chineseName.length(),
                        new ForegroundColorSpan(res.getColor(R.color.text_deactive)))
                .build();
        stock.setText(nameText);

        DecimalFormat format = DecimalUtils.getDivideNumberFormat(2, true);
        operate.setText(mBuy ? R.string.trade_tab_buy : R.string.trade_tab_sale);
        price.setText(format.format(mPriceInfo.getPrice()));
        number.setText(String.valueOf(mNumber));
        amounts.setText(format.format(mAmounts) + " " + mPriceInfo.getCurrency());
        coinLabel.setText(mBuy ? R.string.trade_form_tv_pay : R.string.trade_form_tv_income);
        coinAmounts.setText(format.format(mCoinAmounts) + " " + TradeConstants.COIN_CURRENCY_USDT);
    }

    @OnClick(R2.id.title_btn_left)
    public void onCloseClicked() {
        onBackPressed();
    }

    @OnClick(R2.id.btn_submit)
    public void onSubmitClicked() {
        btnSubmit.setEnabled(false);
        showLoadingDialog(getResources().getString(R.string.trade_order_submit_in_process));

        mPresenter.submit(false, mBuy, mPriceInfo.getSymbol(), mNumber,
                (float) mPriceInfo.getPrice(), TradeConstants.COIN_CURRENCY_USDT);
    }

    @Override
    public void onRequestVerifyPassword() {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.startTransctionPassword(OrderSubmitFragment.this, false, Constants.REQUEST_CODE_ORDER_CONFIRM_TO_VERIFY_ASSET_PWD);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_ORDER_CONFIRM_TO_VERIFY_ASSET_PWD:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    boolean success = data.getBooleanExtra(AccountConstants.BINDING_RESULT_TRANSACTION_PASSWORD_DATA, false);
                    if (success) {
                        mPresenter.submit(true, mBuy, mPriceInfo.getSymbol(), mNumber,
                                (float) mPriceInfo.getPrice(), TradeConstants.COIN_CURRENCY_USDT);
                        break;
                    }
                }
                resetState();
                break;
        }
    }

    @Override
    public void onSubmitResult(OrderSubmitResult result) {
        LogUtil.d("" + result);
        resetState();
        if (result != null && getContext() != null) {
            Context context = getContext();
            int index = -1;
            int msgId = 0;

            switch (result.getStatus()) {
                case OrderConstants.TRADE_STATUS_DONE:
                case OrderConstants.TRADE_DEAL_DONE:
                    index = OrdersFragment.TRADE_INDEX_DONE;
                    msgId = R.string.trade_status_consign_success;
                    break;
                case OrderConstants.TRADE_STATUS_ERROR:
                case OrderConstants.TRADE_CONSIGNING_1:
                case OrderConstants.TRADE_CONSIGNING_2:
                case OrderConstants.TRADING_PENDING:
                case OrderConstants.TRADE_WITHDRAWING:
                case OrderConstants.TRADE_PARTIAL_DEAL:
                    index = OrdersFragment.TRADE_INDEX_CURRENT;
                    msgId = R.string.trade_order_submitted;
                    break;
                case OrderConstants.TRADE_CONSIGN_FAILED:
                case OrderConstants.TRADE_REVOKED:
                case OrderConstants.TRADE_EXPIRED:
                    index = OrdersFragment.TRADE_INDEX_CLOSE;
                    msgId = R.string.trade_status_consign_failed;
                    break;
                default:
                    break;
            }

            if (msgId != 0) {
                ToastUtil.showToastLong(getText(msgId));
            }

            if (index >= 0) {
                showOrderUi(index);
            }
        }
    }

    @Override
    public void onError(Throwable error) {
        LogUtil.d("" + error.getMessage());
        ToastUtil.showToastLong(error.getMessage());
        resetState();
    }

    private void resetState() {
        btnSubmit.setEnabled(true);
        cancelLoadingDialog();
    }

    private void showOrderUi(int index) {
        OrdersFragment ordersFragment = findFragment(OrdersFragment.class);
        if(ordersFragment == null) {
            ordersFragment = OrdersFragment.newInstance(index);
        }
        startWithPop(ordersFragment);
    }


}
