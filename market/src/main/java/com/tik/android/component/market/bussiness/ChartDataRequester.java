package com.tik.android.component.market.bussiness;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Time;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.error.GlobalErrorUtil;
import com.tik.android.component.bussiness.market.IPriceDataCallback;
import com.tik.android.component.bussiness.market.bean.KLineData;
import com.tik.android.component.bussiness.market.bean.StockPriceInfo;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.market.StockApi;
import com.tik.android.component.market.bean.MarketBusinessHours;
import com.tik.android.component.market.bean.MarketBusinessHours.StockTargetBean.CloseDaysBean;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.FormatUtils;
import com.tik.android.component.market.util.ResourceUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 实时价格和k线数据准备的类
 * 异步获取处理数据后，callback在主线程将数据回调给用户。
 * <p>
 * Created by jianglixuan on 2018/11/23
 */
public class ChartDataRequester {
    /**
     * 实时价格数据
     *
     * @param symbol   股票关键字段
     * @param view     用于绑定Lifecycle的对象
     * @param callback
     */
    public static void getStockPrice(final String symbol, final Object view, final @NonNull IPriceDataCallback callback) {
        observe(ApiProxy.getInstance().getApi(StockApi.class).getCurrPriceInfo(symbol))
                .compose(RxUtils.bindToLifecycle(view))
                .observeOn(Schedulers.computation())
                .map(stockPriceInfoResult -> {
                    if (stockPriceInfoResult == null) {
                        return null;
                    }
                    StockPriceInfo priceInfo = stockPriceInfoResult.getData();
                    if (priceInfo == null) {
                        return null;
                    }
                    int marketStatus = getMarketStatus(priceInfo.getLastTime(), priceInfo.getCurrency());
                    priceInfo.setStatus(marketStatus);
                    return priceInfo;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<StockPriceInfo>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        callback.onError();
                    }

                    @Override
                    public void onNext(StockPriceInfo priceInfo) {
                        callback.onSuccess(priceInfo, symbol);
                    }
                });
    }

    /**
     * K线图数据
     *
     * @param symbol   股票关键字段
     * @param type     k线绘制类型
     * @param view     用于绑定Lifecycle的对象
     * @param subscriber
     */
    public static void getKLineData(final String symbol, final int type, final Object view, final @NonNull NormalSubscriber<List[]> subscriber) {
        observe(ApiProxy.getInstance().getApi(StockApi.class).getChartData(symbol, type))
                .compose(RxUtils.bindToLifecycle(view))
                .observeOn(Schedulers.computation())
                .map(listResult -> {
                    if (listResult == null) {
                        return null;
                    }
                    List<KLineData> list = listResult.getData();
                    if (list == null || list.isEmpty()) {
                        return null;
                    }
                    ArrayList<Entry> lineEntries = new ArrayList<>();
                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    ArrayList<CandleEntry> candleEntries = new ArrayList<>();
                    int formatDateType = FormatUtils.getFormatDateType(list.get(0).getTime());
                    for (int i = 0; i < list.size(); i++) {
                        KLineData data = list.get(i);
                        //数据准备阶段时将string时间格式化为date
                        Date date = FormatUtils.formatStringToTime(data.getTime(), Constant.SOURCE_TIME_STRING[formatDateType]);
                        data.setDate(date);
                        lineEntries.add(new Entry(i, Float.valueOf(data.getClose()), data));
                        barEntries.add(new BarEntry(i, Float.valueOf(data.getClose()), data));
                        if (type != Constant.TYPE_TIME_SHARING_M1) {
                            candleEntries.add(new CandleEntry(i, Float.valueOf(data.getHight()), Float.valueOf(data.getLow()), Float.valueOf(data.getOpen()), Float.valueOf(data.getClose()), data));
                        }
                    }
                    return new List[]{lineEntries, barEntries, candleEntries};
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(subscriber);
    }

    private static int getMarketStatus(String currTimeString, String currency) {
        int status = Constant.STATE_NONE;
        try {
            Time currTime = new Time();
            currTime.parse3339(currTimeString);
            Date currDate = new Date(currTime.year - 1900, currTime.month, currTime.monthDay, currTime.hour, currTime.minute, currTime.second);
            String day = currTime.year + "-" + (currTime.month + 1) + "-" + currTime.monthDay;
            MarketBusinessHours marketBusiness = ResourceUtils.getMarketBusiness(BaseApplication.getAPPContext());
            MarketBusinessHours.StockTargetBean hkd = Constant.HKD_STOCK_CURRENCY.equals(currency) ? marketBusiness.getHKD() : marketBusiness.getUSD();
            //基本开盘时间校对
            Date openTime = FormatUtils.formatStringToTime(day + " " + hkd.getOpen(), Constant.TIME_TITLE_FORMAT_BUSINESS);
            Date closeTime = FormatUtils.formatStringToTime(day + " " + hkd.getClose(), Constant.TIME_TITLE_FORMAT_BUSINESS);
            if (currDate.after(openTime) && currDate.before(closeTime)) {
                status = Constant.STATE_OPEN;
            } else {
                status = Constant.STATE_CLOSE;
            }
            //午休判断
            String lunchTime = hkd.getLunch();
            if (!TextUtils.isEmpty(lunchTime)) {
                String[] split = hkd.getLunch().split("-");
                if (split.length == 2) {
                    Date lunchTimeStart = FormatUtils.formatStringToTime(day + " " + split[0], Constant.TIME_TITLE_FORMAT_BUSINESS);
                    Date lunchTimeEnd = FormatUtils.formatStringToTime(day + " " + split[1], Constant.TIME_TITLE_FORMAT_BUSINESS);
                    if (currDate.after(lunchTimeStart) && currDate.before(lunchTimeEnd)) {
                        status = Constant.STATE_LUNCH;
                    }
                }
            }
            //周末判断
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currDate);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekDay == 1 || weekDay == 7 || weekDay == 0) {
                status = Constant.STATE_WEEKEND;
            }
            //特殊节假日判断
            List<CloseDaysBean> closeDays = hkd.getCloseDays();
            for (CloseDaysBean daysBean : closeDays) {
                Time HolidayTime = new Time(daysBean.getDate());
                if (HolidayTime.year == currTime.year && currTime.yearDay == HolidayTime.yearDay) {
                    if (daysBean.isFreeze()) {
                        status = Constant.STATE_HOLIDAY;
                    } else {
                        Date startClose = FormatUtils.formatStringToTime(day + " " + daysBean.getStartClose(), Constant.TIME_TITLE_FORMAT_BUSINESS);
                        Date endClose = FormatUtils.formatStringToTime(day + " " + daysBean.getEndClose(), Constant.TIME_TITLE_FORMAT_BUSINESS);
                        if (currDate.after(startClose) && currDate.before(endClose)) {
                            status = Constant.STATE_HOLIDAY_HALF;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    private static <T> Flowable<T> observe(Flowable<T> observable) {
        return observable
                .compose(GlobalErrorUtil.handleGlobalError())
                .compose(RxUtils.<T>rxSchedulerHelperForFlowable());
    }
}
