package com.tik.android.component.bussiness.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.bussiness.base.R;
import com.tik.android.component.bussiness.base.R2;
import com.tik.android.component.libcommon.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public class WebViewFragment extends BasicFragment {

    @BindView(R2.id.webview)
    WebView webView;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;

    public static WebViewFragment newInstance() {
        Bundle args = new Bundle();

        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayout() {
        return R.layout.base_fragmeng_webview;
    }


    @Override
    protected void init(View view) {
        super.init(view);
        Bundle args = getArguments();
        String url = "";
        String title = "";
        if (args != null) {
            url = args.getString(Constants.WebView.URL, Constants.WebView.Page.PAGE_DEFAULT_URL);
            title = args.getString(Constants.WebView.TITLE);
        }
        initToolbar(title);
        initWebView(url);
    }

    private void initToolbar(String title) {
        this.title.setText(title);
    }

    private void initWebView(String url) {
        webView.loadUrl(TextUtils.isEmpty(url) ? Constants.WebView.Page.PAGE_DEFAULT_URL : url);
    }

    @OnClick(R2.id.back)
    public void goBack() {
        onBackPressed();
    }
}
