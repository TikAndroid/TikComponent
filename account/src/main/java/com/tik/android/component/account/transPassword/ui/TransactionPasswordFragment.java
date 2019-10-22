package com.tik.android.component.account.transPassword.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.login.service.AccountServiceImpl;
import com.tik.android.component.account.transPassword.contract.TransactionPasswordContract;
import com.tik.android.component.account.transPassword.presenter.TransactionPasswordPresenter;
import com.tik.android.component.account.verify.service.VerifyServiceImpl;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.basemvp.WrapperActivity;
import com.tik.android.component.bussiness.account.bean.VerifyCase;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.widget.VerificationCodeInput;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;

/**
 * Created by yangtao on 2018/11/29
 */
public class TransactionPasswordFragment extends BaseMVPFragment<TransactionPasswordPresenter>
        implements TransactionPasswordContract.View {

    @BindView(R2.id.password_title_left)
    ImageView mPasswordTitleLeft;
    @BindView(R2.id.password_infor)
    TextView mPasswordInfor;
    @BindView(R2.id.password_input_code)
    VerificationCodeInput mPasswordInputCode;
    @BindView(R2.id.password_forget)
    TextView mPasswordForget;
    @BindView(R2.id.password_confirm)
    TextView mPasswordConfirm;
    @BindView(R2.id.password_title)
    TextView mPasswordTitle;

    private String mFirstPassword;
    private String mSecondPassword;
    private String mTicket;
    private boolean mIsSetPassword = true;
    public static final String ARGS_IS_SET_PASSWORD = "is_set_password";
    private static final int REQ_REGISTER_VERIFY_FRAGMENT = 0x1005;

    public static TransactionPasswordFragment newInstance(boolean setPassword) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_IS_SET_PASSWORD, setPassword);
        TransactionPasswordFragment fragment = new TransactionPasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayout() {
        return R.layout.account_fragment_transaction_password;
    }

    @Override
    protected void init(View view) {
        super.init(view);

        if (getArguments() != null) {
            mIsSetPassword = getArguments().getBoolean(ARGS_IS_SET_PASSWORD);
        }

        if (mIsSetPassword) {
            mPasswordForget.setVisibility(View.GONE);
            mPasswordInfor.setText(getString(R.string.account_transaction_set_password));
        }

        RxTextView.textChanges(mPasswordInputCode).toFlowable(BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(this))
                .safeSubscribe(new NormalSubscriber<CharSequence>() {
                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (TextUtils.isEmpty(mFirstPassword) && 6 == charSequence.length()) {
                            if (!mIsSetPassword) {
                                mPresenter.verifyPassword(String.valueOf(charSequence));
                                return;
                            }
                            mFirstPassword = String.valueOf(charSequence);
                            mPasswordInputCode.getText().clear();
                            mPasswordConfirm.setEnabled(false);
                            mPasswordConfirm.setVisibility(View.VISIBLE);
                            mPasswordInfor.setText(getString(R.string.account_transaction_confirm_password));
                        } else if (!TextUtils.isEmpty(mFirstPassword) && 6 == charSequence.length()) {
                            mSecondPassword = String.valueOf(charSequence);
                            mPasswordConfirm.setEnabled(true);
                        }
                    }
                });
        showSoftInput(mPasswordInputCode);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsSetPassword) {
            mPasswordTitle.setText(getString(R.string.account_transaction_set_password_title));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    @OnClick({R2.id.password_confirm, R2.id.password_title_left, R2.id.password_forget})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.password_confirm) {
            setPassword();
        } else if (i == R.id.password_title_left) {
            onBackPressed();
        } else if (i == R.id.password_forget) {
            startVerify();
        }

    }

    private void setPassword() {
        if (mSecondPassword.equals(mFirstPassword)) {
            if (TextUtils.isEmpty(mTicket)) {
                mPresenter.setPassword(mSecondPassword);
            } else {
                mPresenter.resetPassword(mSecondPassword, mTicket);
            }
        } else {
            mPasswordInputCode.getText().clear();
            mPasswordInfor.setText(getString(R.string.account_transaction_set_password));
            ToastUtil.showToastShort(getString(R.string.account_transaction_re_enter_password));
            mPasswordConfirm.setVisibility(View.GONE);
            mFirstPassword = "";
            mSecondPassword = "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            mTicket = data.getStringExtra(AccountConstants.TICKET);
            ToastUtil.showToastShort(mTicket);
            if (!TextUtils.isEmpty(mTicket)) {
                mPasswordInputCode.getText().clear();
                mPasswordInfor.setText(getString(R.string.account_transaction_set_password));
                mPasswordForget.setVisibility(View.GONE);
                mIsSetPassword = true;
            }
        }
    }

    private void startVerify() {
        VerifyServiceImpl verifyService = new VerifyServiceImpl();
        verifyService.startVerify(this, VerifyCase.RESET_ASSET_PASSWORD,
                null, REQ_REGISTER_VERIFY_FRAGMENT);
    }


    @Override
    public void exitWithResult(boolean isSuccess) {
        if (_mActivity instanceof WrapperActivity) {
            _mActivity.setResult(RESULT_OK, AccountServiceImpl.createIntentDataForTransactionPassword(isSuccess));
            _mActivity.finish();
        }
    }

    @Override
    public void showDiallog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
        builder.setMessage(getString(R.string.account_transaction_password_error))
                .setPositiveButton(getString(R.string.account_transaction_password_retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPasswordInputCode.getText().clear();
                    }
                }).setNegativeButton(getString(R.string.account_transaction_forget_password), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVerify();
            }
        });
        builder.create().show();
    }
}
