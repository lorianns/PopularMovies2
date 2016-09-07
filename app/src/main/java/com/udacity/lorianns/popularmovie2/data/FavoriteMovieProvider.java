package com.udacity.lorianns.popularmovie2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lorianns on 7/10/16.
 */
public class FavoriteMovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMovieDBHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int FAVORITE_MOVIE = 103;
    static final int POP_MOVIE = 104;
    static final int TOP_RATED_MOVIE = 105;

    private static final SQLiteQueryBuilder sMoviesbyFavoriteQueryBuilder;
    private static final SQLiteQueryBuilder sMoviesbyPopQueryBuilder;
    private static final SQLiteQueryBuilder sMoviesbyTopRatedQueryBuilder;


    static{
        sMoviesbyFavoriteQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //fav_movie INNER JOIN location ON fav_movie._id = location._id
        sMoviesbyFavoriteQueryBuilder.setTables(
                MovieContract.FavoriteMovieEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.FavoriteMovieEntry.TABLE_NAME +
                        "." + MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry._ID);
    }

    static {
        sMoviesbyPopQueryBuilder = new SQLiteQueryBuilder();

        sMoviesbyPopQueryBuilder.setTables(
                MovieContract.PopMovieEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.PopMovieEntry.TABLE_NAME +
                        "." + MovieContract.PopMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry._ID);
    }

    static {
        sMoviesbyTopRatedQueryBuilder = new SQLiteQueryBuilder();

        sMoviesbyTopRatedQueryBuilder.setTables(
                MovieContract.TopRatedMovieEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.TopRatedMovieEntry.TABLE_NAME +
                        "." + MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry._ID);
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoriteMovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case MOVIE_WITH_ID: {

                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }
            case FAVORITE_MOVIE: {
                retCursor = sMoviesbyFavoriteQueryBuilder.query(
                        mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case POP_MOVIE: {
                retCursor = sMoviesbyPopQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TOP_RATED_MOVIE: {
                retCursor = sMoviesbyTopRatedQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            case FAVORITE_MOVIE:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case POP_MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case TOP_RATED_MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITE_MOVIE: {
                long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POP_MOVIE: {
                long _id = db.insert(MovieContract.PopMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.PopMovieEntry.buildPopMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOP_RATED_MOVIE: {
                long _id = db.insert(MovieContract.TopRatedMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TopRatedMovieEntry.buildTopRatedMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case POP_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.PopMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.TopRatedMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITE_MOVIE:
                rowsUpdated = db.update(MovieContract.FavoriteMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case POP_MOVIE:
                rowsUpdated = db.update(MovieContract.PopMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TOP_RATED_MOVIE:
                rowsUpdated = db.update(MovieContract.TopRatedMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POP_MOVIE:
                db.beginTransaction();
                int returnCount2 = 0;
                try {
                    for (ContentValues value : values) {
                        long _idParent = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);

                        if (_idParent != -1) {
                            ContentValues favMovieValues = new ContentValues();
                            favMovieValues.put(MovieContract.PopMovieEntry.COLUMN_MOVIE_KEY, String.valueOf(_idParent));
                            long _id = db.insert(MovieContract.PopMovieEntry.TABLE_NAME, null, favMovieValues);
                            if (_id != -1)
                                returnCount2++;
                        }
                        else
                            Log.e("sdf","sdf");
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount2;
            case TOP_RATED_MOVIE:
                db.beginTransaction();
                int returnCount3 = 0;
                try {
                    for (ContentValues value : values) {
                        long _idParent = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);

                        if (_idParent != -1) {
                            ContentValues favMovieValues = new ContentValues();
                            favMovieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_KEY, String.valueOf(_idParent));
                            long _id  = db.insert(MovieContract.TopRatedMovieEntry.TABLE_NAME, null, favMovieValues);
                            if (_id != -1)
                                returnCount3++;
                        }
                        else
                            Log.e("sdf","sdf");
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount3;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIE, FAVORITE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_POP_MOVIE, POP_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TOP_RATED_MOVIE, TOP_RATED_MOVIE);

        return matcher;
    }
}
