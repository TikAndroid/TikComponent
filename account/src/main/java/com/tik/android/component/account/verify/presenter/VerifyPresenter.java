package com.tik.android.component.account.verify.presenter;

import com.tik.android.component.account.verify.VerifyApi;
import com.tik.android.component.account.verify.bean.Ticket;
import com.tik.android.component.account.verify.contract.VerifyContract;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;

import java.util.Map;

public class VerifyPresenter extends RxPresenter<VerifyContract.View> implements VerifyContract.Presenter {
    @Override
    public void getSmsCode(String countryCode, String mobile) {
        observe(ApiProxy.getInstance().getApi(VerifyApi.class).sendSmsCode(countryCode, mobile))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        if (mView != null) {
                            mView.getSmsCodeSuccess();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (mView != null) {
                            mView.getSmsCodeFail(errorMsg);
                        }
                    }
                });
    }

    @Override
    public void getEmailCode(String language, String type) {
        observe(ApiProxy.getInstance().getApi(VerifyApi.class).userSendMail(language, type))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        if (mView != null) {
                            mView.getEmailCodeSuccess();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (mView != null) {
                            mView.getEmailCodeFail(errorMsg);
                        }
                    }
                });
    }

    @Override
    public void getTicket(String op, String countryCode, String mobile, String email, String smsCode,
                          String emailCode, String googleCode) {
        observe(ApiProxy.getInstance().getApi(VerifyApi.class).getTicket(op, countryCode, mobile,
                email, smsCode, emailCode, googleCode))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result<Ticket>>() {
                    @Override
                    public void onNext(Result<Ticket> ticketResult) {
                        if (mView != null) {
                            mView.getTicketSuccess(ticketResult.getData());
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (mView != null) {
                            mView.getTicketFail(errorMsg);
                        }
                    }
                });
    }

    @Override
    public void doGetRequestByTicket(String url, Map<String, String> map) {
        observe(ApiProxy.getInstance().getApi(VerifyApi.class).doGetRequestByTicket(url, map))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        if (mView != null) {
                            mView.doGetRequestByTicketSuccess();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (mView != null) {
                            mView.doGetRequestByTicketFail(errorMsg);
                        }
                    }
                });
    }

    @Override
    public void doPostRequestByTicket(String url, Map<String, String> map) {
        observe(ApiProxy.getInstance().getApi(VerifyApi.class).doPostRequestByTicket(url, map))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        if (mView != null) {
                            mView.doPostRequestByTicketSuccess();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (mView != null) {
                            mView.doPostRequestByTicketFail(errorMsg);
                        }
                    }
                });
    }
}
