package com.tik.android.component.trade.module.trade.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.AccountStateListener;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.LoginState;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.bussiness.service.IAppService;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.bussiness.service.trade.ITradeService;
import com.tik.android.component.libcommon.CollectionUtils;
import com.tik.android.component.libcommon.Constants;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.order.DiffCallBackUtil;
import com.tik.android.component.trade.module.order.OrdersFragment;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;
import com.tik.android.component.trade.module.order.viewbinder.OrderViewBinder;
import com.tik.android.component.trade.module.trade.contract.TradeContract;
import com.tik.android.component.trade.module.trade.presenter.TradePresenter;
import com.tik.android.component.trade.module.trade.viewbinder.TradeFormViewBinder;
import com.tik.android.component.trade.module.trade.viewbinder.TradeHeaderViewBinder;
import com.tik.android.component.trade.module.widget.AnimChartView;
import com.tik.android.component.trade.module.widget.AnimChartViewBinder;

import org.qiyi.video.svg.Andromeda;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.tik.android.component.trade.module.order.OrderConstants.PAGE_SIZE;
import static com.tik.android.component.trade.module.order.OrdersFragment.TRADE_INDEX_CURRENT;

/**
 * @describe : 交易界面
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/24.
 */
public class TradeFragment extends BaseMVPFragment<TradePresenter>
        implements AnimChartView.OnAnimChartListener, TradeContract.View, TradeFormViewBinder.OnConfirmClickListener {

    @BindView(R2.id.trade_recycler)
    RecyclerView mTradeRecycler;
    @BindView(R2.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.trade_btn_order)
    ImageView mBtnOrder;
    private MultiTypeAdapter mAdapter;
    private List<Object> mItems = new ArrayList<>();
    private List<OrderInfo> mOrderInfos = new ArrayList<>();
    //private String mSymbol = "AAPL";//示例数据 AAPL 后续应该是需要sp保存该字段
    private String mSymbol = "TSLA";//示例数据 AAPL 后续应该是需要sp保存该字段
    private AnimChartViewBinder.ViewHolder mAnimViewHolder;

    private final int TRADE_HEADER_SIZE = 3;
    private final int TRADE_HEADER_INDEX = TRADE_HEADER_SIZE - 1;
    private int mPageIndex;
    private TradeHeaderBean mTradeHeaderBean = new TradeHeaderBean();

    private AccountStateListener mAccountStateListener = new AccountStateListener() {
        @Override
        public void onLoginStateChanged(LoginState state) {
            refreshUIStatus(1);
        }

        @Override
        public void onUserInfoChanged(User user) {
            refreshUIStatus(1);
            refreshTradeList();
        }
    };

    private void refreshUIStatus(int position) {
        if (mBtnOrder != null) {
            mBtnOrder.setVisibility(LocalAccountInfoManager.getInstance().isHoxUser() ? View.VISIBLE : View.GONE);
        }
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
    }

    /**
     * update when user updated
     */
    private void refreshTradeList() {
        boolean isHoxUser = LocalAccountInfoManager.getInstance().isHoxUser();
        if(isHoxUser) {
            if (!mItems.contains(mTradeHeaderBean)){
                mItems.add(TRADE_HEADER_INDEX, mTradeHeaderBean);
                mAdapter.notifyItemInserted(TRADE_HEADER_INDEX);
            }
        } else if(mItems.contains(mTradeHeaderBean)) {
            int total = mItems.size();
            int index = total - 1;
            while (index >= TRADE_HEADER_INDEX) {
                mItems.remove(index--);
            }
            mOrderInfos.clear();
            mAdapter.notifyItemRangeRemoved(TRADE_HEADER_INDEX, total - TRADE_HEADER_INDEX);
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    private void queryCurrentTrades() {
        if (mPresenter != null) {
            mPresenter.getCurrentTrades(1, "", 0);
        }
    }

    public static TradeFragment newInstance() {
        return new TradeFragment();
    }

    /**
     * 从其他页面启动交易页面
     *
     * @param stockSymbol 所需展示的股票代号
     */
    public static void navigateTrade(String stockSymbol) {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_STRING_STOCK_SYMBOL, stockSymbol);
        IAppService appService = Andromeda.getLocalService(IAppService.class);
        if (appService != null) {
            appService.switchToTab(IAppService.TAB_INDEX_TRADE, args);
        }
    }

    @Override
    public int initLayout() {
        return R.layout.trade_fragment;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init(View view) {
        super.init(view);
        initRefreshLayout();
        initViews();
        initRecycler();
        LocalAccountInfoManager.getInstance().addAccountStateListener(mAccountStateListener);
    }

    @SuppressLint("CheckResult")
    private void initViews() {
        if (LocalAccountInfoManager.getInstance().isHoxUser()) {
            mBtnOrder.setVisibility(View.VISIBLE);
        }
        RxView.clicks(mBtnOrder).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(o -> {
            ITradeService tradeService = Andromeda.getLocalService(ITradeService.class);
            if (tradeService != null) {
                tradeService.showOrderUi(this);
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (LocalAccountInfoManager.getInstance().isHoxUser()) {
            queryCurrentTrades();
        }
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        if (mPresenter != null) {
            //mPresenter.getCurrentTrades(1, "", 0);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mAdapter.setItems(mItems);
        mAdapter.notifyDataSetChanged();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setEnableOverScrollBounce(false)
                .setEnableAutoLoadMore(false)
                .setEnableOverScrollDrag(false)
                .setOnRefreshListener(refreshLayout -> {
//                    mAdapter.notifyItemChanged(0);
                    if (getAnimViewHolder() != null) {
                        mAnimViewHolder.refresh();
                    }
                    //todo fresh current order list
                })
                .setOnLoadMoreListener(loadMoreLayout -> {
                    //todo load more
                    mRefreshLayout.finishLoadMore();
                })
                .setEnableScrollContentWhenLoaded(false);
    }

    private AnimChartViewBinder.ViewHolder getAnimViewHolder() {
        if (mAnimViewHolder == null) {
            RecyclerView.ViewHolder holder = mTradeRecycler.findViewHolderForAdapterPosition(0);
            if (holder instanceof AnimChartViewBinder.ViewHolder) {
                mAnimViewHolder = (AnimChartViewBinder.ViewHolder) holder;
            }
        }
        return mAnimViewHolder;
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        if (args != null) {
            String string = args.getString(Constants.EXTRA_STRING_STOCK_SYMBOL);
            if (!TextUtils.isEmpty(string)) {
                mSymbol = string;
                mItems.set(0, mSymbol);
            }
        }
        mAdapter.notifyItemChanged(0);
    }

    private void showOrderUi(int index) {
        OrdersFragment ordersFragment = findFragment(OrdersFragment.class);
        if (ordersFragment == null) {
            ordersFragment = OrdersFragment.newInstance(index);
        }
        startWithRoot(ordersFragment);
    }

    private void initRecycler() {
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(String.class, new AnimChartViewBinder(this));
        mItems.add(0, mSymbol);

        mAdapter.register(StockPriceInfo.class, new TradeFormViewBinder(mPresenter, this, this));
        mItems.add(1, new StockPriceInfo());
        // current trade list
        mAdapter.register(TradeHeaderBean.class, new TradeHeaderViewBinder(new TradeHeaderViewBinder.HeaderClickListener() {
            @Override
            public void onClick(View v) {
                showOrderUi(TRADE_INDEX_CURRENT);
            }

            @Override
            public void onCheckChange(CompoundButton buttonView, boolean isChecked) {
                mTradeHeaderBean.setHideOthers(isChecked);
                if (isChecked) {
                    filterOtherStock(mSymbol);
                } else {
                    showCurrentAll();
                }
            }
        }));
        if(LocalAccountInfoManager.getInstance().isHoxUser()) {
            mItems.add(TRADE_HEADER_INDEX, mTradeHeaderBean);
        }
        mAdapter.register(OrderInfo.class, new OrderViewBinder((view, order) -> {
            // todo show detail info ?
        }));

        mTradeRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mTradeRecycler.setAdapter(mAdapter);
        RecyclerView.ItemAnimator itemAnimator = mTradeRecycler.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
    }

    @OnClick(R2.id.trade_search)
    public void onSearchClick() {
        MarketUtils.goToSearchFragmentForResult(this);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == Constants.Market.SEARCH_FRAGMENT_REQUEST_CODE && data != null
                && resultCode == Constants.Market.SEARCH_FRAGMENT_RESULT_CODE) {
            String string = data.getString(Constants.EXTRA_STRING_STOCK_SYMBOL);
            if (!TextUtils.isEmpty(string)) {
                mSymbol = string;
                mItems.set(0, mSymbol);
            }
            setArguments(null);
            mAdapter.notifyItemChanged(0);
        }
    }

    /**
     * chart刷新完成后的回调
     */
    @Override
    public void onRefreshEnd() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }
    }

    /**
     * chart价格更新后的回调
     */
    @Override
    public void onPriceUpdate(StockPriceInfo info) {
        mItems.set(1, info);
        mAdapter.notifyItemChanged(1);
    }

    @Override
    public void onChartHeightChanged(int value) {
        mTradeRecycler.scrollToPosition(0);
    }

    @Override
    public void onTradeLoaded(String dataType, List<TradeInfo> data) {
        mRefreshLayout.setEnableLoadMore(data.size() >= PAGE_SIZE);
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (TradeInfo trade : data) {
            OrderInfo order = new OrderInfo();
            trade.setDataType(dataType);
            order.setTradeInfo(trade);
            orderInfos.add(order);
        }
        if (CollectionUtils.isBlank(orderInfos)) {
            return;
        }

        mOrderInfos.clear();
        mOrderInfos.addAll(orderInfos);

        if(mTradeHeaderBean.isHideOthers()) {
            orderInfos = filter(mSymbol, orderInfos);
        }

        processOrdersChange(orderInfos);
    }

    private void filterOtherStock(String symbolExcept) {
        List<OrderInfo> source = filter(symbolExcept, mOrderInfos);
        processOrdersChange(source);
    }

    /**
     * find out the current symbol order info(trade info)
     *
     * @param symbolExcept stock symbol
     * @param targetList   will filter it
     * @return filtered order list
     */
    private List<OrderInfo> filter(String symbolExcept, List<OrderInfo> targetList) {
        final List<OrderInfo> targetListShow = new ArrayList<>();
        // find the order item index
        for (OrderInfo orderInfo : targetList) {
            if (orderInfo != null) {
                TradeInfo tradeInfo = orderInfo.getTradeInfo();
                if (symbolExcept.equals(tradeInfo.getStockSymbol())) {
                    targetListShow.add(orderInfo);
                }
            }
        }
        return targetListShow;
    }

    /**
     * get the orders in the adapter
     */
    private List<OrderInfo> getAdapterOrderList() {
        List<OrderInfo> adapterOrders = new ArrayList<>();
        int index = mItems.size() - 1;
        while (index > TRADE_HEADER_INDEX) {
            Object o = mItems.get(index);
            if (o instanceof OrderInfo) {
                OrderInfo orderInfo = (OrderInfo) o;
                adapterOrders.add(0, orderInfo);
            }
            index--;
        }
        return adapterOrders;
    }

    private void showCurrentAll() {
        processOrdersChange(mOrderInfos);
    }

    private void processOrdersChange(final List<OrderInfo> newOrders) {
        RxUtils.async(() -> {
            ArrayList<Object> newList = new ArrayList<>(newOrders.size() + TRADE_HEADER_SIZE);
            newList.addAll(mItems.subList(0, TRADE_HEADER_SIZE));
            newList.addAll(newOrders);
            DiffCallBackUtil diffCallBack = new DiffCallBackUtil(mItems, newList);
            return DiffUtil.calculateDiff(diffCallBack);
        }, diffResult -> {
            int i = mItems.size();
            while (--i > TRADE_HEADER_INDEX) {
                mItems.remove(i);
            }
            mItems.addAll(newOrders);
            diffResult.dispatchUpdatesTo(mAdapter);
        });
    }

    public static class TradeHeaderBean {
        private boolean hideOthers;

        public boolean isHideOthers() {
            return hideOthers;
        }

        public void setHideOthers(boolean hideOthers) {
            this.hideOthers = hideOthers;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_TRADE_TO_LOGIN:
                    mAdapter.notifyItemChanged(1);
                    break;
                case Constants.REQUEST_CODE_TRADE_TO_BIND:
                    if (LocalAccountInfoManager.getInstance().getLoginState() == LoginState.NO_ASSET_PWD) {
                        startSetAssetPwd();
                    }
                    break;
                case Constants.REQUEST_CODE_TRADE_TO_SET_ASSET_PWD:
                    mAdapter.notifyItemChanged(1);
                    break;
            }
        }
    }

    private void startLogin() {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.startRegisterOrLogin(this, true, Constants.REQUEST_CODE_TRADE_TO_LOGIN);
        }
    }

    private void startBind() {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.startBinding(this, Constants.REQUEST_CODE_TRADE_TO_BIND);
        }
    }

    private void startSetAssetPwd() {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.startTransctionPassword(this, true, Constants.REQUEST_CODE_TRADE_TO_SET_ASSET_PWD);
        }
    }

    /**
     * 交易表单提交
     *
     * @param priceInfo   股票价格信息
     * @param number      交易数量
     * @param buy         是否是买入，否则作为卖出
     * @param amounts     委托金额
     * @param coinAmounts 所需数字货币金额
     */
    @Override
    public void onConfirmClick(StockPriceInfo priceInfo, int number, boolean buy, double amounts, double coinAmounts) {
        LoginState state = LocalAccountInfoManager.getInstance().getLoginState();
        hideSoftInput();
        switch (state) {
            case NOT_LOGIN:
                startLogin();
                break;
            case NOT_BIND:
                startBind();
                break;
            case NO_ASSET_PWD:
                startSetAssetPwd();
                break;
            case FULL_LOGIN:
                startWithRoot(OrderSubmitFragment.newInstance(priceInfo, number, buy, amounts, coinAmounts));
                break;
        }
    }
}
