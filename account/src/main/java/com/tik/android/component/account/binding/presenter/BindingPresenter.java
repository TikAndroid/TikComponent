package com.tik.android.component.account.binding.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tik.android.component.account.binding.bean.BindArgs;
import com.tik.android.component.account.binding.bean.VerifiCodeArgs;
import com.tik.android.component.account.binding.contract.BindingContract;
import com.tik.android.component.account.login.Constants;
import com.tik.android.component.account.login.UserApi;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.ToastUtil;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by yangtao on 2018/11/23
 */
public class BindingPresenter extends RxPresenter<BindingContract.View> implements BindingContract.Presenter {


    @Override
    public void getVerificationCode(VerifiCodeArgs args, boolean isMobile) {
        if (isMobile) {
            observe(ApiProxy.getInstance().getApi(UserApi.class)
                    .checkMobileExists(args.getCountryCode(),args.getMobile())).safeSubscribe(new NormalSubscriber<Result>() {
                @Override
                public void onNext(Result result) {
                    sendSmsCode(args.getCountryCode(),args.getMobile());
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    LogUtil.d("errorMsg = "+errorMsg + " getVerificationCode.onError");
                    isBindOauth(args, isMobile);
                }
            });
        } else {
            observe(ApiProxy.getInstance().getApi(UserApi.class).checkEmailExists(args.getEmail()))
                    .safeSubscribe(new NormalSubscriber<Result>() {
                        @Override
                        public void onNext(Result result) {
                            sendVerifyCode(null,args.getEmail(),null);
                        }

                        @Override
                        public void onError(int errorCode, String errorMsg) {
                            isBindOauth(args, isMobile);
                        }
                    });
        }
    }

    @Override
    public void bind(BindArgs args, boolean isMobile) {
        if (!TextUtils.isEmpty(args.getCode())) {
            observe(ApiProxy.getInstance().getApi(UserApi.class).checkSmsCode(args.getCountryCode(),
                    args.getMobile(), args.getCode())).safeSubscribe(new NormalSubscriber<Result>() {
                @Override
                public void onNext(Result result) {
                    observe(ApiProxy.getInstance().getApi(UserApi.class)
                            .checkMobileExists(args.getCountryCode(),
                                    args.getMobile())).safeSubscribe(new NormalSubscriber<Result>() {
                        @Override
                        public void onNext(Result result) {
                            LogUtil.d("result = "+result.getData().toString() + " bind.checkMobileExists.onNext");
                            doBind(args, isMobile);
                        }

                        @Override
                        public void onError(int errorCode, String errorMsg) {
                            if (mView !=null) {
                                mView.showDiallog(Constants.BIND_DEFAULT_DIALOG, args);
                            }
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    ToastUtil.showToastShort(errorMsg);
                }
            });
        } else if (!TextUtils.isEmpty(args.getEmailCode())) {
            observe(ApiProxy.getInstance().getApi(UserApi.class).checkEmailCode(args.getEmail(), args.getEmailCode()))
                    .safeSubscribe(new NormalSubscriber<Result>() {
                        @Override
                        public void onNext(Result result) {
                            observe(ApiProxy.getInstance().getApi(UserApi.class).checkEmailExists(args.getEmail()))
                                    .safeSubscribe(new NormalSubscriber<Result>() {
                                        @Override
                                        public void onNext(Result result) {
                                            doBind(args, isMobile);
                                        }

                                        @Override
                                        public void onError(int errorCode, String errorMsg) {
                                            if (mView != null) {
                                                mView.showDiallog(Constants.BIND_DEFAULT_DIALOG, args);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onError(int errorCode, String errorMsg) {
                            ToastUtil.showToastShort(errorMsg);
                        }
                    });
        }
    }

    public void doBind(BindArgs args, boolean isMobile) {
        if (isMobile) {
            observe(ApiProxy.getInstance().getApi(UserApi.class).bindMobile(args))
                    .safeSubscribe(new NormalSubscriber<Result<Map>>() {
                        @Override
                        public void onNext(Result<Map> mapResult) {
                            Map map = mapResult.getData();
                            updateUser(map.get("uid").toString(), map.get("token").toString());
                        }
                    });
        } else {
            observe(ApiProxy.getInstance().getApi(UserApi.class).bindEmail(args))
                    .safeSubscribe(new NormalSubscriber<Result<Map>>() {
                        @Override
                        public void onNext(Result<Map> mapResult) {
                            Map map = mapResult.getData();
                            updateUser(map.get("uid").toString(), map.get("token").toString());
                        }
                    });
        }
    }

    private void updateUser(String uid, String token) {
        LocalAccountInfoManager.getInstance().clearLoginUser();
        LocalAccountInfoManager.getInstance().saveLoginAuth(uid, token);
        observe(ApiProxy.getInstance().getApi(UserApi.class).getUser())
                .safeSubscribe(new NormalSubscriber<Result<User>>() {
                    @Override
                    public void onNext(Result<User> userResult) {
                        if (userResult != null && userResult.getData() != null) {
                            LocalAccountInfoManager.getInstance().saveUser(userResult.getData());
                            if (mView != null) {
                                mView.exitWithResult(true);
                            }
                        }
                    }
                });
    }



    @Override
    protected <T> Flowable<T> observe(Flowable<T> observable) {
        Flowable<T> observe = super.observe(observable);
        return observe.compose(RxUtils.bindToLifecycle(mView));
    }

    private void isBindOauth(VerifiCodeArgs args, boolean isMobile) {
        observe(ApiProxy.getInstance().getApi(UserApi.class)
                .isBindOauth(args)).safeSubscribe(new NormalSubscriber<Result>() {
            @Override
            public void onNext(Result result) {
                boolean canBindOauth = (boolean) result.getData();
                if (!canBindOauth) {
                    if (isMobile) {
                        sendSmsCode(args.getCountryCode(),args.getMobile());
                    } else {
                        sendVerifyCode(null,args.getEmail(),null);
                    }
                } else {
                    if (mView != null) {
                        mView.showDiallog(args.getType(), null);
                    }
                }
            }
        });
    }

    private void sendSmsCode(String countryCode, String mobile) {
        observe(ApiProxy.getInstance().getApi(UserApi.class).sendSmsCode(countryCode,mobile))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onNext(Result result) {
                        if (mView != null) {
                            mView.getCodeSuccess();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToastShort(errorMsg);
                    }
                });
    }

    private void sendVerifyCode(@Nullable String language, String email, @Nullable String type) {
        observe(ApiProxy.getInstance().getApi(UserApi.class)
                .sendVerifyCode(language, email, type))
                .safeSubscribe(new NormalSubscriber<Result>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToastShort(errorMsg);
                    }

                    @Override
                    public void onNext(Result result) {
                        if (mView != null) {
                            mView.getCodeSuccess();
                        }
                    }
                });
    }

}
