package com.tik.android.component.market.service;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.error.GlobalErrorUtil;
import com.tik.android.component.bussiness.market.IChartView;
import com.tik.android.component.bussiness.market.IPriceDataCallback;
import com.tik.android.component.bussiness.market.IUserFavorCallback;
import com.tik.android.component.bussiness.service.market.IMarketService;
import com.tik.android.component.market.StockApi;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bean.UserFavorParam;
import com.tik.android.component.market.bussiness.ChartDataRequester;
import com.tik.android.component.market.bussiness.database.MarketOperate;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.ui.fragment.SearchStockFragment;
import com.tik.android.component.market.ui.fragment.StockChartFragment;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.MarketModelUtils;
import com.tik.android.component.market.widget.StockChartView;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jianglixuan on 2018/11/15
 */
public class MarketServiceImpl implements IMarketService {
    @Override
    public BasicFragment getStockChartFragment(String symbol) {
        return StockChartFragment.newInstance(symbol);
    }

    @Override
    public void getStockPrice(String symbol, Object view, IPriceDataCallback callback) {
        ChartDataRequester.getStockPrice(symbol, view, callback);
    }

    @Override
    public IChartView getIChartView(Context context) {
        return new StockChartView(context);
    }

    @Override
    public BasicFragment getSearchStockFragment() {
        return SearchStockFragment.newInstance();
    }

    @Override
    public void goToChartFragment(@NonNull BasicFragment from, String symbol) {
        MarketModelUtils.goToChartFragment(from, symbol);
    }

    @Override
    public void goToChartActivity(@NonNull BasicFragment from, String symbol) {
        MarketModelUtils.goToChartActivity(from, symbol);
    }

    @Override
    public void goToSearchFragment(@NonNull BasicFragment from) {
        MarketModelUtils.goToSearchFragment(from);
    }

    @Override
    public void goToSearchFragmentForResult(BasicFragment from) {
        MarketModelUtils.goToSearchFragmentForResult(from);
    }

    @Override
    public void updateUserFavor(boolean isFavor, Bundle bundle, Object view, IUserFavorCallback callback) {
        MarketStockEntity entity = bundle.getParcelable(Constant.USER_FAVOR_DATA);

        if (null == entity) {
            return;
        }

        entity.setTime(System.currentTimeMillis());
        boolean isLocal = UserDataManager.getInstance().getLocalSymbols().contains(entity.getSymbol());

        if (isLocal) {
            entity.setLocal(true);
        } else {
            entity.setLocal(false);
        }

        if (LocalAccountInfoManager.getInstance().getUser() == null) {
            updateGuestFavor(isFavor, entity, view, callback);
        } else {
            updateLoggedFavor(isFavor, entity, view, callback);
        }
    }

    /**
     * 未登录用户存储本地数据库
     */
    private void updateGuestFavor(boolean isFavor, MarketStockEntity entity, Object view, IUserFavorCallback callback) {
        List<String> favorList = UserDataManager.getInstance().getUserFavor();

        if (isFavor) {
            favorList.add(entity.getSymbol());
        } else {
            favorList.remove(entity.getSymbol());
        }

        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                Long insertResult = MarketOperate.getInstance().insertData(entity);
                emitter.onNext(insertResult != 0);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.computation())
                .compose(GlobalErrorUtil.handleGlobalError())
                .compose(RxUtils.rxSchedulerHelperForFlowable())
                .compose(RxUtils.bindToLifecycle(view))
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean integer) {
                        if (integer) {
                            //更新内存数据
                            UserDataManager.getInstance().setUserFavor(favorList);
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        super.onError(errorCode, errorMsg);
                        callback.onError();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        callback.onSuccess();
                    }
                });
    }

    /**
     * 已登录用户更新服务器数据
     */
    private void updateLoggedFavor(boolean isFavor, MarketStockEntity entity, Object view, IUserFavorCallback callback) {
        List<String> favorList = UserDataManager.getInstance().getUserFavor();

        if (isFavor) {
            favorList.add(entity.getSymbol());
        } else {
            favorList.remove(entity.getSymbol());
        }

        //更新服务器数据
        UserFavorParam favor = new UserFavorParam();
        favor.setFav(favorList);

        ApiProxy.getInstance().getApi(StockApi.class).updateUserFavor(favor)
                .compose(GlobalErrorUtil.handleGlobalError())
                .compose(RxUtils.rxSchedulerHelperForFlowable())
                .compose(RxUtils.bindToLifecycle(view))
                .observeOn(Schedulers.computation())
                .map(new Function<Result, Boolean>() {
                    @Override
                    public Boolean apply(Result result) throws Exception {
                        //更新数据库数据
                        Long insertResult = MarketOperate.getInstance().insertData(entity);
                        return insertResult != 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean integer) {
                        if (integer) {
                            //更新内存数据
                            UserDataManager.getInstance().setUserFavor(favorList);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        super.onError(errorCode, errorMsg);
                        callback.onError();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        callback.onSuccess();
                    }
                });
    }
}
