package com.tik.android.component.trade.module.trade.presenter;

import android.text.TextUtils;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.bussiness.property.bean.CoinAssets;
import com.tik.android.component.bussiness.property.bean.CoinInfo;
import com.tik.android.component.bussiness.property.bean.Rate;
import com.tik.android.component.bussiness.property.bean.StockAssets;
import com.tik.android.component.bussiness.service.property.IPropertyService;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.trade.module.order.OrderApi;
import com.tik.android.component.trade.module.order.OrderConstants;
import com.tik.android.component.trade.module.order.bean.TradeInfo;
import com.tik.android.component.trade.module.TradeConstants;
import com.tik.android.component.trade.module.trade.TradeApi;
import com.tik.android.component.trade.module.trade.bean.OrderSubmitResult;
import com.tik.android.component.trade.module.trade.contract.TradeContract;

import org.qiyi.video.svg.Andromeda;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/28.
 */
public class TradePresenter extends RxPresenter<TradeContract.View> implements TradeContract.Presenter {

    private TimeCache<List<CoinAssets>> mQueryCoinProperty;

    private TimeCache<List<StockAssets>> mQueryStockProperty;

    private TimeCache<Double> mUsdtRate;

    private TimeCache<Double> mHkdRate;

    public TradePresenter() {
        IPropertyService propertyService = Andromeda.getLocalService(IPropertyService.class);
        mQueryCoinProperty = new TimeCache<>(propertyService.getCoinAssets(), 30 * 1000, Arrays.asList(new CoinAssets())); // 30s刷新
        mQueryStockProperty = new TimeCache<>(propertyService.getStockAssets(), 30 * 1000, Arrays.asList(new StockAssets()));
        mHkdRate = new TimeCache<>(
                propertyService.getRate().map(Rate::getHKD),
                60 * 1000, // 1分钟
                0.0); // 默认值
        mUsdtRate = new TimeCache<>(
                propertyService.getCoinInfo()
                        .flatMap((Function<List<CoinInfo>, Publisher<CoinInfo>>) Flowable::fromIterable)
                        .filter(coinInfo -> TextUtils.equals(TradeConstants.COIN_CURRENCY_USDT, coinInfo.getSymbol()))
                        .map(info -> 1.0 / (1 - info.getOtcFee())),
                60 * 1000,
                1.0); // 默认值
    }

    @Override
    public Flowable<Double> queryCoinBalance(boolean foreNetwork) {
        return mQueryCoinProperty.getFlowable(foreNetwork)
                .flatMap((Function<List<CoinAssets>, Publisher<CoinAssets>>) Flowable::fromIterable)
                .map(coinAssets -> coinAssets.getBalance() / Math.pow(10, coinAssets.getDecimal()))
                .defaultIfEmpty(0.0);
    }

    @Override
    public Flowable<Integer> queryStockBalance(String stockSymbol, boolean foreNetwork) {
        return mQueryStockProperty.getFlowable(foreNetwork)
                .flatMap((Function<List<StockAssets>, Publisher<StockAssets>>) Flowable::fromIterable)
                .filter(stockProperty -> TextUtils.equals(stockSymbol, stockProperty.getSymbol()))
                .map(StockAssets::getBalance)
                .defaultIfEmpty(0);
    }

    @Override
    public Flowable<Double> queryUsdtRate(boolean foreNetwork) {
        return mUsdtRate.getFlowable(foreNetwork);
    }

    @Override
    public Flowable<Double> queryHkdRate(boolean foreNetwork) {
        return mHkdRate.getFlowable(foreNetwork);
    }

    @Override
    public void getCurrentTrades(int page, String keyWord, int direction) {
        final String dataTypeCurrent = OrderConstants.TRADE_TYPE_CURRENT;

        observe(ApiProxy.getInstance().getApi(OrderApi.class)
                .getTrades(dataTypeCurrent, page, keyWord, direction))
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
                        LogUtil.d("getCurrentTrade:" + tradeInfos);
                        if (mView != null) {
                            mView.onTradeLoaded(dataTypeCurrent, tradeInfos);
                        }
                    }
                });
    }

    public Flowable<Result<OrderSubmitResult>> buy(String stockSymbol, int totalSupply, float price, String coinSymbol) {
        return observe(ApiProxy.getInstance().getApi(TradeApi.class).buy(stockSymbol, totalSupply, price, coinSymbol));
    }

    @Override
    public Flowable<Result<OrderSubmitResult>> sell(String stockSymbol, int totalSupply, float price, String coinSymbol) {
        return observe(ApiProxy.getInstance().getApi(TradeApi.class).sell(stockSymbol, totalSupply, price, coinSymbol));
    }

}
