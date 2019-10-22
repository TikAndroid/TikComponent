package com.tik.android.component.account.transPassword.presenter;

import com.tik.android.component.account.login.UserApi;
import com.tik.android.component.account.transPassword.TransactionPasswordApi;
import com.tik.android.component.account.transPassword.contract.TransactionPasswordContract;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.libcommon.ToastUtil;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by yangtao on 2018/11/29
 */
public class TransactionPasswordPresenter extends RxPresenter<TransactionPasswordContract.View>
        implements TransactionPasswordContract.Presenter{
    @Override
    public void setPassword(String password) {
        observe(ApiProxy.getInstance().getApi(TransactionPasswordApi.class).setPassword(password))
                .flatMap((Function<Result, Publisher<User>>) result -> observe(ApiProxy.getInstance()
                        .getApi(UserApi.class).getUser())
                        .map(Result::getData))
                .safeSubscribe(new NormalSubscriber<User>() {
                    @Override
                    public void onNext(User user) {
                        if (user != null) {
                            LocalAccountInfoManager.getInstance().saveUser(user);
                        }
                        mView.exitWithResult(true);
                    }
                });
    }

    @Override
    public void verifyPassword(String password) {
        observe(ApiProxy.getInstance().getApi(TransactionPasswordApi.class).verifyPassword(password))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        mView.exitWithResult(true);
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        mView.showDiallog();
                    }
                });
    }

    @Override
    public void resetPassword(String password, String ticket) {
        observe(ApiProxy.getInstance().getApi(TransactionPasswordApi.class).resetPassword(password, ticket))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        mView.exitWithResult(true);
                    }
                });
    }

    @Override
    public void checkPasswordWithinTime() {
        observe(ApiProxy.getInstance().getApi(TransactionPasswordApi.class).checkPasswordWithinTime())
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        mView.exitWithResult(true);
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToastShort(errorMsg);
                        mView.exitWithResult(false);
                    }
                });
    }

    @Override
    protected <T> Flowable<T> observe(Flowable<T> observable) {
        Flowable<T> observe = super.observe(observable);
        return observe.compose(RxUtils.bindToLifecycle(mView));
    }
}
