package com.tik.android.component.bussiness.service.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @describe : 登录类型枚举
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/20.
 */
public enum LoginType implements Parcelable {
    HOX(0),
    WECHAT(1),
    GOOGLE(2),
    FACEBOOK(3);

    public boolean isThirdPartLogin() {
        return this.compareTo(HOX) > 0;
    }

    private int mValue;

    LoginType(int value) {
        this.mValue = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginType> CREATOR = new Creator<LoginType>() {
        @Override
        public LoginType createFromParcel(Parcel in) {
            return LoginType.values()[in.readInt()];
        }

        @Override
        public LoginType[] newArray(int size) {
            return new LoginType[size];
        }
    };

}
