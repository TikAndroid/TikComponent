package com.tik.android.component.trade.module.order;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.libcommon.CollectionUtils;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;
import com.tik.android.component.trade.module.order.contract.OrderContract;
import com.tik.android.component.trade.module.order.presenter.OrderPresenter;
import com.tik.android.component.trade.module.order.viewbinder.OrderViewBinder;
import com.tik.android.component.trade.module.order.widget.EmptyHolder;
import com.tik.android.component.trade.module.order.widget.ErrorHolder;
import com.tik.android.component.trade.module.trade.ui.TradeFragment;
import com.tik.android.component.widget.HoxTabLayout;
import com.tik.android.component.widget.placeholderview.PlaceHolderView;
import com.tik.android.component.widget.placeholderview.core.IPlaceHolderManager;
import com.tik.android.component.widget.placeholderview.core.PlaceHolderManager;
import com.tik.android.component.widget.placeholderview.placeholder.LoadingPlaceHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.tik.android.component.trade.module.order.OrderConstants.MODE_DEFAULT;
import static com.tik.android.component.trade.module.order.OrderConstants.MODE_LOAD_MORE;
import static com.tik.android.component.trade.module.order.OrderConstants.MODE_REFRESH;
import static com.tik.android.component.trade.module.order.OrderConstants.PAGE_SIZE;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_DIRECTION_ALL;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_KEYWORD_ALL;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_TYPE_CLOSED;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_TYPE_CURRENT;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_TYPE_DONE;


/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/22
 */
public class OrdersFragment extends BaseMVPFragment<OrderPresenter> implements OrderContract.View {
    private static final String TAG = "OrdersFragment";

    public static final String ARG_TRADE_INDEX = "show_index";
    public static final int TRADE_INDEX_CURRENT = 0;
    public static final int TRADE_INDEX_DONE = 1;
    public static final int TRADE_INDEX_CLOSE = 2;
    private final boolean PLACE_HOLDER_LOADING_ENABLED = false;
    private final boolean PLACE_HOLDER_ERROR_ENABLED = false;

    public static OrdersFragment newInstance() {
        return newInstance(TRADE_INDEX_CURRENT);
    }

    public static OrdersFragment newInstance(int index) {
        OrdersFragment orderFragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRADE_INDEX, index);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    private MultiTypeAdapter mAdapter;

    @BindView(R2.id.trade_order_back)
    ImageView backImage;
    @BindView(R2.id.trade_tab_layout)
    HoxTabLayout mTabLayout;

    @BindView(R2.id.order_list_container)
    LinearLayout mRecycleContainer;

