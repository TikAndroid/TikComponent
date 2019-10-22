package com.tik.android.component.market.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.market.IChartView.RefreshListener;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.libcommon.SpannableStringFormat;
import com.tik.android.component.libcommon.sharedpreferences.IRxSharedPrefer;
import com.tik.android.component.libcommon.sharedpreferences.RxSharedPrefer;
import com.tik.android.component.market.R;
import com.tik.android.component.market.bussiness.ChartDataRequester;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.FormatUtils;
import com.tik.android.component.market.util.ResourceUtils;

import org.reactivestreams.Publisher;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jianglixuan on 2018/11/22
 */
public class ChartViewPresenter {
    private NormalSubscriber<List[]> mChartSubscriber;
    private IStockChartView mCallback;
    private int[] mBtnStringId = {R.string.market_string_chart_default_k, R.string.market_string_5_minute, R.string.market_string_15_minute, R.string.market_string_30_minute,
            R.string.market_string_60_minute, R.string.market_string_chart_day_k, R.string.market_string_chart_week_k, R.string.market_string_chart_month_k};

    public ChartViewPresenter(IStockChartView callback) {
        mCallback = callback;
    }

    public void refresh(final String symbol, final int type) {
        refresh(symbol, type, null);
    }

    private void refresh(final String symbol, final int type, final RefreshListener listener) {
        saveChartType(type);
        ChartDataRequester.getKLineData(symbol, type, mCallback.getContext(), getSubscriber(symbol, type, listener));
    }

    @NonNull
    private NormalSubscriber<List[]> getSubscriber(final String symbol, final int type, final RefreshListener listener) {
        if (mChartSubscriber != null && !mChartSubscriber.isDisposed()) {
            mChartSubscriber.dispose();
        }
        mChartSubscriber = new NormalSubscriber<List[]>() {
            @Override
            public void onNext(List[] lists) {
                if (lists == null || lists.length != 3) {
                    onError(0, null);
                    return;
                }
                if (listener != null) {
                    listener.onRefreshEnd();
                }
                mCallback.refreshContent(lists[0], lists[1], lists[2], symbol, type);
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (listener != null) {
                    listener.onRefreshEnd();
                }
                mCallback.refreshContent(null, null, null, symbol, type);
            }
        };
        return mChartSubscriber;
    }

    public void refresh(String symbol) {
        refresh(symbol, null);
    }

