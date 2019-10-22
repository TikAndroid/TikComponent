package com.tik.android.component.trade.module.order;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.trade.R;
import com.tik.android.component.trade.R2;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.trade.module.order.bean.TradeInfo;
import com.tik.android.component.trade.module.order.contract.OrderContract;
import com.tik.android.component.trade.module.order.presenter.OrderPresenter;
import com.tik.android.component.trade.module.order.viewbinder.OrderDetailStatusViewBinder;
import com.tik.android.component.trade.module.order.viewbinder.OrderDetailViewBinder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.tik.android.component.trade.module.TradeConstants.STOCK_CURRENCY_HK;
import static com.tik.android.component.trade.module.order.OrderConstants.MONEY_EMPTY_STRING;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_CONSIGN_FAILED;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_DIRECTION_ALL;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_DIRECTION_BUY;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_KEYWORD_ALL;
import static com.tik.android.component.trade.module.order.OrderConstants.TRADE_TYPE_DONE;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/29
 */
public class OrdersDetailFragment extends BaseMVPFragment<OrderPresenter> implements OrderContract.View {

    public static final String ARG_ORDER_INFO = "order_info";
    public static final String ARG_TRADE_INFO = "trade_info";
    public static final String ARG_TRADE_TYPE = "trade_type";

    public static OrdersDetailFragment newInstance() {
        return new OrdersDetailFragment();
    }

    // header
    @BindView(R2.id.trade_stock_currency)
    ImageView stockCurrency;
    @BindView(R2.id.trade_stock_symbol)
    TextView stockSymbol;
    @BindView(R2.id.trade_stock_full_name)
    TextView stockFullName;
    @BindView(R2.id.trade_status)
    TextView tradeStatus;

