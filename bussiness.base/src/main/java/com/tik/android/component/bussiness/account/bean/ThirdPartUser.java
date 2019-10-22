package com.tik.android.component.bussiness.account.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tik.android.component.bussiness.base.BuildConfig;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/19
 */
public class ThirdPartUser implements Parcelable{
    private String uuid;
    private String accessToken;
    private String name;
    private boolean newUser;
    private String userType;

    public ThirdPartUser() {
    }

    protected ThirdPartUser(Parcel in) {
        uuid = in.readString();
        accessToken = in.readString();
        name = in.readString();
        newUser = in.readByte() != 0;
    }

    @Override
    public String toString() {
        if(BuildConfig.DEBUG) {
            return "ThirdPartUser{" +
                    "uuid='" + uuid + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", name='" + name + '\'' +
                    ", newUser=" + newUser +
                    ", userType=" + userType +
                    '}';
        }
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(accessToken);
        dest.writeString(name);
        dest.writeByte((byte)(newUser ? 1 : 0));
    }

    public static final Parcelable.Creator<ThirdPartUser> CREATOR = new Parcelable.Creator<ThirdPartUser>() {
        @Override
        public ThirdPartUser createFromParcel(Parcel in) {
            return new ThirdPartUser(in);
        }

        @Override
        public ThirdPartUser[] newArray(int size) {
            return new ThirdPartUser[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
