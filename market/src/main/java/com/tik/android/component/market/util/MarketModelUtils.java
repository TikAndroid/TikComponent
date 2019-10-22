package com.tik.android.component.market.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.basemvp.RouteUtil;
import com.tik.android.component.libcommon.Constants;
import com.tik.android.component.market.ui.fragment.SearchStockFragment;
import com.tik.android.component.market.ui.fragment.StockChartFragment;

/**
 * Created by jianglixuan on 2018/11/28
 */
public class MarketModelUtils {
    //from == startFragment时说明启动页面要么为二级页面，需要出栈（要么为是MainFragment，这里要求不能使用MainFragment来启动搜索和个股页面）
    public static void goToChartFragment(@NonNull BasicFragment from, String symbol) {
        Fragment rootFragment = from.getRootFragment();
        if (rootFragment instanceof BasicFragment) {
            BasicFragment startFragment = (BasicFragment) rootFragment;
            StockChartFragment fragment = startFragment.findFragment(StockChartFragment.class);
            if (fragment == null) {
                fragment = StockChartFragment.newInstance(symbol);
                startFragment.start(fragment);
            } else {
                if (from == startFragment) {
                    fragment.refreshChartFragment(symbol);
                    from.pop();
                }
            }
        }
    }

    public static void goToChartActivity(@NonNull BasicFragment from, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_STRING_STOCK_SYMBOL,symbol);
        RouteUtil.startWithActivity(from,StockChartFragment.class,bundle,null);
    }

    public static void goToSearchFragment(@NonNull BasicFragment from) {
        Fragment rootFragment = from.getRootFragment();
        if (rootFragment instanceof BasicFragment) {
            BasicFragment startFragment = (BasicFragment) rootFragment;
            SearchStockFragment fragment = startFragment.findFragment(SearchStockFragment.class);
            if (fragment == null) {
                fragment = SearchStockFragment.newInstance();
                startFragment.start(fragment);
            } else if (from == startFragment) {
                from.pop();
            }
        }
    }

    public static void goToSearchFragmentForResult(BasicFragment from) {
        SearchStockFragment fragment = SearchStockFragment.newInstance();
        from.startForResultWithRoot(fragment, Constants.Market.SEARCH_FRAGMENT_REQUEST_CODE);
    }
}
