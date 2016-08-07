package com.udacity.lorianns.popularmovie2;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lorianns on 8/6/16.
 */
public class ReviewEntity implements Parcelable{

    final String _ID = "id";
    final String _AUTHOR = "author";
    final String _CONTENT = "content";
    final String _URL = "url";

    private String id;
    private String author;
    private String content;
    private String url;

    public ReviewEntity(JSONObject movieJson) throws JSONException {
        id = movieJson.getString(_ID);
        author = movieJson.getString(_AUTHOR);
        content = movieJson.getString(_CONTENT);
        url = movieJson.getString(_URL);
    }


    public ReviewEntity(Parcel parcel) {
        id = parcel.readString();
        author = parcel.readString();
        content = parcel.readString();
        url = parcel.readString();
    }

    public static final Parcelable.Creator<ReviewEntity> CREATOR = new Parcelable.Creator<ReviewEntity>() {
        @Override
        public ReviewEntity createFromParcel(Parcel parcel) {
            return new ReviewEntity(parcel);
        }

        @Override
        public ReviewEntity[] newArray(int size) {
            return new ReviewEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);
    }
}
