package com.tik.android.component.market.bean;

import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiqi on 2018/11/29.
 * 缓存用户数据: 美股、港股、自选列表
 */
public class UserDataManager {
    List<String> mUserFavorSymbols = new ArrayList<>();
    List<MarketStockEntity> mUserFavorList = new ArrayList<>();
    List<MarketStockEntity> mUsdStockList = new ArrayList<>();
    List<MarketStockEntity> mHkdStockList = new ArrayList<>();
    List<String> mUsdSymbols = new ArrayList<>();
    List<String> mHkdSymbols = new ArrayList<>();

    // 本地指定的美股港股
    List<String> mLocalSymbols = new ArrayList<>();
    String mSharedPreferName = "";

    private static volatile UserDataManager INSTANCE;

    public static UserDataManager getInstance() {
        if (null == INSTANCE) {
            synchronized (UserDataManager.class) {
                INSTANCE = new UserDataManager();
            }
        }

        return INSTANCE;
    }

    public UserDataManager() {
    }

    public List<String> getUserFavor() {
        return mUserFavorSymbols;
    }

    public void setUserFavor(List<String> mUserFavor) {
        this.mUserFavorSymbols.clear();
        this.mUserFavorSymbols.addAll(mUserFavor);
    }

    public List<MarketStockEntity> getUsdStockList() {
        return mUsdStockList;
    }

    public void setUsdStockList(List<MarketStockEntity> mUsdStockList) {
        this.mUsdStockList.clear();
        this.mUsdStockList.addAll(mUsdStockList);
    }

    public List<MarketStockEntity> getHkdStockList() {
        return mHkdStockList;
    }

    public void setHkdStockList(List<MarketStockEntity> mHkdStockList) {
        this.mHkdStockList.clear();
        this.mHkdStockList.addAll(mHkdStockList);
    }

    public List<String> getUsdSymbols() {
        return mUsdSymbols;
    }

    public void setUsdSymbols(List<String> mUsdSymbols) {
        this.mUsdSymbols.clear();
        this.mUsdSymbols.addAll(mUsdSymbols);
    }

    public List<String> getHkdSymbols() {
        return mHkdSymbols;
    }

    public void setHkdSymbols(List<String> mHkdSymbols) {
        this.mHkdSymbols.clear();
        this.mHkdSymbols.addAll(mHkdSymbols);
    }

    public List<String> getLocalSymbols() {
        return mLocalSymbols;
    }

    public void setLocalSymbols(List<String> mLocalSymbols) {
        this.mLocalSymbols.clear();
        this.mLocalSymbols.addAll(mLocalSymbols);
    }

    public String getSharedPreferName() {
        return mSharedPreferName;
    }

    public void setSharedPreferName(String mSharedPreferName) {
        this.mSharedPreferName = mSharedPreferName;
    }

    public List<MarketStockEntity> getUserFavorList() {
        return mUserFavorList;
    }

    public void setUserFavorList(List<MarketStockEntity> mUserFavorList) {
        this.mUserFavorList.clear();
        this.mUserFavorList.addAll(mUserFavorList);
    }

    public void release() {
        if (null != INSTANCE) {
            mUserFavorSymbols = null;
            mUsdStockList = null;
            mHkdStockList = null;
            mUsdSymbols = null;
            mHkdSymbols = null;
            mUserFavorList = null;
            INSTANCE = null;
        }
    }
}
