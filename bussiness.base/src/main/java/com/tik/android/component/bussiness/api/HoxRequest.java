package com.tik.android.component.bussiness.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * @describe :动态请求bean
 * @usage :
 * <p>
 * </p>
 * Created by baowei on 2018/11/28.
 */
public class HoxRequest implements Parcelable {
    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_POST = "POST";
    private String url;
    private String requestType;
    private Map<String, String> map;

    private HoxRequest(Builder builder) {
        setUrl(builder.url);
        requestType = builder.requestType;
        setMap(builder.map);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 是否是post请求
     *
     * @return
     */
    public boolean isPost() {
        return REQUEST_POST.equals(requestType);
    }

    /**
     * 是否是get请求
     *
     * @return
     */
    public boolean isGet() {
        return REQUEST_GET.equals(requestType);
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.requestType);
        dest.writeInt(this.map.size());
        for (Map.Entry<String, String> entry : this.map.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public HoxRequest() {
    }

    protected HoxRequest(Parcel in) {
        this.url = in.readString();
        this.requestType = in.readString();
        int mapSize = in.readInt();
        this.map = new HashMap<String, String>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.map.put(key, value);
        }
    }

    public static final Creator<HoxRequest> CREATOR = new Creator<HoxRequest>() {
        @Override
        public HoxRequest createFromParcel(Parcel source) {
            return new HoxRequest(source);
        }

        @Override
        public HoxRequest[] newArray(int size) {
            return new HoxRequest[size];
        }
    };

    public static final class Builder {
        private String url;
        private String requestType;
        private Map<String, String> map;

        public Builder() {
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder requestType(String val) {
            requestType = val;
            return this;
        }

        public Builder map(Map<String, String> val) {
            map = val;
            return this;
        }

        public HoxRequest build() {
            return new HoxRequest(this);
        }
    }
}
