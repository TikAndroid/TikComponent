package com.tik.android.component.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by jianglixuan on 2018/11/17
 */
public class MarketBusinessHours implements Parcelable {
    private StockTargetBean HKD;
    private StockTargetBean USD;

    protected MarketBusinessHours(Parcel in) {
        HKD = in.readParcelable(StockTargetBean.class.getClassLoader());
        USD = in.readParcelable(StockTargetBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(HKD, flags);
        dest.writeParcelable(USD, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarketBusinessHours> CREATOR = new Creator<MarketBusinessHours>() {
        @Override
        public MarketBusinessHours createFromParcel(Parcel in) {
            return new MarketBusinessHours(in);
        }

        @Override
        public MarketBusinessHours[] newArray(int size) {
            return new MarketBusinessHours[size];
        }
    };

    public StockTargetBean getHKD() {
        return HKD;
    }

    public StockTargetBean getUSD() {
        return USD;
    }

    public static class StockTargetBean implements Parcelable {
        /**
         * city : New York
         * dollar : $
         * open : 09:30
         * close : 16:00
         * country : United States
         * id : NYSE
         * name : New York Stock Exchange
         * tz : America/New_York
         */

        private String city;
        private String dollar;
        private String open;
        private String close;
        private String country;
        private String id;
        private String lunch;
        private String name;
        private String tz;
        private List<CloseDaysBean> closeDays;

        protected StockTargetBean(Parcel in) {
            city = in.readString();
            dollar = in.readString();
            open = in.readString();
            close = in.readString();
            country = in.readString();
            id = in.readString();
            lunch = in.readString();
            name = in.readString();
            tz = in.readString();
            closeDays = in.createTypedArrayList(CloseDaysBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(city);
            dest.writeString(dollar);
            dest.writeString(open);
            dest.writeString(close);
            dest.writeString(country);
            dest.writeString(id);
            dest.writeString(lunch);
            dest.writeString(name);
            dest.writeString(tz);
            dest.writeTypedList(closeDays);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<StockTargetBean> CREATOR = new Creator<StockTargetBean>() {
            @Override
            public StockTargetBean createFromParcel(Parcel in) {
                return new StockTargetBean(in);
            }

            @Override
            public StockTargetBean[] newArray(int size) {
                return new StockTargetBean[size];
            }
        };

        public String getCity() {
            return city;
        }

        public String getDollar() {
            return dollar;
        }

        public String getOpen() {
            return open;
        }

        public String getClose() {
            return close;
        }


        public String getCountry() {
            return country;
        }

        public String getId() {
            return id;
        }


        public String getName() {
            return name;
        }


        public String getTz() {
            return tz;
        }


        public List<CloseDaysBean> getCloseDays() {
            return closeDays;
        }

        public String getLunch() {
            return lunch;
        }


        public static class CloseDaysBean implements Parcelable {
            /**
             * date : 2018-09-25
             * startClose : 09:30
             * endClose : 16:00
             * freeze : true
             */

            private String date;
            private String startClose;
            private String endClose;
            private boolean freeze;

            protected CloseDaysBean(Parcel in) {
                date = in.readString();
                startClose = in.readString();
                endClose = in.readString();
                freeze = in.readByte() != 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(date);
                dest.writeString(startClose);
                dest.writeString(endClose);
                dest.writeByte((byte) (freeze ? 1 : 0));
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<CloseDaysBean> CREATOR = new Creator<CloseDaysBean>() {
                @Override
                public CloseDaysBean createFromParcel(Parcel in) {
                    return new CloseDaysBean(in);
                }

                @Override
                public CloseDaysBean[] newArray(int size) {
                    return new CloseDaysBean[size];
                }
            };

            public String getDate() {
                return date;
            }


            public String getStartClose() {
                return startClose;
            }


            public String getEndClose() {
                return endClose;
            }

            public boolean isFreeze() {
                return freeze;
            }

        }
    }
}
