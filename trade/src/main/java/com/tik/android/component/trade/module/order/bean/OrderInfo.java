package com.tik.android.component.trade.module.order.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/24
 */
public class OrderInfo implements Parcelable {
    private TradeInfo tradeInfo; // trade

    @SerializedName("_id")
    private String id;
    private String uid;
    private String uniqueId;
    private int qty;            // amount
    private BigDecimal price;   // deal price
    private long createdAt;
    private long updatedAt;
    @SerializedName("__v")
    int v;

    public TradeInfo getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(TradeInfo tradeInfo) {
        this.tradeInfo = tradeInfo;
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.tradeInfo, flags);
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.uniqueId);
        dest.writeInt(this.qty);
        dest.writeSerializable(this.price);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
        dest.writeInt(this.v);
    }

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        this.tradeInfo = in.readParcelable(TradeInfo.class.getClassLoader());
        this.id = in.readString();
        this.uid = in.readString();
        this.uniqueId = in.readString();
        this.qty = in.readInt();
        this.price = (BigDecimal) in.readSerializable();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.v = in.readInt();
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
