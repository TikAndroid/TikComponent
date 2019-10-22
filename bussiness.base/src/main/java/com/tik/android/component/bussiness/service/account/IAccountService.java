package com.tik.android.component.bussiness.service.account;

import com.tik.android.component.basemvp.BasicFragment;

import io.reactivex.Flowable;

public interface IAccountService {

    /**
     * 以startActivityForResult的方式启动到注册引导页，处理结果需在对应的Fragment的onActivityResult中处理。
     *<p/>
     * 返回参数提取：<br/>
     *&emsp;if (resultCode == RESULT_OK) {<br/>
     *&emsp;&emsp;boolean success = data.getBoolean(AccountConstants.LOGIN_RESULT_BOOLEAN_DATA_SUCCESS);<br/>
     * &emsp;&emsp;LoginType loginType = data.getParcelable(AccountConstants.LOGIN_RESULT_LOGINTYPE_DATA_TYPE);<br/>
     *&emsp;}<br/>
     *<p/>
     * @param from 用者的fragment,用于代理执行startActivityForResult
     * @param requestCode 请求码，可在处理onActivityResult时对比使用
     */
    void startRegisterIntro(BasicFragment from, int requestCode);

    /**
     * 以startActivityForResult的方式请求注册或者登录，处理结果需在对应的Fragment的onActivityResult中处理。
     *<p/>
     * 返回参数提取：<br/>
     *&emsp;if (resultCode == RESULT_OK) {<br/>
     *&emsp;&emsp;boolean success = data.getBoolean(AccountConstants.LOGIN_RESULT_BOOLEAN_DATA_SUCCESS);<br/>
     * &emsp;&emsp;LoginType loginType = data.getParcelable(AccountConstants.LOGIN_RESULT_LOGINTYPE_DATA_TYPE);<br/>
     *&emsp;}<br/>
     *<p/>
     * @param from 调用者的fragment,用于代理执行startActivityForResult
     * @param isLogin 是否时请求首先显示登录页面（用户可在后续操作过程中手动切换注册登录）
     * @param requestCode 请求码，可在处理onActivityResult时对比使用
     */
    void startRegisterOrLogin(BasicFragment from, boolean isLogin, int requestCode);

    /**
     * 退出登录
     * @return 退出成功返回true，否则false
     */
    boolean logout();

    /**
     * 绑定手机/邮箱
     * @return 绑定成功返回true，否则false
     */
    void startBinding(BasicFragment from, int requestCode);

    /**
     * 设置/验证交易密码
     * @return 设置/验证成功返回true，否则false
     */
    void startTransctionPassword(BasicFragment from, boolean isSetPassword, int requestCode);

    /**
     * 检查是否在授权时间内
     * @return
     */
    Flowable<Boolean> checkPasswordWithinTime();
}
