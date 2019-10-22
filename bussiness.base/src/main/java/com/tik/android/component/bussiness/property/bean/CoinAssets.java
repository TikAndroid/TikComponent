package com.tik.android.component.bussiness.property.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CoinAssets implements Parcelable {
    @SerializedName("_id")
    private String id;
    private String symbol;
    private int decimal;
    private String address;
    private long volume; //数字货币资产
    private long balance; // 购买力
    private long freeze; //冻结资金
    private int currentPrice;

    public CoinAssets() {
    }

    protected CoinAssets(Parcel in) {
        id = in.readString();
        symbol = in.readString();
        decimal = in.readInt();
        address = in.readString();
        volume = in.readLong();
        balance = in.readLong();
        freeze = in.readLong();
        currentPrice = in.readInt();
    }

    public static final Creator<CoinAssets> CREATOR = new Creator<CoinAssets>() {
        @Override
        public CoinAssets createFromParcel(Parcel in) {
            return new CoinAssets(in);
        }

        @Override
        public CoinAssets[] newArray(int size) {
            return new CoinAssets[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getVolume() {
        return volume;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public void setFreeze(long freeze) {
        this.freeze = freeze;
    }

    public long getFreeze() {
        return freeze;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(symbol);
        dest.writeInt(decimal);
        dest.writeString(address);
        dest.writeLong(volume);
        dest.writeLong(balance);
        dest.writeLong(freeze);
        dest.writeInt(currentPrice);
    }

    @Override
    public String toString() {
        return "CoinAssets{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", decimal=" + decimal +
                ", address='" + address + '\'' +
                ", volume=" + volume +
                ", balance=" + balance +
                ", freeze=" + freeze +
                ", currentPrice=" + currentPrice +
                '}';
    }
}