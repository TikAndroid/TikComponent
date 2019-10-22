package com.tik.android.component.account.verify;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.verify.bean.Ticket;
import com.tik.android.component.account.verify.contract.VerifyContract;
import com.tik.android.component.account.verify.presenter.VerifyPresenter;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.account.bean.VerifyArgs;
import com.tik.android.component.bussiness.api.HoxRequest;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.libcommon.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;


public class VerifyFragment extends BaseMVPFragment<VerifyPresenter> implements VerifyContract.View {
    @BindView(R2.id.ll_email)
    LinearLayout mLinearLayoutEmail;
    @BindView(R2.id.ll_sms)
    LinearLayout mLinearLayoutSms;
    @BindView(R2.id.ll_google)
    LinearLayout mLinearLayoutGoogle;
    @BindView(R2.id.edit_email)
    EditText mEditEmail;
    @BindView(R2.id.edit_sms)
    EditText mEditSms;
    @BindView(R2.id.edit_google)
    EditText mEditGoogle;
    @BindView(R2.id.btn_commit)
    TextView mBtnCommit;
    @BindView(R2.id.tv_getCode_email)
    TextView mTvEmailGetCode;
    @BindView(R2.id.tv_getCode_sms)
    TextView mTvSmsGetCode;
    @BindView(R2.id.tv_phone)
    TextView mTvPhone;
    @BindView(R2.id.tv_email)
    TextView mTvEmail;
    @BindView(R2.id.sms_divider)
    View smsDivider;
    @BindView(R2.id.email_divider)
    View emailDivider;

    private String mOpType;//授权类型
    private String mEmailType;//接收邮件类型
    private HoxRequest mRequest;
    private VerifyArgs mVerifyArgs;
    private User mUser;
    private User.Auth mAuth;

    @Override
    public int initLayout() {
        return R.layout.account_fragment_verify;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mUser = LocalAccountInfoManager.getInstance().getUser();
        if (getArguments() == null  || mUser == null || mUser.getAuth() == null) {
            return;
        }
        mAuth = mUser.getAuth();
        mVerifyArgs = getArguments().getParcelable(AccountConstants.VERIFY_ARGS);
        if (mVerifyArgs == null) {
            return;
        }
        mTvPhone.setText(mUser.getMobile());
        mTvEmail.setText(mUser.getEmail());
        mRequest = getArguments().getParcelable(AccountConstants.REQUEST);
        mEmailType = mVerifyArgs.getEmailType();
        mOpType = mVerifyArgs.getOpType();

        if (mVerifyArgs.isSingleVerify()) {
            showSingleVerify();
        } else if (mVerifyArgs.isMultipleVerify()) {
            showMultipleVerify();
        }
    }

    private void showSingleVerify() {
        if (mVerifyArgs.isSmsVerify()) {
            showSmsVerify();
        } else if (mVerifyArgs.isEmailVerify()) {
            showEmailVerify();
        } else if (mVerifyArgs.isGoogleVerify()) {
            showGoogleVerify();
        } else {
            showVerifyByPriority();
        }
    }

    private void showMultipleVerify() {
        ArrayList<Observable<CharSequence>> observables = new ArrayList<>();
        if (mAuth.getGoogle()) {
            mLinearLayoutGoogle.setVisibility(View.VISIBLE);
            observables.add(RxTextView.textChanges(mEditGoogle));
        }
        if (mAuth.getSms()) {
            mLinearLayoutSms.setVisibility(View.VISIBLE);
            smsDivider.setVisibility(emailOrGoogleEnable() ? View.VISIBLE : View.GONE);
            observables.add(RxTextView.textChanges(mEditSms));
        }
        if (mAuth.getEmail()) {
            mLinearLayoutEmail.setVisibility(View.VISIBLE);
            emailDivider.setVisibility(mAuth.getGoogle() ? View.VISIBLE : View.GONE);
            observables.add(RxTextView.textChanges(mEditEmail));
        }
        checkMultipleEditText(observables);
    }

    private boolean verifyInput(Object[] objects) {
        for (Object object : objects) {
            if (TextUtils.isEmpty(object.toString())) {
                return false;
            }
        }
        return true;
    }

    private void showGoogleVerify() {
        mLinearLayoutGoogle.setVisibility(View.VISIBLE);
        checkSingleEditText(RxTextView.textChanges(mEditGoogle));
    }

