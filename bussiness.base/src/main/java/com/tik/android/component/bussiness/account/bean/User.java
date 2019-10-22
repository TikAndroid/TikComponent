package com.tik.android.component.bussiness.account.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("_id")
    private String id;
    private String email;
    private String countryCode;
    private String mobile;
    private String language;
    private Auth auth;
    private Oauth oauth;
    private Address address;
    private String lang;
    private String displayName;
    private boolean hadCashIn;
    private ThirdPartUser thirdPartUser;

    public boolean isHoxUser() {
        return !TextUtils.isEmpty(id);
    }

    public boolean isThirdUser() {
        return thirdPartUser != null && !TextUtils.isEmpty(thirdPartUser.getUuid());
    }

    public ThirdPartUser getThirdPartUser() {
        return thirdPartUser;
    }

    public void setThirdPartUser(ThirdPartUser thirdPartUser) {
        this.thirdPartUser = thirdPartUser;
    }

    public boolean isHadCashIn() {
        return hadCashIn;
    }

    public void setHadCashIn(boolean hadCashIn) {
        this.hadCashIn = hadCashIn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setOauth(Oauth oauth) {
        this.oauth = oauth;
    }

    public Oauth getOauth() {
        return oauth;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static class Address implements Parcelable {

        private String usdt;

        public void setUsdt(String usdt) {
            this.usdt = usdt;
        }

        public String getUsdt() {
            return usdt;
        }

        protected Address(Parcel in) {
            usdt = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(usdt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Address> CREATOR = new Creator<Address>() {
            @Override
            public Address createFromParcel(Parcel in) {
                return new Address(in);
            }

            @Override
            public Address[] newArray(int size) {
                return new Address[size];
            }
        };
    }

    public static class Auth implements Parcelable {

        private boolean email;
        private boolean google;
        private boolean sms;
        private String inviteCode;
        private String formattedKey;
        private String assetPassword;
        private boolean issetAssetPassword;
        private boolean issetPassword;

        public void setEmail(boolean email) {
            this.email = email;
        }

        public boolean getEmail() {
            return email;
        }

        public void setGoogle(boolean google) {
            this.google = google;
        }

        public boolean getGoogle() {
            return google;
        }

        public void setSms(boolean sms) {
            this.sms = sms;
        }

        public boolean getSms() {
            return sms;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setFormattedKey(String formattedKey) {
            this.formattedKey = formattedKey;
        }

        public String getFormattedKey() {
            return formattedKey;
        }

        public void setAssetPassword(String assetPassword) {
            this.assetPassword = assetPassword;
        }

        public String getAssetPassword() {
            return assetPassword;
        }

        public void setIssetAssetPassword(boolean issetAssetPassword) {
            this.issetAssetPassword = issetAssetPassword;
        }

        public boolean getIssetAssetPassword() {
            return issetAssetPassword;
        }

        public void setIssetPassword(boolean issetPassword) {
            this.issetPassword = issetPassword;
        }

        public boolean getIssetPassword() {
            return issetPassword;
        }

        protected Auth(Parcel in) {
            email = in.readByte() != 0;
            google = in.readByte() != 0;
            sms = in.readByte() != 0;
            inviteCode = in.readString();
            formattedKey = in.readString();
            assetPassword = in.readString();
            issetAssetPassword = in.readByte() != 0;
            issetPassword = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (email ? 1 : 0));
            dest.writeByte((byte) (google ? 1 : 0));
            dest.writeByte((byte) (sms ? 1 : 0));
            dest.writeString(inviteCode);
            dest.writeString(formattedKey);
            dest.writeString(assetPassword);
            dest.writeByte((byte) (issetAssetPassword ? 1 : 0));
            dest.writeByte((byte) (issetPassword ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Auth> CREATOR = new Creator<Auth>() {
            @Override
            public Auth createFromParcel(Parcel in) {
                return new Auth(in);
            }

            @Override
            public Auth[] newArray(int size) {
                return new Auth[size];
            }
        };
    }

    public static class Oauth implements Parcelable {

        private boolean google;
        private String googleName;
        private boolean facebook;
        private String facebookName;
        private boolean wechat;
        private String wechatName;

        public void setGoogle(boolean google) {
            this.google = google;
        }

        public boolean getGoogle() {
            return google;
        }

        public void setGoogleName(String googleName) {
            this.googleName = googleName;
        }

        public String getGoogleName() {
            return googleName;
        }

        public void setFacebook(boolean facebook) {
            this.facebook = facebook;
        }

        public boolean getFacebook() {
            return facebook;
        }

        public void setFacebookName(String facebookName) {
            this.facebookName = facebookName;
        }

        public String getFacebookName() {
            return facebookName;
        }

        public void setWechat(boolean wechat) {
            this.wechat = wechat;
        }

        public boolean getWechat() {
            return wechat;
        }

        public void setWechatName(String wechatName) {
            this.wechatName = wechatName;
        }

        public String getWechatName() {
            return wechatName;
        }

        protected Oauth(Parcel in) {
            google = in.readByte() != 0;
            googleName = in.readString();
            facebook = in.readByte() != 0;
            facebookName = in.readString();
            wechat = in.readByte() != 0;
            wechatName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (google ? 1 : 0));
            dest.writeString(googleName);
            dest.writeByte((byte) (facebook ? 1 : 0));
            dest.writeString(facebookName);
            dest.writeByte((byte) (wechat ? 1 : 0));
            dest.writeString(wechatName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Oauth> CREATOR = new Creator<Oauth>() {
            @Override
            public Oauth createFromParcel(Parcel in) {
                return new Oauth(in);
            }

            @Override
            public Oauth[] newArray(int size) {
                return new Oauth[size];
            }
        };
    }

    /**
     * for third-part user, the user info is empty
     * just hold the field: ThirdPartUser
     */
    public User() {
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        countryCode = in.readString();
        mobile = in.readString();
        language = in.readString();
        auth = in.readParcelable(Auth.class.getClassLoader());
        oauth = in.readParcelable(Oauth.class.getClassLoader());
        address = in.readParcelable(Address.class.getClassLoader());
        lang = in.readString();
        displayName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(countryCode);
        dest.writeString(mobile);
        dest.writeString(language);
        dest.writeParcelable(auth, flags);
        dest.writeParcelable(oauth, flags);
        dest.writeParcelable(address, flags);
        dest.writeString(lang);
        dest.writeString(displayName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
