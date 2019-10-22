package com.tik.android.component.bussiness.service.webview;

import android.support.annotation.NonNull;

import com.tik.android.component.basemvp.BasicFragment;

/**
 * @describe : 直接在当前进程routeUrl()就好了；
 *             如果后续业务涉及Js交互，再改成aidl接口
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/21.
 */
public interface IWebService {

    public void routeUrl(@NonNull BasicFragment from, @NonNull String url, @NonNull String title);

}
