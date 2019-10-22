package com.tik.android.component.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tik.android.component.bussiness.market.bean.MessageBean;

public class StockSearchResult implements Parcelable {
    /**
     * _id : 5b80ffd816252e001555b226
     * symbol : 02318
     * currency : HKD
     * name : PING AN
     * price : 72.55
     * step : 500
     * createdAt : 1535180760231
     * updatedAt : 1540908343096
     * cname : 中国平安
     * market : SEHK
     * gains : -0.275%
     * gainsValue : -0.200
     * pinyin : zhongguopingan
     * pinyinInitial : zgpa
     * lowername : ping an
     * close : 72.75
     * high : 74.05
     * lastTime : 2018-10-30T08:08:40.000Z
     * low : 71.5
     * open : 71.75
     * isChecked : true
     * message : {"laohu":true,"futu":true}
     * category :
     */

    @SerializedName("_id")
    private String id;
    private String symbol;
    private String currency;
    private String name;
    private double price;
    private String step;
    private long createdAt;
    private long updatedAt;
    private String cname;
    private String market;
    private String gains;
    private String gainsValue;
    private String pinyin;
    private String pinyinInitial;
    private String lowername;
    private double close;
    private double high;
    private String lastTime;
    private double low;
    private double open;
    private boolean isChecked;
    private MessageBean message;
    private String category;

    public StockSearchResult() {
    }

    protected StockSearchResult(Parcel in) {
        id = in.readString();
        symbol = in.readString();
        currency = in.readString();
        name = in.readString();
        price = in.readDouble();
        step = in.readString();
        createdAt = in.readLong();
        updatedAt = in.readLong();
        cname = in.readString();
        market = in.readString();
        gains = in.readString();
        gainsValue = in.readString();
        pinyin = in.readString();
        pinyinInitial = in.readString();
        lowername = in.readString();
        close = in.readDouble();
        high = in.readDouble();
        lastTime = in.readString();
        low = in.readDouble();
        open = in.readDouble();
        isChecked = in.readByte() != 0;
        message = in.readParcelable(MessageBean.class.getClassLoader());
        category = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(symbol);
        dest.writeString(currency);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(step);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
        dest.writeString(cname);
        dest.writeString(market);
        dest.writeString(gains);
        dest.writeString(gainsValue);
        dest.writeString(pinyin);
        dest.writeString(pinyinInitial);
        dest.writeString(lowername);
        dest.writeDouble(close);
        dest.writeDouble(high);
        dest.writeString(lastTime);
        dest.writeDouble(low);
        dest.writeDouble(open);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeParcelable(message, flags);
        dest.writeString(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StockSearchResult> CREATOR = new Creator<StockSearchResult>() {
        @Override
        public StockSearchResult createFromParcel(Parcel in) {
            return new StockSearchResult(in);
        }

        @Override
        public StockSearchResult[] newArray(int size) {
            return new StockSearchResult[size];
        }
    };

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinInitial() {
        return pinyinInitial;
    }

    public void setPinyinInitial(String pinyinInitial) {
        this.pinyinInitial = pinyinInitial;
    }

    public String getLowername() {
        return lowername;
    }

    public void setLowername(String lowername) {
        this.lowername = lowername;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
