package com.tik.android.component.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.bussiness.account.AccountStateListener;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.LoginState;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.bussiness.service.account.LoginType;
import com.tik.android.component.bussiness.service.mine.IMineService;
import com.tik.android.component.bussiness.service.mine.MineConstants;
import com.tik.android.component.bussiness.service.trade.ITradeService;
import com.tik.android.component.bussiness.service.webview.IWebService;
import com.tik.android.component.libcommon.AppUtil;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.Constants;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.libcommon.sharedpreferences.RxSharedPrefer;
import com.tik.android.component.mine.utils.DialogCreator;
import com.tik.android.component.widget.DotSettingItem;

import org.qiyi.video.svg.Andromeda;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MineFragment extends BaseMVPFragment {

    public static final String TAG = "MineFragment";

    private static final int REQ_SECURITY = 0x1000;
    private static final int REQ_LANGUAGE = 0x1001;
    private static final int REQ_REGISTER_LOGIN = 0x1002;
    private static final int REQ_REGISTER_BINDING = 0x1003;
    private static final int REQ_REGISTER_TRANSACTION_PASSWORD = 0x1004;

    // 2 milliseconds for refresh delay
    private static final int FRESH_DELAY = 2;

    @BindView(R2.id.mine_login_btn)
    public TextView mBtnLogin;

    @BindView(R2.id.mine_register_btn)
    public TextView mRegisterBtn;

    @BindView(R2.id.mine_logout_btn_container)
    protected View mLogoutContainer;

    @BindView(R2.id.mine_user_name)
    public TextView mUserNameTv;

    @BindView(R2.id.mine_about_setting)
    public DotSettingItem mAppAboutItem;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public int initLayout() {
        return R.layout.mine_fragment;
    }

    private void refreshUI() {
        LocalAccountInfoManager account = LocalAccountInfoManager.getInstance();
        User user = account.getUser();

        String displayName = null;

        if (user != null) {
            if (user.isHoxUser()) {
                displayName = user.getDisplayName();
            } else if (user.isThirdUser()) {
                displayName = user.getThirdPartUser().getName();
            }
        }
        if (TextUtils.isEmpty(displayName)) {
            LogUtil.i("error: login info is illegal");
        }
        // name is empty, maybe is an illegal user, do not show

        if (null != mUserNameTv) {
            mUserNameTv.setText(displayName);
        }

        handleViewWithLogin(notEmpty(displayName));
    }

    private void handleViewWithLogin(boolean login) {
        if (login) {
            mUserNameTv.setVisibility(View.VISIBLE);
            mLogoutContainer.setVisibility(View.VISIBLE);
            mBtnLogin.setVisibility(View.GONE);
            mRegisterBtn.setVisibility(View.GONE);
        } else {
            mUserNameTv.setVisibility(View.GONE);
            mLogoutContainer.setVisibility(View.GONE);
            mBtnLogin.setVisibility(View.VISIBLE);
            mRegisterBtn.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.mine_logout_btn_container)
    public void logout(View view) {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.logout();
//            refreshUIDelay();
        }
    }

    @OnClick(R2.id.mine_login_btn)
    public void login(View view) {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.startRegisterOrLogin(this, true, REQ_REGISTER_LOGIN);
        }
    }

    private boolean isLogin() {
        return LocalAccountInfoManager.getInstance().getUser() != null;
    }

    @OnClick(R2.id.mine_register_btn)
    public void register(View view) {
        IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
        if (accountService != null) {
            accountService.startRegisterIntro(this, REQ_REGISTER_LOGIN);
        }
    }

    @OnClick(R2.id.mine_orders)
    public void showOrders(View view) {
        if (!isLogin()) {
            login(view);
            return;
        }
        ITradeService tradeService = Andromeda.getLocalService(ITradeService.class);
        if (tradeService != null) {
            tradeService.showOrderUi(this);
        }
    }

    @OnClick(R2.id.mine_security_setting)
    public void toSecurity(View view) {
        // todo go security fragment
        if (!isLogin()) {
            login(view);
            return;
        }

        User user = LocalAccountInfoManager.getInstance().getUser();
        if (user != null && user.getThirdPartUser() != null) {
            IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
            if (accountService != null) {
                accountService.startBinding(this, REQ_REGISTER_BINDING);
                return;
            }
        }

        String tips = getString(R.string.mine_security_setting_browser);
        new DialogCreator
                .Builder(_mActivity)
                .message(tips)
                .build()
                .show();
    }

    @OnClick(R2.id.mine_language_setting)
    public void settingLanguage(View view) {
        turnOnPage(LanguageSettingFragment.TAG);
    }

    @OnClick(R2.id.mine_assist_setting)
    public void requestAssist(View view) {
        IWebService webService = Andromeda.getLocalService(IWebService.class);
        if (webService != null) {
            webService.routeUrl(this, Constants.WebView.Page.PAGE_ASSIST_URL, getString(R.string.mine_assist_setting));
        }
    }

    @OnClick(R2.id.mine_report_setting)
    public void reportInfo(View view) {
        IWebService webService = Andromeda.getLocalService(IWebService.class);
        if (webService != null) {
            webService.routeUrl(this, Constants.WebView.Page.PAGE_ASSIST_URL, getString(R.string.mine_report_setting));
        }
    }

    private AccountStateListener mAccountStateListener = new AccountStateListener() {
        @Override
        public void onLoginStateChanged(LoginState state) {
        }

        @Override
        public void onUserInfoChanged(User user) {
            refreshUI();
        }
    };

    @Override
    protected void init(View view) {
        super.init(view);

        refreshUI();

        LocalAccountInfoManager.getInstance().addAccountStateListener(mAccountStateListener);

        //mPresenter.getAppVersion("0.8.0", "android", "ultimate");
        RxView.clicks(mAppAboutItem)
                .throttleFirst(500L, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        final IMineService mine = Andromeda.getLocalService(IMineService.class);
                        if (mine != null) {
                            mine.doAppCheck(0, true, true);
                        }
                    }
                });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // lazy update after
        updateVersion();
    }

    /**
     * update the version item
     */
    private void updateVersion() {
        String curAppVersion = AppUtil.getProjectVersionName(BaseApplication.getAPPContext());
        String newVersion = RxSharedPrefer
                .builder()
                .context(BaseApplication.getAPPContext())
                .build()
                .read()
                .getString(MineConstants.KEY_VERSION_CODE_SP, curAppVersion);
        mAppAboutItem.setDotVisible(newVersion.compareTo(curAppVersion) > 0);
        mAppAboutItem.setSubtitle(getString(R.string.mine_app_version_display, curAppVersion));
    }

    private void turnOnPage(String tag) {
    }

    private int getRequestCode(String tag) {
        int reqCode = 0;
        switch (tag) {
            case LanguageSettingFragment.TAG:
                reqCode = REQ_LANGUAGE;
                break;
        }
        return reqCode;
    }

    private boolean notEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_LANGUAGE:
                handleLanguageReq(resultCode, data);
                break;
            case REQ_REGISTER_LOGIN:
                handleRegisterLoginReq(resultCode, data);
                break;
            case REQ_REGISTER_BINDING:
                handleBindingReq(resultCode, data);
                break;
            case REQ_REGISTER_TRANSACTION_PASSWORD:
                handleTransactionPasswordReq(resultCode, data);
                break;
        }
    }

    private void handleTransactionPasswordReq(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            boolean success = data.getBooleanExtra(AccountConstants.BINDING_RESULT_TRANSACTION_PASSWORD_DATA, false);
            LogUtil.d("sucess = " + success);
            ToastUtil.showToastShort("result = " + success);
        }
    }

    private void handleBindingReq(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            boolean success = data.getBooleanExtra(AccountConstants.BINDING_RESULT_BOOLEAN_DATA, false);
            LogUtil.d("sucess = " + success);
            ToastUtil.showToastShort("result = " + success);
        }
    }

    private void handleLanguageReq(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //todo language changed
        }
    }

    private void handleRegisterLoginReq(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            boolean success = data.getBooleanExtra(AccountConstants.LOGIN_RESULT_BOOLEAN_DATA_SUCCESS, false);
            LoginType loginType = data.getParcelableExtra(AccountConstants.LOGIN_RESULT_LOGINTYPE_DATA_TYPE);
            LogUtil.d("sucess = " + success + ", login type = " + loginType);
            refreshUI();
        }

    }

}
