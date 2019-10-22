package com.tik.android.component.account.verify.contract;

import com.tik.android.component.account.verify.bean.Ticket;
import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;

import java.util.Map;

public interface VerifyContract {
    interface View extends BaseView {
        void getSmsCodeSuccess();
        void getSmsCodeFail(String errorMsg);

        void getEmailCodeSuccess();
        void getEmailCodeFail(String errorMsg);

        void getTicketSuccess(Ticket ticket);
        void getTicketFail(String errorMsg);

        void doGetRequestByTicketSuccess();
        void doPostRequestByTicketSuccess();

        void doGetRequestByTicketFail(String errorMsg);
        void doPostRequestByTicketFail(String errorMsg);
    }

     interface Presenter extends BasePresenter<View> {
        void getSmsCode(String countryCode, String mobile);
        void getEmailCode(String language, String type);
        void getTicket(String op, String countryCode, String mobile, String email, String smsCode,
                       String emailCode, String googleCode);

         void doGetRequestByTicket(String url, Map<String, String> map);
         void doPostRequestByTicket(String url, Map<String, String> map);
    }

}
