package com.tik.android.component.market.bussiness.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.tik.android.component.market.util.Constant;

/**
 * Created by wangqiqi on 2018/11/23.
 * 股票信息表（美股 + 港股 + 自选 + 搜索历史记录）
 */
@Entity(tableName = Constant.MARKET_STOCK_TABLE)
public class MarketStockEntity implements Parcelable {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = Constant.COLUMN_SYMBOL)
    private String symbol;

    @ColumnInfo(name = Constant.COLUMN_NAME)
    private String name;

    @ColumnInfo(name = Constant.COLUMN_CNAME)
    private String cname;

    @ColumnInfo(name = Constant.COLUMN_CURRENCY)
    private String currency;

    @ColumnInfo(name = Constant.COLUMN_UPDATE_TIME)
    private long time;

    @ColumnInfo(name = Constant.COLUMN_FAVORITE)
    private boolean favor;

    @ColumnInfo(name = Constant.COLUMN_HISTORY)
    private boolean history;

    @Ignore
    private int type;

    @ColumnInfo(name = Constant.COLUMN_PRICE)
    private double price;

    @ColumnInfo(name = Constant.COLUMN_GAINS)
    private String gains;

    @ColumnInfo(name = Constant.COLUMN_GAINS_VALUE)
    private String gainsValue;

    @ColumnInfo(name = Constant.COLUMN_LOCAL)
    private boolean local;

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFavor() {
        return favor;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGains() {
        return gains;
    }

    public void setGains(String gains) {
        this.gains = gains;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getGainsValue() {
        return gainsValue;
    }

    public void setGainsValue(String gainsValue) {
        this.gainsValue = gainsValue;
    }

    public MarketStockEntity() {
    }

    public MarketStockEntity(int type) {
        this.type = type;
    }

    protected MarketStockEntity(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        cname = in.readString();
        currency = in.readString();
        time = in.readLong();
        favor = in.readByte() != 0;
        history = in.readByte() != 0;
        type = in.readInt();
        price = in.readDouble();
        gains = in.readString();
        gainsValue = in.readString();
    }

    public static final Creator<MarketStockEntity> CREATOR = new Creator<MarketStockEntity>() {
        @Override
        public MarketStockEntity createFromParcel(Parcel in) {
            return new MarketStockEntity(in);
        }

        @Override
        public MarketStockEntity[] newArray(int size) {
            return new MarketStockEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(name);
        dest.writeString(cname);
        dest.writeString(currency);
        dest.writeLong(time);
        dest.writeByte((byte) (favor ? 1 : 0));
        dest.writeByte((byte) (history ? 1 : 0));
        dest.writeInt(type);
        dest.writeDouble(price);
        dest.writeString(gains);
        dest.writeString(gainsValue);
    }
}
