package com.tik.android.component.bussiness.service.account;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.bussiness.api.HoxRequest;

public interface IVerifyService {
    /**
     *
     * @param from 从哪个fragment发起调用请求
     * @param verifyCase 安全验证场景
     * @param hoxRequest 是否需要VerifyFragment执行请求，若需要，应包装对应的请求参数
     * @param requestCode 请求码
     */
    void startVerify(BasicFragment from, int verifyCase, HoxRequest hoxRequest, int requestCode);
}
