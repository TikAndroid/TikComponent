package com.tik.android.component.account.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tik.android.component.wxapi.WxApiHolder;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.login.bean.Country;
import com.tik.android.component.account.login.bean.RegisterArgs;
import com.tik.android.component.account.login.contract.LoginContract;
import com.tik.android.component.account.login.presenter.LoginPresenter;
import com.tik.android.component.account.login.service.AccountServiceImpl;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.basemvp.WrapperActivity;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.service.account.LoginType;
import com.tik.android.component.libcommon.SpannableBuilder;
import com.tik.android.component.libcommon.StringUtils;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.libcommon.ViewUtils;

import org.qiyi.video.svg.Andromeda;
import org.qiyi.video.svg.event.Event;
import org.qiyi.video.svg.event.EventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;

import static com.tik.android.component.wxapi.WXEntryActivity.DATA_WECHAT_LOGIN_SUCCEED;
import static com.tik.android.component.wxapi.WXEntryActivity.EVENT_WECHAT_LOGIN;

/**
 * @describe : 注册/登录界面（不要被名字误导）
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/14.
 */
public class RegisterFragment extends BaseMVPFragment<LoginPresenter> implements LoginContract.View, EventListener {
    public static final String TAG = RegisterFragment.class.getSimpleName();
    public static final String ARGS_IS_LOGIN = "is_login";
    public static final String ARGS_IS_EMAIL = "is_email";
    static final int REQUEST_CODE_CHOOSE_COUNTRY = 100;
    static final int REQUEST_CODE_REGISTER_VERIFY = 101;

    public static RegisterFragment newInstance() {
        return newInstance(false, false);
    }

