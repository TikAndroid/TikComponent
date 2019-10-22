package com.tik.android.component.account.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/15.
 */
public class Country implements Parcelable {
    private String country;
    private String code;
    private String language;

    public CharSequence getCountry() {
        return country;
    }

    public void setCountry(CharSequence country) {
        this.country = String.valueOf(country);
    }

    public CharSequence getCode() {
        return code;
    }

    public void setCode(CharSequence code) {
        this.code = String.valueOf(code);
    }

    public CharSequence getLanguage() {
        return language;
    }

    public void setLanguage(CharSequence language) {
        this.language = String.valueOf(language);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.code);
        dest.writeString(this.language);
    }

    public Country() {
    }

    public Country(Country other) {
        this(other.country, other.code, other.language);
    }

    public Country(String country, String code, String language) {
        this.country = country;
        this.code = code;
        this.language = language;
    }

    protected Country(Parcel in) {
        this.country = in.readString();
        this.code = in.readString();
        this.language = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
