package com.tik.android.component.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wangqiqi on 2018/11/23.
 * 更新服务器上用户自选列表信息
 */
public class UserFavorParam implements Parcelable {
    List<String> fav;

    protected UserFavorParam(Parcel in) {
        fav = in.createStringArrayList();
    }

    public UserFavorParam() {
    }

    public static final Creator<UserFavorParam> CREATOR = new Creator<UserFavorParam>() {
        @Override
        public UserFavorParam createFromParcel(Parcel in) {
            return new UserFavorParam(in);
        }

        @Override
        public UserFavorParam[] newArray(int size) {
            return new UserFavorParam[size];
        }
    };

    public List<String> getFav() {
        return fav;
    }

    public void setFav(List<String> fav) {
        this.fav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(fav);
    }
}
