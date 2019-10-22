package com.tik.android.component.bussiness.webview;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.basemvp.RouteUtil;
import com.tik.android.component.bussiness.service.webview.IWebService;
import com.tik.android.component.libcommon.Constants;

/**
 * @describe : 直接在当前进程routeUrl()就好了；
 *             如果后续业务涉及Js交互，再改成aidl接口
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/21.
 */
public class WebServiceImpl implements IWebService {

    @Override
    public void routeUrl(@NonNull BasicFragment from, @NonNull String url, @NonNull String title) {
        Bundle args = new Bundle();
        args.putString(Constants.WebView.URL, url);
        args.putString(Constants.WebView.TITLE, title);
        RouteUtil.startWithActivity(from, WebViewFragment.class, args, null);
    }
}
