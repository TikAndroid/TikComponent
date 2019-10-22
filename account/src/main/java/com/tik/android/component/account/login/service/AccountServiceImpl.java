package com.tik.android.component.account.login.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.tik.android.component.account.binding.ui.BindingFragment;
import com.tik.android.component.account.login.LoginIntroFragment;
import com.tik.android.component.account.login.RegisterFragment;
import com.tik.android.component.account.transPassword.TransactionPasswordApi;
import com.tik.android.component.account.transPassword.ui.TransactionPasswordFragment;
import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RouteUtil;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.bussiness.service.account.LoginType;

import io.reactivex.Flowable;

/**
 * Detail:
 *
 * </p>
 * Created by tanlin on 2018/11/12
 */
public class AccountServiceImpl implements IAccountService {

    @Override
    public void startRegisterIntro(BasicFragment from, int requestCode) {
        RouteUtil.startForResultWithActivity(from, LoginIntroFragment.class, null, requestCode, null);
    }

    @Override
    public void startRegisterOrLogin(BasicFragment from, boolean isLogin, int requestCode) {
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putBoolean(RegisterFragment.ARGS_IS_LOGIN, isLogin);
        RouteUtil.startForResultWithActivity(from, RegisterFragment.class, fragmentArgs, requestCode, null);
    }

    public static Intent createIntentDataForRegister(boolean success, LoginType type) {
        Intent data = new Intent();
        data.putExtra(AccountConstants.LOGIN_RESULT_BOOLEAN_DATA_SUCCESS, success);
        data.putExtra(AccountConstants.LOGIN_RESULT_LOGINTYPE_DATA_TYPE, (Parcelable) type);
        return data;
    }

    public static Intent createIntentDataForBinding(boolean success) {
        Intent data = new Intent();
        data.putExtra(AccountConstants.BINDING_RESULT_BOOLEAN_DATA, success);
        return data;
    }

    public static Intent createIntentDataForTransactionPassword(boolean success) {
        Intent data = new Intent();
        data.putExtra(AccountConstants.BINDING_RESULT_TRANSACTION_PASSWORD_DATA, success);
        return data;
    }

    public static Bundle createBundleDataForRegister(boolean success, LoginType type) {
        Bundle data = new Bundle();
        data.putBoolean(AccountConstants.LOGIN_RESULT_BOOLEAN_DATA_SUCCESS, success);
        data.putParcelable(AccountConstants.LOGIN_RESULT_LOGINTYPE_DATA_TYPE, type);
        return data;
    }

    @Override
    public boolean logout() {
        LocalAccountInfoManager.getInstance().clearLoginUser();
        return true;
    }

    @Override
    public void startBinding(BasicFragment from, int requestCode) {
        RouteUtil.startForResultWithActivity(from, BindingFragment.class, null, requestCode, null);
    }

    @Override
    public void startTransctionPassword(BasicFragment from, boolean isSetPassword, int requestCode) {
        Bundle fragmentArge = new Bundle();
        fragmentArge.putBoolean(TransactionPasswordFragment.ARGS_IS_SET_PASSWORD, isSetPassword);
        RouteUtil.startForResultWithActivity(from,TransactionPasswordFragment.class, fragmentArge, requestCode,null);

    }

    @Override
    public Flowable<Boolean> checkPasswordWithinTime() {
        return ApiProxy.getInstance()
                .getApi(TransactionPasswordApi.class)
                .checkPasswordWithinTime()
                .map(Result::isSuccess)
                .compose(RxUtils.rxSchedulerHelperForFlowable());
    }
}
