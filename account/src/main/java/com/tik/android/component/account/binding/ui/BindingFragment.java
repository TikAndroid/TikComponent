package com.tik.android.component.account.binding.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.binding.bean.BindArgs;
import com.tik.android.component.account.binding.bean.VerifiCodeArgs;
import com.tik.android.component.account.binding.contract.BindingContract;
import com.tik.android.component.account.binding.presenter.BindingPresenter;
import com.tik.android.component.account.login.Constants;
import com.tik.android.component.account.login.CountryChooseFragment;
import com.tik.android.component.account.login.RegisterFragment;
import com.tik.android.component.account.login.bean.Country;
import com.tik.android.component.account.login.service.AccountServiceImpl;
import com.tik.android.component.account.verify.VerifyUtils;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.basemvp.WrapperActivity;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.ThirdPartUser;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.libcommon.PhoneNumberUtils;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.widget.EditTextField;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.tik.android.component.account.verify.VerifyUtils.CODE_COUNT_DOWN;

/**
 * Created by yangtao on 2018/11/23
 */
public class BindingFragment extends BaseMVPFragment<BindingPresenter> implements BindingContract.View {

    @BindView(R2.id.title_btn_left)
    ImageView mTitleBtnLeft;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.binding_country_code)
    TextView mBindingCountryCode;
    @BindView(R2.id.binding_number)
    EditTextField mBindingNumber;
    @BindView(R2.id.binding_email)
    EditTextField mBindingEmail;
    @BindView(R2.id.binding_verify_code)
    EditTextField mBindingVerifyCode;
    @BindView(R2.id.binding_get_verify_code)
    TextView mBindingGetVerifyCode;
    @BindView(R2.id.binding_bind)
    TextView mBindingBind;

    @BindView(R2.id.binding_item_country)
    View bindingItemCountryHeader;
    @BindView(R2.id.btn_country_choose)
    TextView mBtnChooseCountry;
    @BindView(R2.id.binding_divider_below_country)
    View bindingItemCountryDivider;
    @BindView(R2.id.binding_item_phone)
    View bindingItemPhoneNumber;
    @BindView(R2.id.binding_type)
    TextView mBindType;

    private ThirdPartUser mThirdPartUser;
    static final int REQUEST_CODE_CHOOSE_COUNTRY = 0x2001;
    private Country mCountry = new Country("China (中国)", "+86", "cn");
    private boolean mIsMobile = true;
    private String mThirdUserType;
    // count down can not bind to life cycle
    private Disposable mCountdownDisposable;

    public static BindingFragment newInstance() {
        Bundle bundle = new Bundle();
        BindingFragment fragment = new BindingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayout() {
        return R.layout.account_fragment_binding;
    }

    @Override
    protected void init(View view) {
        super.init(view);

        if (LocalAccountInfoManager.getInstance().getUser() != null) {
            mThirdPartUser = LocalAccountInfoManager.getInstance().getUser().getThirdPartUser();
        }
        if (mThirdPartUser == null || TextUtils.isEmpty(mThirdPartUser.getUuid())) {
            onBackPressed();
            return;
        }
        String userType = mThirdPartUser.getUserType();
        if (!TextUtils.isEmpty(userType) && userType.contains("_")) {
            mThirdUserType = userType.split("_")[0];
        } else {
            mThirdUserType = userType;
        }

        updateCountryCode(mCountry);
        updateEdits(mIsMobile);

        // use RxBinding update bind button status.
        Observable.combineLatest(RxTextView.textChanges(mBindingNumber),
                RxTextView.textChanges(mBindingEmail),
                RxTextView.textChanges(mBindingVerifyCode),
                (charSequence, charSequence2, charSequence3) -> inputEmpty())
                .toFlowable(BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(this))
                .safeSubscribe(new NormalSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean verifyOk) {
                        mBindingBind.setEnabled(verifyOk);
                    }
                });
    }

    private void updateEdits(boolean phoneDefault) {
        if (phoneDefault) {
            mTitle.setText(R.string.account_binding_title_phone);
            mBindType.setText(R.string.account_binding_type_email);
            mBindingEmail.setVisibility(View.GONE);
            bindingItemCountryHeader.setVisibility(View.VISIBLE);
            bindingItemCountryDivider.setVisibility(View.VISIBLE);
            bindingItemPhoneNumber.setVisibility(View.VISIBLE);
        } else {
            mTitle.setText(R.string.account_binding_title_email);
            mBindType.setText(R.string.account_binding_type_phone);
            bindingItemCountryHeader.setVisibility(View.GONE);
            bindingItemCountryDivider.setVisibility(View.GONE);
            bindingItemPhoneNumber.setVisibility(View.GONE);
            mBindingEmail.setVisibility(View.VISIBLE);
        }
    }

    private boolean inputEmpty() {
        if (textEmpty(mBindingVerifyCode)) {
            return false;
        }
        if (mIsMobile) {
            return !textEmpty(mBindingNumber);
        } else {
            return !textEmpty(mBindingEmail);
        }
    }

    private boolean textEmpty(EditTextField text) {
        return TextUtils.isEmpty(String.valueOf(text.getText()));
    }

    @OnClick({R2.id.binding_get_verify_code, R2.id.binding_bind
            , R2.id.title_btn_left, R2.id.binding_country_code})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_btn_left) {
            onBackPressed();
        } else if (i == R.id.binding_get_verify_code) {
            getVerifyCode();
        } else if (i == R.id.binding_bind) {
            bind();
        } else if (i == R.id.binding_country_code) {
            startForResult(CountryChooseFragment.newInstance(), REQUEST_CODE_CHOOSE_COUNTRY);
        }
    }

    private void getVerifyCode() {
        String countryCode = getCountryCode();
        String mobile = String.valueOf(mBindingNumber.getText());
        String email = String.valueOf(mBindingEmail.getText());
        String type_app = mThirdPartUser.getUserType();
        VerifiCodeArgs args = new VerifiCodeArgs(countryCode, mobile, email, mThirdUserType);
        if (inputFormatRight(args)) {
            mPresenter.getVerificationCode(args, mIsMobile);
        }
    }

    private boolean inputFormatRight(VerifiCodeArgs args) {
        if (mIsMobile) {
            boolean numFormatOk = PhoneNumberUtils.isValidPhoneNumber(args.getMobile(), Integer.parseInt(args.getCountryCode()));
            if (!numFormatOk) {
                ToastUtil.showToastShort(getString(R.string.account_binding_number_format_error));
                return false;
            }
        } else {
            boolean emailFormatOk = VerifyUtils.isEmail(args.getEmail());
            if (!emailFormatOk) {
                ToastUtil.showToastShort(getString(R.string.account_binding_email_format_error));
                return false;
            }
        }

        return true;
    }

    private void bind() {
        BindArgs args = new BindArgs();
        String verifiCode = String.valueOf(mBindingVerifyCode.getText());
        String countryCode = getCountryCode();
        String mobile = String.valueOf(mBindingNumber.getText());
        String email = String.valueOf(mBindingEmail.getText());
        if (mIsMobile) {
            args.setCountryCode(countryCode);
            args.setMobile(mobile);
            args.setCode(verifiCode);
        } else {
            args.setEmail(email);
            args.setEmailCode(verifiCode);
        }
        args.setUuid(mThirdPartUser.getUuid());
        args.setType(mThirdUserType);
        mPresenter.bind(args, mIsMobile);
    }

    private String getCountryCode() {
        String countryCode = String.valueOf(mBindingCountryCode.getText());
        if (countryCode.startsWith("+")) {
            countryCode = countryCode.substring(1, countryCode.length());
        }
        return countryCode;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_COUNTRY:
                if (resultCode == RESULT_OK && data != null) {
                    mCountry = data.getParcelable(CountryChooseFragment.RESULT_COUNTRY);
                    updateCountryCode(mCountry);
                }
                break;
        }
    }

    private void updateCountryCode(Country country) {
        if (country == null) {
            return;
        }
        mBindingCountryCode.setText(country.getCode());
        mBtnChooseCountry.setText(country.getCountry());
    }


    @OnClick(R2.id.btn_country_choose)
    public void chooseCountry() {
        startForResult(CountryChooseFragment.newInstance(), REQUEST_CODE_CHOOSE_COUNTRY);
    }

    @OnClick(R2.id.binding_type)
    public void switchBindType(View view) {
        if (mIsMobile) {
            mIsMobile = false;
            mBindingEmail.setVisibility(View.VISIBLE);
            mBindingEmail.setFocusable(true);
            mBindingEmail.requestFocus();
            mBindingNumber.getText().clear();
            mBindingVerifyCode.getText().clear();
        } else {
            mIsMobile = true;
            mBindingEmail.setVisibility(View.GONE);
            mBindingEmail.getText().clear();
            mBindingVerifyCode.getText().clear();
            mBindingNumber.setFocusable(true);
            mBindingNumber.requestFocus();
        }
        updateEdits(mIsMobile);
    }

    @Override
    public void showDiallog(String type, BindArgs args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
        if (Constants.BIND_DEFAULT_DIALOG.equals(type)) {
            builder.setMessage(getString(R.string.account_binding_dialog_default_message))
                    .setPositiveButton(getString(R.string.account_binding_dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.doBind(args, mIsMobile);
                        }
                    }).setNegativeButton(getString(R.string.account_binding_dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            builder.setMessage(getString(R.string.account_binding_dialog_message, type.toString()))
                    .setPositiveButton(getString(R.string.account_binding_dialog_login), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            start(RegisterFragment.newInstance(true, !mIsMobile));
                        }
                    }).setNegativeButton(getString(R.string.account_binding_dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        builder.create().show();
    }

    @Override
    public void exitWithResult(boolean isSuccess) {
        if (isSuccess) {
            ToastUtil.showToastShort(getString(R.string.account_binding_success));
        }
        if (_mActivity instanceof WrapperActivity) {
            _mActivity.setResult(RESULT_OK, AccountServiceImpl.createIntentDataForBinding(isSuccess));
            _mActivity.finish();
        }
    }

    @Override
    public void getCodeSuccess() {
        countdown(CODE_COUNT_DOWN);
    }

    private void countdown(int count) {
        final TextView countDownTv = mBindingGetVerifyCode;

        RxUtils.countdown(count)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        cancelObserver(mCountdownDisposable);
                        mCountdownDisposable = disposable;
                        countDownTv.setEnabled(false);
                        countDownTv.setTextColor(getResources().getColor(R.color.account_text_deactive));
                    }
                })
                .safeSubscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        countDownTv.setText(getString(R.string.account_binding_code_resend, integer));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        countDownTv.setText(R.string.account_resend);
                        countDownTv.setTextColor(getResources().getColor(R.color.account_highlight));
                        countDownTv.setEnabled(true);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelObserver(mCountdownDisposable);
    }

    private void cancelObserver(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