    public static RegisterFragment newInstance(boolean isLogin, boolean isEmail) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_IS_LOGIN, isLogin);
        bundle.putBoolean(ARGS_IS_EMAIL, isEmail);
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private GeetestVerify mGeetestVerify;
    private boolean mIsEmail;
    private boolean mIsLogin;
    private Country mCountry = new Country("China (中国)", "+86", "cn");
    private RegisterArgs mRegisterArgs = new RegisterArgs();
    private final Boolean HIDE_INVITE = true;

    // hox-wechat-api
    private WxApiHolder mWxApiHolder;

    @BindView(R2.id.title)
    public TextView mTitleView;
    @BindView(R2.id.title_btn_right)
    public TextView mBtnTitleRight;
    @BindView(R2.id.register_item_country)
    public View mItemCountry;
    @BindView(R2.id.btn_country_choose)
    public TextView mBtnChooseCountry;
    @BindView(R2.id.divider_below_country)
    public View mDividerBelowCountry;
    @BindView(R2.id.register_item_phone)
    public View mItemPhone;
    @BindView(R2.id.register_item_mail)
    public View mItemMail;
    @BindView(R2.id.register_input_country_code)
    public TextView mInputCountryCode;
    @BindView(R2.id.register_input_phone)
    public TextView mInputPhone;
    @BindView(R2.id.register_input_mail)
    public TextView mInputMail;
    @BindView(R2.id.register_input_pwd)
    public TextView mInputPwd;
    @BindView(R2.id.divider_above_invite)
    public View mDividerAboveInvite;
    @BindView(R2.id.register_item_invite)
    public View mItemInvite;
    @BindView(R2.id.register_input_invite)
    public TextView mInputInvite;
    @BindView(R2.id.register_btn_ok)
    public TextView mBtnOk;
    @BindView(R2.id.register_btn_register_by_another)
    public TextView mBtnRegisterByAnother;
    @BindView(R2.id.register_btn_login_by_another)
    public TextView mBtnLoginByAnother;
    @BindView(R2.id.register_btn_forgot_pwd)
    public TextView mBtnForgotPwd;
    @BindView(R2.id.service_contract)
    public TextView mServiceContract;
    @BindView(R2.id.login_by_third)
    public View mLoginByThirdGroup;

    @Override
    public int initLayout() {
        return R.layout.account_fragment_register;
    }

    @Override
    protected void init(View view) {
        super.init(view);

        // 设置国家选项相关控件
        updateCountry();

        // 设置服务协议的链接
        String contractContent = getString(R.string.account_register_service_contract);
        List<int[]> linksIndex = StringUtils.indexOfAllWithReg(contractContent, "《.*?》");
        SpannableBuilder builder = SpannableBuilder.create(contractContent);
        for (int[] index : linksIndex) {
            builder.span(index[0], index[1], new URLSpan("https://www.baidu.com"));
        }
        mServiceContract.setText(builder.build());
        mServiceContract.setMovementMethod(LinkMovementMethod.getInstance());

        // 对几个内容输入框开启监听以更新注册/登录按钮的状态
        Observable.combineLatest(RxTextView.textChanges(mInputMail),
                RxTextView.textChanges(mInputPhone),
                RxTextView.textChanges(mInputPwd),
                (charSequence, charSequence2, charSequence3) -> verifyInput(getRegisterArgs(), false))
                .toFlowable(BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(this))
                .safeSubscribe(new NormalSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean verifyOk) {
                        mBtnOk.setEnabled(verifyOk);
                    }
                });

        // 切换到对应的状态
        boolean isLogin = false;
        boolean isEmail = false;
        if (getArguments() != null) {
            isLogin = getArguments().getBoolean(ARGS_IS_LOGIN, isLogin);
            mIsEmail = getArguments().getBoolean(ARGS_IS_EMAIL, isEmail);
        }
        setStateAndSetupPage(isLogin, isEmail);

        // 初始化极验
        mGeetestVerify = new GeetestVerify();
        mGeetestVerify.initGeetestUtils(_mActivity);

        if (mIsLogin) {
            mWxApiHolder = new WxApiHolder();
        }
        Andromeda.subscribe(EVENT_WECHAT_LOGIN, this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        // 设置window在输入法弹出时不调整窗口高度，在每次变得可见时都需要设置，不然会重置
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGeetestVerify.dismiss();
        mGeetestVerify.releaseGeetest();
        if (mWxApiHolder != null) {
            mWxApiHolder.releaseWxSdkApi();
        }
        Andromeda.unsubscribe(this);
    }

    @Override
    public void onNotify(Event event) {
        if (EVENT_WECHAT_LOGIN.equals(event.getName())) {
            if (event.getData() != null && event.getData().getBoolean(DATA_WECHAT_LOGIN_SUCCEED)) {
                //todo if login failed
                exitWithSuccess(LoginType.WECHAT);
            }
        }
    }

    @OnClick({R2.id.title_btn_right, R2.id.register_btn_register_by_another, R2.id.register_btn_login_by_another})
    public void switchState(View view) {
        boolean isLogin = mIsLogin;
        boolean isEmail = mIsEmail;
        if (view != null) {
            int i = view.getId();
            if (i == R.id.title_btn_right) {
                isLogin = !isLogin;
            } else if (i == R.id.register_btn_register_by_another || i == R.id.register_btn_login_by_another) {
                isEmail = !isEmail;
            }
        }

        setStateAndSetupPage(isLogin, isEmail);
    }

    @OnClick(R2.id.btn_country_choose)
    public void chooseCountry() {
        startForResult(CountryChooseFragment.newInstance(), REQUEST_CODE_CHOOSE_COUNTRY);
    }

    private void updateCountry() {
        if (mCountry == null) {
            return;
        }
        if (mBtnChooseCountry != null) {
            mBtnChooseCountry.setText(mCountry.getCountry());
        }
        if (mInputCountryCode != null) {
            mInputCountryCode.setText(mCountry.getCode());
        }
    }

    private void setStateAndSetupPage(boolean isLogin, boolean isEmail) {
        mTitleView.setText(!isLogin ? R.string.account_register_title : R.string.account_register_login);
        mBtnTitleRight.setText(isLogin ? R.string.account_register_title : R.string.account_register_login);
        mItemCountry.setVisibility(isEmail ? View.GONE : View.VISIBLE);
        mDividerBelowCountry.setVisibility(isEmail ? View.GONE : View.VISIBLE);
        mItemPhone.setVisibility(isEmail ? View.GONE : View.VISIBLE);
        mItemMail.setVisibility(!isEmail ? View.GONE : View.VISIBLE);
        mInputPwd.setHint(isLogin ? R.string.account_login_input_pwd : R.string.account_register_input_pwd);
        mDividerAboveInvite.setVisibility(HIDE_INVITE || isLogin ? View.GONE : View.VISIBLE);
        mItemInvite.setVisibility(HIDE_INVITE || isLogin ? View.GONE : View.VISIBLE);

        mBtnOk.setText(isLogin ? R.string.account_register_login : R.string.account_register_btn_register_immediately);

        mBtnRegisterByAnother.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        mBtnRegisterByAnother.setText(isEmail ? R.string.account_register_btn_register_by_phone : R.string.account_register_btn_register_by_mail);

        mBtnLoginByAnother.setVisibility(!isLogin ? View.GONE : View.VISIBLE);
        mBtnLoginByAnother.setText(!isEmail ? R.string.account_login_by_mail : R.string.account_login_by_phone);

        mBtnForgotPwd.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        mLoginByThirdGroup.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        mServiceContract.setVisibility(!isLogin ? View.VISIBLE : View.GONE);

        if (isEmail) {
            ViewUtils.requestFocus(mInputMail);
        } else {
            ViewUtils.requestFocus(mInputPhone);
        }

        if (isLogin && mWxApiHolder == null) {
            mWxApiHolder = new WxApiHolder();
        }
        mInputPwd.setText("");
        mIsLogin = isLogin;
        mIsEmail = isEmail;
    }

    @OnClick({R2.id.title_btn_left, R2.id.register_btn_ok})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_btn_left) {
            onBackPressed();
        } else if (i == R.id.register_btn_ok) {
            mGeetestVerify.startVerify(new GeetestVerify.VerifyCallback() {
                @Override
                public void onVerifySuccess(String challenge, String validate, String seccode) {
                    mGeetestVerify.dismiss();
                    if (mIsLogin) {
                        login(challenge, validate, seccode);
                    } else {
                        register(challenge, validate, seccode);
                    }

                }

                @Override
                public void onVerifyFailed() {

                }
            });

        }
    }

    public void register(String challenge, String validate, String seccode) {
        RegisterArgs args = getRegisterArgs();
        if (verifyInput(args, true)) {
            startForResult(RegisterVerifyFragment.newInstance(args, challenge, validate, seccode), REQUEST_CODE_REGISTER_VERIFY);
        }
    }

    public void login(String challenge, String validate, String seccode) {
        RegisterArgs args = getRegisterArgs();
        if (verifyInput(args, true)) {
            if (args.isMobileType()) {
                mPresenter.loginByMobile(challenge, validate, seccode, args.getCountryCode(), args.getMobile(), args.getPassword());
            } else if (args.isEmailType()) {
                mPresenter.login(challenge, validate, seccode, args.getEmail(), args.getPassword());
            }
        }
    }

    private boolean verifyInput(RegisterArgs args, boolean showToast) {
        if (mIsEmail) {
            if (!args.isEmailType()) {
                if (showToast) {
                    ToastUtil.showToastLong(getResources().getString(R.string.account_register_email_error));
                }
                return false;
            }
        } else {
            if (!args.isMobileType()) {
                if (showToast) {
                    ToastUtil.showToastLong(getResources().getString(R.string.account_register_phone_error));
                }
                return false;
            }
        }

        if (TextUtils.isEmpty(mInputPwd.getText())) {
            if (showToast) {
                ToastUtil.showToastLong(getResources().getString(R.string.account_register_pwd_error));
            }
            return false;
        }

        return true;
    }

    /**
     * 从页面中提取注册/登录需要的信息
     *
     * @return
     */
    RegisterArgs getRegisterArgs() {
        if (mIsEmail) {
            mRegisterArgs.setEmail(String.valueOf(mInputMail.getText()));
            mRegisterArgs.setCode("");
            mRegisterArgs.setMobile("");
        } else {
            String countryCode = String.valueOf(mInputCountryCode.getText());
            if (countryCode.startsWith("+")) {
                countryCode = countryCode.substring(1, countryCode.length());
            }
            mRegisterArgs.setCountryCode(String.valueOf(countryCode));
            mRegisterArgs.setMobile(String.valueOf(mInputPhone.getText()));
            mRegisterArgs.setEmail("");
        }
        if (!mIsLogin) {
            mRegisterArgs.setInviteCode(String.valueOf(mInputInvite.getText()));
        }

        mRegisterArgs.setPassword(String.valueOf(mInputPwd.getText()));
        return mRegisterArgs;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_COUNTRY:
                if (resultCode == RESULT_OK && data != null) {
                    mCountry = data.getParcelable(CountryChooseFragment.RESULT_COUNTRY);
                    updateCountry();
                }
                break;
            case REQUEST_CODE_REGISTER_VERIFY:
                if (resultCode == RESULT_OK) {
                    exitWithSuccess(LoginType.HOX);
                }
                break;


        }
    }

    @Override
    public void onLoginSuccess() {
        exitWithSuccess(LoginType.HOX);
    }

    @Override
    public void onRegisterSuccess() {
        exitWithSuccess(LoginType.HOX);
    }

    @Override
    public void onVerifyResult(int result) {

    }

    private void exitWithSuccess(LoginType type) {
        if (_mActivity instanceof WrapperActivity) {
            _mActivity.setResult(RESULT_OK, AccountServiceImpl.createIntentDataForRegister(true, type));
            _mActivity.finish();
        } else {
            setFragmentResult(RESULT_OK, AccountServiceImpl.createBundleDataForRegister(true, type));
            pop();
        }
    }

    @OnClick({R2.id.btn_wechat, R2.id.btn_google, R2.id.btn_facebook})
    public void loginByOthers(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_wechat) {
            handleWechatLogin();
        } else if (viewId == R.id.btn_google) {
        } else if (viewId == R.id.btn_facebook) {
        }
    }

    private void handleWechatLogin() {
        if (mWxApiHolder.isWechatInstalled()) {
            mWxApiHolder.wechatLogin();
        } else {
            ToastUtil.showToastShort(getString(R.string.account_install_tips_wechat));
            mWxApiHolder.processInstallWechat(this);
        }
    }
}
