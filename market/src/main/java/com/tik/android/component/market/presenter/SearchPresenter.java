package com.tik.android.component.market.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.bussiness.market.IUserFavorCallback;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.market.R;
import com.tik.android.component.market.StockApi;
import com.tik.android.component.market.bean.StockSearchResult;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bussiness.database.MarketOperate;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.contract.ISearchContract;
import com.tik.android.component.market.ui.adapter.diffutil.SearchHistoryCallback;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.MarketDataUtils;

import org.qiyi.video.svg.Andromeda;
import org.qiyi.video.svg.event.Event;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends RxPresenter<ISearchContract.View> implements ISearchContract.Presenter {
    private List<MarketStockEntity> mSearchEntities = new ArrayList<>();
//    private PlaceHolderManager placeHolderMgr;

    /*public void setPlaceHolderMgr(PlaceHolderManager placeHolderMgr) {
        this.placeHolderMgr = placeHolderMgr;
    }*/

    @Override
    public void getSearchResult(String srcText) {
        observe(ApiProxy.getInstance().getApi(StockApi.class).getSearchResult(srcText))
                .compose(RxUtils.bindToLifecycle(mView))
                /*.doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        placeHolderMgr.showPlaceHolder(LoadingPlaceHolder.class);
                    }
                })*/
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .map(new Function<Result<List<StockSearchResult>>, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(Result<List<StockSearchResult>> searchResult) throws Exception {
                        List<MarketStockEntity> results = new ArrayList<>();
                        if (searchResult != null) {
                            List<StockSearchResult> stockSearchResults = searchResult.getData();
                            if (stockSearchResults != null && stockSearchResults.size() != 0) {
                                List<String> userFavor = UserDataManager.getInstance().getUserFavor();

                                for (StockSearchResult stockSearchResult : stockSearchResults) {
                                    if (stockSearchResult != null) {
                                        MarketStockEntity entity = MarketDataUtils.convertSearchEntity(stockSearchResult);

                                        if (userFavor.contains(stockSearchResult.getSymbol())) {
                                            entity.setFavor(true);
                                        } else {
                                            entity.setFavor(false);
                                        }

                                        results.add(entity);
                                    }
                                }
                            }
                        }
                        return results;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                /*.doOnNext(new Consumer<List<MarketStockEntity>>() {
                    @Override
                    public void accept(List<MarketStockEntity> results) throws Exception {
                        if (results.isEmpty()) {
                            placeHolderMgr.showPlaceHolder(SearchNoDataPlaceHolder.class);
                        } else {
                            placeHolderMgr.hidePlaceHolder();
                        }
                    }
                })*/
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> searchEntities) {
                        if (searchEntities.size() == 0) {
                            searchEntities.add(new MarketStockEntity(Constant.NO_SEARCH_RESULT));
                        }

                        getAndUpdateData(searchEntities);
                    }
                });
    }

    @Override
    public void clearSearchHistory() {
        Flowable.just(1)
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.bindToLifecycle(mView))
                .map(new Function<Integer, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(Integer integer) throws Exception {
                        MarketOperate.getInstance().clearSearchData();
                        return new ArrayList();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                /*.doOnNext(new Consumer<List<MarketStockEntity>>() {
                    @Override
                    public void accept(List<MarketStockEntity> result) throws Exception {
                        if (result.isEmpty()) {
                            placeHolderMgr.showPlaceHolder(SearchNoDataPlaceHolder.class);
                        } else {
                            placeHolderMgr.hidePlaceHolder();
                        }
                    }
                })*/
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> searchEntities) {
                        getAndUpdateData(searchEntities);
                    }
                });
    }

    @Override
    public void loadSearchHistory() {
        MarketOperate.getInstance().getHistory()
                .subscribeOn(Schedulers.computation())
                /*.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        placeHolderMgr.showPlaceHolder(LoadingPlaceHolder.class);
                    }
                })*/
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(mView))
                .map(new Function<List<MarketStockEntity>, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(List<MarketStockEntity> searchEntities) throws Exception {
                        if (searchEntities.size() != 0) {
                            MarketStockEntity entity = new MarketStockEntity(Constant.HEAD_TYPE);
                            searchEntities.add(0, entity);
                        }
                        return searchEntities;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                /*.doOnSuccess(new Consumer<List<MarketStockEntity>>() {
                    @Override
                    public void accept(List<MarketStockEntity> result) throws Exception {
                        if (result.isEmpty()) {
                            placeHolderMgr.showPlaceHolder(SearchNoDataPlaceHolder.class);
                        } else {
                            placeHolderMgr.hidePlaceHolder();
                        }
                    }
                })*/
                .subscribe(new SingleObserver<List<MarketStockEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<MarketStockEntity> marketStockEntities) {
                        getAndUpdateData(marketStockEntities);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void updateUserFavor(boolean isFavor, Bundle bundle, Object view) {
        MarketUtils.updateUserFavor(isFavor, bundle, view, new IUserFavorCallback() {
            @Override
            public void onSuccess() {
                if (isFavor) {
                    ToastUtil.cancel();
                    ToastUtil.showToastShort(BaseApplication.getAPPContext().getString(R.string.market_string_add_favor_success));
                } else {
                    ToastUtil.cancel();
                    ToastUtil.showToastShort(BaseApplication.getAPPContext().getString(R.string.market_string_delete_favor_success));
                }

                Andromeda.publish(new Event(Constant.EVENT_FAVOR_ENABLE, null));
            }

            @Override
            public void onError() {
                //Todo 更新服务器用户自选失败，刷新UI返回之前状态 updateUI有问题
                Andromeda.publish(new Event(Constant.EVENT_FAVOR_ENABLE, null));
            }
        });
    }

    @NonNull
    private void getAndUpdateData(List<MarketStockEntity> newList) {
        if (mView != null) {
            if (mSearchEntities.isEmpty()) {
                mSearchEntities.addAll(newList);
                mView.firstFillUpData(mSearchEntities);
            }
            List<MarketStockEntity> mySearchHistories = new ArrayList<>(mSearchEntities.size());
            mySearchHistories.addAll(mSearchEntities);
            SearchHistoryCallback diffCallback = new SearchHistoryCallback(mySearchHistories, newList);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            mSearchEntities.clear();
            mSearchEntities.addAll(newList);
            mView.updateData(diffResult);
        }
    }
}
