package com.tik.android.component.bussiness.service.market;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.bussiness.market.IChartView;
import com.tik.android.component.bussiness.market.IPriceDataCallback;
import com.tik.android.component.bussiness.market.IUserFavorCallback;

public interface IMarketService {
    BasicFragment getStockChartFragment(String symbol);

    void getStockPrice(final String symbol, final Object view, final IPriceDataCallback callback);

    /**
     * @param context 要求 context instanceof RxActivity或者context instanceof RxFragment
     */
    IChartView getIChartView(Context context);

    BasicFragment getSearchStockFragment();

    void goToChartFragment(@NonNull BasicFragment from, String symbol);

    void goToChartActivity(@NonNull BasicFragment from, String symbol);

    void goToSearchFragment(@NonNull BasicFragment from);

    void goToSearchFragmentForResult(BasicFragment from);

    void updateUserFavor(boolean isFavor, Bundle bundle, Object view, IUserFavorCallback callback);
}
