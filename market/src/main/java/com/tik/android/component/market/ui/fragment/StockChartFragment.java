package com.tik.android.component.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.market.IChartView;
import com.tik.android.component.bussiness.market.IUserFavorCallback;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.bussiness.service.trade.ITradeService;
import com.tik.android.component.libcommon.Constants;
import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bussiness.database.MarketOperate;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.contract.IStockChartContract;
import com.tik.android.component.market.presenter.StockChartPresenter;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.MarketDataUtils;
import com.tik.android.component.market.util.MarketModelUtils;
import com.tik.android.component.market.widget.StockChartView;

import org.qiyi.video.svg.Andromeda;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class StockChartFragment extends BaseMVPFragment<StockChartPresenter> implements IStockChartContract.View, IChartView.RefreshListener {
    @BindView(R2.id.stock_chart_view)
    StockChartView mChartView;

    @BindView(R2.id.chart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.chart_text_self_watch)
    TextView mFavorView;

    @BindViews({R2.id.stock_title_name, R2.id.stock_curr_sign_and_price, R2.id.stock_gains_and_value,
            R2.id.stock_low_value, R2.id.stock_high_value, R2.id.stock_close_value, R2.id.stock_open_value})
    List<TextView> mTextViews;
    private String mSymbol = Constants.Market.EMPTY_DEFAULT_VALUE;
    private MarketStockEntity mMarketStockEntity;
    private boolean mIsFavor;
    private boolean mIsHistory;

    @Override
    public int initLayout() {
        return R.layout.market_activity_stock_chart;
    }

    public static StockChartFragment newInstance(String symbol) {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_STRING_STOCK_SYMBOL, symbol);
        StockChartFragment fragment = new StockChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        MarketOperate.getInstance().getHistory()
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.bindToLifecycle(this))
                .map(new Function<List<MarketStockEntity>, String>() {
                    @Override
                    public String apply(List<MarketStockEntity> marketStockEntities) throws Exception {
                        for (MarketStockEntity entity : marketStockEntities) {
                            if (entity.getSymbol().equals(mSymbol)) {
                                mIsHistory = true;
                                break;
                            }
                        }

                        return "";
                    }
                }).subscribe();
    }

    /**
     * 更新当前价格信息，数据为空时显示默认样式
     *
     * @param info
     * @param symbol
     */
    @Override
    public void onObtainPriceInfo(StockPriceInfo info, String symbol) {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }

        if (mTextViews == null || mPresenter == null) return;

        if (info == null) {
            updatePriceInfoDefault(symbol);
            return;
        }

        for (TextView view : mTextViews) {
            setTextEffective(view, mPresenter.getSuitableText(view.getId(), info));
        }

        mMarketStockEntity = MarketDataUtils.convertPriceEntity(info);
        mMarketStockEntity.setHistory(mIsHistory);
    }

    private boolean setTextEffective(TextView view, SpannableString suitableText) {
        CharSequence text = view.getText();
        if (suitableText != null && text != null && suitableText.toString().equals(text.toString())) {
            return false;
        }
        view.setText(suitableText);
        return true;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        Bundle args = getArguments();
        mMarketStockEntity = new MarketStockEntity();
        updatePriceInfoDefault(mSymbol);
        if (args != null) {
            String symbol = args.getString(Constants.EXTRA_STRING_STOCK_SYMBOL);
            if (!TextUtils.isEmpty(symbol)) {
                mSymbol = symbol;
                refreshChartFragment();
            }
        }
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new MaterialHeader(getContext()).setShowBezierWave(false))
                .setEnableOverScrollBounce(false)
                .setEnableLoadMore(false)
                .setEnableOverScrollDrag(false)
                .setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        refreshChartFragment();
                        // TODO: 2018/11/19 刷新操作可能没有执行回调导致mRefreshLayout不会消失
                    }
                });
    }

    /**
     * 当前价格信息控件显示默认值为"--"的样式
     *
     * @param symbol
     */
    private void updatePriceInfoDefault(String symbol) {
        for (TextView view : mTextViews) {
            setTextEffective(view, mPresenter.getSuitableText(view.getId(), symbol));
        }
    }

    @OnClick(R2.id.btn_chart_back)
    public void onCancelClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R2.id.btn_chart_search)
    public void onSearchClick() {
        MarketModelUtils.goToSearchFragment(this);
    }

    @OnClick(R2.id.chart_text_self_watch)
    public void onWatchClick() {
        //add here
        boolean newFavor = !mIsFavor;
        mIsFavor = newFavor;
        mMarketStockEntity.setFavor(newFavor);
        mFavorView.setSelected(newFavor);
        mFavorView.setEnabled(false);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.USER_FAVOR_DATA, mMarketStockEntity);

        MarketUtils.updateUserFavor(newFavor, bundle, StockChartFragment.this, new IUserFavorCallback() {
            @Override
            public void onSuccess() {
                if (null != mFavorView) {
                    mFavorView.setEnabled(true);
                }
            }

            @Override
            public void onError() {
                //Todo 更新服务器用户自选失败，刷新UI返回之前状态 updateUI有问题
                if (null != mFavorView) {
                    mFavorView.setEnabled(true);
                }
            }
        });
    }

    @OnClick(R2.id.chart_text_share)
    public void onShareClick() {

    }

    @OnClick(R2.id.chart_goto_trade)
    public void onTradeClick() {
        ITradeService service = Andromeda.getLocalService(ITradeService.class);
        if (service != null) {
            service.navigateTrade(mSymbol);
        }
    }

    public void refreshChartFragment(String symbol) {
        mSymbol = symbol;
        mChartView.refreshChartView(symbol);
        mPresenter.updateCurrPriceInfo(symbol);
        refreshFavorState();
    }

    public void refreshChartFragment() {
        mChartView.refreshChartView(mSymbol, this);
        mPresenter.updateCurrPriceInfo(mSymbol);
        refreshFavorState();
    }

    private void refreshFavorState() {
        List<String> favorList = UserDataManager.getInstance().getUserFavor();

        if (favorList.contains(mSymbol)) {
            mIsFavor = true;
        } else {
            mIsFavor = false;
        }

        mMarketStockEntity.setFavor(mIsFavor);
        mFavorView.setSelected(mIsFavor);
    }

    @Override
    public void onRefreshEnd() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }
    }
}