    @BindView(R2.id.trade_order_detail_layout)
    protected SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R2.id.trade_order_detail_listview)
    protected RecyclerView mRecyclerView;
    @BindView(R2.id.trade_order_detail_to_mark)
    protected TextView mMarketTv;
    @BindView(R2.id.trade_order_cancel)
    protected TextView mOrderCancelTv;

    private MultiTypeAdapter mAdapter;

    private TradeInfo mTradeInfo;
    private OrderInfo mOrderInfo;
    private List<Object> mDatas;
    private String mOrderType = TRADE_TYPE_DONE;

    @Override
    public int initLayout() {
        return R.layout.trade_order_detail_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        initFreshLayout();
        processArgs();
        // gone cancel
        mOrderCancelTv.setVisibility(View.GONE);
    }

    private void processArgs() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_ORDER_INFO)) {
            //OrderInfo mOrderInfo = args.getParcelable(ARG_ORDER_INFO);
            mOrderInfo = args.getParcelable(ARG_ORDER_INFO);
            mOrderType = args.getString(ARG_TRADE_TYPE);
            mDatas = new ArrayList<>();
            //mDatas.add(mOrderInfo);
            initHeader(mOrderInfo.getTradeInfo());
            mDatas.addAll(buildItems(mOrderInfo));
            mAdapter.setItems(mDatas);
        }
    }

    private void initHeader(TradeInfo tradeInfo) {
        if(tradeInfo == null) {
            return;
        }
        if (STOCK_CURRENCY_HK.equals(tradeInfo.getCurrency())) {
            stockCurrency.setImageResource(R.drawable.ic_tag_hk);
        } else {
            stockCurrency.setImageResource(R.drawable.ic_tag_us);
        }
        stockSymbol.setText(tradeInfo.getStockSymbol());
        stockFullName.setText(OrderUtils.buildStockDisplayName(tradeInfo));
        tradeStatus.setText(tradeInfo.getStatusStr(_mActivity));
    }

    private void initFreshLayout() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            doRefresh();
            refreshLayout.finishRefresh();
        });

        mAdapter = new MultiTypeAdapter();
        mAdapter.register(OrderInfo.class, new OrderDetailStatusViewBinder((v, orderInfo) -> {
            LogUtil.d("status click click view:" + v + ", orderInfo:" + orderInfo);
        }));
        mAdapter.register(ItemBean.class, new OrderDetailViewBinder());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void doRefresh() {
        mPresenter.getOrders(1, TRADE_KEYWORD_ALL, TRADE_DIRECTION_ALL);
    }

    @OnClick(R2.id.trade_order_cancel)
    public void cancelOrder(View v) {
        //mOrderCancelTv
    }

    @OnClick(R2.id.trade_order_detail_to_mark)
    public void goToMarket(View v) {
        //mMarketTv
        if(mOrderInfo == null || mOrderInfo.getTradeInfo() == null) {
            LogUtil.e("no symbol, ignore click");
            return;
        }
        String symbol = mOrderInfo.getTradeInfo().getStockSymbol();
        MarketUtils.goToChartActivity(this, symbol);
    }

    @OnClick(R2.id.trade_order_detail_back)
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void onTradeLoaded(String dataType, List<TradeInfo> data) {
        // maybe trade list ?
    }

    @Override
    public void onOrdersLoaded(List<OrderInfo> orders) {
        LogUtil.d("got the message:\n" + orders);
        List<TradeInfo> tradeInfos = new LinkedList<>();
        for (OrderInfo orderInfo : orders) {
            tradeInfos.add(orderInfo.getTradeInfo());
        }
        onTradeLoaded(mOrderType, tradeInfos);
    }

    @Override
    public void onQueryFailed(int errorCode, String msg) {
        LogUtil.e("error:" + errorCode + ", msg:" + msg);
    }

    private List<ItemBean> buildItems(OrderInfo order) {
        if (order == null) {
            LogUtil.e("receive a null order info");
            return Collections.emptyList();
        }
        final TradeInfo info = order.getTradeInfo();
        final List<ItemBean> items = new LinkedList<>();

        ItemBean itemDirection = new ItemBean();
        Resources res = getResources();
        if (info.getType() == TRADE_DIRECTION_BUY) {
            itemDirection.value = res.getString(R.string.trade_order_direction_buy);
            itemDirection.color = R.color.trade_direction_buy;
        } else { // sell
            itemDirection.value = res.getString(R.string.trade_order_direction_sell);
            itemDirection.color = R.color.trade_direction_sell;
        }
        itemDirection.title = res.getString(R.string.trade_order_detail_trade_direction);
        items.add(itemDirection);

        // from order deal done
        if (TRADE_TYPE_DONE.equals(mOrderType)) {
            generateItem(order, items);
        } else { // closed || current
            generateItem(info, items);
        }

        return items;
    }

    private void generateItem(TradeInfo info, List<ItemBean> items) {
        if (info == null) {
            LogUtil.e("generate item error: order is null");
            return;
        }
        Resources res = getResources();

        ItemBean itemDealNumber = new ItemBean();
        itemDealNumber.title = res.getString(R.string.trade_order_detail_trade_process);
        itemDealNumber.value = info.getDealNumStr(getContext());
        items.add(itemDealNumber);

        // do not show if trade failed
        if (info.getStatus() != TRADE_CONSIGN_FAILED) {
            ItemBean itemAveragePrice = new ItemBean();
            itemAveragePrice.title = res.getString(R.string.trade_order_detail_trade_average);
            if (info.hasTrade()) {
                itemAveragePrice.value = OrderUtils.getMoneyString(info.getActualPrice());
            } else {
                itemAveragePrice.value = MONEY_EMPTY_STRING;
            }
            items.add(itemAveragePrice);

            ItemBean itemTurnover = new ItemBean();
            itemTurnover.title = res.getString(R.string.trade_order_detail_trade_turnover);
            itemTurnover.value = getTurnoverStr(info) + getUnit(info);
            items.add(itemTurnover);

            ItemBean itemFee = new ItemBean();
            itemFee.title = res.getString(R.string.trade_order_detail_trade_fee);
            itemFee.value = getTransactionFee(info) + getUnit(info);
            items.add(itemFee);
        }
        // commission price
        ItemBean itemCommissionPrice = new ItemBean();
        itemCommissionPrice.title = res.getString(R.string.trade_order_detail_commission_price);
        itemCommissionPrice.value = OrderUtils.getMoneyString(info.getPrice());
        items.add(itemCommissionPrice);
        ItemBean itemCommissionAmount = new ItemBean();
        itemCommissionAmount.title = res.getString(R.string.trade_order_detail_commission_amount);
        itemCommissionAmount.value = getTradeTurnoverStr(info) + getUnit(info);
        items.add(itemCommissionAmount);

        ItemBean itemCommissionTime = new ItemBean();
        itemCommissionTime.title = res.getString(R.string.trade_order_detail_commission_time);
        itemCommissionTime.value = OrderUtils.getDateString(info.getCreatedAt());
        items.add(itemCommissionTime);

    }

    private void generateItem(OrderInfo order, List<ItemBean> items) {
        if (order == null || order.getTradeInfo() == null) {
            LogUtil.e("generate item error:" + order);
            return;
        }
        TradeInfo trade = order.getTradeInfo();
        Resources res = getResources();
        ItemBean itemDealNumber = new ItemBean();
        itemDealNumber.title = res.getString(R.string.trade_order_detail_trade_amount);
        itemDealNumber.value = order.getQty() + "";
        items.add(itemDealNumber);
        ItemBean itemAveragePrice = new ItemBean();
        itemAveragePrice.title = res.getString(R.string.trade_order_detail_trade_average);
        itemAveragePrice.value = order.getPrice() != null ? order.getPrice().toPlainString() : "";
        items.add(itemAveragePrice);
        ItemBean itemTurnover = new ItemBean();
        itemTurnover.title = res.getString(R.string.trade_order_detail_trade_turnover);
        itemTurnover.value = getTurnoverStr(order) + getUnit(trade);
        items.add(itemTurnover);

        ItemBean itemFee = new ItemBean();
        itemFee.title = res.getString(R.string.trade_order_detail_trade_fee);
        itemFee.value = getTransactionFee(order) + getUnit(trade);
        items.add(itemFee);

        ItemBean itemCommissionPrice = new ItemBean();
        itemCommissionPrice.title = res.getString(R.string.trade_order_detail_commission_price);
        itemCommissionPrice.value = trade.getPrice() != null ? trade.getPrice().toPlainString() : ""; // trade
        items.add(itemCommissionPrice);
        ItemBean itemCommissionAmount = new ItemBean();
        itemCommissionAmount.title = res.getString(R.string.trade_order_detail_commission_amount);
        itemCommissionAmount.value = getTradeTurnoverStr(trade) + getUnit(trade);
        items.add(itemCommissionAmount);

        ItemBean itemCommissionTime = new ItemBean();
        itemCommissionTime.title = res.getString(R.string.trade_order_detail_commission_time);
        itemCommissionTime.value = OrderUtils.getDateString(trade.getCreatedAt());
        items.add(itemCommissionTime);

        ItemBean itemDealTime = new ItemBean();
        itemDealTime.title = res.getString(R.string.trade_order_detail_deal_time);
        itemDealTime.value = OrderUtils.getDateString(order.getUpdatedAt());
        items.add(itemDealTime);
    }

    /**
     * @param tradeInfo just trade info
     * @return
     */
    private String getTurnoverStr(TradeInfo tradeInfo) {
        if (tradeInfo.getActualPrice() != null) {
            BigDecimal amount = BigDecimal.valueOf(tradeInfo.getTradeSuccessAmount());
            BigDecimal total = tradeInfo.getActualPrice().multiply(amount);
            return OrderUtils.getMoneyString(total);
        }
        return OrderUtils.getMoneyString(0);
    }

    /**
     * the deal turnover with Order info.
     * order.price is the deal price.
     * order.qty is total deal number.
     *
     * @param order
     * @return
     */
    private String getTurnoverStr(OrderInfo order) {
        if (order.getPrice() != null && order.getQty() > 0) {
            // qty * price
            BigDecimal total = order.getPrice().multiply(BigDecimal.valueOf(order.getQty()));
            return OrderUtils.getMoneyString(total);
        }
        return OrderUtils.getMoneyString(0);
    }

    /**
     * insert a space into string to make '100USD' to '100 USD'
     *
     * @param tradeInfo trade info contains the Stock Currency
     * @return format string
     */
    private String getUnit(TradeInfo tradeInfo) {
        return " " + tradeInfo.getCurrency();
    }

    private String getTransactionFee(OrderInfo order) {

        BigDecimal feeRatio = new BigDecimal(order.getTradeInfo().getFeeRatio());
        BigDecimal total = BigDecimal.valueOf(order.getQty())
                .multiply(order.getPrice())
                .multiply(feeRatio);
        return OrderUtils.getMoneyString(total);
    }

    private String getTransactionFee(TradeInfo trade) {
        BigDecimal feeRatio = new BigDecimal(trade.getFeeRatio());
        BigDecimal tradeCount = BigDecimal.valueOf(trade.getTotalSupply());
        BigDecimal total = tradeCount.multiply(feeRatio).multiply(trade.getActualPrice());
        return OrderUtils.getMoneyString(total);
    }

    /**
     * get trade price
     */
    private String getTradeTurnoverStr(TradeInfo tradeInfo) {
        BigDecimal tradeCount = BigDecimal.valueOf(tradeInfo.getTotalSupply());
        BigDecimal total = tradeInfo.getPrice().multiply(tradeCount);
        return OrderUtils.getMoneyString(total);
    }

    public static class ItemBean {
        String title;
        String value;
        int color;

        public ItemBean() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
