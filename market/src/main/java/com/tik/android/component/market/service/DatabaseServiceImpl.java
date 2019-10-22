package com.tik.android.component.market.service;

import com.google.gson.reflect.TypeToken;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.service.market.utils.IDatabaseService;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.JsonUtil;
import com.tik.android.component.libcommon.StringUtils;
import com.tik.android.component.libcommon.sharedpreferences.RxSharedPrefer;
import com.tik.android.component.market.R;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bussiness.database.HoxDatabase;
import com.tik.android.component.market.bussiness.database.MarketOperate;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.util.Constant;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangqiqi on 2018/11/30.
 */
public class DatabaseServiceImpl implements IDatabaseService {
    Disposable mDisposable = null;

    @Override
    public void resetDatabase() {
        releaseDatabase();

        //增加重新init数据的逻辑
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        initData();
    }

    @Override
    public void releaseDatabase() {
        MarketOperate.getInstance().reset();
        HoxDatabase.getInstance().release();
        UserDataManager.getInstance().release();
    }

    @Override
    public void initData() {
        String spName = getUserSpName();
        UserDataManager.getInstance().setSharedPreferName(spName);
        HoxDatabase.getInstance().release();
        boolean initialized = RxSharedPrefer
                .builder()
                .context(BaseApplication.getAPPContext())
                .build()
                .read()
                .getBoolean(spName, false);

        if (!initialized) {
            initMarketDB();
        } else {
            initCache();
        }
    }

    /**
     * 初始化用户缓存数据
     */
    public void initCache() {
        mDisposable = MarketOperate.getInstance().getAllStocks()
                .subscribeOn(Schedulers.computation())
                .map(new Function<List<MarketStockEntity>, String>() {
                    @Override
                    public String apply(List<MarketStockEntity> marketStockEntities) throws Exception {
                        List<String> userFavor = new ArrayList<>();
                        List<MarketStockEntity> userFavorList = new ArrayList<>();
                        List<MarketStockEntity> usdStockList = new ArrayList<>();
                        List<MarketStockEntity> hkdStockList = new ArrayList<>();
                        List<String> usdSymbols = new ArrayList<>();
                        List<String> hkdSymbols = new ArrayList<>();
                        List<String> localSymbols = new ArrayList<>();

                        for (MarketStockEntity entity : marketStockEntities) {
                            if (entity.isLocal()) {
                                if (entity.getCurrency().equals(Constant.HKD_STOCK_CURRENCY)) {
                                    hkdStockList.add(entity);
                                    hkdSymbols.add(entity.getSymbol());
                                } else if (entity.getCurrency().equals(Constant.USD_STOCK_CURRENCY)) {
                                    usdStockList.add(entity);
                                    usdSymbols.add(entity.getSymbol());
                                }

                                localSymbols.add(entity.getSymbol());
                            }

                            if (entity.isFavor()) {
                                userFavorList.add(entity);
                                userFavor.add(entity.getSymbol());
                            }
                        }

                        UserDataManager.getInstance().setHkdSymbols(hkdSymbols);
                        UserDataManager.getInstance().setHkdStockList(hkdStockList);
                        UserDataManager.getInstance().setUsdSymbols(usdSymbols);
                        UserDataManager.getInstance().setUsdStockList(usdStockList);
                        UserDataManager.getInstance().setLocalSymbols(localSymbols);
                        UserDataManager.getInstance().setUserFavor(userFavor);
                        UserDataManager.getInstance().setUserFavorList(userFavorList);
                        return "";
                    }
                })
                .subscribe();
    }

    /**
     * 初始化数据库，将本地raw文件中股票信息存入数据库
     */
    public void initMarketDB() {
        mDisposable = Flowable.create(new FlowableOnSubscribe<List<MarketStockEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MarketStockEntity>> emitter) throws Exception {
                List<MarketStockEntity> entityList = JsonUtil.json2object(
                        StringUtils.readString(BaseApplication.getAPPContext().getResources().openRawResource(R.raw.market_stock)),
                        new TypeToken<List<MarketStockEntity>>() {});
                List<String> userFavor = new ArrayList<>();
                List<MarketStockEntity> usdStockList = new ArrayList<>();
                List<MarketStockEntity> hkdStockList = new ArrayList<>();
                List<String> usdSymbols = new ArrayList<>();
                List<String> hkdSymbols = new ArrayList<>();
                List<String> localSymbols = new ArrayList<>();

                for (MarketStockEntity entity : entityList) {
                    //区分股票类型放入内存
                    if (Constant.USD_STOCK_CURRENCY.equals(entity.getCurrency())) {
                        usdStockList.add(entity);
                        usdSymbols.add(entity.getSymbol());
                    } else if (Constant.HKD_STOCK_CURRENCY.equals(entity.getCurrency())) {
                        hkdStockList.add(entity);
                        hkdSymbols.add(entity.getSymbol());
                    }

                    localSymbols.add(entity.getSymbol());
                }

                //缓存数据
                UserDataManager.getInstance().setUsdStockList(usdStockList);
                UserDataManager.getInstance().setUsdSymbols(usdSymbols);
                UserDataManager.getInstance().setHkdStockList(hkdStockList);
                UserDataManager.getInstance().setHkdSymbols(hkdSymbols);
                UserDataManager.getInstance().setLocalSymbols(localSymbols);

                MarketOperate.getInstance().insertDatas(entityList);
                RxSharedPrefer.builder().context(BaseApplication.getAPPContext()).build().edit()
                        .putBoolean(UserDataManager.getInstance().getSharedPreferName(), true)
                        .apply();

                emitter.onNext(entityList);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private String getUserSpName() {
        StringBuilder userSpName = new StringBuilder();

        if (null == LocalAccountInfoManager.getInstance().getUser()) {
            userSpName.append(Constant.KEY_DB_INITED);
        } else {
            userSpName.append(LocalAccountInfoManager.getInstance().getUid()).append("_").append(Constant.KEY_DB_INITED);
        }

        return userSpName.toString();
    }
}