    private void showSmsVerify() {
        mLinearLayoutSms.setVisibility(View.VISIBLE);
        smsDivider.setVisibility(View.GONE);
        checkSingleEditText(RxTextView.textChanges(mEditSms));
    }

    private void showEmailVerify() {
        mLinearLayoutEmail.setVisibility(View.VISIBLE);
        emailDivider.setVisibility(View.GONE);
        checkSingleEditText(RxTextView.textChanges(mEditEmail));
    }

    public void showVerifyByPriority() {
        if (mAuth.getGoogle()) {
            showGoogleVerify();
        } else if (mAuth.getSms()) {
            showSmsVerify();
        } else if (mAuth.getEmail()) {
            showEmailVerify();
        }
    }

    private boolean emailOrGoogleEnable() {
        return mAuth.getEmail() || mAuth.getGoogle();
    }

    @OnClick(R2.id.tv_getCode_email)
    public void onClickGetEmailCode() {
        mPresenter.getEmailCode("en", mEmailType);
    }

    @OnClick(R2.id.tv_getCode_sms)
    public void onClickGetSmsCode() {
        mPresenter.getSmsCode(mUser.getCountryCode(), mUser.getMobile());
    }

    @OnClick(R2.id.ic_close)
    public void onCloseClick() {
        onBackPressed();
    }

    @OnClick(R2.id.btn_commit)
    public void onCommitClick() {
        String emailCode = mEditEmail != null ? mEditEmail.getText().toString() : "";
        String smsCode = mEditSms != null ? mEditSms.getText().toString() : "";
        String googleCode = mEditGoogle != null ? mEditGoogle.getText().toString() : "";
        mPresenter.getTicket(mOpType, mUser.getCountryCode(), mUser.getMobile(), mUser.getEmail(),
                smsCode, emailCode, googleCode);
    }

    @Override
    public void getSmsCodeSuccess() {
        VerifyUtils.showCountDownText(mTvSmsGetCode, this);
    }

    @Override
    public void getSmsCodeFail(String errorMsg) {
        ToastUtil.showToastShort(errorMsg);
    }

    @Override
    public void getEmailCodeSuccess() {
        VerifyUtils.showCountDownText(mTvEmailGetCode, this);
    }

    @Override
    public void getEmailCodeFail(String errorMsg) {
        ToastUtil.showToastShort(errorMsg);
    }

    @Override
    public void getTicketSuccess(Ticket ticket) {
        if (ticket == null) {
            return;
        }
        if (mRequest != null) {
            Map<String, String> map = mRequest.getMap();
            map.put(AccountConstants.TICKET, ticket.getTicket());
            if (mRequest.isPost()) {
                mPresenter.doPostRequestByTicket(mRequest.getUrl(), map);
            } else if (mRequest.isGet()) {
                mPresenter.doGetRequestByTicket(mRequest.getUrl(), map);
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra(AccountConstants.TICKET, ticket.getTicket());
            _mActivity.setResult(RESULT_OK, intent);
            _mActivity.finish();
        }
    }

    @Override
    public void getTicketFail(String errorMsg) {
        ToastUtil.showToastShort(errorMsg);
    }

    @Override
    public void doGetRequestByTicketSuccess() {
        setResultOk();
    }

    @Override
    public void doPostRequestByTicketSuccess() {
        setResultOk();
    }

    @Override
    public void doGetRequestByTicketFail(String errorMsg) {
        ToastUtil.showToastShort(errorMsg);
    }

    @Override
    public void doPostRequestByTicketFail(String errorMsg) {
        ToastUtil.showToastShort(errorMsg);
    }

    private void setResultOk() {
        Intent intent = new Intent();
        _mActivity.setResult(RESULT_OK, intent);
        _mActivity.finish();
    }

    private void checkSingleEditText(Observable<CharSequence> observable) {
        observable.compose(RxUtils.bindToLifecycle(this))
                .subscribe(s -> {
                    mBtnCommit.setEnabled(!TextUtils.isEmpty(s));
                });
    }

    private void checkMultipleEditText(List<Observable<CharSequence>> observables) {
        Observable.combineLatest(observables, objects -> verifyInput(objects))
                .compose(RxUtils.bindToLifecycle(this))
                .subscribe(result -> {
                    mBtnCommit.setEnabled(result);
                });
    }
}
