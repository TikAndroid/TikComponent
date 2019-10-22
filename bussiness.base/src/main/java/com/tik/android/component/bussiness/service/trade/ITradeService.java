package com.tik.android.component.bussiness.service.trade;

import com.tik.android.component.basemvp.BasicFragment;

public interface ITradeService {

    /**
     * 使主界面的TAB导航到交易页
     * @param symbol 导航到交易页需要查询的股票代号
     */
    void navigateTrade(String symbol);

    /**
     * Show orders
     * @param from previous fragment
     */
    void showOrderUi(BasicFragment from);
}
