package com.tik.android.component.bussiness.account;

import android.support.annotation.MainThread;

import com.tik.android.component.bussiness.account.bean.User;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/12/3.
 */
public interface AccountStateListener {
    @MainThread
    void onLoginStateChanged(LoginState state);

    @MainThread
    void onUserInfoChanged(User user);
}
