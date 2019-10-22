package com.tik.android.component.trade.module.trade.presenter;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.trade.module.trade.TradeApi;
import com.tik.android.component.trade.module.trade.bean.OrderSubmitResult;
import com.tik.android.component.trade.module.trade.contract.TradeSubmitContract;

import org.qiyi.video.svg.Andromeda;

import io.reactivex.Flowable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/28.
 */
public class TradeSubmitPresenter extends RxPresenter<TradeSubmitContract.View> implements TradeSubmitContract.Presenter {

    @Override
    public void submit(boolean notCheckPwd, boolean buy, String stockSymbol, int totalSupply, float price, String coinSymbol) {
        if (notCheckPwd) {
            Flowable<Result<OrderSubmitResult>> resultFlowable = null;
            if (buy) {
                resultFlowable = observe(ApiProxy.getInstance().getApi(TradeApi.class).buy(stockSymbol, totalSupply, price, coinSymbol));
            } else {
                resultFlowable = observe(ApiProxy.getInstance().getApi(TradeApi.class).sell(stockSymbol, totalSupply, price, coinSymbol));
            }

            resultFlowable.map(Result::getData)
                    .compose(RxUtils.bindToLifecycle(mView))
                    .safeSubscribe(new NormalSubscriber<OrderSubmitResult>() {
                        @Override
                        public void onNext(OrderSubmitResult result) {
                            if (mView != null) {
                                mView.onSubmitResult(result);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (mView != null) {
                                mView.onError(e);
                            }
                        }
                    });
        } else {
            IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
            if (accountService != null) {
                accountService.checkPasswordWithinTime()
                        .compose(RxUtils.bindToLifecycle(mView))
                        .safeSubscribe(new NormalSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean tradePwdWithInTime) {
                                if (tradePwdWithInTime) {
                                    submit(true, buy, stockSymbol, totalSupply, price, coinSymbol);
                                } else {
                                    if (mView != null) {
                                        mView.onRequestVerifyPassword();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                if (mView != null) {
                                    mView.onError(e);
                                }
                            }
                        });

            }
        }
    }

}
