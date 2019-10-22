package com.tik.android.component.account.login.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @describe : 请求注册时需要的参数封装
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/14.
 */
public class RegisterArgs implements Parcelable {
    /**
     * 通过极验拿到的ticket凭证
     */
    private String ticket;
    /**
     * 注册通道
     */
    private String channel;
    /**
     * 国家码
     */
    private String countryCode;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 设置的密码
     */
    private String password;
    /**
     * 获取的验证码
     */
    private String code;
    /**
     * 邀请码
     */
    private String inviteCode;

    public RegisterArgs() {}

    protected RegisterArgs(Parcel in) {
        ticket = in.readString();
        channel = in.readString();
        countryCode = in.readString();
        mobile = in.readString();
        email = in.readString();
        password = in.readString();
        code = in.readString();
        inviteCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticket);
        dest.writeString(channel);
        dest.writeString(countryCode);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(code);
        dest.writeString(inviteCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegisterArgs> CREATOR = new Creator<RegisterArgs>() {
        @Override
        public RegisterArgs createFromParcel(Parcel in) {
            return new RegisterArgs(in);
        }

        @Override
        public RegisterArgs[] newArray(int size) {
            return new RegisterArgs[size];
        }
    };

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public boolean isEmailType() {
        return !TextUtils.isEmpty(email);
    }

    public boolean isMobileType() {
        return !TextUtils.isEmpty(countryCode) && !TextUtils.isEmpty(mobile);
    }
}
