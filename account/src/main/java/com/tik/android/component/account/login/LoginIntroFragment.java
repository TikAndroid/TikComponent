package com.tik.android.component.account.login;

import android.view.View;

import com.tik.android.component.wxapi.WxApiHolder;
import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.login.service.AccountServiceImpl;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.WrapperActivity;
import com.tik.android.component.bussiness.service.account.LoginType;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.libcommon.LogUtil;

import org.qiyi.video.svg.Andromeda;
import org.qiyi.video.svg.event.Event;
import org.qiyi.video.svg.event.EventListener;

import butterknife.OnClick;

import static com.tik.android.component.wxapi.WXEntryActivity.EVENT_WECHAT_LOGIN;

/**
 * @describe : 第一次引导登录界面
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/14.
 */
public class LoginIntroFragment extends BaseMVPFragment implements EventListener {
    public static final String TAG = "LoginIntroFragment";

    // hox-wechat-api
    private WxApiHolder mWxApiHolder;

    public static LoginIntroFragment newInstance() {
        return new LoginIntroFragment();
    }

    private void initWechat() {
        mWxApiHolder = new WxApiHolder();
        Andromeda.subscribe(EVENT_WECHAT_LOGIN, this);
    }

    @Override
    protected void init(View view) {
        super.init(view);
        initWechat();
    }

    @Override
    public int initLayout() {
        return R.layout.account_fragment_login_intro;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Andromeda.unsubscribe(this);
    }

    @Override
    public void onNotify(Event event) {
        if(EVENT_WECHAT_LOGIN.equals(event.getName())) {
            LogUtil.d("ev" + event);
            if(event.getData() != null) {
                //todo if login failed
                exitWithSuccess(LoginType.WECHAT);
            }
        }
    }

    @OnClick({R2.id.title_btn_left, R2.id.intro_register, R2.id.intro_login})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_btn_left) {
            onBackPressed();
        } else if (i == R.id.intro_register) {
            start(RegisterFragment.newInstance(false,false));
        } else if (i == R.id.intro_login) {
            start(RegisterFragment.newInstance(true,false));
        }
    }

    @OnClick({R2.id.btn_wechat, R2.id.btn_google, R2.id.btn_facebook})
    public void loginByOthers(View view) {
        int id = view.getId();
        if (id == R.id.btn_wechat) {
            handleWechatLogin();
        } else if (id == R.id.btn_google) {
        } else if (id == R.id.btn_facebook) {
        }
    }

    private void handleWechatLogin() {
        if(mWxApiHolder.isWechatInstalled()) {
            mWxApiHolder.wechatLogin();
        } else {
            ToastUtil.showToastShort(getString(R.string.account_install_tips_wechat));
            mWxApiHolder.processInstallWechat(this);
        }
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
}
