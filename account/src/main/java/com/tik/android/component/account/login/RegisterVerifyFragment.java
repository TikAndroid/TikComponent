package com.tik.android.component.account.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.login.bean.RegisterArgs;
import com.tik.android.component.account.login.contract.LoginContract;
import com.tik.android.component.account.login.presenter.LoginPresenter;
import com.tik.android.component.account.login.service.AccountServiceImpl;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.basemvp.WrapperActivity;
import com.tik.android.component.bussiness.service.account.LoginType;
import com.tik.android.component.libcommon.SpannableBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.tik.android.component.account.verify.VerifyUtils.CODE_COUNT_DOWN;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/14.
 */
public class RegisterVerifyFragment extends BaseMVPFragment<LoginPresenter> implements LoginContract.View {
    public static final String TAG = RegisterVerifyFragment.class.getSimpleName();

    public static final String ARGS_REGISTER_ARGS = "register_args";
    public static final String ARGS_CHALLENGE = "challenge";
    public static final String ARGS_VALIDATE = "validate";
    public static final String ARGS_SECCODE = "seccode";

    private Disposable mCountdownDisposable;

    public static RegisterVerifyFragment newInstance(RegisterArgs registerArgs, String challenge, String validate, String seccode) {
        RegisterVerifyFragment fragment = new RegisterVerifyFragment();
        Bundle args = new Bundle();
        args.putParcelable(RegisterVerifyFragment.ARGS_REGISTER_ARGS, registerArgs);
        args.putString(RegisterVerifyFragment.ARGS_CHALLENGE, challenge);
        args.putString(RegisterVerifyFragment.ARGS_VALIDATE, validate);
        args.putString(RegisterVerifyFragment.ARGS_SECCODE, seccode);
        fragment.setArguments(args);
        return fragment;
    }

    private RegisterArgs mRegisterArgs;

    @BindView(R2.id.title)
    public TextView mTitle;
    @BindView(R2.id.btn_resend_code)
    public TextView mBtnResend;
    @BindView(R2.id.verify_input_code)
    public TextView mInputCode;

    @Override
    public int initLayout() {
        return R.layout.account_fragment_register_verify;
    }

    @Override
    protected void init(View view) {
        super.init(view);

        if (getArguments() == null || (mRegisterArgs = getArguments().getParcelable(ARGS_REGISTER_ARGS)) == null) {
            onBackPressed();
            return;
        }

        if (mRegisterArgs.isMobileType()) {
            mTitle.setText(R.string.account_verify_sms);
        } else {
            mTitle.setText(R.string.account_verify_email);
        }

        sendVerifyCode();

    }

    @OnClick(R2.id.btn_resend_code)
    public void onResendClick() {
        sendVerifyCode();
    }

    private void sendVerifyCode() {
        if (mRegisterArgs.isEmailType()) {
            mPresenter.sendVerifyCode(getResources().getConfiguration().locale.getLanguage(), mRegisterArgs.getEmail(), null);
        } else if (mRegisterArgs.isMobileType()) {
            mPresenter.sendSmsCode(mRegisterArgs.getCountryCode(), mRegisterArgs.getMobile());
        } else {
            return;
        }

        RxUtils.countdown(CODE_COUNT_DOWN)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        cancelObserver(mCountdownDisposable);
                        mCountdownDisposable = disposable;
                        mBtnResend.setEnabled(false);
                        mBtnResend.setTextColor(getResources().getColor(R.color.account_text_deactive));
                    }
                })
                .safeSubscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        String countDown = getString(R.string.account_resend_countdown, integer);
                        int timeIndexEnd = countDown.indexOf("s") + 1;
                        mBtnResend.setText(SpannableBuilder.create(countDown, 0, timeIndexEnd)
                                .span(new ForegroundColorSpan(getResources().getColor(R.color.account_text_primary))).build());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mBtnResend.setText(R.string.account_resend);
                        mBtnResend.setTextColor(getResources().getColor(R.color.account_highlight));
                        mBtnResend.setEnabled(true);
                    }
                });
    }

    @OnClick(R2.id.verify_register)
    public void onRegisterClick() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }

        mRegisterArgs.setCode(String.valueOf(mInputCode.getText()));
        String challenge = args.getString(ARGS_CHALLENGE);
        String validate = args.getString(ARGS_VALIDATE);
        String seccode = args.getString(ARGS_SECCODE);
        if (!TextUtils.isEmpty(mRegisterArgs.getEmail())) {
            mPresenter.register(challenge, validate, seccode, mRegisterArgs);
        } else if (!TextUtils.isEmpty(mRegisterArgs.getCountryCode()) && !TextUtils.isEmpty(mRegisterArgs.getMobile())) {
            mPresenter.registerByMobile(challenge, validate, seccode, mRegisterArgs);
        }
    }

    @OnClick(R2.id.title_btn_left)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onRegisterSuccess() {
        exitWithSuccess();
    }

    private void exitWithSuccess() {
        if (_mActivity instanceof WrapperActivity) {
            _mActivity.setResult(RESULT_OK, AccountServiceImpl.createIntentDataForRegister(true, LoginType.HOX));
            _mActivity.finish();
        } else {
            setFragmentResult(RESULT_OK, AccountServiceImpl.createBundleDataForRegister(true, LoginType.HOX));
            pop();
        }
    }

    @Override
    public void onVerifyResult(int result) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelObserver(mCountdownDisposable);
    }

    private void cancelObserver(Disposable disposable) {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
