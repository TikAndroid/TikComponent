package com.tik.android.component.trade.module.trade.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderSubmitResult implements Parcelable {

    /**
     * currency : USD
     * rate : 1
     * checkCount : 0
     * errorCode : 0
     * lock : 0
     * _id : 5c0227768bbc97001cd74c92
     * uid : 5bfe74cfa1cea5001c9b5181
     * ibTradeId : test-1543645046685
     * type : 1
     * coinSymbol : USDT
     * coinPrice : 0.983
     * coinFreeze : 0
     * coinDecimal : 6
     * stockSymbol : AAPL
     * stockName : Apple, Inc.
     * stockPrice : 178.58
     * price : 178.58
     * feeRatio : 0
     * totalSupply : 1
     * restSupply : 0
     * actualPrice : 178.58
     * status : 7
     * version : 0
     * createdAt : 1543645046686
     * updatedAt : 1543645047713
     * __v : 0
     */

    private String currency;
    private float rate;
    private int checkCount;
    private int errorCode;
    private int lock;
    @SerializedName("_id")
    private String id;
    private String uid;
    private String ibTradeId;
    private int type;
    private String coinSymbol;
    private double coinPrice;
    private float coinFreeze;
    private int coinDecimal;
    private String stockSymbol;
    private String stockName;
    private double stockPrice;
    private double price;
    private float feeRatio;
    private int totalSupply;
    private int restSupply;
    private double actualPrice;
    private int status;
    private int version;
    private long createdAt;
    private long updatedAt;
    private int __v;

    public OrderSubmitResult() {
    }

    protected OrderSubmitResult(Parcel in) {
        currency = in.readString();
        rate = in.readFloat();
        checkCount = in.readInt();
        errorCode = in.readInt();
        lock = in.readInt();
        id = in.readString();
        uid = in.readString();
        ibTradeId = in.readString();
        type = in.readInt();
        coinSymbol = in.readString();
        coinPrice = in.readDouble();
        coinFreeze = in.readFloat();
        coinDecimal = in.readInt();
        stockSymbol = in.readString();
        stockName = in.readString();
        stockPrice = in.readDouble();
        price = in.readDouble();
        feeRatio = in.readFloat();
        totalSupply = in.readInt();
        restSupply = in.readInt();
        actualPrice = in.readDouble();
        status = in.readInt();
        version = in.readInt();
        createdAt = in.readLong();
        updatedAt = in.readLong();
        __v = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeFloat(rate);
        dest.writeInt(checkCount);
        dest.writeInt(errorCode);
        dest.writeInt(lock);
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(ibTradeId);
        dest.writeInt(type);
        dest.writeString(coinSymbol);
        dest.writeDouble(coinPrice);
        dest.writeFloat(coinFreeze);
        dest.writeInt(coinDecimal);
        dest.writeString(stockSymbol);
        dest.writeString(stockName);
        dest.writeDouble(stockPrice);
        dest.writeDouble(price);
        dest.writeFloat(feeRatio);
        dest.writeInt(totalSupply);
        dest.writeInt(restSupply);
        dest.writeDouble(actualPrice);
        dest.writeInt(status);
        dest.writeInt(version);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
        dest.writeInt(__v);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderSubmitResult> CREATOR = new Creator<OrderSubmitResult>() {
        @Override
        public OrderSubmitResult createFromParcel(Parcel in) {
            return new OrderSubmitResult(in);
        }

        @Override
        public OrderSubmitResult[] newArray(int size) {
            return new OrderSubmitResult[size];
        }
    };

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIbTradeId() {
        return ibTradeId;
    }

    public void setIbTradeId(String ibTradeId) {
        this.ibTradeId = ibTradeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public double getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(double coinPrice) {
        this.coinPrice = coinPrice;
    }

    public float getCoinFreeze() {
        return coinFreeze;
    }

    public void setCoinFreeze(float coinFreeze) {
        this.coinFreeze = coinFreeze;
    }

    public int getCoinDecimal() {
        return coinDecimal;
    }

    public void setCoinDecimal(int coinDecimal) {
        this.coinDecimal = coinDecimal;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(float feeRatio) {
        this.feeRatio = feeRatio;
    }

    public int getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(int totalSupply) {
        this.totalSupply = totalSupply;
    }

    public int getRestSupply() {
        return restSupply;
    }

    public void setRestSupply(int restSupply) {
        this.restSupply = restSupply;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    @Override
    public String toString() {
        return "OrderSubmitResult{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                ", checkCount=" + checkCount +
                ", errorCode=" + errorCode +
                ", lock=" + lock +
                ", id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", ibTradeId='" + ibTradeId + '\'' +
                ", type=" + type +
                ", coinSymbol='" + coinSymbol + '\'' +
                ", coinPrice=" + coinPrice +
                ", coinFreeze=" + coinFreeze +
                ", coinDecimal=" + coinDecimal +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", price=" + price +
                ", feeRatio=" + feeRatio +
                ", totalSupply=" + totalSupply +
                ", restSupply=" + restSupply +
                ", actualPrice=" + actualPrice +
                ", status=" + status +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", __v=" + __v +
                '}';
    }
}