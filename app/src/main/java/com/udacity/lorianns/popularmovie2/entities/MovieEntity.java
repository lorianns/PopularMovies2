package com.udacity.lorianns.popularmovie2.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.lorianns.popularmovie2.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lorianns on 6/25/16.
 */
public class MovieEntity implements Parcelable {

    final String _ID = "_id";
    final String _API_ID = "id";
    final String _IMAGE = "poster_path";
    final String _TITLE = "original_title";
    final String _RELEASE_DATE = "release_date";
    final String _OVERVIEW = "overview";
    final String _RATING = "vote_average";
    final String _IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private String id;
    private String apiId;
    private String imagePath;
    private String title;
    private String releaseDate;
    private String overview;
    private String rating;

    private Boolean isSelected = false;

    public MovieEntity() {
    }

    public MovieEntity(JSONObject movieJson) throws JSONException {

        apiId = movieJson.getString(_API_ID);
        imagePath = _IMAGE_BASE_URL + movieJson.getString(_IMAGE);
        title = movieJson.getString(_TITLE);
        releaseDate = movieJson.getString(_RELEASE_DATE);
        overview = movieJson.getString(_OVERVIEW);
        rating = movieJson.getString(_RATING);
    }

    public void fromCursor(Cursor cursor) {
        id = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_ID);
        apiId = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_API_ID);
        imagePath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_IMAGE);
        title = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_TITLE);
        releaseDate = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_RELEASE_DATE);
        overview = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_SYNOPSIS);
        rating = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_RATING);
    }

    public MovieEntity(Parcel parcel) {
        id = parcel.readString();
        apiId = parcel.readString();
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
        parcel.writeString(apiId);
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

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
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

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