    @BindView(R2.id.refresh_layout)
    protected SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R2.id.trade_order_list)
    protected RecyclerView mRecycleView;

    private int mDataFreshMode = MODE_DEFAULT;

    private String mTradeType = TRADE_TYPE_CURRENT;
    private HashMap<String, List<OrderInfo>> mOrderMaps;
    private HashMap<String, Integer> mPageMap;

    private PlaceHolderManager mPlaceHolderMgr;

    @Override
    public int initLayout() {
        return R.layout.trade_orders_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);

        processArgs();
        initPage();
        initData();
        initFreshLayout();
        iniTab();
    }

    private void iniTab() {
        String current = getString(R.string.trade_order_filter_current);
        String done = getString(R.string.trade_order_filter_histories);
        String closed = getString(R.string.trade_order_filter_closed);
        mTabLayout.setItems(new String[]{current, done, closed}, typeToIndex(mTradeType));
        mTabLayout.setOnItemClickListener(new HoxTabLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView view, int index) {
                if(PLACE_HOLDER_LOADING_ENABLED) {
                    mPlaceHolderMgr.showPlaceHolder(LoadingPlaceHolder.class);
                }
            }

            @Override
            public void OnItemClickAnimEnd(TextView view, int index, boolean cancel) {
                switchTabTo(indexToType(index));
            }
        });
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        processArgs();
    }

    private void processArgs() {
        Bundle args = getArguments();
        if(args != null && args.containsKey(ARG_TRADE_INDEX)) {
            mTradeType = indexToType(args.getInt(ARG_TRADE_INDEX));
        }
    }

    private String indexToType(int index) {
        String type = TRADE_TYPE_CURRENT;
        switch (index) {
            case TRADE_INDEX_CURRENT:
                type = TRADE_TYPE_CURRENT;
                break;
            case TRADE_INDEX_DONE:
                type = TRADE_TYPE_DONE;
                break;
            case TRADE_INDEX_CLOSE:
                type = TRADE_TYPE_CLOSED;
                break;
        }
        return type;
    }

    private int typeToIndex(String type) {
        int index = TRADE_INDEX_CURRENT;
        switch (type) {
            case TRADE_TYPE_CURRENT:
                index = TRADE_INDEX_CURRENT;
                break;
            case TRADE_TYPE_DONE:
                index = TRADE_INDEX_DONE;
                break;
            case TRADE_TYPE_CLOSED:
                index = TRADE_INDEX_CLOSE;
                break;
        }
        return index;
    }

    private void doQuery(int page, String keyword, int direction) {
        if (mPresenter == null) {
            return;
        }
        switch (mTradeType) {
            case TRADE_TYPE_CURRENT:
                mPresenter.getCurrentTrades(page, keyword, direction);
                break;
            case TRADE_TYPE_DONE:
                //mPresenter.getHistoryTrades(getPageNumber(), "", 0);
                mPresenter.getOrders(page, keyword, direction);
                break;
            case TRADE_TYPE_CLOSED:
                mPresenter.getClosedTrades(page, keyword, direction);
                break;
            default:
                mPresenter.getCurrentTrades(page, keyword, direction);
                break;
        }
    }

    private void initPage() {
        mPageMap = new HashMap<>();
        mPageMap.put(TRADE_TYPE_CURRENT, 1);
        mPageMap.put(TRADE_TYPE_DONE, 1);
        mPageMap.put(TRADE_TYPE_CLOSED, 1);
    }

    private void initData() {
        mOrderMaps = new HashMap<>();
        mOrderMaps.put(TRADE_TYPE_CURRENT, new LinkedList<>());
        mOrderMaps.put(TRADE_TYPE_DONE, new LinkedList<>());
        mOrderMaps.put(TRADE_TYPE_CLOSED, new LinkedList<>());
    }

    private void initFreshLayout() {
        mSmartRefreshLayout.setEnableOverScrollBounce(false)
                .setEnableLoadMore(true)
                .setEnableAutoLoadMore(false)
                .setEnableOverScrollDrag(false)
                .setRefreshFooter(
                        new ClassicsFooter(_mActivity)
                        .setFinishDuration(0)) /* for load more UE */
                .setOnRefreshListener(refreshLayout -> {
                    mDataFreshMode = MODE_REFRESH;
                    doRefresh(mTradeType, TRADE_KEYWORD_ALL, TRADE_DIRECTION_ALL);
                })
                .setOnLoadMoreListener(refreshLayout -> {
                    mDataFreshMode = MODE_LOAD_MORE;
                    doLoadMore(mTradeType, TRADE_KEYWORD_ALL, TRADE_DIRECTION_ALL);
                });

        mAdapter = new MultiTypeAdapter(mOrderMaps.get(TRADE_TYPE_CURRENT));
        //mAdapter.register(EmptyViewBinder.EmptyData.class, new EmptyViewBinder());
        mAdapter.register(OrderInfo.class, new OrderViewBinder((v, order) -> {
            // show status
            order.getTradeInfo().setDataType(null);
            Bundle data = new Bundle();
            data.putParcelable(OrdersDetailFragment.ARG_ORDER_INFO, order);
            data.putString(OrdersDetailFragment.ARG_TRADE_TYPE, mTradeType);
            OrdersDetailFragment detailFragment = findFragment(OrdersDetailFragment.class);
            if (detailFragment == null) {
                detailFragment = OrdersDetailFragment.newInstance();
            }
            detailFragment.setArguments(data);
            start(detailFragment);
        }));
        //mAdapter.register(OrderInfo.class, new OrderDetailViewBinder());

        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(mAdapter);

        // place holder
        PlaceHolderView empty = new PlaceHolderView.Config()
                .addPlaceHolder(EmptyHolder.class)
                .addPlaceHolder(ErrorHolder.class)
                .addPlaceHolder(LoadingPlaceHolder.class)
                .build();
        mPlaceHolderMgr = empty.bind(mRecycleView);
    }

    /**
     * do pull down refresh
     *
     * @param currentType current filter type
     */
    private void doRefresh(String currentType, String keyWord, int direction) {
        if (mPresenter == null) {
            return;
        }
        // query first page
        int requestPage = 1;
        switch (currentType) {
            case TRADE_TYPE_CURRENT:
                mPresenter.getCurrentTrades(requestPage, keyWord, direction);
                break;
            case TRADE_TYPE_DONE:
                //mPresenter.getHistoryTrades(requestPage, keyWord, direction);
                mPresenter.getOrders(requestPage, keyWord, direction);
                break;
            case TRADE_TYPE_CLOSED:
                mPresenter.getClosedTrades(requestPage, keyWord, direction);
                break;
        }
    }

    /**
     * do pull up load
     *
     * @param currentType current filter type
     */
    private void doLoadMore(String currentType, String keyWord, int direction) {
        if (mPresenter == null) {
            return;
        }
        int requestPage = mPageMap.get(currentType) + 1;
        switch (currentType) {
            case TRADE_TYPE_CURRENT:
                mPresenter.getCurrentTrades(requestPage, keyWord, direction);
                break;
            case TRADE_TYPE_DONE:
                mPresenter.getOrders(requestPage, keyWord, direction);
                break;
            case TRADE_TYPE_CLOSED:
                mPresenter.getClosedTrades(requestPage, keyWord, direction);
                break;
        }
    }

    @OnClick(R2.id.trade_order_back)
    public void goBack() {
        onBackPressed();
    }

    private void switchTabTo(String typeTo) {
        mTradeType = typeTo;
        mDataFreshMode = MODE_DEFAULT;
        doQuery(getPageNumber(), TRADE_KEYWORD_ALL, TRADE_DIRECTION_ALL);
    }

    private int getPageNumber() {
        return mPageMap.get(mTradeType);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        switchTabTo(mTradeType);
        if(PLACE_HOLDER_LOADING_ENABLED) {
            mPlaceHolderMgr.showPlaceHolder(LoadingPlaceHolder.class);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    public void doEmptyAction(String type) {
        switch (type) {
            case TRADE_TYPE_CURRENT:
                TradeFragment.navigateTrade(null);
                break;
            case TRADE_TYPE_DONE:
                TradeFragment.navigateTrade(null);
                break;
            case TRADE_TYPE_CLOSED:
                TradeFragment.navigateTrade(null);
                break;
        }
    }

    @Override
    public void onTradeLoaded(String type, List<TradeInfo> data) {
        LogUtil.d(type + " updated, data size:" + data.size());
        updateOrders(type, convertToOrderList(data));
        updateEmptyView(type);
    }

    @Override
    public void onOrdersLoaded(List<OrderInfo> data) {
        LogUtil.d(mTradeType + " updated, data size:" + data.size());
        updateOrders(mTradeType, data);
        updateEmptyView(mTradeType);
    }

    @Override
    public void onQueryFailed(int errorCode, String msg) {
        finishRefresh(false);
        setLoadMoreStatus(false);
        final String tips = "errorCode:" + errorCode;
        mPlaceHolderMgr.hidePlaceHolder();
        if(PLACE_HOLDER_ERROR_ENABLED) {
            mPlaceHolderMgr.showPlaceHolder(ErrorHolder.class, new IPlaceHolderManager.IExpose() {
                @Override
                public void expose(@NonNull View placeHolder) {
                    ImageView emptyImageView;
                    TextView mEmptyTypeTipsView;

                    emptyImageView = placeHolder.findViewById(R.id.trade_list_error_img);
                    mEmptyTypeTipsView = placeHolder.findViewById(R.id.trade_error_type_tips);

                    mEmptyTypeTipsView.setText(tips);
                }
            });
        }
    }

    private void setLoadMoreStatus(boolean canLoadMore) {
        mSmartRefreshLayout.setEnableLoadMore(canLoadMore);
    }

    private void updateEmptyView(String type) {
        List<OrderInfo> currentTrades = mOrderMaps.get(type);
        if (CollectionUtils.isNotBlank(currentTrades)) {
            mPlaceHolderMgr.hidePlaceHolder();
            return;
        }
        final String tips;
        final String actionTips;
        Resources res = getResources();
        switch (type) {
            case TRADE_TYPE_CURRENT:
                tips = res.getString(R.string.trade_order_empty_current);
                actionTips = res.getString(R.string.trade_order_empty_do_trade);
                break;
            case TRADE_TYPE_DONE:
                tips = res.getString(R.string.trade_order_empty_done);
                actionTips = res.getString(R.string.trade_order_empty_do_trade);
                break;
            case TRADE_TYPE_CLOSED:
                tips = res.getString(R.string.trade_order_empty_closed);
                actionTips = res.getString(R.string.trade_order_empty_do_trade);
                break;
            default:
                tips = null;
                actionTips = null;
                break;
        }

        mPlaceHolderMgr.showPlaceHolder(EmptyHolder.class, new IPlaceHolderManager.IExpose() {
            @Override
            public void expose(@NonNull View placeHolder) {
                ImageView emptyImageView;
                TextView mEmptyTypeTipsView;
                TextView mEmptyActionView;

                emptyImageView = placeHolder.findViewById(R.id.trade_list_empty_img);
                mEmptyTypeTipsView = placeHolder.findViewById(R.id.trade_empty_type_tips);
                mEmptyActionView = placeHolder.findViewById(R.id.trade_empty_action);

                mEmptyTypeTipsView.setText(tips);
                mEmptyActionView.setText(actionTips);
                mEmptyActionView.setOnClickListener(view -> doEmptyAction(type));
            }
        });
    }

    private List<OrderInfo> convertToOrderList(List<TradeInfo> data) {
        // convert to Orders
        List<OrderInfo> orders = new ArrayList<>();
        for (TradeInfo tradeInfo : data) {
            OrderInfo info = new OrderInfo();
            info.setTradeInfo(tradeInfo);
            orders.add(info);
        }
        return orders;
    }

    private void updateOrders(final String listType, List<OrderInfo> data) {
        updateTradeInfoType(listType, data);
        final List<OrderInfo> orderInfos = mOrderMaps.get(listType);
        // tab change
        if (mDataFreshMode == MODE_DEFAULT) {
            orderInfos.clear();
            orderInfos.addAll(data);
            mAdapter.setItems(orderInfos);
            mAdapter.notifyDataSetChanged();
            mOrderMaps.put(listType, orderInfos);
        } else {
            final List<OrderInfo> newOrderInfos = new LinkedList<>();
            if(mDataFreshMode == MODE_LOAD_MORE) {
                // append the previous data all
                newOrderInfos.addAll(0, orderInfos);
            }
            newOrderInfos.addAll(data);
            RxUtils.async(
                    () -> {
                        DiffCallBackUtil diffCallBack = new DiffCallBackUtil(orderInfos, newOrderInfos);
                        return DiffUtil.calculateDiff(diffCallBack);
                    },
                    diffResult -> {
                        orderInfos.clear();
                        orderInfos.addAll(newOrderInfos);
                        mOrderMaps.put(listType, orderInfos);
                        diffResult.dispatchUpdatesTo(mAdapter);
                        finishRefresh(true);
                    });
        }
        // has more data
        setLoadMoreStatus(data.size() >= PAGE_SIZE);
    }

    private void finishRefresh(boolean success) {
        if(mSmartRefreshLayout.isLoading()) {
            mSmartRefreshLayout.finishLoadMore(0, success, false);
        }
        if(mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh(0, success);
        }
    }


    /**
     * the order done list, can not display trade-status
     *
     * @param type  which type for this list
     * @param infos the trades list
     */
    private void updateTradeInfoType(String type, List<OrderInfo> infos) {
        for (OrderInfo info : infos) {
            TradeInfo trade = info.getTradeInfo();
            trade.setDataType(type);
        }
    }

}
