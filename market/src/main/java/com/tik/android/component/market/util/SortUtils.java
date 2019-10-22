package com.tik.android.component.market.util;

import com.tik.android.component.market.bean.StockFullList;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {
    /**
     * 将StockFullList按价格排序
     *
     * @param list
     * @param isAscending true时为升序，反之降序
     */
    public static void sortByPrice(List<StockFullList> list, final boolean isAscending) {
        Comparator<StockFullList> itemComparator = new Comparator<StockFullList>() {
            public int compare(StockFullList o1, StockFullList o2) {
                double diffValue = o1.getPrice() - o2.getPrice();
                if (diffValue == 0) {
                    return 0;
                }
                int coefficient = isAscending ? 1 : -1;
                return diffValue > 0 ? 1 * coefficient : -1 * coefficient;
            }
        };
        Collections.sort(list, itemComparator);
    }

    /**
     * 将StockFullList按涨跌幅排序
     *
     * @param list
     * @param isAscending true时为升序，反之降序
     */
    public static void sortByGains(List<StockFullList> list, final boolean isAscending) {
        Comparator<StockFullList> itemComparator = new Comparator<StockFullList>() {
            public int compare(StockFullList o1, StockFullList o2) {
                double diffValue = 0;
                NumberFormat percentInstance = NumberFormat.getPercentInstance();
                try {
                    diffValue = percentInstance.parse(o1.getGains()).doubleValue() * Constant.GAINS_SORT_FACTOR
                            - percentInstance.parse(o2.getGains()).doubleValue() * Constant.GAINS_SORT_FACTOR;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (diffValue == 0) {
                    return 0;
                }
                int coefficient = isAscending ? 1 : -1;
                return diffValue > 0 ? 1 * coefficient : -1 * coefficient;
            }
        };
        Collections.sort(list, itemComparator);
    }

    /**
     * 将StockFullList按价格排序
     *
     * @param list
     * @param isAscending true时为升序，反之降序
     */
    public static void sortByPriceMarketEntity(List<MarketStockEntity> list, final boolean isAscending) {
        Comparator<MarketStockEntity> itemComparator = new Comparator<MarketStockEntity>() {
            public int compare(MarketStockEntity o1, MarketStockEntity o2) {
                double diffValue = o1.getPrice() - o2.getPrice();
                if (diffValue == 0) {
                    return 0;
                }
                int coefficient = isAscending ? 1 : -1;
                return diffValue > 0 ? 1 * coefficient : -1 * coefficient;
            }
        };

        Collections.sort(list, itemComparator);
    }

    /**
     * 将StockFullList按涨跌幅排序
     *
     * @param list
     * @param isAscending true时为升序，反之降序
     */
    public static void sortByGainsMarketEntity(List<MarketStockEntity> list, final boolean isAscending) {
        Comparator<MarketStockEntity> itemComparator = new Comparator<MarketStockEntity>() {
            public int compare(MarketStockEntity o1, MarketStockEntity o2) {
                double diffValue = 0;
                NumberFormat percentInstance = NumberFormat.getPercentInstance();
                try {
                    diffValue = percentInstance.parse(o1.getGains()).doubleValue() * Constant.GAINS_SORT_FACTOR
                            - percentInstance.parse(o2.getGains()).doubleValue() * Constant.GAINS_SORT_FACTOR;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (diffValue == 0) {
                    return 0;
                }
                int coefficient = isAscending ? 1 : -1;
                return diffValue > 0 ? 1 * coefficient : -1 * coefficient;
            }
        };

        Collections.sort(list, itemComparator);
    }
}
