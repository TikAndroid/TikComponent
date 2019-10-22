package com.tik.android.component.property.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.tik.android.component.bussiness.property.bean.CoinAssets;
import com.tik.android.component.bussiness.property.bean.Rate;
import com.tik.android.component.bussiness.property.bean.StockAssets;

import java.util.List;

/**
 * 1.总资产 = 证券资产 + 数字货币资产
 * 2.证券资产 = 港股持仓总价 + 美股持仓总价
 * 3.持仓盈亏 = 港股持仓总盈亏 + 美股持仓总盈亏
 * 4.盈亏比例 = 持仓盈亏/成本价
 *
 */
public class PropertyData implements Parcelable {
    private List<CoinAssets> coin;
    private Rate rate;
    private List<StockAssets> stock;
    private double totalAssets; //总资产
    private double stockAssets; //证券资产
    private double buyPower; //购买力
    private double profit; //持仓盈亏
    private double profitRatio ;//盈亏比例
    private double hkStockAssets; //港股持仓总价
    private double usStockAssets; //美股持仓总价
    private double freezeAssets; //冻结资产

    public PropertyData() {

    }

    protected PropertyData(Parcel in) {
        coin = in.createTypedArrayList(CoinAssets.CREATOR);
        rate = in.readParcelable(Rate.class.getClassLoader());
        stock = in.createTypedArrayList(StockAssets.CREATOR);
        totalAssets = in.readDouble();
        stockAssets = in.readDouble();
        buyPower = in.readDouble();
        profit = in.readDouble();
        profitRatio = in.readDouble();
        hkStockAssets = in.readDouble();
        usStockAssets = in.readDouble();
        freezeAssets = in.readDouble();
    }

    public static final Creator<PropertyData> CREATOR = new Creator<PropertyData>() {
        @Override
        public PropertyData createFromParcel(Parcel in) {
            return new PropertyData(in);
        }

        @Override
        public PropertyData[] newArray(int size) {
            return new PropertyData[size];
        }
    };

    public double getHkStockAssets() {
        return hkStockAssets;
    }

    public void setHkStockAssets(double hkStockAssets) {
        this.hkStockAssets = hkStockAssets;
    }

    public List<CoinAssets> getCoin() {
        return coin;
    }

    public void setCoin(List<CoinAssets> coin) {
        this.coin = coin;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public List<StockAssets> getStock() {
        return stock;
    }

    public void setStock(List<StockAssets> stock) {
        this.stock = stock;
    }

    public double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(double totalCurrency) {
        this.totalAssets = totalCurrency;
    }

    public double getStockAssets() {
        return stockAssets;
    }

    public void setStockAssets(double stockCurrency) {
        this.stockAssets = stockCurrency;
    }

    public double getBuyPower() {
        return buyPower;
    }

    public void setBuyPower(double digitalCurrency) {
        this.buyPower = digitalCurrency;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfitRatio() {
        return profitRatio;
    }

    public void setProfitRatio(double profitRatio) {
        this.profitRatio = profitRatio;
    }

    public double getUsStockAssets() {
        return usStockAssets;
    }

    public void setUsStockAssets(double usStockCurrency) {
        this.usStockAssets = usStockCurrency;
    }

    public double getFreezeAssets() {
        return freezeAssets;
    }

    public void setFreezeAssets(double freezeAssets) {
        this.freezeAssets = freezeAssets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(coin);
        dest.writeParcelable(rate, flags);
        dest.writeTypedList(stock);
        dest.writeDouble(totalAssets);
        dest.writeDouble(stockAssets);
        dest.writeDouble(buyPower);
        dest.writeDouble(profit);
        dest.writeDouble(profitRatio);
        dest.writeDouble(hkStockAssets);
        dest.writeDouble(usStockAssets);
        dest.writeDouble(freezeAssets);
    }
}
