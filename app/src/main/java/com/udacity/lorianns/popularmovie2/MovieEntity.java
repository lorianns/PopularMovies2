package com.udacity.lorianns.popularmovie2;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lorianns on 6/25/16.
 */
public class MovieEntity implements Parcelable {

    final String _ID = "id";
    final String _IMAGE = "poster_path";
    final String _TITLE = "original_title";
    final String _RELEASE_DATE = "release_date";
    final String _OVERVIEW = "overview";
    final String _RATING = "vote_average";
    final String _IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private String id;
    private String imagePath;
    private String title;
    private String releaseDate;
    private String overview;
    private String rating;

    private Boolean isFavorite;
    private Boolean isHighRated;
    private Boolean isMostPopular;


    public MovieEntity(JSONObject movieJson) throws JSONException {

        id = movieJson.getString(_ID);
        imagePath = _IMAGE_BASE_URL + movieJson.getString(_IMAGE);
        title = movieJson.getString(_TITLE);
        releaseDate = movieJson.getString(_RELEASE_DATE);
        overview = movieJson.getString(_OVERVIEW);
        rating = movieJson.getString(_RATING);
    }

    public MovieEntity(Parcel parcel) {
        id = parcel.readString();
        imagePath = parcel.readString();
        title = parcel.readString();
        releaseDate = parcel.readString();
        overview = parcel.readString();
        rating = parcel.readString();

    }

    public static final Creator<MovieEntity> CREATOR = new Creator<MovieEntity>() {
        @Override
        public MovieEntity createFromParcel(Parcel parcel) {
            return new MovieEntity(parcel);
        }

        @Override
        public MovieEntity[] newArray(int size) {
            return new MovieEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(imagePath);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeString(rating);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
