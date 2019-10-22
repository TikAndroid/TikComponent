package com.tik.android.component.trade.module;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.bussiness.service.trade.ITradeService;
import com.tik.android.component.trade.module.order.OrdersFragment;
import com.tik.android.component.trade.module.trade.ui.TradeFragment;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/23
 */
public class TradeServiceImpl implements ITradeService {
    @Override
    public void showOrderUi(BasicFragment from) {
        from.startWithRoot(OrdersFragment.newInstance());
    }

    @Override
    public void navigateTrade(String symbol) {
        TradeFragment.navigateTrade(symbol);
    }
}
