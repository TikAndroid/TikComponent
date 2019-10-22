package com.tik.android.component.account.login.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.bussiness.account.bean.User;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/16
 */
public interface ThirdPartContract {
    interface View extends BaseView {
        void onLogin(User user);
    }

    interface Presenter extends BasePresenter<View> {
        void thirdPartLoginProcess(String type, String thirdToken);
    }
}
