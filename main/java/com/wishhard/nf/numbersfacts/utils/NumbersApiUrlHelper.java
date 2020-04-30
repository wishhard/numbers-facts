package com.wishhard.nf.numbersfacts.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class NumbersApiUrlHelper implements Parcelable {

    private static final String MAIN_url = "http://numbersapi.com/";
    private static final String JSON_QUERY_PARAM = "?json";

    public static final String TYPE_DATE = "/date";
    public static final String TYPE_MATH = "/math";
    public static final String TYPE_YEAR = "/year";

    private String type;

    private int math,month,dayOfMonth,year;

    public void setType(String type) {
        this.type = type;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public void setMonthAndDay(int month,int dayOfMonth) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String requestAddress() {
        if(type.equals(TYPE_MATH)) {
            return MAIN_url+math+type+JSON_QUERY_PARAM;
        } else if(type.equals(TYPE_DATE)) {
            return MAIN_url+month+"/"+dayOfMonth+type+JSON_QUERY_PARAM;
        }
        return MAIN_url+year+TYPE_YEAR+JSON_QUERY_PARAM;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeInt(this.math);
        dest.writeInt(this.month);
        dest.writeInt(this.dayOfMonth);
        dest.writeInt(this.year);
    }

    public NumbersApiUrlHelper() {
    }

    protected NumbersApiUrlHelper(Parcel in) {
        this.type = in.readString();
        this.math = in.readInt();
        this.month = in.readInt();
        this.dayOfMonth = in.readInt();
        this.year = in.readInt();
    }

    public static final Creator<NumbersApiUrlHelper> CREATOR = new Creator<NumbersApiUrlHelper>() {
        @Override
        public NumbersApiUrlHelper createFromParcel(Parcel source) {
            return new NumbersApiUrlHelper(source);
        }

        @Override
        public NumbersApiUrlHelper[] newArray(int size) {
            return new NumbersApiUrlHelper[size];
        }
    };
}
