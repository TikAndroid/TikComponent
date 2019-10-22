package com.tik.android.component.bussiness.service.market.utils;

import android.content.Context;
import android.os.Bundle;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.bussiness.market.IChartView;
import com.tik.android.component.bussiness.market.IPriceDataCallback;
import com.tik.android.component.bussiness.market.IUserFavorCallback;
import com.tik.android.component.bussiness.service.market.IMarketService;
import com.tik.android.component.bussiness.service.market.ISearchService;
import com.tik.android.component.libcommon.LogUtil;

import org.qiyi.video.svg.Andromeda;

/**
 * Created by jianglixuan on 2018/11/15
 */
public class MarketUtils {
    /**
     * 跳转到个股fragment页面
     *
     * @param symbol 目标股票的关键字段
     * @param from 根布局下的任意一个SupportFragment
     * @return
     */
    public static boolean goToChartFragment(BasicFragment from, String symbol) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        if (marketService == null) {
            return false;
        }
        marketService.goToChartFragment(from, symbol);
        return true;
    }

    public static boolean goToChartActivity(BasicFragment from, String symbol) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        if (marketService == null) {
            return false;
        }
        marketService.goToChartActivity(from, symbol);
        return true;
    }

    public static boolean goToSearchFragment(BasicFragment from) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        if (marketService == null) {
            return false;
        }
        marketService.goToSearchFragment(from);
        return true;
    }

    public static boolean goToSearchFragmentForResult(BasicFragment from) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        if (marketService == null) {
            return false;
        }
        marketService.goToSearchFragmentForResult(from);
        return true;
    }

    /**
     * 插入搜索历史数据库
     * @param bundle 需要插入的item，封装SearchEntity
     * @return
     */
    public static boolean insertSearchHistory(Bundle bundle, Object view) {
        ISearchService searchService = Andromeda.getLocalService(ISearchService.class);

        if (null == searchService) {
            LogUtil.e("insertSearchHistory register ISearchService failed!");
            return false;
        }

        searchService.insertSearchDB(bundle, view);
        return true;
    }

    /**
     * 用户登录和注销时，切换数据库
     * @return
     */
    public static void resetDatabase() {
        IDatabaseService databaseService = Andromeda.getLocalService(IDatabaseService.class);

        if (null == databaseService) {
            LogUtil.e("resetDatabase register ISearchService failed!");

            return;
        }

        databaseService.resetDatabase();
    }

    public static void initData() {
        IDatabaseService databaseService = Andromeda.getLocalService(IDatabaseService.class);

        if (null == databaseService) {
            LogUtil.e("initCache register ISearchService failed!");

            return;
        }

        databaseService.initData();
    }

    /**
     * 关闭数据库资源
     */
    public static void releaseDatabase() {
        IDatabaseService databaseService = Andromeda.getLocalService(IDatabaseService.class);

        if (null == databaseService) {
            LogUtil.e("resetDatabase register ISearchService failed!");

            return;
        }

        databaseService.releaseDatabase();
    }

    public static void updateUserFavor(boolean isFavor, Bundle bundle, Object view, IUserFavorCallback callback) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);

        if (null == marketService) {
            LogUtil.e("updateUserFavor register IMarketService failed!");

            return;
        }

        marketService.updateUserFavor(isFavor, bundle, view, callback);
    }

    public static BasicFragment getStockChartFragment(String symbol) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        return marketService == null ? null : marketService.getStockChartFragment(symbol);
    }

    public static BasicFragment getSearchStockFragment() {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        return marketService == null ? null : marketService.getSearchStockFragment();
    }

    public static boolean getStockPrice(String symbol, Object view, IPriceDataCallback callback) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        if (marketService != null) {
            marketService.getStockPrice(symbol, view, callback);
            return true;
        }
        return false;
    }

    public static IChartView getStockChartView(Context context) {
        IMarketService marketService = Andromeda.getLocalService(IMarketService.class);
        if (marketService != null) {
            return marketService.getIChartView(context);
        }
        return null;
    }
}
