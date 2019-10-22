package com.tik.android.component.wxapi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.tik.android.component.account.login.Constants;
import com.tik.android.component.libcommon.BaseApplication;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.tik.android.component.account.login.Constants.WECHAT_APP_ID;

/**
 * @describe : for wechat login
 * @usage :
 * WxApiHolder apiHolder = new WxApiHolder();
 * apiHolder.wechatLogin();
 *
 * </p>
 * Created by tanlin on 2018/11/18
 */
public class WxApiHolder {
    private IWXAPI mWxSdkApi;

    public WxApiHolder() {
        initWechat();
    }

    public IWXAPI getWxSdkApi() {
        return mWxSdkApi;
    }

    public void releaseWxSdkApi() {
        if(mWxSdkApi != null) {
            mWxSdkApi.unregisterApp();
            mWxSdkApi = null;
        }
    }

    private void initWechat() {
        mWxSdkApi = WXAPIFactory.createWXAPI(BaseApplication.getAPPContext(), WECHAT_APP_ID);
        mWxSdkApi.registerApp(WECHAT_APP_ID);
    }

    public void wechatLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login_serial" + System.currentTimeMillis();
        mWxSdkApi.sendReq(req);
    }

    public boolean isWechatInstalled() {
        return mWxSdkApi.isWXAppInstalled();
    }

    /**
     *
     * @param fragment to start activity
     */
    public void processInstallWechat(Fragment fragment) {
        Uri uri = Uri.parse(Constants.WECHAT_DOWNLOAD_URL);
        fragment.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void processInstallWechat(Context context) {
        Uri uri = Uri.parse(Constants.WECHAT_DOWNLOAD_URL);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
