package com.example.lucia.popularmoviesstage2;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    //This is the YouTube BASE for getting the trailer site
    private static final String BASE_YOUTUBE = "https://www.youtube.com/watch?v=";
    //This variable stores the key of the trailer
    private String mTrailerKey;
    //This variable stores the name of the trailer
    private String mTrailerName;
    //This variable stores the YouTube site for each trailer
    private String mTrailerSite;

    public Trailer(String trailerKey, String trailerName) {
        mTrailerKey = trailerKey;
        mTrailerName = trailerName;
    }

    protected Trailer(Parcel in) {
        mTrailerKey = in.readString();
        mTrailerName = in.readString();
        mTrailerSite = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerKey);
        dest.writeString(mTrailerName);
        dest.writeString(mTrailerSite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.mTrailerKey = trailerKey;
    }

    public String getTrailerName() {
        return mTrailerName;
    }

    public void setTrailerName(String trailerName) {
        this.mTrailerName = trailerName;
    }

    public String getTrailerSite() {
        mTrailerName = BASE_YOUTUBE + getTrailerKey();
        return mTrailerName;
    }

    public void setTrailerSite(String trailerSite) {
        this.mTrailerSite = trailerSite;
    }
}