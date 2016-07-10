package com.udacity.lorianns.popularmovie2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by lorianns on 7/10/16.
 */
public class FavoriteMovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMovieDBHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int MOVIE_REVIEW_AND_TRAILERS = 102;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteMovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, FavoriteMovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);
//        matcher.addURI(authority, FavoriteMovieContract.PATH_MOVIE + "/*/#", MOVIE_REVIEW_AND_TRAILERS);

        return matcher;
    }
}
