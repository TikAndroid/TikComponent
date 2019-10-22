package com.tik.android.component.bussiness.service.account.utils;

import android.os.Bundle;

import com.tik.android.component.bussiness.service.account.AccountConstants;

/**
 * Detail:
 *
 * </p>
 * Created by tanlin on 2018/11/12
 */
public class AccountUtils {

    /**
     * get the login request params
     * @return login request bundle data
     */
    public static Bundle getLoginBundle() {
        Bundle login = new Bundle();
        login.putString(AccountConstants.PAGE_TYPE, AccountConstants.PAGE_LOGIN);
        login.putString(AccountConstants.ACTION, AccountConstants.ACTION_LOGIN);
        return login;
    }

    /**
     * get the logout request param
     * @return
     */
    public static Bundle getLogoutBundle() {
        Bundle logout = new Bundle();
        logout.putString(AccountConstants.PAGE_TYPE, AccountConstants.PAGE_LOGIN);
        logout.putString(AccountConstants.ACTION, AccountConstants.ACTION_LOGOUT);
        return logout;
    }
}
