package com.tik.android.component.account.verify.service;

import android.os.Bundle;

import com.tik.android.component.account.verify.VerifyFragment;
import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.basemvp.RouteUtil;
import com.tik.android.component.bussiness.account.bean.VerifyArgs;
import com.tik.android.component.bussiness.account.bean.VerifyCase;
import com.tik.android.component.bussiness.api.HoxRequest;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.bussiness.service.account.IVerifyService;

public class VerifyServiceImpl implements IVerifyService {
    @Override
    public void startVerify(BasicFragment from, int verifyCase, HoxRequest hoxRequest, int requestCode) {
        VerifyArgs verifyArgs = null;
        switch (verifyCase) {
            case VerifyCase.TURN_ON_GOOGLE:
                verifyArgs = createVerifyArgs(VerifyArgs.SINGLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_TURN_ON_GOOGLE, AccountConstants.OP_TYPE_TURN_ON_AUTHENTICATOR);
                break;
            case VerifyCase.TURN_ON_EMAIL:
                verifyArgs = createVerifyArgs(VerifyArgs.SINGLE_VERIFY, VerifyArgs.SINGLE_VERIFY_EMAIL,
                        AccountConstants.EMAIL_TYPE_TURN_ON_EMAIL, AccountConstants.OP_TYPE_TURN_ON_AUTHENTICATOR);
                break;
            case VerifyCase.TURN_ON_SMS:
                verifyArgs = createVerifyArgs(VerifyArgs.SINGLE_VERIFY, VerifyArgs.SINGLE_VERIFY_SMS,
                        AccountConstants.EMAIL_TYPE_TURN_ON_SMS, AccountConstants.OP_TYPE_TURN_ON_AUTHENTICATOR);
                break;
            case VerifyCase.TURN_OFF_GOOGLE:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_TURN_OFF_GOOGLE, AccountConstants.OP_TYPE_TURN_OFF_AUTHENTICATOR);
                break;
            case VerifyCase.TURN_OFF_EMAIL:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_TURN_OFF_EMAIL, AccountConstants.OP_TYPE_TURN_OFF_AUTHENTICATOR);
                break;
            case VerifyCase.TURN_OFF_SMS:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_TURN_OFF_SMS, AccountConstants.OP_TYPE_TURN_OFF_AUTHENTICATOR);
                break;
            case VerifyCase.CASH_OUT:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_CASH_OUT, AccountConstants.OP_TYPE_CASH_OUT);
                break;
            case VerifyCase.CHANGE_MOBILE:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_CHANGE_MOBILE, AccountConstants.OP_TYPE_CHANGE_MOBILE);
                break;
            case VerifyCase.CHANGE_EMAIL:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_CHANGE_EMAIL, AccountConstants.OP_TYPE_CHANGE_EMAIL);
                break;
            case VerifyCase.CHANGE_PASSWORD:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_CHANGE_PASSWORD, AccountConstants.OP_TYPE_CHANGE_PASSWORD);
                break;
            case VerifyCase.SET_PASSWORD:
                verifyArgs = createVerifyArgs(VerifyArgs.SINGLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_SET_PASSWORD, AccountConstants.OP_TYPE_SET_PASSWORD);
                break;
            case VerifyCase.RESET_ASSET_PASSWORD:
                verifyArgs = createVerifyArgs(VerifyArgs.MULTIPLE_VERIFY, "",
                        AccountConstants.EMAIL_TYPE_RESET_ASSET_PASSWORD, AccountConstants.OP_TYPE_CHANGE_ASSET_PASSWORD);
                break;
            default:
                break;
        }
        if (verifyArgs == null) {
            return;
        }
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putParcelable(AccountConstants.REQUEST, hoxRequest);
        fragmentArgs.putParcelable(AccountConstants.VERIFY_ARGS, verifyArgs);
        RouteUtil.startForResultWithActivity(from, VerifyFragment.class, fragmentArgs, requestCode, null);
    }

    private VerifyArgs createVerifyArgs(String verifyType, String verifyTypeDetail, String emailType, String opType) {
        return new VerifyArgs.Builder()
                .verifyType(verifyType)
                .verifyTypeDetail(verifyTypeDetail)
                .emailType(emailType)
                .opType(opType)
                .builder();
    }
}
