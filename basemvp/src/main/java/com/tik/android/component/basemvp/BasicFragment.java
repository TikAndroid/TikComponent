package com.tik.android.component.basemvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tik.android.component.widget.Config;

import butterknife.ButterKnife;
import me.jessyan.autosize.AutoSizeConfig;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * This basic fragment handles:
 * <p>
 * 1.ButterKnife view binding.
 * 2.Default page.
 */
public abstract class BasicFragment extends RxFragment implements BaseView {

    /**
     * 代理子fragment的startForResult时保存的子fragment对象，用于回调onFragmentResult
     */
    private BasicFragment mWaitingForResult;
    private View rootView;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(initLayout(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    public abstract int initLayout();

    protected void init(View view) {
    }

    public void onBackPressed() {
        if (_mActivity != null) {
            _mActivity.onBackPressed();
        }
    }

    public Fragment getRootFragment() {
        Fragment root = this;
        while (root.getParentFragment() != null) {
            root = root.getParentFragment();
        }

        return root;
    }

    /**
     * 以根fragment执行start
     *
     * @param toFragment
     */
    public void startWithRoot(ISupportFragment toFragment) {
        Fragment root = getRootFragment();
        if (root != this && root instanceof SupportFragment) {
            ((SupportFragment) root).start(toFragment);
        }
    }

    /**
     * 以根fragment执行startForResult
     *
     * @param toFragment
     * @param requestCode
     */
    public void startForResultWithRoot(ISupportFragment toFragment, int requestCode) {
        Fragment root = getRootFragment();
        if (root != this && root instanceof BasicFragment) {
            ((BasicFragment) root).startForResultFromChild(this, toFragment, requestCode);
        } else {
            super.startForResult(toFragment, requestCode);
        }
    }

    private void startForResultFromChild(BasicFragment from, ISupportFragment to, int request) {
        mWaitingForResult = from;
        super.startForResult(to, request);
    }

    @CallSuper
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (mWaitingForResult != null) {
            mWaitingForResult.onFragmentResult(requestCode, resultCode, data);
            mWaitingForResult = null;
        } else {
            super.onFragmentResult(requestCode, resultCode, data);
        }
    }

    public void showLoadingDialog(CharSequence text) {
        showLoadingDialog(text, ProgressDialog.STYLE_SPINNER);
    }

    /**
     * @param text
     * @param style
     */
    public void showLoadingDialog(CharSequence text, int style) {
        getProgressDialog();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(style);
        mProgressDialog.setMessage(text);
        if (Config.OPEN_AUTOSIZE) {
            AutoSizeConfig.getInstance().stop(getActivity());
        }

        mProgressDialog.show();
    }

    public void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }

        if (Config.OPEN_AUTOSIZE) {
            AutoSizeConfig.getInstance().restart();
        }
    }

    protected ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        return mProgressDialog;
    }
}
