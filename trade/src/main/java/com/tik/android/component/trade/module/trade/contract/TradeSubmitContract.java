package com.tik.android.component.trade.module.trade.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.trade.module.trade.bean.OrderSubmitResult;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/28.
 */
public interface TradeSubmitContract {

    interface View extends BaseView {
        /**
         * 当需要校验交易密码时，回调此方法让fragment请求密码验证界面
         */
        void onRequestVerifyPassword();

        /**
         * 请求委托出现错误时回调
         * @param error
         */
        void onError(Throwable error);

        /**
         * 回调请求委托的结果
         * @param result
         */
        void onSubmitResult(OrderSubmitResult result);
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 提交委托
         * @param notCheckPwd 已经确定不需要校验交易密码
         * @param stockSymbol 股票代号
         * @param totalSupply 买入数量
         * @param price 委托买入价格
         * @param coinSymbol 买入时使用的数字货币代号，一般时USDT
         * @return
         */
        void submit(boolean notCheckPwd, boolean buy, String stockSymbol, int totalSupply, float price, String coinSymbol);
    }
}
