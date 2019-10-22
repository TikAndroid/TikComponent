package com.tik.android.component.bussiness.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StockPriceInfo implements Parcelable {
    /**
     * _id : 5b81042d16252e001555b230
     * symbol : 00700
     * currency : HKD
     * name : TENCENT
     * price : 290.6
     * step : 100
     * createdAt : 1535181869782
     * updatedAt : 1541397054948
     * cname : 腾讯控股
     * market : SEHK
     * gains : -4.282%
     * gainsValue : -13.000
     * pinyin : tengxunkonggu
     * pinyinInitial : txkg
     * lowername : tencent
     * close : 303.6
     * high : 296.4
     * lastTime : 2018-11-05T05:50:49.000Z
     * low : 286.8
     * open : 294.6
     * isChecked : true
     * message : {"laohu":true,"futu":true}
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

    //1 开盘； 2 午休； 3 未开盘； 4.周末 5 节假日（全天）6.节假日（半天）
    private int status;


    public StockPriceInfo() {
    }

    protected StockPriceInfo(Parcel in) {
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StockPriceInfo> CREATOR = new Creator<StockPriceInfo>() {
        @Override
        public StockPriceInfo createFromParcel(Parcel in) {
            return new StockPriceInfo(in);
        }

        @Override
        public StockPriceInfo[] newArray(int size) {
            return new StockPriceInfo[size];
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

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "StockPriceInfo{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", currency='" + currency + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", step='" + step + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", cname='" + cname + '\'' +
                ", market='" + market + '\'' +
                ", gains='" + gains + '\'' +
                ", gainsValue='" + gainsValue + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", pinyinInitial='" + pinyinInitial + '\'' +
                ", lowername='" + lowername + '\'' +
                ", close=" + close +
                ", high=" + high +
                ", lastTime='" + lastTime + '\'' +
                ", low=" + low +
                ", open=" + open +
                ", isChecked=" + isChecked +
                ", message=" + message +
                ", status=" + status +
                '}';
    }
}
