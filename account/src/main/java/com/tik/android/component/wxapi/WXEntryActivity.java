package com.tik.android.component.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tik.android.component.account.R;
import com.tik.android.component.account.login.contract.ThirdPartContract;
import com.tik.android.component.account.login.presenter.ThirdPartPresenter;
import com.tik.android.component.basemvp.BaseMvpActivity;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.libcommon.LogUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.qiyi.video.svg.Andromeda;
import org.qiyi.video.svg.event.Event;

import static com.tik.android.component.account.login.Constants.THIRD_PART_WECHAT;

public class WXEntryActivity extends BaseMvpActivity<ThirdPartPresenter>
        implements IWXAPIEventHandler, ThirdPartContract.View {

    public static final String EVENT_WECHAT_LOGIN = "event_wechat_login";
    public static final String DATA_WECHAT_LOGIN_SUCCEED = "data_wechat_login_succeed";
    private WxApiHolder mApiHolder;

    @Override
    protected void init() {
        super.init();
        initWeiXin();
    }

    @Override
    public int initLayout() {
        return 0;
    }

    private void initWeiXin() {
        ensureApi();
        try {
            mApiHolder.getWxSdkApi().handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("exceptions :" + e);
        }
    }

    private void ensureApi() {
        if (mApiHolder == null) {
            mApiHolder = new WxApiHolder();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initWeiXin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mApiHolder != null) {
            mApiHolder.releaseWxSdkApi();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.d("msg:" + baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            final String codeString = resp.code;
            final int errorCode = resp.errCode;
            switch (errorCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if(mPresenter != null) {
                        mPresenter.thirdPartLoginProcess(THIRD_PART_WECHAT, codeString);
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    break;
                default:
                    // the secret code not right, usually error-code is '-6'
                    break;
            }
            if (errorCode != BaseResp.ErrCode.ERR_OK) {
                // It is a temp UI, finish if wechat deauthorization.
                finish();
            }
        } else {
            // no data return
            finish();
        }
    }

    @Override
    public void onLogin(User user) {
        String[] thirdApps = getResources().getStringArray(R.array.account_third_part_app);

        boolean succeed;
        succeed = user != null && (user.isThirdUser() || user.isHoxUser());
        if(succeed) {
            ToastUtil.showToastShort(getString(R.string.account_third_part_authorized, thirdApps[0]));
        } else {
            ToastUtil.showToastShort(getString(R.string.account_third_part_authorize_failed, thirdApps[0]));
        }
        Bundle data = new Bundle();
        data.putBoolean(DATA_WECHAT_LOGIN_SUCCEED, succeed);
        Andromeda.publish(new Event(EVENT_WECHAT_LOGIN, data));
        finish();

    }
}
