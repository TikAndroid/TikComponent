package com.tik.android.component.property.presenter;

import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.bussiness.property.bean.StockAssets;
import com.tik.android.component.bussiness.property.bean.CoinAssets;
import com.tik.android.component.property.PropertyServiceImpl;
import com.tik.android.component.property.bean.PropertyData;
import com.tik.android.component.property.contract.PropertyContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PropertyPresenter extends RxPresenter<PropertyContract.View> implements PropertyContract.Presenter {
    @Override
    public void getTotalData() {
        PropertyServiceImpl service = new PropertyServiceImpl();
        Flowable.zip(service.getCoinAssets(), service.getStockAssets(), service.getRate(),
                (coinAssets, stockAssets, rate) -> wrapData(coinAssets, stockAssets, rate.getHKD()))
                .compose(RxUtils.bindToLifecycle(mView))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<PropertyData>() {
                    @Override
                    public void onNext(PropertyData propertyData) {
                        if (mView != null) {
                            mView.showTotalData(propertyData);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (mView != null) {
                            mView.showError();
                        }
                    }
                });
    }

    private PropertyData wrapData(List<CoinAssets> coins, List<StockAssets> stocks, double rateUsToHk) {
        PropertyData propertyData = new PropertyData();
        double hkStockAssets = 0;
        double usStockAssets = 0;
        double profit = 0;
        double totalHoldPrice = 0;
        List<StockAssets> stockList = new ArrayList<>();
        for (int i = 0; i < stocks.size(); i++) {
            StockAssets stock = stocks.get(i);
            if (stock.getBalance() == 0) {
                //过滤 balance 为 0 的股票
                continue;
            }
            //市价
            stock.setTotalCurrentPrice(stock.getCurrentPrice() * stock.getVolume());
            //盈亏
            stock.setProfit((stock.getCurrentPrice() - stock.getHoldPrice()) * stock.getVolume());
            //盈亏比例
            if (stock.getHoldPrice() * stock.getVolume() != 0) {
                stock.setProfitRatio(stock.getProfit() / (stock.getHoldPrice() * stock.getVolume()));
            }
            if (stock.getCurrency().equals("HKD")) {
                hkStockAssets = hkStockAssets + stock.getTotalCurrentPrice();
                profit = profit + stock.getProfit() / rateUsToHk;
                totalHoldPrice = totalHoldPrice + stock.getHoldPrice() * stock.getVolume() / rateUsToHk;
            } else {
                usStockAssets = usStockAssets + stock.getTotalCurrentPrice();
                profit = profit + stock.getProfit();
                totalHoldPrice = totalHoldPrice + stock.getHoldPrice() * stock.getVolume();
            }
            stockList.add(stock);
        }
        propertyData.setStock(stockList);
        //证券资产
        propertyData.setStockAssets(hkStockAssets / rateUsToHk + usStockAssets);
        //持仓盈亏
        propertyData.setProfit(profit);
        //盈亏比例
        if (totalHoldPrice != 0) {
            propertyData.setProfitRatio(profit / totalHoldPrice);
        }
        //港股持仓
        propertyData.setHkStockAssets(hkStockAssets);
        //美股持仓
        propertyData.setUsStockAssets(usStockAssets);

        CoinAssets coin = coins.get(0);
        double digitalAssets = coin.getVolume() / Math.pow(10, coin.getDecimal());
        double freezeAssets = coin.getFreeze() / Math.pow(10, coin.getDecimal());
        double buyPower = coin.getBalance() / Math.pow(10, coin.getDecimal());
        //购买力
        propertyData.setBuyPower(buyPower);
        //冻结资金
        propertyData.setFreezeAssets(freezeAssets);
        //总资产
        propertyData.setTotalAssets(digitalAssets + propertyData.getStockAssets());
        return propertyData;
    }
}
