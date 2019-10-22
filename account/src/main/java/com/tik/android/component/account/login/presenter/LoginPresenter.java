package com.tik.android.component.account.login.presenter;

import android.support.annotation.Nullable;

import com.tik.android.component.account.login.UserApi;
import com.tik.android.component.account.login.bean.LoginInfo;
import com.tik.android.component.account.login.bean.RegisterArgs;
import com.tik.android.component.account.login.contract.LoginContract;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.libcommon.JsonUtil;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.ToastUtil;

import org.reactivestreams.Publisher;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by baowei on 18/11/2.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    @Override
    public void register(String challenge, String validate, String seccode, final RegisterArgs args) {
        login(getTicket(challenge, validate, seccode)
                .flatMap((Function<String, Publisher<Result<LoginInfo>>>) ticket -> {
                    args.setTicket(ticket);
                    return ApiProxy.getInstance().getApi(UserApi.class).register(args);
                }), true);
    }

    @Override
    public void registerByMobile(String challenge, String validate, String seccode, final RegisterArgs args) {
        login(getTicket(challenge, validate, seccode)
                .flatMap((Function<String, Publisher<Result<LoginInfo>>>) ticket -> {
                    args.setTicket(ticket);
                    return ApiProxy.getInstance().getApi(UserApi.class).registerByMobile(args);
                }), true);
    }

    @Override
    public void login(String challenge, String validate, String seccode, String email, String password) {
        login(ApiProxy.getInstance().getApi(UserApi.class)
                .login(challenge, validate, seccode, email, password), false);
    }

    @Override
    public void loginByMobile(String challenge, String validate, String seccode, String countryCode, String mobile, String password) {
        login(ApiProxy.getInstance().getApi(UserApi.class)
                .loginByMobile(challenge, validate, seccode, countryCode, mobile, password), false);
    }

    @Override
    public void sendVerifyCode(@Nullable String language, String email, @Nullable String type) {
        observe(ApiProxy.getInstance().getApi(UserApi.class)
                .sendVerifyCode(language, email, type))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToastShort(errorMsg);
                    }

                    @Override
                    public void onNext(Result result) {

                    }
                });
    }

    @Override
    public void sendSmsCode(String countryCode, String mobile) {
        observe(ApiProxy.getInstance().getApi(UserApi.class)
                .sendSmsCode(countryCode, mobile))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToastShort(errorMsg);
                    }

                    @Override
                    public void onNext(Result result) {

                    }
                });
    }

    @Override
    protected <T> Flowable<T> observe(Flowable<T> observable) {
        Flowable<T> observe = super.observe(observable);
        return observe.compose(RxUtils.bindToLifecycle(mView));
    }

    private void login(Flowable<Result> loginObserve, final boolean isRegister) {
        observe(observeOnIO(loginObserve)
                .observeOn(Schedulers.io())
                .flatMap((Function<Result, Publisher<Result<User>>>) result -> {
                    if (result == null || result.getData() == null) return null;
                    LoginInfo loginInfo = null;
                    if (result.getData() instanceof Map) {
                        Map map = (Map) result.getData();
                        loginInfo = JsonUtil.map2object(map, LoginInfo.class);
                    } else if (result.getData() instanceof LoginInfo) {
                        loginInfo = (LoginInfo) result.getData();
                    }
                    if (loginInfo != null) {
                        LocalAccountInfoManager.getInstance().clearLoginUser();
                        LocalAccountInfoManager.getInstance().saveLoginAuth(loginInfo.getUid(), loginInfo.getToken());
                        return ApiProxy.getInstance().getApi(UserApi.class).getUser();
                    }
                    return null;
                })
                .map(result -> {
                    if (result != null && result.getData() != null) {
                        LocalAccountInfoManager.getInstance().saveUser(result.getData());
                        return true;
                    }

                    return false;
                }))
                .safeSubscribe(new NormalSubscriber<Boolean>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToastShort(errorMsg);
                    }

                    @Override
                    public void onNext(Boolean success) {
                        LogUtil.d("login success = " + success);
                        if (success && mView != null) {
                            if (isRegister) {
                                mView.onRegisterSuccess();
                            } else {
                                mView.onLoginSuccess();
                            }
                        }
                    }
                });

    }

    private Flowable<String> getTicket(String challenge, String validate, String seccode) {
        return observeOnIO(ApiProxy.getInstance().getApi(UserApi.class)
                .getTicket(challenge, validate, seccode))
                .observeOn(Schedulers.io())
                .map(ticketResult -> {
                    if (ticketResult.getData() != null) {
                        return (String) ticketResult.getData().get("ticket");
                    }

                    return "";
                });
    }

}
