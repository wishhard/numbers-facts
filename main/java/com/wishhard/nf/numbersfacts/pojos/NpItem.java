package com.wishhard.nf.numbersfacts.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class NpItem implements Parcelable {
    private String fact;
    private boolean found;
    private String type;

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fact);
        dest.writeByte(this.found ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
    }

    public NpItem() {
    }

    protected NpItem(Parcel in) {
        this.fact = in.readString();
        this.found = in.readByte() != 0;
        this.type = in.readString();
    }

    public static final Creator<NpItem> CREATOR = new Creator<NpItem>() {
        @Override
        public NpItem createFromParcel(Parcel source) {
            return new NpItem(source);
        }

        @Override
        public NpItem[] newArray(int size) {
            return new NpItem[size];
        }
    };
}