    public void refresh(final String symbol, final RefreshListener listener) {
        RxSharedPrefer.builder()
                .context(mCallback.getContext())
                .build().readAsFlowable()
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.bindToLifecycle(mCallback.getContext()))
                .flatMap(new Function<SharedPreferences, Publisher<Integer>>() {
                    @Override
                    public Publisher<Integer> apply(SharedPreferences sharedPreferences) throws Exception {
                        return Flowable.just(sharedPreferences.getInt(Constant.KEY_CHART_TYPE, Constant.TYPE_TIME_SHARING_M1));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<Integer>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        if (listener != null) {
                            listener.onRefreshEnd();
                        }
                    }

                    @Override
                    public void onNext(Integer type) {
                        refresh(symbol, type, listener);
                    }
                });
    }

    public void saveChartType(final int type) {
        RxSharedPrefer.builder().context(mCallback.getContext()).build()
                .editAsAsyncFlowable(new IRxSharedPrefer.Consumer<SharedPreferences.Editor>() {
                    @Override
                    public void accept(SharedPreferences.Editor editor) {
                        editor.putInt(Constant.KEY_CHART_TYPE, type).apply();
                    }

                    @Override
                    public void error(String msg) {

                    }
                });
    }

    public int getTypeByItemIndex(int index) {
        int type;
        if (index == 0) {
            type = Constant.TYPE_TIME_SHARING_M1;
        } else if (index == 2) {
            type = Constant.TYPE_TIME_SHARING_DAY;
        } else if (index == 3) {
            type = Constant.TYPE_TIME_SHARING_WEEK;
        } else if (index == 4) {
            type = Constant.TYPE_TIME_SHARING_MONTH;
        } else {
            type = Constant.TYPE_TIME_SHARING_M5;
        }
        return type;
    }

    public interface IStockChartView {
        void refreshContent(List<Entry> lineEntries, List<BarEntry> barEntries, List<CandleEntry> candleEntries, String symbol, int type);

        Context getContext();
    }

    public int getItemIndexByType(int type) {
        int index;
        if (type == Constant.TYPE_TIME_SHARING_DAY) {
            index = 2;
        } else if (type == Constant.TYPE_TIME_SHARING_M1) {
            index = 0;
        } else if (type == Constant.TYPE_TIME_SHARING_WEEK) {
            index = 3;
        } else if (type == Constant.TYPE_TIME_SHARING_MONTH) {
            index = 4;
        } else {
            index = 1;
        }
        return index;
    }

    public String getTextByType(int type) {
        if (type < Constant.TYPE_TIME_SHARING_M1 || type > Constant.TYPE_TIME_SHARING_MONTH) {
            return null;
        }
        return mCallback.getContext().getResources().getString(mBtnStringId[type - 1]);
    }

    public String getRangeTimeFormat(int highestVisibleX, int lowestVisibleX, List<BarEntry> barEntries) {
        if (highestVisibleX >= 0 && lowestVisibleX >= 0 && lowestVisibleX < barEntries.size() && highestVisibleX < barEntries.size()) {
            Date dateMin = ((KLineData) barEntries.get(lowestVisibleX).getData()).getDate();
            Date dateMax = ((KLineData) barEntries.get(highestVisibleX).getData()).getDate();
            long diffTime = dateMax.getTime() - dateMin.getTime();
            String displayTimeFormat;
            if (diffTime < Constant.MILLI_SECOND_2_DAY) {
                displayTimeFormat = Constant.TIME_SHARING_HH_MM;
            } else if (diffTime < Constant.MILLI_SECOND_1_YEAR) {
                displayTimeFormat = Constant.TIME_SHARING_MM_DD;
            } else {
                displayTimeFormat = Constant.TIME_SHARING_YY_MM;
            }
            return displayTimeFormat;
        }
        return null;
    }

    public void resetViewClickable(final View view) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                emitter.onNext(0l);
            }
        }).delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(mCallback.getContext()))
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        view.setClickable(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.setClickable(true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void updateHighLineMarkViewInfo(KLineData data, String formatTime,List<TextView> highLineViews) {
        String price = data.getPrice();
        if (TextUtils.isEmpty(formatTime)) {
            formatTime = data.getTime();
        }
        highLineViews.get(0).setText(SpannableStringFormat.createSpannableBuild(formatTime)
                .textColorSize(0, formatTime.length(), Color.parseColor("#23262F"), 13, true)
                .build());
        CharSequence spannableString1, spannableString2, spannableString3;
        if (TextUtils.isEmpty(price)) {
            spannableString1 = formatStringStyle(ResourceUtils.getStrPriceOpen() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatValue(data.getOpen()),
                    ResourceUtils.getStrPriceClose() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatValue(data.getClose()));
            spannableString2 = formatStringStyle(ResourceUtils.getStrPriceHigh() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatValue(data.getHight()),
                    ResourceUtils.getStrPriceLow() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatValue(data.getLow()));
            spannableString3 = formatStringStyle(ResourceUtils.getStrPriceVolume() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatVolume(data.getVolume()),
                    ResourceUtils.getStrSpace(),
                    ResourceUtils.getStrSpace());
        } else {
            spannableString1 = formatStringStyle(ResourceUtils.getStrCurrPrice() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatValue(price),
                    ResourceUtils.getStrPriceVolume() + ResourceUtils.getStrSpace(),
                    FormatUtils.formatVolume(data.getVolume()));
            spannableString2 = ResourceUtils.getStrSpace();
            spannableString3 = ResourceUtils.getStrSpace();
        }
        highLineViews.get(1).setText(spannableString1);
        highLineViews.get(2).setText(spannableString2);
        highLineViews.get(3).setText(spannableString3);
    }

    private SpannableString formatStringStyle(String prevSign, String prevValue, String nextSign, String nextValue) {
        String text = prevSign + prevValue + "\n" + nextSign + nextValue;
        int midLength = (prevSign + prevValue).length();
        return SpannableStringFormat.createSpannableBuild(text)
                .textColorSize(0, prevSign.length(), Color.parseColor("#A6AEBC"), 10, true)
                .textColorSize(midLength, text.length(), Color.parseColor("#A6AEBC"), 10, true)
                .textColorSize(prevSign.length(), midLength, Color.parseColor("#6F7385"), 12, true)
                .textColorSize(midLength + nextSign.length(), text.length(), Color.parseColor("#6F7385"), 12, true)
                .build();
    }
}
