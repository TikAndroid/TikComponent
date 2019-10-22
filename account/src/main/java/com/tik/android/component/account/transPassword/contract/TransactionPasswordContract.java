package com.tik.android.component.account.transPassword.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;

/**
 * Created by yangtao on 2018/11/29
 */
public interface TransactionPasswordContract {

    interface View extends BaseView {
        void exitWithResult(boolean isSuccess);
        void showDiallog();
    }

    interface Presenter extends BasePresenter<View> {
        void setPassword(String password);
        void verifyPassword(String password);
        void resetPassword(String password, String ticket);
        void checkPasswordWithinTime();
    }
}
