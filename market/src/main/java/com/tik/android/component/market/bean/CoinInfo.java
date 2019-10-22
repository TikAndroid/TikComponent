package com.tik.android.component.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CoinInfo implements Parcelable{
    /**
     * cashLimit : {"auth":{"singleMax":0,"dayMax":0},"unauth":{"singleMax":7500000000,"dayMax":7500000000},"fee":500000,"otcFee":0,"minimum":100000000}
     * decimal : 6
     * _id : 5b4c5736bfe11e3ad8af82c4
     * symbol : USDT
     * logo : https://img.bianhua8.com//article/2018/07/25/5b58663ec856f.png
     * price : 1
     * otcFee : 0.005
     * createdAt : 1531727374000
     * updatedAt : 1531727374000
     */

    private CashLimitBean cashLimit;
    private int decimal;
    @SerializedName("_id")
    private String id;
    private String symbol;
    private String logo;
    private float price;
    private double otcFee;
    private long createdAt;
    private long updatedAt;

    protected CoinInfo(Parcel in) {
        decimal = in.readInt();
        id = in.readString();
        symbol = in.readString();
        logo = in.readString();
        price = in.readFloat();
        otcFee = in.readDouble();
        createdAt = in.readLong();
        updatedAt = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(decimal);
        dest.writeString(id);
        dest.writeString(symbol);
        dest.writeString(logo);
        dest.writeFloat(price);
        dest.writeDouble(otcFee);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CoinInfo> CREATOR = new Creator<CoinInfo>() {
        @Override
        public CoinInfo createFromParcel(Parcel in) {
            return new CoinInfo(in);
        }

        @Override
        public CoinInfo[] newArray(int size) {
            return new CoinInfo[size];
        }
    };

    public CashLimitBean getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(CashLimitBean cashLimit) {
        this.cashLimit = cashLimit;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getOtcFee() {
        return otcFee;
    }

    public void setOtcFee(double otcFee) {
        this.otcFee = otcFee;
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

    public static class CashLimitBean {
        /**
         * auth : {"singleMax":0,"dayMax":0}
         * unauth : {"singleMax":7500000000,"dayMax":7500000000}
         * fee : 500000
         * otcFee : 0
         * minimum : 100000000
         */

        private AuthBean auth;
        private AuthBean unauth;
        private int fee;
        private int otcFee;
        private int minimum;

        public AuthBean getAuth() {
            return auth;
        }

        public void setAuth(AuthBean auth) {
            this.auth = auth;
        }

        public AuthBean getUnauth() {
            return unauth;
        }

        public void setUnauth(AuthBean unauth) {
            this.unauth = unauth;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getOtcFee() {
            return otcFee;
        }

        public void setOtcFee(int otcFee) {
            this.otcFee = otcFee;
        }

        public int getMinimum() {
            return minimum;
        }

        public void setMinimum(int minimum) {
            this.minimum = minimum;
        }

        public static class AuthBean implements Parcelable {
            /**
             * singleMax : 7500000000
             * dayMax : 7500000000
             */

            private long singleMax;
            private long dayMax;

            protected AuthBean(Parcel in) {
                singleMax = in.readLong();
                dayMax = in.readLong();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(singleMax);
                dest.writeLong(dayMax);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<AuthBean> CREATOR = new Creator<AuthBean>() {
                @Override
                public AuthBean createFromParcel(Parcel in) {
                    return new AuthBean(in);
                }

                @Override
                public AuthBean[] newArray(int size) {
                    return new AuthBean[size];
                }
            };

            public long getSingleMax() {
                return singleMax;
            }

            public void setSingleMax(long singleMax) {
                this.singleMax = singleMax;
            }

            public long getDayMax() {
                return dayMax;
            }

            public void setDayMax(long dayMax) {
                this.dayMax = dayMax;
            }
        }
    }
}
