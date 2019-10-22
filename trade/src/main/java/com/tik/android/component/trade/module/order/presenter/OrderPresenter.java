package com.tik.android.component.trade.module.order.presenter;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.trade.module.order.OrderConstants;
import com.tik.android.component.trade.module.order.OrderApi;
import com.tik.android.component.trade.module.order.bean.OrderInfo;
import com.tik.android.component.bussiness.api.PageBean;
import com.tik.android.component.trade.module.order.bean.TradeInfo;
import com.tik.android.component.trade.module.order.contract.OrderContract;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/23
 */
public class OrderPresenter extends RxPresenter<OrderContract.View> implements OrderContract.Presenter {

    @Override
    public void getCurrentTrades(int page, String keyWord, int direction) {
        getTrades(OrderConstants.TRADE_TYPE_CURRENT, page, keyWord, direction)
                .map(pageBeanResult -> {
                    List<TradeInfo> tradeInfos = Collections.emptyList();
                    if (pageBeanResult.getData() != null && pageBeanResult.getData().getTotal() > 0) {
                        tradeInfos = pageBeanResult.getData().getList();
                    }
                    return tradeInfos;
                })
                .safeSubscribe(new NormalSubscriber<List<TradeInfo>>() {
                    @Override
                    public void onNext(List<TradeInfo> tradeInfos) {
                        LogUtil.d("getCurrentTrade:" + tradeInfos);
                        if (mView != null) {
                            mView.onTradeLoaded(OrderConstants.TRADE_TYPE_CURRENT, tradeInfos);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if(mView != null) {
                            mView.onQueryFailed(errorCode, errorMsg);
                        }
                    }
                });
    }

    @Override
    public void getHistoryTrades(int page, String keyWord, int direction) {
        observe(ApiProxy.getInstance().getApi(OrderApi.class)
                .getHistoryTrades(page, keyWord, direction)) /* get history directly */
                .compose(RxUtils.bindToLifecycle(mView))
                .map(pageBeanResult -> {
                    List<TradeInfo> tradeInfos = Collections.emptyList();
                    if (pageBeanResult.getData() != null && pageBeanResult.getData().getTotal() > 0) {
                        tradeInfos = pageBeanResult.getData().getList();
                    }
                    return tradeInfos;
                })
                .safeSubscribe(new NormalSubscriber<List<TradeInfo>>() {
                    @Override
                    public void onNext(List<TradeInfo> tradeInfos) {
                        LogUtil.d("getHistoryTrades:" + tradeInfos);
                        if (mView != null) {
                            mView.onTradeLoaded(OrderConstants.TRADE_TYPE_DONE, tradeInfos);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if(mView != null) {
                            mView.onQueryFailed(errorCode, errorMsg);
                        }
                    }
                });
    }

    @Override
    public void getSucceedTrades(int page, String keyWord, int direction) {
        getTrades(OrderConstants.TRADE_TYPE_SUCCEED, page, keyWord, direction)
                .map(pageBeanResult -> {
                    List<TradeInfo> tradeInfos = Collections.emptyList();
                    if (pageBeanResult.getData() != null && pageBeanResult.getData().getTotal() > 0) {
                        tradeInfos = pageBeanResult.getData().getList();
                    }
                    return tradeInfos;
                })
                .safeSubscribe(new NormalSubscriber<List<TradeInfo>>() {
                    @Override
                    public void onNext(List<TradeInfo> tradeInfos) {
                        LogUtil.d("getSucceedTrades:" + tradeInfos);
                        if (mView != null) {
                            mView.onTradeLoaded(OrderConstants.TRADE_TYPE_SUCCEED, tradeInfos);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if(mView != null) {
                            mView.onQueryFailed(errorCode, errorMsg);
                        }
                    }
                });
    }

    @Override
    public void getClosedTrades(int page, String keyWord, int direction) {
        getTrades(OrderConstants.TRADE_TYPE_CLOSED, page, keyWord, direction)
                .map(pageBeanResult -> {
                    List<TradeInfo> tradeInfos = Collections.emptyList();
                    if (pageBeanResult.getData() != null && pageBeanResult.getData().getTotal() > 0) {
                        tradeInfos = pageBeanResult.getData().getList();
                    }
                    return tradeInfos;
                })
                .safeSubscribe(new NormalSubscriber<List<TradeInfo>>() {
                    @Override
                    public void onNext(List<TradeInfo> tradeInfos) {
                        LogUtil.d("getClosedTrades:" + tradeInfos);
                        if (mView != null) {
                            mView.onTradeLoaded(OrderConstants.TRADE_TYPE_CLOSED, tradeInfos);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if(mView != null) {
                            mView.onQueryFailed(errorCode, errorMsg);
                        }
                    }
                });
    }

    private Flowable<Result<PageBean<TradeInfo>>> getTrades(String type, int page, String keyWord, int direction) {
        return observe(ApiProxy.getInstance().getApi(OrderApi.class)
                .getTrades(type, page, keyWord, direction))
                .compose(RxUtils.bindToLifecycle(mView));
    }

    /**
     * get the trade that status is succeed
     * @param page
     * @param keyWord
     * @param direction
     */
    @Override
    public void getOrders(int page, String keyWord, int direction) {
        observe(ApiProxy.getInstance().getApi(OrderApi.class)
                .getOrders(page, keyWord, direction))
                .compose(RxUtils.bindToLifecycle(mView))
                .map(pageBean -> {
                    List<OrderInfo> orderInfos = Collections.emptyList();
                    if (pageBean.getData() != null && pageBean.getData().getTotal() > 0) {
                        orderInfos = pageBean.getData().getList();
                    }
                    return orderInfos;
                })
                .safeSubscribe(new NormalSubscriber<List<OrderInfo>>() {
                    @Override
                    public void onNext(List<OrderInfo> orderBeans) {
                        if(mView != null) {
                            mView.onOrdersLoaded(orderBeans);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if(mView != null) {
                            mView.onQueryFailed(errorCode, errorMsg);
                        }
                    }
                });
    }

}
