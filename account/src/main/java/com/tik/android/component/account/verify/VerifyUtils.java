package com.tik.android.component.account.verify;

import android.util.Patterns;
import android.widget.TextView;

import com.tik.android.component.account.R;
import com.tik.android.component.basemvp.RxFragment;
import com.tik.android.component.basemvp.RxUtils;

import java.util.regex.Matcher;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VerifyUtils {

    public static final int CODE_COUNT_DOWN = 60;

    /**
     * handle the lifecycle careful when UI already visible
     * @param textView target countdown tv
     * @param fragment ui
     */
    public static void showCountDownText(TextView textView, RxFragment fragment) {
        RxUtils.countdown(CODE_COUNT_DOWN)
                .compose(RxUtils.bindToLifecycle(fragment))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        textView.setEnabled(false);
                        textView.setTextColor(fragment.getResources().getColor(R.color.account_text_deactive));
                    }
                })
                .safeSubscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        textView.setText(fragment.getString(R.string.account_binding_code_resend, integer));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        textView.setText(R.string.account_resend);
                        textView.setTextColor(fragment.getResources().getColor(R.color.account_highlight));
                        textView.setEnabled(true);
                    }
                });
    }

    /**
     * verify the mailbox
     * @param email
     * @return pass:true，otherwise:false
     */
    public static boolean isEmail(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        if (matcher != null) {
           return matcher.matches();
        }
        return false;
    }
}
