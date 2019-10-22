package com.tik.android.component.bussiness.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageBean implements Parcelable {
    /**
     * laohu : true
     * futu : true
     */

    private boolean laohu;
    private boolean futu;

    protected MessageBean(Parcel in) {
        laohu = in.readByte() != 0;
        futu = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (laohu ? 1 : 0));
        dest.writeByte((byte) (futu ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel in) {
            return new MessageBean(in);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };

    public boolean isLaohu() {
        return laohu;
    }

    public void setLaohu(boolean laohu) {
        this.laohu = laohu;
    }

    public boolean isFutu() {
        return futu;
    }

    public void setFutu(boolean futu) {
        this.futu = futu;
    }
}
