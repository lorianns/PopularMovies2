package com.udacity.lorianns.popularmovie2;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

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

    private Boolean isFavorite;
    private Boolean isHighRated;
    private Boolean isMostPopular;


//    final String _POSTER_PATH = "poster_path";
//    final String _OVERVIEW = "overview";
//    final String _RELEASE_DATE = "release_date";
//    final String _GENRE_IDS = "genre_ids";
//    final String _ORIGINAL_TITLE = "original_title";
//    final String _ORIGINAL_LANGUAGE = "original_language";
//    final String _TITLE = "title";
//    final String _BACKDROP_PATH = "backdrop_path";
//    final String _ADULT = "adult";
//    final String _VIDEO = "video";
//    final String _ID = "id";
//    final String _VOTE_COUNT = "vote_count";
//    final String _POPULARITY = "popularity";
//    final String _VOTE_AVERAGE = "vote_average";
//
//    private String posterPath;
//    private String overview;
//    private String release_date;
//    private String[] genre_ids;
//    private String originalTitle;
//    private String originalLanguage;
//    private String title;
//    private String backdropPath;
//
//    private Boolean adult;
//    private Boolean video;
//
//    private Integer id;
//    private Integer voteCount;
//
//    private Double popularity;
//    private Double voteAverage;

    public MovieEntity() {
    }

    public MovieEntity(JSONObject movieJson) throws JSONException {

        apiId = movieJson.getString(_API_ID);
        imagePath = _IMAGE_BASE_URL + movieJson.getString(_IMAGE);
        title = movieJson.getString(_TITLE);
        releaseDate = movieJson.getString(_RELEASE_DATE);
        overview = movieJson.getString(_OVERVIEW);
        rating = movieJson.getString(_RATING);

//        posterPath = movieJson.getString(_POSTER_PATH);
//        overview = movieJson.getString(_OVERVIEW);
//        release_date = movieJson.getString(_RELEASE_DATE);
//        originalTitle = movieJson.getString(_ORIGINAL_TITLE);
//        originalLanguage = movieJson.getString(_ORIGINAL_LANGUAGE);
//        title = movieJson.getString(_TITLE);
//        backdropPath = movieJson.getString(_BACKDROP_PATH);
////        adult = movieJson.getBoolean(_ADULT);
////        video = movieJson.getBoolean(_VIDEO);
//        id = movieJson.getInt(_ID);
//        voteCount = movieJson.getInt(_VOTE_COUNT);
//        popularity = movieJson.getDouble(_POPULARITY);
//        voteAverage = movieJson.getDouble(_VOTE_AVERAGE);
    }

    public void fromCursor(Cursor cursor) {
        id = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_ID);
        apiId = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_API_ID);
        imagePath = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_IMAGE);
        title = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_TITLE);
        releaseDate = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_RELEASE_DATE);
        overview = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_SYNOPSIS);
        rating = cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_RATING);
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

//    public MovieEntity(Parcel parcel) {
//        posterPath = parcel.readString();
//        overview = parcel.readString();
//        release_date = parcel.readString();
//        originalTitle = parcel.readString();
//        originalLanguage = parcel.readString();
//        title = parcel.readString();
//        backdropPath = parcel.readString();
////        adult = parcel.readB
////        video = movieJson.getBoolean(_VIDEO);
//        id = parcel.readInt();
//        voteCount = parcel.readInt();
//        popularity = parcel.readDouble();
//        voteAverage = parcel.readDouble();
//    }
//
//    public static final Parcelable.Creator<ReviewEntity> CREATOR = new Parcelable.Creator<MovieEntity>() {
//        @Override
//        public ReviewEntity createFromParcel(Parcel parcel) {
//            return new MovieEntity(parcel);
//        }
//
//        @Override
//        public MovieEntity[] newArray(int size) {
//            return new MovieEntity[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int flags) {
//        parcel.writeString(posterPath);
//        parcel.writeString(overview);
//        parcel.writeString(release_date);
//        parcel.writeString(originalTitle);
//        parcel.writeString(originalLanguage);
//        parcel.writeString(title);
//        parcel.writeString(backdropPath);
//
//        parcel.writeInt(id);
//        parcel.writeInt(voteCount);
//        parcel.writeDouble(popularity);
//        parcel.writeDouble(voteAverage);
//    }
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


}
