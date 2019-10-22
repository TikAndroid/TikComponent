package com.tik.android.component.libcommon;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/10.
 */
public class Constants {
    public static final String EXTRA_STRING_STOCK_SYMBOL = "stock_symbol";
    public static final String FRAGMENTATION_ARG_RESULT_RECORD = "fragment_arg_result_record";

    // request code
    public static final int REQUEST_CODE_TRADE_TO_LOGIN = 100; // 交易页跳转登录
    public static final int REQUEST_CODE_TRADE_TO_BIND = 101; // 交易页跳转绑定邮箱/手机
    public static final int REQUEST_CODE_TRADE_TO_SET_ASSET_PWD = 102; // 交易页跳转设置交易密码
    public static final int REQUEST_CODE_ORDER_CONFIRM_TO_VERIFY_ASSET_PWD = 103; // 委托确认页跳转校验交易密码

    public static class APP {
        public static final String DEFAULT_VERSION_NAME = "1.0.0";
    }

    public static class WebView {
        public static final String DEFAULT_JSBRIDGE_PROTOCOL = "JsBridge";
        public static final String STATIC_METHOD_NAME = "@static";
        public static final String URL = "web_url";
        public static final String TITLE = "web_title";

        public static class Page {
            public static final String PAGE_DEFAULT_URL = "https://www.baidu.com/";
            public static final String PAGE_ASSIST_URL = "https://www.baidu.com/";
        }
    }

    public static class Account {
        public static String ACCOUNT_INFO = "account_info";
        public static String TOKEN = "token";
        public static String UID = "uid";
    }

    public static class Market {
        public static final String EMPTY_DEFAULT_VALUE = "--";
        public static final String SIGN_STOCK_CURRENCY_HKD = "HKD";
        public static final String SIGN_STOCK_CURRENCY_USD = "USD";
        public static final int SEARCH_FRAGMENT_REQUEST_CODE = 1001;
        public static final int SEARCH_FRAGMENT_RESULT_CODE = 1002;
    }
}
