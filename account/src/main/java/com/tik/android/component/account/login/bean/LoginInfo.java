package com.tik.android.component.account.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

public class LoginInfo implements Parcelable {

    private String uid;
    private String token;
    private JsonObject user;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setUser(JsonObject user) {
        this.user = user;
    }

    public JsonObject getUser() {
        return user;
    }


    protected LoginInfo(Parcel in) {
        uid = in.readString();
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel in) {
            return new LoginInfo(in);
        }

        @Override
        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };

}
