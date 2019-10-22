package com.tik.android.component.bussiness.service.account;

/**
 * Detail:
 *
 * </p>
 * Created by tanlin on 2018/11/12
 */
public class AccountConstants {

    public static final int REQ_REGISTER_LOGIN = 1000;

    /**
     * which page
     * @see #PAGE_LOGIN
     * @see #PAGE_OTHER
     */
    public static final String PAGE_TYPE = "pageType";

    /**
     * the page action you want to do
     * {@link #ACTION_LOGIN}, {@link #ACTION_LOGOUT}
     */
    public static final String ACTION = "action";
    public static final String PAGE_LOGIN = "page_login";
    public static final String PAGE_OTHER = "page_other";
    public static final String ACTION_LOGOUT = "action_logout";
    public static final String ACTION_LOGIN = "action_login";

    public static final String LOGIN_RESULT_BOOLEAN_DATA_SUCCESS = "login_success";
    public static final String LOGIN_RESULT_LOGINTYPE_DATA_TYPE = "login_type";

    public static final String BINDING_RESULT_BOOLEAN_DATA = "binding_result";
    public static final String BINDING_RESULT_TRANSACTION_PASSWORD_DATA = "transaction_password_result";

    //二次验证类型
    public static final String AUTHENTICATOR_TYPE_GOOGLE = "google";
    public static final String AUTHENTICATOR_TYPE_SMS = "sms";
    public static final String AUTHENTICATOR_TYPE_EMAIL = "email";

    //邮箱类型
    public static final String EMAIL_TYPE_CHANGE_EMAIL = "changeEmail";
    public static final String EMAIL_TYPE_CHANGE_MOBILE = "changeMobile";
    public static final String EMAIL_TYPE_CHANGE_PASSWORD = "changePassword";
    public static final String EMAIL_TYPE_TURN_ON_GOOGLE = "turnOnGoogle";
    public static final String EMAIL_TYPE_TURN_OFF_GOOGLE = "turnOffGoogle";
    public static final String EMAIL_TYPE_TURN_ON_EMAIL = "turnOnEmail";
    public static final String EMAIL_TYPE_TURN_OFF_EMAIL = "turnOffEmail";
    public static final String EMAIL_TYPE_TURN_OFF_SMS = "turnOffSMS";
    public static final String EMAIL_TYPE_TURN_ON_SMS = "turnOnSMS";
    public static final String EMAIL_TYPE_CASH_OUT = "cashOut";
    public static final String EMAIL_TYPE_RESET_ASSET_PASSWORD = "resetAssetPassword";
    public static final String EMAIL_TYPE_SET_PASSWORD = "setPassword";
    public static final String EMAIL_TYPE_UNBIND_FACEBOOK = "unbindFacebook";
    public static final String EMAIL_TYPE_UNBIND_WECHAT = "unbindWechat";
    public static final String EMAIL_TYPE_UNBIND_GOOGLE = "unbindGoogle";
    public static final String EMAIL_TYPE_BIND_EMAIL = "bindEmail";

    //授权行为
    public static final String OP_TYPE_CHANGE_EMAIL = "changeEmail";
    public static final String OP_TYPE_CHANGE_MOBILE = "changeMobile";
    public static final String OP_TYPE_FORGET_PASSWORD = "forgetPassword";
    public static final String OP_TYPE_CHANGE_PASSWORD = "changePassword";
    public static final String OP_TYPE_SET_PASSWORD = "setPassword";
    public static final String OP_TYPE_CHANGE_ASSET_PASSWORD = "changAssetPassword";
    public static final String OP_TYPE_CASH_OUT = "cashout";
    public static final String OP_TYPE_TURN_ON_AUTHENTICATOR = "turnOnAuthenticator";
    public static final String OP_TYPE_TURN_OFF_AUTHENTICATOR = "turnOffAuthenticator";
    public static final String OP_TYPE_UNBIND_OAUTH = "unbindOauth";

    public static final String TICKET = "ticket";
    public static final String REQUEST = "request";
    public static final String VERIFY_ARGS = "verifyArgs";
}
