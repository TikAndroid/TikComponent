package com.tik.android.component.bussiness.account.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class VerifyArgs implements Parcelable {
    public static final String SINGLE_VERIFY = "single_verify";//单项验证
    public static final String MULTIPLE_VERIFY = "multiple_verify";//多项验证
    public static final String SINGLE_VERIFY_SMS = "single_verify_sms";//单项验证短信
    public static final String SINGLE_VERIFY_EMAIL = "single_verify_email";//单项验证邮箱
    public static final String SINGLE_VERIFY_GOOGLE = "single_verify_google";//单项验证谷歌


    protected VerifyArgs(Parcel in) {
        opType = in.readString();
        emailType = in.readString();
        verifyType = in.readString();
        verifyTypeDetail = in.readString();
    }

    public static final Creator<VerifyArgs> CREATOR = new Creator<VerifyArgs>() {
        @Override
        public VerifyArgs createFromParcel(Parcel in) {
            return new VerifyArgs(in);
        }

        @Override
        public VerifyArgs[] newArray(int size) {
            return new VerifyArgs[size];
        }
    };

    private String opType;//授权类型
    private String emailType;//发送邮件类型
    private String verifyType;//指定安全验证类型：单项/多项
    private String verifyTypeDetail;//指定具体单项安全验证类型：短信/邮箱/谷歌

    private VerifyArgs(Builder builder) {
        this.opType = builder.opType;
        this.emailType = builder.emailType;
        this.verifyType = builder.verifyType;
        this.verifyTypeDetail = builder.verifyTypeDetail;
    }

    public String getEmailType() {
        return emailType;
    }

    public String getOpType() {
        return opType;
    }

    public boolean isSingleVerify() {
        return SINGLE_VERIFY.equals(verifyType);
    }

    public boolean isMultipleVerify() {
        return MULTIPLE_VERIFY.equals(verifyType);
    }

    public boolean isSmsVerify() {
        return SINGLE_VERIFY_SMS.equals(verifyTypeDetail);
    }

    public boolean isEmailVerify() {
        return SINGLE_VERIFY_EMAIL.equals(verifyTypeDetail);
    }

    public boolean isGoogleVerify() {
        return SINGLE_VERIFY_GOOGLE.equals(verifyTypeDetail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(opType);
        dest.writeString(emailType);
        dest.writeString(verifyType);
        dest.writeString(verifyTypeDetail);
    }

    public static final class Builder {
        private String opType;
        private String emailType;
        private String verifyType;
        private String verifyTypeDetail;

        public Builder(){}

        public Builder opType(String opType) {
            this.opType = opType;
            return this;
        }

        public Builder emailType(String emailType) {
            this.emailType = emailType;
            return this;
        }

        public Builder verifyType(String verifyType) {
            this.verifyType = verifyType;
            return this;
        }

        public Builder verifyTypeDetail(String verifyTypeDetail) {
            this.verifyTypeDetail = verifyTypeDetail;
            return this;
        }

        public VerifyArgs builder() {
            return new VerifyArgs(this);
        }
    }
}
