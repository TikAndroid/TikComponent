package com.tik.android.component.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/14
 */
public class VersionInfo implements Parcelable{
    String currentVersion;
    boolean forceUpdate;
    JsonObject message;
    String download;

    public VersionInfo() {
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public JsonObject getMessage() {
        return message;
    }

    public void setMessage(JsonObject message) {
        this.message = message;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    protected VersionInfo(Parcel in) {
        currentVersion = in.readString();
        forceUpdate = in.readByte() != 0;
        download = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(currentVersion);
        dest.writeByte((byte) (forceUpdate ? 1 : 0));
        dest.writeString(download);
    }

    public static final Creator<VersionInfo> CREATOR = new Creator<VersionInfo>() {
        @Override
        public VersionInfo createFromParcel(Parcel in) {
            return new VersionInfo(in);
        }

        @Override
        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };

}
