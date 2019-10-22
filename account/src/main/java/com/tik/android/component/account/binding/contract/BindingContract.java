package com.tik.android.component.account.binding.contract;

import com.tik.android.component.account.binding.bean.BindArgs;
import com.tik.android.component.account.binding.bean.VerifiCodeArgs;
import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;

/**
 * Created by yangtao on 2018/11/23
 */
public interface BindingContract {

    interface View extends BaseView {
        void showDiallog(String type, BindArgs args);
        void exitWithResult(boolean isSuccess);
        void getCodeSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void getVerificationCode(VerifiCodeArgs args, boolean isMobile);
        void bind(BindArgs args, boolean isMobile);
    }
}
