package com.tik.android.component.account.login.contract;

import android.support.annotation.Nullable;

import com.tik.android.component.account.login.bean.RegisterArgs;
import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;

public interface LoginContract {

    interface View extends BaseView {
        void onLoginSuccess();//登录成功
        void onRegisterSuccess();//注册成功
        void onVerifyResult(int result);
    }

    interface Presenter extends BasePresenter<View> {
        void register(String challenge, String validate, String seccode, final RegisterArgs args);
        void registerByMobile(String challenge, String validate, String seccode, final RegisterArgs args);
        void login(String challenge, String validate, String seccode, String email, String password);
        void loginByMobile(String challenge, String validate, String seccode, String countryCode, String mobile, String password);
        void sendVerifyCode(@Nullable String language, String email, @Nullable String type);
        void sendSmsCode(String countryCode, String mobile);
    }

}
