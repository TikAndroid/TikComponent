package com.tik.android.component.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StockFullList implements Parcelable {
    /**
     * feeRatio : 0.002
     * step : 100
     * _id : 5b4c57d0bfe11e3ad8af8ddd
     * symbol : 00700
     * name : Tencent
     * currency : HKD
     * logo : https://dove8023.oss-cn-beijing.aliyuncs.com/Tencent%402x.png
     * price : 308.4
     * createdAt : 1533022624383
     * updatedAt : 1533022624383
     * displayPrice : 317.2
     * openPrice : 312
     * bourse : 香港联合交易所,SEHK
     * gains : -1.39%
     * gainsValue : -5
     * lastTime : 2018-08-24 16:08:00
     */

    private double feeRatio;
    private int step;
    @SerializedName("_id")
    private String id;
    private String symbol;
    private String name;
    private String currency;
    private String logo;
    private double price;
    private long createdAt;
    private long updatedAt;
    private double displayPrice;
    private float openPrice;
    private String bourse;
    private String gains;
    private String gainsValue;
    private String lastTime;

    protected StockFullList(Parcel in) {
        feeRatio = in.readDouble();
        step = in.readInt();
        id = in.readString();
        symbol = in.readString();
        name = in.readString();
        currency = in.readString();
        logo = in.readString();
        price = in.readDouble();
        createdAt = in.readLong();
        updatedAt = in.readLong();
        displayPrice = in.readDouble();
        openPrice = in.readFloat();
        bourse = in.readString();
        gains = in.readString();
        gainsValue = in.readString();
        lastTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(feeRatio);
        dest.writeInt(step);
        dest.writeString(id);
        dest.writeString(symbol);
        dest.writeString(name);
        dest.writeString(currency);
        dest.writeString(logo);
        dest.writeDouble(price);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
        dest.writeDouble(displayPrice);
        dest.writeFloat(openPrice);
        dest.writeString(bourse);
        dest.writeString(gains);
        dest.writeString(gainsValue);
        dest.writeString(lastTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StockFullList> CREATOR = new Creator<StockFullList>() {
        @Override
        public StockFullList createFromParcel(Parcel in) {
            return new StockFullList(in);
        }

        @Override
        public StockFullList[] newArray(int size) {
            return new StockFullList[size];
        }
    };

    public double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(double feeRatio) {
        this.feeRatio = feeRatio;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(double displayPrice) {
        this.displayPrice = displayPrice;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public String getBourse() {
        return bourse;
    }

    public void setBourse(String bourse) {
        this.bourse = bourse;
    }

    public String getGains() {
        return gains;
    }

    public void setGains(String gains) {
        this.gains = gains;
    }

    public String getGainsValue() {
        return gainsValue;
    }

    public void setGainsValue(String gainsValue) {
        this.gainsValue = gainsValue;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
