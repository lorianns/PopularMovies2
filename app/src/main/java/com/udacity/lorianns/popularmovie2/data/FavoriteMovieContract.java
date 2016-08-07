package com.udacity.lorianns.popularmovie2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lorianns on 7/7/16.
 */
public class FavoriteMovieContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.lorianns.popularmovie2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIE= "movie";
    public static final String PATH_MOVIE= "movie";

    /* Inner class that defines the table contents of the movie table */
    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "favorite_movie";
        // Movie id as returned by API, to identify the movie
        public static final String COLUMN_MOVIE_KEY = "id";

        public static final int COL_MOVIE_ID = 0;

        public static Uri buildFavoriteMovieUri() {
            return CONTENT_URI;
        }

        //insert
        public static Uri buildFavoriteMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // Movie id as returned by API, to identify the movie
        public static final String COLUMN_MOVIE_ID = "id";

        // Image path, stored as string
        public static final String COLUMN_IMAGE = "image_path";

        // Title of the movie, as provided by API.
        public static final String COLUMN_TITLE = "title";

        // Description of the movie, as provided by API.
        public static final String COLUMN_SYNOPSIS = "synopsis";

        // Rating of the movie, as provided by API.
        //Ex: 5.62
        public static final String COLUMN_RATING= "rating";

        //Release date of the movie, as provided by API.
        //Ex: 2016-03-23
        public static final String COLUMN_RELEASE_DATE= "release_date";

//        public static final String COLUMN_HIGH_RATED= "high_rated";
//        public static final String COLUMN_MOST_POPULAR = "most_popular";

        public static final int COL_MOVIE_ID = 0;
        public static final int COL_MOVIE_SYNOPSIS = 1;
        public static final int COL_MOVIE_RATING = 2;
        public static final int COL_MOVIE_RELEASE_DATE = 3;
        public static final int COL_MOVIE_IMAGE = 4;
        public static final int COL_MOVIE_TITLE = 5;
        public static final int COL_MOVIE_API_ID = 6;

        public static Uri buildMovieUri() {
            return CONTENT_URI;
        }

        //insert
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
