package com.tik.android.component.bussiness.property.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StockAssets implements Parcelable {
    @SerializedName("_id")
	private String id;
	private String uid;
	private long createdAt;
	private long updatedAt;
	private String currency; //类型: HKD 或者 USD
	private String symbol; //股票标志
	private String stockName; //股票名称
	private int volume; //持仓
	private int balance; //可用持仓
	private int freeze; //冻结持仓
	private double currentPrice; //当前单价
	private double holdPrice; //成本单价

	private double profitRatio;//总盈亏率
	private double profit;//总盈亏
	private double totalCurrentPrice;//当前总价

	public StockAssets() {
	}

	protected StockAssets(Parcel in) {
		id = in.readString();
		uid = in.readString();
		createdAt = in.readLong();
		updatedAt = in.readLong();
		currency = in.readString();
		symbol = in.readString();
		stockName = in.readString();
		volume = in.readInt();
		balance = in.readInt();
		freeze = in.readInt();
		currentPrice = in.readDouble();
		holdPrice = in.readDouble();
		profitRatio = in.readDouble();
		profit = in.readDouble();
		totalCurrentPrice = in.readDouble();
	}

	public static final Creator<StockAssets> CREATOR = new Creator<StockAssets>() {
		@Override
		public StockAssets createFromParcel(Parcel in) {
			return new StockAssets(in);
		}

		@Override
		public StockAssets[] newArray(int size) {
			return new StockAssets[size];
		}
	};

	public double getTotalCurrentPrice() {
		return totalCurrentPrice;
	}

	public void setTotalCurrentPrice(double totalCurrentPrice) {
		this.totalCurrentPrice = totalCurrentPrice;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public void setProfitRatio(double profitRatio) {
		this.profitRatio = profitRatio;
	}

	public double getProfitRatio() {
		return profitRatio;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setHoldPrice(double holdPrice) {
		this.holdPrice = holdPrice;
	}

	public double getHoldPrice() {
		return holdPrice;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getVolume() {
		return volume;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getBalance() {
		return balance;
	}

	public void setFreeze(int freeze) {
		this.freeze = freeze;
	}

	public int getFreeze() {
		return freeze;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(currency);
		dest.writeString(id);
		dest.writeString(uid);
		dest.writeString(symbol);
		dest.writeString(stockName);
		dest.writeInt(volume);
		dest.writeInt(balance);
		dest.writeInt(freeze);
		dest.writeLong(createdAt);
		dest.writeLong(updatedAt);
		dest.writeDouble(currentPrice);
		dest.writeDouble(holdPrice);
		dest.writeDouble(profitRatio);
	}

    @Override
    public String toString() {
        return "StockAssets{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", currency='" + currency + '\'' +
                ", symbol='" + symbol + '\'' +
                ", stockName='" + stockName + '\'' +
                ", volume=" + volume +
                ", balance=" + balance +
                ", freeze=" + freeze +
                ", currentPrice=" + currentPrice +
                ", holdPrice=" + holdPrice +
                ", profitRatio=" + profitRatio +
                ", profit=" + profit +
                ", totalCurrentPrice=" + totalCurrentPrice +
                '}';
    }
}