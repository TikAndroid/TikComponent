package com.tik.android.component.market.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.tik.android.component.bussiness.market.IChartView;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.presenter.ChartViewPresenter;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.FormatUtils;
import com.tik.android.component.market.util.ResourceUtils;
import com.tik.android.component.market.widget.chart.BarAXisValueFormatter;
import com.tik.android.component.market.widget.chart.FingerTouchListener;
import com.tik.android.component.market.widget.chart.HoxCandleChartRenderer;
import com.tik.android.component.market.widget.chart.HoxChartTouchListener;
import com.tik.android.component.market.widget.chart.HoxLineChartRenderer;
import com.tik.android.component.market.widget.chart.MyBarDataSet;
import com.tik.android.component.market.widget.chart.SyncChartGestureListener;
import com.tik.android.component.widget.HoxTabLayout;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class StockChartView extends LinearLayout implements FingerTouchListener.HighlightListener, HoxLineChartRenderer.CrossIconBitmapCallback,
        HoxCandleChartRenderer.CrossIconBitmapCallback, ChartViewPresenter.IStockChartView, IChartView,HoxChartTouchListener.ITransChangedListener {
    private Context mContext;
    @BindView(R2.id.line_chart)
    LineChart mLineChart;
    @BindView(R2.id.bar_chart)
    BarChart mBarChart;
    @BindView(R2.id.candle_chart)
    CandleStickChart mCandleChart;
    ViewStub mTmpViewStub;
    @BindColor(R2.color.market_color_bar_chart_positive)
    int mPositiveColor;
    @BindColor(R2.color.market_color_bar_chart_negative)
    int mNegativeColor;
    @BindColor(R2.color.market_color_kline_line_path)
    int mLinePathColor;
    int mLineHighLineColor = Color.GRAY;
    float mLineWidth = 0.8f;
    float mHighLineWidth = 0.34f;
    @BindView(R2.id.chart_tab_layout)
    HoxTabLayout mTabLayout;
    @BindView(R2.id.chart_mode_btn)
    ImageButton mModeBtn;
    private int[] mHighLineIds = new int[]{
            R.id.tmp_high_line_time, R.id.tmp_high_line_open_close, R.id.tmp_high_line_high_low, R.id.tmp_high_line_volume_gains
    };
    List<TextView> mHighLineViews = new ArrayList<>();
    private int mChartType;
    private int mTmpM5Type = Constant.TYPE_TIME_SHARING_M5;//下拉控件默认选中type
    private String mSymbol;
    private XAxis mLineXAxis;
    private XAxis mBarXAxis;
    private Bitmap mCrossBitmap;
    private PopupChartSelectWindow mPopupChartSelectWindow;
    private BarAXisValueFormatter mBarAXisValueFormatter;
    private ChartViewPresenter mChartPresenter;
    private String[] mTypeItems ;
    private View mTmpHighLineView;
    private boolean mIsInitChart;
    private XAxis mCandleXAxis;
    private boolean mIsCandleMode;
    private int mScaleEdge;

    public StockChartView(Context context) {
        this(context, null);
    }

    public StockChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.market_view_stock_chart, this, true);
        ButterKnife.bind(this);
        initTabLayout();
        mChartPresenter = new ChartViewPresenter(this);
        if (mIsCandleMode) {
            mCandleChart.setVisibility(VISIBLE);
            mLineChart.setVisibility(GONE);
            mModeBtn.setSelected(true);
        }
    }

    @SuppressLint("CheckResult")
    private void initChartMode() {
        Observable<Object> observable = RxView.clicks(this).share();
        observable.observeOn(AndroidSchedulers.mainThread())
                .buffer(observable.debounce(300, TimeUnit.MILLISECONDS))
                .map(list -> list.size())
                .compose(RxLifecycleAndroid.bindView(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer == 3) {
                        changedChartMode();
                    }
                });
        RxView.clicks(mModeBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .compose(RxLifecycleAndroid.bindView(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> changedChartMode());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initChartMode();
    }

    private void changedChartMode() {
        if (mChartType == Constant.TYPE_TIME_SHARING_M1) {
            if (mLineChart.getVisibility() != VISIBLE) {
                mCandleChart.setVisibility(GONE);
                mLineChart.setVisibility(VISIBLE);
            }
            return;
        }
        if (mIsCandleMode) {
            mIsCandleMode = false;
            mCandleChart.setVisibility(GONE);
            mLineChart.setVisibility(VISIBLE);
        } else {
            mIsCandleMode = true;
            mCandleChart.setVisibility(VISIBLE);
            mLineChart.setVisibility(GONE);
        }
        mModeBtn.setSelected(mIsCandleMode);
        OnChartGestureListener onChartGestureListener = mBarChart.getOnChartGestureListener();
        if (onChartGestureListener instanceof SyncChartGestureListener) {
            ((SyncChartGestureListener) onChartGestureListener).syncCharts();
        }
    }

    private void initTabLayout() {
        Resources res = getResources();
        mTypeItems = new String[]{
                res.getString(R.string.market_string_chart_default_k),
                res.getString(R.string.market_string_chart_day_k),
                res.getString(R.string.market_string_chart_week_k),
                res.getString(R.string.market_string_chart_month_k)
        };
        mTabLayout.setItems(mTypeItems);
        RightDrawCenterTextView textView = new RightDrawCenterTextView(getContext());
        Drawable drawable = res.getDrawable(R.drawable.market_selector_type_selection_arrow_bg);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        textView.setCompoundDrawablePadding(res.getDimensionPixelSize(R.dimen.market_chart_tab_drawable_padding));
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        textView.setText(res.getString(R.string.market_string_5_minute));
        mTabLayout.addItemView(textView,1);
        mTabLayout.setOnItemClickListener(new HoxTabLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView view, int index) {
                onClickBtn(view, index);
            }

            @Override
            public void OnItemClickAnimEnd(TextView view, int index, boolean cancel) {

            }
        });
    }

    @SuppressLint("CheckResult")
    @NonNull
    private void initClicksObservable() {
//        Observable.merge(RxView.clicks(mBtnViews[0]).map(o -> mBtnViews[0]), RxView.clicks(mBtnViews[2]).map(o -> mBtnViews[2]),
//                RxView.clicks(mBtnViews[3]).map(o -> mBtnViews[3]), RxView.clicks(mBtnViews[4]).map(o -> mBtnViews[4]))
//                .throttleFirst(300, TimeUnit.MILLISECONDS)
//                .compose(RxLifecycleAndroid.bindView(this))
//                .subscribe(textView -> onClickBtn(textView));
    }

    private void initBarChart() {
        mBarChart.setDrawBorders(false);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setDragEnabled(true);
        mBarChart.animateX(Constant.DEFAULT_CHART_LINE_ANIM_TIME);
        mBarChart.setBackgroundColor(Color.TRANSPARENT);
        mBarChart.setDragDecelerationFrictionCoef(Constant.DEFAULT_CHART_FRICTION_COEF);
        mBarChart.setAutoScaleMinMaxEnabled(true);
        Description description = new Description();
        description.setEnabled(false);
        mBarChart.setDescription(description);
        mBarChart.setTouchEnabled(true);
        mBarXAxis = mBarChart.getXAxis();
        mBarXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarXAxis.setDrawGridLines(false);
        mBarXAxis.setTextColor(getResources().getColor(R.color.text_little_label));
        mBarXAxis.setLabelCount(5,true);
        mBarXAxis.setAvoidFirstLastClipping(true);
        mBarXAxis.setDrawAxisLine(false);
        mBarChart.setNoDataText("");
        mBarChart.setHighlightPerTapEnabled(false);
        YAxis axisLeft = mBarChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisLineColor(Color.TRANSPARENT);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setSpaceTop(30);
        axisLeft.setSpaceBottom(10);
        axisLeft.setValueFormatter((value, axis) -> "");
        YAxis axisRight = mBarChart.getAxisRight();
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRight.setEnabled(false);
        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mLineChart.highlightValue(h);
                showTmpHighLineInfoView(e);
            }

            @Override
            public void onNothingSelected() {
                mLineChart.highlightValue(null);
            }
        });
        mBarChart.setOnChartGestureListener(new SyncChartGestureListener(mBarChart, new Chart[]{mLineChart, mCandleChart}));
        mBarChart.setOnTouchListener(new FingerTouchListener(mBarChart,this));
        mBarChart.setMinOffset(0);
        mBarChart.setExtraBottomOffset(5);
    }

    private void showTmpHighLineInfoView(Entry e) {
        String formatTime = "";
        KLineData data = (KLineData) e.getData();
        if (mBarAXisValueFormatter != null && data != null) {
            formatTime = mBarAXisValueFormatter.formatLabelTime((int) e.getX(), data);
        }
        inflateHighLighMarkViewIfNeed();
        if (data == null) {
            mTmpHighLineView.setVisibility(GONE);
            return;
        }
        mChartPresenter.updateHighLineMarkViewInfo(data, formatTime, mHighLineViews);
        if (mTmpHighLineView.getVisibility() != VISIBLE) {
            mTmpHighLineView.setVisibility(VISIBLE);
        }
    }

    private void inflateHighLighMarkViewIfNeed() {
        if (mTmpHighLineView == null) {
            mTmpViewStub = (ViewStub) findViewById(R.id.tmp_view_stub);
            mTmpViewStub.inflate();
            mTmpHighLineView = findViewById(R.id.tmp_high_line_info);
            for (int id : mHighLineIds) {
                TextView view = (TextView) findViewById(id);
                mHighLineViews.add(view);
            }
            mTmpViewStub = null;
        }
    }

    private void initLineChart() {
        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        mLineChart.setBackgroundColor(Color.TRANSPARENT);
        mLineChart.setDragEnabled(true);
        mLineChart.setTouchEnabled(true);
        Description description = new Description();
        description.setEnabled(false);
        mLineChart.setDescription(description);
        //设置和barchart同样的label但是不显示 保证坐标轴基线一致
        mLineXAxis = mLineChart.getXAxis();
        mLineXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineXAxis.setTextColor(Color.TRANSPARENT);
        mLineXAxis.setDrawGridLines(false);
        mLineXAxis.setDrawAxisLine(false);
        //保证lineChart和barChart的起始点一致
        mLineXAxis.setAxisMinimum(-0.5f);
        mLineChart.setAutoScaleMinMaxEnabled(true);
        YAxis axisLeft = mLineChart.getAxisLeft();
        axisLeft.setSpaceBottom(25);
        axisLeft.setSpaceTop(10);
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisLineColor(Color.TRANSPARENT);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setValueFormatter((value, axis) -> "");
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setScaleYEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setNoDataText(getResources().getString(R.string.market_string_chart_data_null));
        mLineChart.setNoDataTextColor(Color.GRAY);
        mLineChart.setHighlightPerTapEnabled(false);
        mLineChart.setDragDecelerationFrictionCoef(Constant.DEFAULT_CHART_FRICTION_COEF);

        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showTmpHighLineInfoView(e);
                mBarChart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                if (mTmpHighLineView != null && mTmpHighLineView.getVisibility() == VISIBLE) {
                    mTmpHighLineView.setVisibility(GONE);
                }
                mBarChart.highlightValue(null);
            }
        });
        mLineChart.setOnChartGestureListener(new SyncChartGestureListener(mLineChart, new Chart[]{mBarChart, mCandleChart}));
        mLineChart.setOnTouchListener(new FingerTouchListener(mLineChart,this));
        HoxLineChartRenderer hoxLineChartRenderer = new HoxLineChartRenderer(mLineChart, mLineChart.getAnimator(), mLineChart.getViewPortHandler());
        hoxLineChartRenderer.setCrossIconBitmapCallback(this);
        mLineChart.setRenderer(hoxLineChartRenderer);
        mLineChart.setOnTouchListener(new HoxChartTouchListener(mLineChart,this));
        mLineChart.setMinOffset(0);
        mLineChart.setExtraBottomOffset(5);
        mLineChart.setData(new LineData());
    }

    private void initCandleChart() {
        mCandleChart.setDrawGridBackground(false);
        mCandleChart.setDrawBorders(false);
        mCandleChart.setBackgroundColor(Color.TRANSPARENT);
        mCandleChart.setDragEnabled(true);
        mCandleChart.setTouchEnabled(true);
        Description description = new Description();
        description.setEnabled(false);
        mCandleChart.setDescription(description);
        //设置和barchart同样的label但是不显示 保证坐标轴基线一致
        mCandleXAxis = mCandleChart.getXAxis();
        mCandleXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mCandleXAxis.setTextColor(Color.TRANSPARENT);
        mCandleXAxis.setDrawGridLines(false);
        mCandleXAxis.setDrawAxisLine(false);

        mCandleChart.setAutoScaleMinMaxEnabled(true);
        YAxis axisLeft = mCandleChart.getAxisLeft();
        axisLeft.setSpaceBottom(25);
        axisLeft.setSpaceTop(10);
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisLineColor(Color.TRANSPARENT);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setValueFormatter((value, axis) -> "");
        mCandleChart.getAxisRight().setEnabled(false);
        mCandleChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mCandleChart.getLegend().setEnabled(false);
        mCandleChart.setScaleYEnabled(false);
        mCandleChart.setDoubleTapToZoomEnabled(false);
        mCandleChart.setNoDataText(getResources().getString(R.string.market_string_chart_data_null));
        mCandleChart.setNoDataTextColor(Color.GRAY);
        mCandleChart.setHighlightPerTapEnabled(false);
        mCandleChart.setDragDecelerationFrictionCoef(Constant.DEFAULT_CHART_FRICTION_COEF);

        mCandleChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showTmpHighLineInfoView(e);
                mBarChart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                if (mTmpHighLineView != null && mTmpHighLineView.getVisibility() == VISIBLE) {
                    mTmpHighLineView.setVisibility(GONE);
                }
                mBarChart.highlightValue(null);
            }
        });
        mCandleChart.setOnChartGestureListener(new SyncChartGestureListener(mCandleChart, new Chart[]{mBarChart, mLineChart}));
        mCandleChart.setOnTouchListener(new FingerTouchListener(mCandleChart,this));
        HoxCandleChartRenderer hoxCandleChartRenderer = new HoxCandleChartRenderer(mCandleChart, mCandleChart.getAnimator(), mCandleChart.getViewPortHandler());
        hoxCandleChartRenderer.setCrossIconBitmapCallback(this);
        mCandleChart.setOnTouchListener(new HoxChartTouchListener(mCandleChart,this));
        mCandleChart.setRenderer(hoxCandleChartRenderer);
        mCandleChart.setMinOffset(0);
        mCandleChart.setExtraBottomOffset(5);
        mCandleChart.setData(new CandleData());
    }

    @Override
    public void refreshContent(List<Entry> lineEntries, List<BarEntry> barEntries, List<CandleEntry> candleEntries, String symbol, int type) {
        mSymbol = symbol;
        updateSelectType(type);
        mChartPresenter.saveChartType(type);
        initDataSet(lineEntries, barEntries, candleEntries);
    }

    private void updateSelectType(int type) {
        mChartType = type;
        boolean enableScale = type != Constant.TYPE_TIME_SHARING_M1;
        mBarChart.setScaleXEnabled(enableScale);
        mLineChart.setScaleXEnabled(enableScale);
        mModeBtn.setEnabled(enableScale);
        int itemIndex = mChartPresenter.getItemIndexByType(type);
        mTabLayout.setSelectItem(itemIndex);
    }

    private void initDataSet(List<Entry> lineEntries, final List<BarEntry> barEntries, List<CandleEntry> candleEntries) {
        if (barEntries == null || barEntries.isEmpty()) {
            mLineChart.clear();
            mBarChart.clear();
            mCandleChart.clear();
            return;
        }
        int listSize = barEntries.size();
        if (listSize > Constant.DEFAULT_CHART_LINE_COUNT) {
            mScaleEdge = HoxChartTouchListener.TRANS_EDGE_FREE;
            mBarXAxis.resetAxisMaximum();
            mCandleXAxis.resetAxisMaximum();
            mLineXAxis.setAxisMaximum(listSize - 0.5f);
        } else {
            mScaleEdge = HoxChartTouchListener.TRANS_EDGE_LEFT;
            mBarXAxis.setAxisMaximum(Constant.DEFAULT_CHART_LINE_COUNT);
            mLineXAxis.setAxisMaximum(Constant.DEFAULT_CHART_LINE_COUNT);
            mCandleXAxis.setAxisMaximum(Constant.DEFAULT_CHART_LINE_COUNT);
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");
        lineDataSet.setColor(mLinePathColor);
        lineDataSet.setLineWidth(mLineWidth);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawIcons(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.enableDashedHighlightLine(10, 15, 0);
        lineDataSet.setHighLightColor(mLineHighLineColor);
        lineDataSet.setHighlightLineWidth(mHighLineWidth);
        LineData lineData = new LineData(lineDataSet);
        //初始化BarChart数据
        MyBarDataSet barDataSet = new MyBarDataSet(barEntries, "");
        barDataSet.setColors(new int[]{mPositiveColor, mNegativeColor});
        barDataSet.setHighLightAlpha(80);
        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);
        mBarAXisValueFormatter = getBarAxisValueFormatter(barEntries);
        mBarXAxis.setValueFormatter(mBarAXisValueFormatter);

        mLineChart.animateX(Constant.DEFAULT_CHART_LINE_ANIM_TIME);
        mBarChart.animateX(Constant.DEFAULT_CHART_LINE_ANIM_TIME);

        //重置放大倍数，避免下一次刷新时被前一次放大倍数限制
        mLineChart.getViewPortHandler().setMaximumScaleX(0.f);
        mBarChart.getViewPortHandler().setMaximumScaleX(0.f);

        mLineChart.setData(lineData);
        mBarChart.setData(barData);

        float scaleRatio = listSize / Constant.DEFAULT_CHART_LINE_COUNT;
        if (mChartType == Constant.TYPE_TIME_SHARING_M1) {
            scaleRatio = 1.0f;
        }
        Matrix m = new Matrix();
        m.postScale(scaleRatio, 1f);
        mLineChart.getViewPortHandler().refresh(m, mLineChart, false);
        mBarChart.getViewPortHandler().refresh(m, mBarChart, false);

        mBarChart.moveViewToX(listSize );
        mLineChart.moveViewToX(listSize);

        mBarChart.setVisibleXRangeMinimum(Constant.MIN_CHART_LINE_COUNT);
        mLineChart.setVisibleXRangeMinimum(Constant.MIN_CHART_LINE_COUNT);

        if (candleEntries != null && !candleEntries.isEmpty()) {
            CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "");
            candleDataSet.setColor(mLinePathColor);
            candleDataSet.setDrawIcons(false);
            candleDataSet.setDrawValues(false);
            candleDataSet.setHighLightColor(mLineHighLineColor);
            candleDataSet.setHighlightLineWidth(mHighLineWidth);
            candleDataSet.enableDashedHighlightLine(10,15,0);
            candleDataSet.setShadowColor(Color.DKGRAY);
            candleDataSet.setShadowColorSameAsCandle(true);
            candleDataSet.setShadowWidth(0.7f);
            candleDataSet.setDecreasingColor(ResourceUtils.getColorRed());
            candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setIncreasingColor(ResourceUtils.getColorGreen());
            candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setNeutralColor(ResourceUtils.getColorRed());
            CandleData candleData = new CandleData(candleDataSet);
            mCandleChart.animateX(Constant.DEFAULT_CHART_LINE_ANIM_TIME);
            mCandleChart.getViewPortHandler().setMaximumScaleX(0.f);
            mCandleChart.setData(candleData);
            mCandleChart.getViewPortHandler().refresh(m, mCandleChart, false);
            mCandleChart.moveViewToX(listSize);
            mCandleChart.setVisibleXRangeMinimum(Constant.MIN_CHART_LINE_COUNT);
        }

    }

    private void setLimitLine(LineDataSet lineDataSet) {
        //设置均线
        float yMin = lineDataSet.getYMin();
        float yMax = lineDataSet.getYMax();
        LimitLine limitLine = new LimitLine((yMax + yMin) / 2, FormatUtils.formatValue((yMax + yMin) / 2) + "");
        limitLine.setLineWidth(mLineWidth);
        limitLine.setLineColor(getResources().getColor(R.color.market_color_chart_line_average));
        limitLine.setEnabled(true);
        limitLine.setTextColor(getResources().getColor(R.color.text_little_label));
        limitLine.enableDashedLine(5,3f,0);
        YAxis axisLeft = mLineChart.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setDrawGridLinesBehindData(true);
        axisLeft.addLimitLine(limitLine);
    }

    private BarAXisValueFormatter getBarAxisValueFormatter(List<BarEntry> barEntries) {
        return new BarAXisValueFormatter() {
            @Override
            public String calculateFormattedValue(int index) {
                if (index >= barEntries.size()) {
                    return "";
                }
                BarEntry barEntry = barEntries.get(index);
                Date time = ((KLineData) barEntry.getData()).getDate();
                return new SimpleDateFormat(mDisplayTimeFormat).format(time);
            }

            @Override
            public void needUpdateValueRange() {
                int highestVisibleX = (int) mBarChart.getHighestVisibleX();
                int lowestVisibleX = (int) mBarChart.getLowestVisibleX();
                String displayTimeFormat = mChartPresenter.getRangeTimeFormat(highestVisibleX, lowestVisibleX, barEntries);
                if (!TextUtils.isEmpty(displayTimeFormat)) {
                    setDisplayTimeFormat(displayTimeFormat);
                }
            }
        };
    }

    @Override
    public void enableHighlight() {

    }

    @Override
    public void disableHighlight() {

    }

    @Override
    public Bitmap getCrossBitmap() {
        if (mCrossBitmap == null) {
            mCrossBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.stock_high_line_cross_icon);
        }
        return mCrossBitmap;
    }

    private void onClickBtn(TextView view, int index) {
        syncModeState(index);
        if (index == 1) {
            if (mChartType >= Constant.TYPE_TIME_SHARING_M5 && mChartType <= Constant.TYPE_TIME_SHARING_M60) {
                showPopupSelectWindow(view);
            } else {
                clearChart();
                mChartType = mTmpM5Type;
                mChartPresenter.refresh(mSymbol, mTmpM5Type);
            }
            return;
        }
        int tmpType = mChartPresenter.getTypeByItemIndex(index);
        LogUtil.i("onClick event !  tmpType = " + tmpType + "; mChartType = " + mChartType);
        if (mChartType == tmpType || tmpType == 0) {
            return;
        }
        clearChart();
        mChartType = tmpType;
        mChartPresenter.refresh(mSymbol, tmpType);
    }

    private void syncModeState(int index) {
        mModeBtn.setEnabled(index != 0);
        if (index == 0 && mLineChart.getVisibility() != VISIBLE) {
            mCandleChart.setVisibility(GONE);
            mLineChart.setVisibility(VISIBLE);
        } else if (mIsCandleMode && mCandleChart.getVisibility() != VISIBLE) {
            mCandleChart.setVisibility(VISIBLE);
            mLineChart.setVisibility(GONE);
        }
    }

    private void showPopupSelectWindow(final View view) {
        if (mPopupChartSelectWindow == null) {
            mPopupChartSelectWindow = new PopupChartSelectWindow(mContext);
            mPopupChartSelectWindow.setChartPopupListener(new PopupChartSelectWindow.ChartPopupListener() {
                @Override
                public void onItemClick(int type, String text) {
                    mTmpM5Type = type;
                    if (mChartType == type) {
                        return;
                    }
                    mChartType = type;
                    clearChart();
                    mChartPresenter.refresh(mSymbol, type);
                    ((TextView) view).setText(text);
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onDismiss() {
                    mChartPresenter.resetViewClickable(view);
                }
            });
        }
        view.setClickable(false);
        mPopupChartSelectWindow.setSelectItem(mTmpM5Type);
        mPopupChartSelectWindow.showAsDropDown(view, (view.getWidth() - mPopupChartSelectWindow.getWidth()) / 2, 30);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCrossBitmap != null) {
            mCrossBitmap.recycle();
        }
        MoveViewJob.getInstance(null, 0, 0, null, null);
        MoveViewJob.getInstance(null, 0, 0, null, null);
    }

    public void clearChart() {
        if (mBarChart.getData() != null) {
            mBarChart.clearValues();
        } else {
            mBarChart.setData(new BarData());
        }
        if (mLineChart.getData() != null) {
            mLineChart.clearValues();
        } else {
            mLineChart.setData(new LineData());
        }

        if (mCandleChart.getData() != null) {
            mCandleChart.clearValues();
        } else {
            mCandleChart.setData(new CandleData());
        }
    }

    @Override
    public void refreshChartView(String symbol) {
        clearChart();
        mSymbol = symbol;
        initChartIfNeed();
        mChartPresenter.refresh(symbol);
    }

    @Override
    public void refreshChartView() {
        refreshChartView(mSymbol);
    }

    @Override
    public void refreshChartView(String symbol, IChartView.RefreshListener listener) {
        clearChart();
        mSymbol = symbol;
        initChartIfNeed();
        mChartPresenter.refresh(symbol, listener);
    }

    @Override
    public void refreshChartView(IChartView.RefreshListener listener) {
        refreshChartView(mSymbol, listener);
    }

    private void initChartIfNeed() {
        if (!mIsInitChart) {
            initLineChart();
            initBarChart();
            initCandleChart();
            mIsInitChart = true;
        }
    }

    @Override
    public int getTransEdge() {
        return mScaleEdge;
    }
}
