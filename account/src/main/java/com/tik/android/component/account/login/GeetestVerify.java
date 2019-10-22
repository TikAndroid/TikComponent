package com.tik.android.component.account.login;

import android.app.Activity;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.libcommon.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/14.
 */
public class GeetestVerify {

    interface VerifyCallback {
        void onVerifySuccess(String challenge, String validate, String seccode);
        void onVerifyFailed();
    }

    /**
     * api1，请求challenge的url
     */
    private static final String captchaURL = ApiProxy.API_URL + "gt/register";
    /**
     * api2，二次验证的url，暂时未提供，二次验证融入登录流程
     */
    private static final String validateURL = "";

    private Activity activity;
    private GT3GeetestUtilsBind gt3GeetestUtils;

    /**
     * 初始化极验验证码工具，务必在oncreate里初始化
     */
    public void initGeetestUtils(Activity activity) {
        this.activity = activity;
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        // 设置debug模式，开代理抓包可使用，默认关闭，生产环境务必设置为false
        gt3GeetestUtils.setDebug(false);
        // 设置加载webview超时时间，单位毫秒，默认15000，仅且webview加载静态文件超时，不包括之前的http请求
        gt3GeetestUtils.setTimeout(15000);
        // 设置webview请求超时(用户点选或滑动完成，前端请求后端接口)，单位毫秒，默认10000
        gt3GeetestUtils.setWebviewTimeout(10000);
    }

    public void dismiss() {
        gt3GeetestUtils.gt3Dismiss();
    }

    /**
     * 释放资源
     */
    public void releaseGeetest() {
        // 页面关闭时释放资源
        gt3GeetestUtils.cancelUtils();
    }

    /**
     * 开始验证
     */
    public void startVerify(final VerifyCallback callback) {
        gt3GeetestUtils.getGeetest(activity, captchaURL, validateURL, null, new GT3GeetestBindListener() {

            /**
             * @param num 1: 点击验证码的关闭按钮, 2: 点击屏幕关闭验证码, 3: 点击返回键关闭验证码
             */
            @Override
            public void gt3CloseDialog(int num) {
                LogUtil.d("gt3CloseDialog-->num: " + num);
                if (callback != null)
                    callback.onVerifyFailed();
            }

            /**
             * 为API1接口添加数据，数据拼接在URL后，API1接口默认get请求
             */
            @Override
            public Map<String, String> gt3CaptchaApi1() {
                LogUtil.d("gt3CaptchaApi1");
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", "" + System.currentTimeMillis());
                return map;
            }

            /**
             * api1接口返回数据
             */
            @Override
            public void gt3FirstResult(JSONObject jsonObject) {
                LogUtil.d("gt3FirstResult-->" + jsonObject);
            }


            /**
             * 准备完成，即将弹出验证码
             */
            @Override
            public void gt3DialogReady() {
                LogUtil.d("gt3DialogReady");
            }

            /**
             * 数据统计，从开启验证到成功加载验证码结束，具体解释详见GitHub文档
             */
            @Override
            public void gt3GeetestStatisticsJson(JSONObject jsonObject) {
                LogUtil.d("gt3GeetestStatisticsJson-->" + jsonObject);
            }

            /**
             * 返回是否自定义api2，true为自定义api2
             * false： gt3GetDialogResult(String result)，返回api2需要参数
             * true： gt3GetDialogResult(boolean a, String result)，返回api2需要的参数
             */
            @Override
            public boolean gt3SetIsCustom() {
                LogUtil.d("gt3SetIsCustom");
                return true;
            }

            /**
             * 用户滑动或点选完成后触发，gt3SetIsCustom配置为false才走此接口
             *
             * @param result api2接口需要参数
             */
            @Override
            public void gt3GetDialogResult(String result) {
                LogUtil.d("gt3GetDialogResult-->" + result);
            }

            /**
             * 用户滑动或点选完成后触发，gt3SetIsCustom配置为true才走此接口
             *
             * @param status 验证是否成功
             * @param result api2接口需要参数
             */
            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                LogUtil.d("gt3GetDialogResult-->status: " + status + "result: " + result);
                if (status) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String challenge = jsonObject.getString("geetest_challenge");
                        String validate = jsonObject.getString("geetest_validate");
                        String seccode = jsonObject.getString("geetest_seccode");
                        // 完成验证，进行登录操作
                        gt3GeetestUtils.gt3TestFinish();
                        if (callback != null)
                            callback.onVerifySuccess(challenge, validate, seccode);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callback != null)
                            callback.onVerifyFailed();
                    }
                } else {
                    gt3GeetestUtils.gt3TestClose();
                    if (callback != null) {
                        callback.onVerifyFailed();
                    }
                }
            }

            /**
             * 为API2接口添加数据，数据拼接在URL后，API2接口默认get请求
             * 默认已有数据：geetest_challenge，geetest_validate，geetest_seccode
             * TODO 注意： 切勿重复添加以上数据
             */
            @Override
            public Map<String, String> gt3SecondResult() {
                LogUtil.d("gt3SecondResult");
                Map<String, String> map = new HashMap<String, String>();
                map.put("test", "test");
                return map;
            }

            /**
             * api2完成回调，判断是否验证成功，且成功调用gt3TestFinish，失败调用gt3TestClose
             *
             * @param result api2接口返回数据
             */
            @Override
            public void gt3DialogSuccessResult(String result) {
                LogUtil.d("gt3DialogSuccessResult-->" + result);
            }

            /**
             * @param error 返回错误码，具体解释见GitHub文档
             */
            @Override
            public void gt3DialogOnError(String error) {
                LogUtil.e("gt3DialogOnError-->" + error);
            }
        });
        // 设置是否可以点击Dialog灰色区域关闭验证码
        gt3GeetestUtils.setDialogTouch(false);
    }
}
