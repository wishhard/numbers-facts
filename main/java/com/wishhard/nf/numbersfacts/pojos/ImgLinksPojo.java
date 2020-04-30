package com.wishhard.nf.numbersfacts.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class ImgLinksPojo implements Parcelable {

    private String largeImgUrl;
    private String thumbnailImgUrl;

    public String getLargeImgUrl() {
        return largeImgUrl;
    }

    public void setLargeImgUrl(String largeImgUrl) {
        this.largeImgUrl = largeImgUrl;
    }

    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }

    public void setThumbnailImgUrl(String thumbnailImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.largeImgUrl);
        dest.writeString(this.thumbnailImgUrl);
    }

    public ImgLinksPojo() {
    }

    protected ImgLinksPojo(Parcel in) {
        this.largeImgUrl = in.readString();
        this.thumbnailImgUrl = in.readString();
    }

    public static final Parcelable.Creator<ImgLinksPojo> CREATOR = new Parcelable.Creator<ImgLinksPojo>() {
        @Override
        public ImgLinksPojo createFromParcel(Parcel source) {
            return new ImgLinksPojo(source);
        }

        @Override
        public ImgLinksPojo[] newArray(int size) {
            return new ImgLinksPojo[size];
        }
    };
}
