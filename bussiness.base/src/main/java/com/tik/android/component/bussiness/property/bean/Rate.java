package com.tik.android.component.bussiness.property.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Rate implements Parcelable {

    /**
     * BTC : 2.4887873E-4
     * CNY : 6.956611
     * EOS : 0.32034232
     * ETH : 0.0086903163
     * HKD : 7.827795
     * USD : 1
     * USDT : 1
     */

    private double BTC;
    private double CNY;
    private double EOS;
    private double ETH;
    private double HKD;
    private double USD;
    private double USDT;

    protected Rate(Parcel in) {
        BTC = in.readDouble();
        CNY = in.readDouble();
        EOS = in.readDouble();
        ETH = in.readDouble();
        HKD = in.readDouble();
        USD = in.readDouble();
        USDT = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(BTC);
        dest.writeDouble(CNY);
        dest.writeDouble(EOS);
        dest.writeDouble(ETH);
        dest.writeDouble(HKD);
        dest.writeDouble(USD);
        dest.writeDouble(USDT);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rate> CREATOR = new Creator<Rate>() {
        @Override
        public Rate createFromParcel(Parcel in) {
            return new Rate(in);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };

    public double getBTC() {
        return BTC;
    }

    public void setBTC(double BTC) {
        this.BTC = BTC;
    }

    public double getCNY() {
        return CNY;
    }

    public void setCNY(double CNY) {
        this.CNY = CNY;
    }

    public double getEOS() {
        return EOS;
    }

    public void setEOS(double EOS) {
        this.EOS = EOS;
    }

    public double getETH() {
        return ETH;
    }

    public void setETH(double ETH) {
        this.ETH = ETH;
    }

    public double getHKD() {
        return HKD;
    }

    public void setHKD(double HKD) {
        this.HKD = HKD;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getUSDT() {
        return USDT;
    }

    public void setUSDT(double USDT) {
        this.USDT = USDT;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "BTC=" + BTC +
                ", CNY=" + CNY +
                ", EOS=" + EOS +
                ", ETH=" + ETH +
                ", HKD=" + HKD +
                ", USD=" + USD +
                ", USDT=" + USDT +
                '}';
    }
}