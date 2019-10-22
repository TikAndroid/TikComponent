package com.tik.android.component.trade.module.order.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tik.android.component.trade.module.order.OrderConstants;
import com.tik.android.component.trade.module.order.OrderUtils;

import java.math.BigDecimal;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/23
 */
public class TradeInfo implements Parcelable {
    private String currency;
    private BigDecimal rate;         // big decimal ?
    private int checkCount;
    private int errorCode;
    private int lock;
    @SerializedName("_id")
    private String id;
    private String uid;
    private String ibTradeId;
    private int type;           // buy:1, sell:2
    private String coinSymbol;  // usdt etc.
    private BigDecimal coinPrice;
    private int coinFreeze;
    private int coinDecimal;    // coin decimal
    private String stockSymbol; // stock preview name
    private String stockName;   // stock detail name
    private BigDecimal stockPrice; // stock price
    private BigDecimal price;        // trade price, most equal with stock price
    private double feeRatio;     // fee ratio
    private int totalSupply;    // total
    private int restSupply;     // number of failed
    private BigDecimal actualPrice;  // ?

    /**
     * @see OrderConstants#TRADE_CONSIGNING_1
     * to
     * @see OrderConstants#TRADE_EXPIRED
     */
    private int status;         //
    private int version;        // ?
    private long createdAt;     // create time stamp
    private long updatedAt;     // update time stamp
    private OrderBean[] orders;           // all orders about this trade
    private String dataType;

    /**
     * @param context for get resource
     * @return
     */
    public String getStatusStr(Context context) {
        if (OrderConstants.TRADE_TYPE_DONE.equals(dataType)) {
            return null;
        }

        return OrderUtils.getStatusStr(context, status);
    }

    public String getDealNumStr(Context context) {
        int succeedNumber = totalSupply - restSupply;
        if (OrderConstants.TRADE_TYPE_DONE.equals(dataType)) {
            return "" + succeedNumber;
        }
        return "" + succeedNumber + "/" + totalSupply;
    }

    public String getPriceStr(Context context) {
        if (OrderConstants.TRADE_TYPE_DONE.equals(dataType)) {
            return "@ " + price.toPlainString();
        }
        return price.toPlainString();
    }

    public boolean hasTrade() {
        return totalSupply > restSupply;
    }

    public int getTradeSuccessAmount() {
        return totalSupply - restSupply;
    }

    public String getTurnoverStr() {
        return getPrice().multiply(BigDecimal.valueOf(getTotalSupply())).toPlainString();
    }

    public String getTradeTurnoverStr() {
        return getPrice().multiply(BigDecimal.valueOf(getTotalSupply())).toPlainString();
    }


    public TradeInfo() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
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

    public BigDecimal getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(BigDecimal coinPrice) {
        this.coinPrice = coinPrice;
    }

    public int getCoinFreeze() {
        return coinFreeze;
    }

    public void setCoinFreeze(int coinFreeze) {
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

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(double feeRatio) {
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

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
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

    public OrderBean[] getOrders() {
        return orders;
    }

    public void setOrders(OrderBean[] orders) {
        this.orders = orders;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currency);
        dest.writeSerializable(this.rate);
        dest.writeInt(this.checkCount);
        dest.writeInt(this.errorCode);
        dest.writeInt(this.lock);
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.ibTradeId);
        dest.writeInt(this.type);
        dest.writeString(this.coinSymbol);
        dest.writeSerializable(this.coinPrice);
        dest.writeInt(this.coinFreeze);
        dest.writeInt(this.coinDecimal);
        dest.writeString(this.stockSymbol);
        dest.writeString(this.stockName);
        dest.writeSerializable(this.stockPrice);
        dest.writeSerializable(this.price);
        dest.writeDouble(this.feeRatio);
        dest.writeInt(this.totalSupply);
        dest.writeInt(this.restSupply);
        dest.writeSerializable(this.actualPrice);
        dest.writeInt(this.status);
        dest.writeInt(this.version);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
        dest.writeTypedArray(this.orders, flags);
    }

    protected TradeInfo(Parcel in) {
        this.currency = in.readString();
        this.rate = (BigDecimal)in.readSerializable();
        this.checkCount = in.readInt();
        this.errorCode = in.readInt();
        this.lock = in.readInt();
        this.id = in.readString();
        this.uid = in.readString();
        this.ibTradeId = in.readString();
        this.type = in.readInt();
        this.coinSymbol = in.readString();
        this.coinPrice = (BigDecimal) in.readSerializable();
        this.coinFreeze = in.readInt();
        this.coinDecimal = in.readInt();
        this.stockSymbol = in.readString();
        this.stockName = in.readString();
        this.stockPrice = (BigDecimal) in.readSerializable();
        this.price = (BigDecimal) in.readSerializable();
        this.feeRatio = in.readDouble();
        this.totalSupply = in.readInt();
        this.restSupply = in.readInt();
        this.actualPrice = (BigDecimal) in.readSerializable();
        this.status = in.readInt();
        this.version = in.readInt();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.orders = in.createTypedArray(OrderBean.CREATOR);
    }

    public static final Creator<TradeInfo> CREATOR = new Creator<TradeInfo>() {
        @Override
        public TradeInfo createFromParcel(Parcel source) {
            return new TradeInfo(source);
        }

        @Override
        public TradeInfo[] newArray(int size) {
            return new TradeInfo[size];
        }
    };

    /**
     * just in trade info object
     */
    public static class OrderBean implements Parcelable {
        @SerializedName("_id")
        private String id;
        private BigDecimal price;
        private BigDecimal qty;
        private BigDecimal createdAt;

        public OrderBean() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getQty() {
            return qty;
        }

        public void setQty(BigDecimal qty) {
            this.qty = qty;
        }

        public BigDecimal getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(BigDecimal createdAt) {
            this.createdAt = createdAt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeSerializable(this.price);
            dest.writeSerializable(this.qty);
            dest.writeSerializable(this.createdAt);
        }

        protected OrderBean(Parcel in) {
            this.id = in.readString();
            this.price = (BigDecimal) in.readSerializable();
            this.qty = (BigDecimal) in.readSerializable();
            this.createdAt = (BigDecimal) in.readSerializable();
        }

        public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
            @Override
            public OrderBean createFromParcel(Parcel source) {
                return new OrderBean(source);
            }

            @Override
            public OrderBean[] newArray(int size) {
                return new OrderBean[size];
            }
        };
    }
}
