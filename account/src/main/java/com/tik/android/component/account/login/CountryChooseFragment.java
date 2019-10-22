package com.tik.android.component.account.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.login.bean.Country;
import com.tik.android.component.account.login.multitype.CountryItemViewBinder;
import com.tik.android.component.account.login.multitype.SpecialItemViewBinder;
import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.libcommon.CollectionUtils;
import com.tik.android.component.libcommon.JsonUtil;
import com.tik.android.component.libcommon.SpannableBuilder;
import com.tik.android.component.libcommon.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @describe : 国家选择界面
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/14.
 */
public class CountryChooseFragment extends BasicFragment {
    public static final String TAG = "CountryChooseFragment";

    public static final String RESULT_COUNTRY = "result_country";

    public static CountryChooseFragment newInstance() {
        return new CountryChooseFragment();
    }

    @BindView(R2.id.input_search)
    public EditText mInputSearch;

    @BindView(R2.id.recycler)
    public RecyclerView mRecycler;

    private MultiTypeAdapter mAdapter;

    private List<Country> mAllCountries;

    private Consumer<Country> mOnCountryClickListener = country -> {
        Bundle data = new Bundle();
        data.putParcelable(RESULT_COUNTRY, country);
        setFragmentResult(RESULT_OK, data);
        pop();
    };

    @Override
    protected void init(View view) {
        super.init(view);
        initRecyclerView();

        RxTextView.textChanges(mInputSearch)
                .toFlowable(BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(this))
                .observeOn(Schedulers.computation(), false, 1)
                .skipWhile(sequence -> mAllCountries == null)
                .map(search -> {
                    List results;
                    boolean empty = TextUtils.isEmpty(search);
                    if (empty) {
                        results = mAllCountries;
                    } else {
                        results = getSearchedCountryItems(String.valueOf(search));
                    }

                    if (CollectionUtils.isBlank(results)) {
                        results.add(SpecialItemViewBinder.SpecialMultiType.EMPTY);
                    }

                    mAdapter.setItems(results);
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mInputSearch.requestFocus();
    }

    private void initRecyclerView() {
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Country.class, new CountryItemViewBinder(mOnCountryClickListener));
        mAdapter.register(SpecialItemViewBinder.SpecialMultiType.class, new SpecialItemViewBinder());

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecycler.addItemDecoration(divider);

        RxUtils.async(this, () -> JsonUtil.json2object(
                StringUtils.readString(getResources().openRawResource(R.raw.account_country)),
                new TypeToken<List<Country>>() {}.getType()),
                (RxUtils.Main<List<Country>>) countries -> {
                    mAllCountries = countries;
                    mAdapter.setItems(countries);
                    mRecycler.setAdapter(mAdapter);
                });
    }

    @Override
    public int initLayout() {
        return R.layout.account_country_choose;
    }

    @OnClick({R2.id.title_btn_left})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_btn_left) {
            onBackPressed();
        }
    }

    private List getSearchedCountryItems(String search) {
        List<CountryItemViewBinder.CountryItem> items = new ArrayList<>();
        for (Country country : mAllCountries) {
            boolean matched = false;
            CharSequence name = country.getCountry();
            CharSequence code = country.getCode();
            CountryItemViewBinder.CountryItem item = new CountryItemViewBinder.CountryItem(name, code);
            List<int[]> indexs = StringUtils.indexOfAll(name, search, true);
            if (indexs.size() > 0) {
                matched = true;
                item.setCountry(getSpannableString(name, indexs));
            }

            indexs = StringUtils.indexOfAll(code, search, true);
            if (indexs.size() > 0) {
                matched = true;
                item.setCode(getSpannableString(code, indexs));
            }

            if (matched) {
                items.add(item);
            }
        }

        return items;
    }

    private CharSequence getSpannableString(CharSequence sequence, List<int[]> indexs) {
        SpannableBuilder builder = SpannableBuilder.create(String.valueOf(sequence));
        for (int[] index : indexs) {
            builder.span(index[0], index[1], new ForegroundColorSpan(Color.RED));
        }

        return builder.build();
    }
}
