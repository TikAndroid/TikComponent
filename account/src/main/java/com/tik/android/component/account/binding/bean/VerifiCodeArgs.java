package com.tik.android.component.account.binding.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangtao on 2018/11/28
 */
public class VerifiCodeArgs implements Parcelable {
    /**区号*/
    private String countryCode;
    /**手机号*/
    private String mobile;
    /**邮箱*/
    private String email;
    /**第三方类型google, facebook, wechat */
    private String type;

    protected VerifiCodeArgs(Parcel in) {
        countryCode = in.readString();
        mobile = in.readString();
        email = in.readString();
        type = in.readString();
    }

    public VerifiCodeArgs(String countryCode, String mobile, String email, String type) {
        this.countryCode = countryCode;
        this.mobile = mobile;
        this.email = email;
        this.type = type;
    }

    public static final Creator<VerifiCodeArgs> CREATOR = new Creator<VerifiCodeArgs>() {
        @Override
        public VerifiCodeArgs createFromParcel(Parcel in) {
            return new VerifiCodeArgs(in);
        }

        @Override
        public VerifiCodeArgs[] newArray(int size) {
            return new VerifiCodeArgs[size];
        }
    };

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryCode);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(type);
    }
}
