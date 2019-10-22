package com.tik.android.component.market.presenter;

import com.google.gson.internal.LinkedTreeMap;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.libcommon.JsonUtil;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.StringUtils;
import com.tik.android.component.market.StockApi;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bussiness.database.MarketOperate;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.contract.IMarketContract;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.MarketDataUtils;
import com.tik.android.component.market.util.SortUtils;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangqiqi on 2018/11/22.
 */
public class MarketPresenter extends RxPresenter<IMarketContract.View> implements IMarketContract.Presenter {
    List<MarketStockEntity> mOriginUsdStocks = new ArrayList<>();
    List<MarketStockEntity> mOriginHkdStocks = new ArrayList<>();
    List<MarketStockEntity> mOriginFavorStocks = new ArrayList<>();
    private NormalSubscriber<List<MarketStockEntity>> mPriceSubscriber;

    public void initOriginData() {
        mOriginUsdStocks.clear();
        mOriginHkdStocks.clear();
        mOriginFavorStocks.clear();
        mOriginUsdStocks.addAll(UserDataManager.getInstance().getUsdStockList());
        mOriginHkdStocks.addAll(UserDataManager.getInstance().getHkdStockList());
        mOriginFavorStocks.addAll(UserDataManager.getInstance().getUserFavorList());
    }

