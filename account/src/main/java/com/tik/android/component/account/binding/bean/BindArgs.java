package com.tik.android.component.account.binding.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @describe : 请求绑定手机/邮箱的请求参数封装
 *
 * Created by yangtao on 2018/11/24
 */
public class BindArgs implements Parcelable {

    /**注册来源*/
    private String channel;
    /**第三方类型google, facebook, wechat */
    private String type;
    /**三方账户的 uuid */
    private String uuid;
    /**第三方昵称*/
    private String name;
    /**头像*/
    private String avatar;
    /**区号*/
    private String countryCode;
    /**手机号*/
    private String mobile;
    /**短信验证码*/
    private String code;
    /**密码,可选*/
    private String password;
    /**邀请码，可选*/
    private String inviteCode;
    /**资产密码 可选*/
    private String assetPassword;

    /**邮箱*/
    private String email;
    /**邮箱验证码*/
    private String emailCode;


    public BindArgs() {}

    protected BindArgs(Parcel in) {
        channel = in.readString();
        type = in.readString();
        uuid = in.readString();
        name = in.readString();
        avatar = in.readString();
        countryCode = in.readString();
        mobile = in.readString();
        code = in.readString();
        password = in.readString();
        inviteCode = in.readString();
        assetPassword = in.readString();
        email = in.readString();
        emailCode = in.readString();
    }

    public static final Creator<BindArgs> CREATOR = new Creator<BindArgs>() {
        @Override
        public BindArgs createFromParcel(Parcel in) {
            return new BindArgs(in);
        }

        @Override
        public BindArgs[] newArray(int size) {
            return new BindArgs[size];
        }
    };

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getAssetPassword() {
        return assetPassword;
    }

    public void setAssetPassword(String assetPassword) {
        this.assetPassword = assetPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channel);
        dest.writeString(type);
        dest.writeString(uuid);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(countryCode);
        dest.writeString(mobile);
        dest.writeString(code);
        dest.writeString(password);
        dest.writeString(inviteCode);
        dest.writeString(assetPassword);
        dest.writeString(email);
        dest.writeString(emailCode);
    }
}
