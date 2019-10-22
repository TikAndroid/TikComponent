package com.tik.android.component.mine.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.tik.android.component.mine.R;

/**
 * @describe : create dialog
 * @usage : {@link #create(Builder)}
 * builder = new Builder(activity)
 *          .title(string)
 *          .message(string)
 *          .positiveText(string)
 *          .negativeText(string)
 *          .dialogListener(listener);
 * {@link #create(Builder).show()}
 *
 * </p>
 * Created by tanlin on 2018/11/14
 */
public class DialogCreator {

    public static class DialogListener implements DialogInterface.OnClickListener,
            DialogInterface.OnShowListener, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
        }

        @Override
        public void onCancel(DialogInterface dialog) {
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
        }

        @Override
        public void onShow(DialogInterface dialog) {
        }
    }

    public static class Builder {
        private String dlgTitle;
        private String dlgMsg;
        private String negMsg;
        private String posMsg;
        private boolean cancelButtonVisible;
        private boolean cancelable;
        private Activity mHost;
        private DialogListener mDlgListener;

        /**
         * must has a host
         * @param host activity usually
         */
        public Builder(Activity host) {
            mHost = host;
            // default no title
            // dlgTitle = host.getResources().getString(R.string.mine_hox_dialog_title);
            posMsg = host.getResources().getString(R.string.mine_hox_dialog_ok);
            negMsg = host.getResources().getString(R.string.mine_hox_dialog_cancel);
            // default true
            cancelable = true;
            cancelButtonVisible = true;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder title(String title) {
            dlgTitle = title;
            return this;
        }

        public Builder message(String msg) {
            dlgMsg = msg;
            return this;
        }

        public Builder cancelBtnVisible(boolean visible) {
            cancelButtonVisible = visible;
            return this;
        }

        public Builder negativeText(String msg) {
            negMsg = msg;
            return this;
        }

        public Builder positiveText(String msg) {
            posMsg = msg;
            return this;
        }

        public Builder dialogListener(DialogListener listener) {
            if(listener != null) {
                mDlgListener = listener;
            }
            return this;
        }

        public Dialog build() {
            final Dialog dialog = new Dialog(mHost, R.style.MineDialogTheme);
            //View root = dialog.getLayoutInflater().inflate(R.layout.mine_dialog_2, null, false);
            View root = dialog.getLayoutInflater().inflate(R.layout.mine_dialog, null, false);
            TextView pos = (TextView) root.findViewById(R.id.mine_dialog_positive);
            TextView neg = (TextView) root.findViewById(R.id.mine_dialog_negative);
            TextView titleView = (TextView) root.findViewById(R.id.mine_dialog_title);
            TextView messageView = (TextView) root.findViewById(R.id.mine_dialog_content);
            titleView.setText(dlgTitle);
            messageView.setText(dlgMsg);
            pos.setText(posMsg);
            pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDlgListener != null) {
                        mDlgListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                    dialog.dismiss();
                }
            });
            neg.setText(negMsg);
            neg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDlgListener != null) {
                        mDlgListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                    dialog.dismiss();
                }
            });
            if(!cancelButtonVisible) {
                neg.setVisibility(View.GONE);
            } else {
                neg.setVisibility(View.VISIBLE);
            }
            if(TextUtils.isEmpty(dlgTitle)) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
            }
            dialog.setCancelable(cancelable);
            dialog.setOnDismissListener(mDlgListener);
            dialog.setOnCancelListener(mDlgListener);
            dialog.setOnShowListener(mDlgListener);
            dialog.setContentView(root);
            // for text scroll
            messageView.setMovementMethod(ScrollingMovementMethod.getInstance());
            return dialog;
        }
    }

    public static Dialog create(Builder builder) {
        return builder.build();
    }

}