    /**
     * 联网状态下，从数据库中获取股票行情价格
     */
    @Override
    public void getMarketPriceFromDB(String type) {
        //Todo 修改为重新initCache
        List<String> symbolList = new ArrayList<>();
        String querySymbols = "";

        if (type.equals(Constant.HKD_STOCK_CURRENCY)) {
            symbolList = UserDataManager.getInstance().getHkdSymbols();
        } else if (type.equals(Constant.USD_STOCK_CURRENCY)) {
            symbolList = UserDataManager.getInstance().getUsdSymbols();
        }

        querySymbols = StringUtils.listToString(symbolList, ",");

        observe(ApiProxy.getInstance().getApi(StockApi.class).getCurrPriceInfoArray(querySymbols))
                .compose(RxUtils.bindToLifecycle(mView))
                .subscribeOn(Schedulers.computation())
                .map(new Function<Result, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(Result result) throws Exception {
                        List<MarketStockEntity> entityList = new ArrayList<>();
                        List<String> userFavor = new ArrayList<>();
                        List<MarketStockEntity> usdStockList = new ArrayList<>();
                        List<MarketStockEntity> hkdStockList = new ArrayList<>();
                        List<String> usdSymbols = new ArrayList<>();
                        List<String> hkdSymbols = new ArrayList<>();

                        if (null != result) {
                            if (result.getData() instanceof List) { //返回值为List
                                List<LinkedTreeMap> list = (List) result.getData();

                                for (LinkedTreeMap map : list) {
                                    StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                    MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                    entityList.add(entity);

                                    //区分股票类型放入内存
                                    if (Constant.USD_STOCK_CURRENCY.equals(entity.getCurrency())) {
                                        usdStockList.add(entity);
                                        usdSymbols.add(info.getSymbol());
                                    } else if (Constant.HKD_STOCK_CURRENCY.equals(entity.getCurrency())) {
                                        hkdStockList.add(entity);
                                        hkdSymbols.add(info.getSymbol());
                                    }
                                }
                            } else if (result.getData() instanceof LinkedTreeMap) { //返回值为单个数据
                                LinkedTreeMap map = (LinkedTreeMap) result.getData();
                                StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                entityList.add(entity);

                                //区分股票类型放入内存
                                if (Constant.USD_STOCK_CURRENCY.equals(entity.getCurrency())) {
                                    usdStockList.add(entity);
                                    usdSymbols.add(info.getSymbol());
                                } else if (Constant.HKD_STOCK_CURRENCY.equals(entity.getCurrency())) {
                                    hkdStockList.add(entity);
                                    hkdSymbols.add(info.getSymbol());
                                }
                            }

                            UserDataManager.getInstance().setUsdStockList(usdStockList);
                            UserDataManager.getInstance().setUsdSymbols(usdSymbols);
                            UserDataManager.getInstance().setHkdStockList(hkdStockList);
                            UserDataManager.getInstance().setHkdSymbols(hkdSymbols);

                            mOriginUsdStocks.clear();
                            mOriginUsdStocks.addAll(usdStockList);
                            mOriginHkdStocks.clear();
                            mOriginHkdStocks.addAll(hkdStockList);
                        } else {
                            LogUtil.e("getMarketPriceFromDB, Result Error");
                        }

                        //Todo 实时数据存入数据库
//                        MarketOperate.getInstance().insertDatas(entityList);
                        return entityList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> marketPriceEntities) {
                        mView.showMarketStocksPrice(marketPriceEntities);
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        super.onError(errorCode, errorMsg);
                        //无网络时读取本地数据库信息
                        getMarketLocalFromDB(type);
                    }
                });
    }

    /**
     * 离线状态下，获取数据库股票行情信息
     */
    @Override
    public void getMarketLocalFromDB(String type) {
        Flowable.create(new FlowableOnSubscribe<List<MarketStockEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MarketStockEntity>> emitter) throws Exception {
                List<MarketStockEntity> symbolList = new ArrayList<>();

                if (type.equals(Constant.HKD_STOCK_CURRENCY)) {
                    symbolList = UserDataManager.getInstance().getHkdStockList();
                } else if (type.equals(Constant.USD_STOCK_CURRENCY)) {
                    symbolList = UserDataManager.getInstance().getUsdStockList();
                }

                emitter.onNext(symbolList);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.bindToLifecycle(mView))
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> marketStockEntities) {
                        mView.showMarketStocksPrice(marketStockEntities);
                    }
                });
    }

    /**
     * 按照type进行排序，刷新UI
     * @param type 排序类型
     * @param index 展示列表类型
     * @param order 顺序类型
     */
    @Override
    public void sortStock(String type, int index, int order) {
        Flowable.create(new FlowableOnSubscribe<List<MarketStockEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MarketStockEntity>> emitter) throws Exception {
                List<MarketStockEntity> entityList = new ArrayList<>();

                switch (index) {
                    case Constant.MARKET_TAB_FAVOR:
                        if (order == Constant.ORIGIN_ORDER) {
                            entityList = mOriginFavorStocks;
                        } else if (order == Constant.DOWN_ORDER) {
                            if (type.equals(Constant.PRICE_ORDER)) {
                                SortUtils.sortByPriceMarketEntity(UserDataManager.getInstance().getUserFavorList(), false);
                            } else if (type.equals(Constant.GAINS_ORDER)) {
                                SortUtils.sortByGainsMarketEntity(UserDataManager.getInstance().getUserFavorList(), false);
                            }

                            entityList = UserDataManager.getInstance().getUserFavorList();
                        } else if (order == Constant.UP_ORDER) {
                            if (type.equals(Constant.PRICE_ORDER)) {
                                SortUtils.sortByPriceMarketEntity(UserDataManager.getInstance().getUserFavorList(), true);
                            } else if (type.equals(Constant.GAINS_ORDER)) {
                                SortUtils.sortByGainsMarketEntity(UserDataManager.getInstance().getUserFavorList(), true);
                            }

                            entityList = UserDataManager.getInstance().getUserFavorList();
                        }

                        break;
                    case Constant.MARKET_TAB_USD:
                        if (order == Constant.ORIGIN_ORDER) {
                            entityList = mOriginUsdStocks;
                        } else if (order == Constant.DOWN_ORDER) {
                            if (type.equals(Constant.PRICE_ORDER)) {
                                SortUtils.sortByPriceMarketEntity(UserDataManager.getInstance().getUsdStockList(), false);
                            } else if (type.equals(Constant.GAINS_ORDER)) {
                                SortUtils.sortByGainsMarketEntity(UserDataManager.getInstance().getUsdStockList(), false);
                            }

                            entityList = UserDataManager.getInstance().getUsdStockList();
                        } else if (order == Constant.UP_ORDER) {
                            if (type.equals(Constant.PRICE_ORDER)) {
                                SortUtils.sortByPriceMarketEntity(UserDataManager.getInstance().getUsdStockList(), true);
                            } else if (type.equals(Constant.GAINS_ORDER)) {
                                SortUtils.sortByGainsMarketEntity(UserDataManager.getInstance().getUsdStockList(), true);
                            }

                            entityList = UserDataManager.getInstance().getUsdStockList();
                        }

                        break;
                    case Constant.MARKET_TAB_HKD:
                        if (order == Constant.ORIGIN_ORDER) {
                            entityList = mOriginHkdStocks;
                        } else if (order == Constant.DOWN_ORDER) {
                            if (type.equals(Constant.PRICE_ORDER)) {
                                SortUtils.sortByPriceMarketEntity(UserDataManager.getInstance().getHkdStockList(), false);
                            } else if (type.equals(Constant.GAINS_ORDER)) {
                                SortUtils.sortByGainsMarketEntity(UserDataManager.getInstance().getHkdStockList(), false);
                            }

                            entityList = UserDataManager.getInstance().getHkdStockList();
                        } else if (order == Constant.UP_ORDER) {
                            if (type.equals(Constant.PRICE_ORDER)) {
                                SortUtils.sortByPriceMarketEntity(UserDataManager.getInstance().getHkdStockList(), true);
                            } else if (type.equals(Constant.GAINS_ORDER)) {
                                SortUtils.sortByGainsMarketEntity(UserDataManager.getInstance().getHkdStockList(), true);
                            }

                            entityList = UserDataManager.getInstance().getHkdStockList();
                        }

                        break;
                    default:
                        break;
                }

                emitter.onNext(entityList);
            }
        }, BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(mView))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> marketStockEntities) {
                        mView.showMarketStocksPrice(marketStockEntities);
                    }
                });
    }

    /**
     * 获取用户自选列表
     */
    @Override
    public void getUserFavors() {
        observe(ApiProxy.getInstance().getApi(StockApi.class).getUserFavor())
                .compose(RxUtils.bindToLifecycle(mView))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(new Function<Result<List<String>>, Publisher<Result>>() {
                    @Override
                    public Publisher<Result> apply(Result<List<String>> listResult) throws Exception {
                        List<String> userFavor = new ArrayList<>();

                        if (null != listResult) {
                            userFavor.addAll(listResult.getData());
                        } else {
                            LogUtil.e("getUserFavors, Result Error");
                        }

                        return ApiProxy.getInstance().getApi(StockApi.class)
                                .getCurrPriceInfoArray(StringUtils.listToString(userFavor, ","));
                    }
                })
                .map(new Function<Result, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(Result listResult) throws Exception {
                        List<MarketStockEntity> entityList = new ArrayList<>();
                        boolean needUpdateDB = false;
                        List<String> userFavor = new ArrayList<>();

                        if (null != listResult) {
                            if (listResult.getData() instanceof List) { //返回值为List
                                List<LinkedTreeMap> list = (List) listResult.getData();

                                for (LinkedTreeMap map : list) {
                                    StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                    MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                    entity.setFavor(true);
                                    entityList.add(entity);
                                    userFavor.add(entity.getSymbol());
                                }
                            } else if (listResult.getData() instanceof LinkedTreeMap) { //返回值为单个数据
                                LinkedTreeMap map = (LinkedTreeMap) listResult.getData();

                                if (null == map || 0 ==map.size()) {
                                    MarketStockEntity entity = new MarketStockEntity(Constant.NO_FAVOR);
                                    entityList.add(entity);
                                } else {
                                    StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                    MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                    entity.setFavor(true);
                                    entityList.add(entity);
                                    userFavor.add(entity.getSymbol());
                                }
                            }

                            if (userFavor.size() != 0) {
                                needUpdateDB = !(MarketDataUtils.areFavorTheSame(userFavor,
                                        UserDataManager.getInstance().getUserFavor()));
                            }

                            if (listResult.getData() == null) {
                                MarketStockEntity entity = new MarketStockEntity(Constant.NO_FAVOR);
                                entityList.add(entity);
                            }

                            //Todo 插入数据库,更新数据库(如果symbol不存在才插入)  无数据的情况不插入
                            if (needUpdateDB) {
                                MarketOperate.getInstance().insertDatas(entityList);
                                UserDataManager.getInstance().setUserFavor(userFavor);
                                UserDataManager.getInstance().setUserFavorList(entityList);
                                mOriginFavorStocks.clear();
                                mOriginFavorStocks.addAll(entityList);
                            }
                        } else {
                            LogUtil.e("getUserFavors, Result ");
                        }

                        return entityList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> result) {
                        mView.showMarketStocksPrice(result);
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        super.onError(errorCode, errorMsg);
                        //Todo 离线状态、 未登录 下获取本地数据库
                        getLocalUserFavor();
                    }
                });
    }

    /**
     * 离线、未登录情况下，从数据库获取用户自选列表
     */
    @Override
    public void getLocalUserFavor() {
        List<MarketStockEntity> localFavorEntities = new ArrayList<>();
        MarketOperate.getInstance().getFavorStocks()
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.bindToLifecycle(mView))
                .observeOn(Schedulers.computation())
                .flatMap(new Function<List<MarketStockEntity>, Publisher<Result>>() {
                    @Override
                    public Publisher<Result> apply(List<MarketStockEntity> marketPriceEntities) throws Exception {
                        List<String> userFavor = new ArrayList<>();

                        for (MarketStockEntity entity : marketPriceEntities) {
                            userFavor.add(entity.getSymbol());
                        }

                        if (marketPriceEntities.size() == 0) {
                            MarketStockEntity entity = new MarketStockEntity(Constant.NO_FAVOR);
                            localFavorEntities.add(entity);
                        } else {
                            localFavorEntities.addAll(marketPriceEntities);
                            UserDataManager.getInstance().setUserFavorList(localFavorEntities);
                            mOriginFavorStocks.clear();
                            mOriginFavorStocks.addAll(localFavorEntities);
                        }

                        UserDataManager.getInstance().setUserFavor(userFavor);
                        return ApiProxy.getInstance().getApi(StockApi.class)
                                .getCurrPriceInfoArray(StringUtils.listToString(userFavor, ","));
                    }
                })
                .map(new Function<Result, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(Result result) throws Exception {
                        List<MarketStockEntity> entityList = new ArrayList<>();

                        if (null != result) {
                            if (result.getData() != null) {
                                if (result.getData() instanceof List) { //返回值为List
                                    List<LinkedTreeMap> list = (List) result.getData();

                                    for (LinkedTreeMap map : list) {
                                        StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                        MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                        entityList.add(entity);
                                    }

                                    if (list.size() == 0) {
                                        MarketStockEntity entity = new MarketStockEntity(Constant.NO_FAVOR);
                                        entityList.add(entity);
                                    }

                                    mOriginFavorStocks.clear();
                                    mOriginFavorStocks.addAll(entityList);
                                } else if (result.getData() instanceof LinkedTreeMap) { //返回值为单个数据
                                    LinkedTreeMap map = (LinkedTreeMap) result.getData();

                                    if (null == map || 0 == map.size()) {
                                        MarketStockEntity entity = new MarketStockEntity(Constant.NO_FAVOR);
                                        entityList.add(entity);
                                    } else {
                                        StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                        MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                        entityList.add(entity);
                                        mOriginFavorStocks.clear();
                                        mOriginFavorStocks.addAll(entityList);
                                    }
                                }
                            } else {
                                MarketStockEntity entity = new MarketStockEntity(Constant.NO_FAVOR);
                                entityList.add(entity);
                            }

                            UserDataManager.getInstance().setUserFavorList(entityList);
                        } else {
                            LogUtil.e("getLocalUserFavor, Result Error");
                        }

                        //Todo 实时数据存入数据库
//                        .insertDatas(entityList);
                        return entityList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<List<MarketStockEntity>>() {
                    @Override
                    public void onNext(List<MarketStockEntity> marketPriceEntities) {
                        mView.showMarketStocksPrice(marketPriceEntities);
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        super.onError(errorCode, errorMsg);
                        //无网络时读取本地数据库信息
                        mView.showMarketStocksPrice(localFavorEntities);
                    }
                });
    }

    /**
     * 根据symbols获取到股票的价格信息，刷新界面
     * @param marketStocks 多个股票symbol
     */
    @Override
    public void getMarketStocksPrice(String marketStocks, String type) {
        observe(ApiProxy.getInstance().getApi(StockApi.class).getCurrPriceInfoArray(marketStocks))
                .compose(RxUtils.bindToLifecycle(mView))
                .subscribeOn(Schedulers.computation())
                .map(new Function<Result, List<MarketStockEntity>>() {
                    @Override
                    public List<MarketStockEntity> apply(Result result) throws Exception {
                        List<MarketStockEntity> entityList = new ArrayList<>();

                        if (null != result) {
                            if (result.getData() instanceof List) { //返回值为List
                                List<LinkedTreeMap> list = (List) result.getData();

                                for (LinkedTreeMap map : list) {
                                    StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                    MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                    entityList.add(entity);
                                }
                            } else if (result.getData() instanceof LinkedTreeMap) { //返回值为单个数据
                                LinkedTreeMap map = (LinkedTreeMap) result.getData();
                                StockPriceInfo info = JsonUtil.map2object(map, StockPriceInfo.class);
                                MarketStockEntity entity = MarketDataUtils.convertPriceEntity(info);
                                entityList.add(entity);
                            }
                        } else {
                            LogUtil.e("getMarketStocksPrice, Result Error");
                        }

                        if (type.equals(Constant.USD_STOCK_CURRENCY)) {
                            UserDataManager.getInstance().setUsdStockList(entityList);
                            mOriginUsdStocks.clear();
                            mOriginUsdStocks.addAll(entityList);
                        } else if (type.equals(Constant.HKD_STOCK_CURRENCY)) {
                            UserDataManager.getInstance().setHkdStockList(entityList);
                            mOriginHkdStocks.clear();
                            mOriginHkdStocks.addAll(entityList);
                        }

                        return entityList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(getPriceSubscriber(type));
    }

    private NormalSubscriber<List<MarketStockEntity>> getPriceSubscriber(String type) {
        if (mPriceSubscriber != null && !mPriceSubscriber.isDisposed()) {
            mPriceSubscriber.dispose();
        }

        mPriceSubscriber =new NormalSubscriber<List<MarketStockEntity>>() {
            @Override
            public void onNext(List<MarketStockEntity> stockPriceInfoResults) {
                //刷新UI
                mView.showMarketStocksPrice(stockPriceInfoResults);
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                super.onError(errorCode, errorMsg);
                //无网络时读取本地数据库信息
                getMarketLocalFromDB(type);
            }
        };

        return mPriceSubscriber;
    }
}
