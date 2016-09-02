package com.udacity.lorianns.popularmovie2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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
    static final int FAVORITE_MOVIE = 103;
    static final int POP_MOVIE = 104;
    static final int TOP_RATED_MOVIE = 105;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    private static final SQLiteQueryBuilder sWoviesbyPopQueryBuilder;
    private static final SQLiteQueryBuilder sWoviesbyTopRatedQueryBuilder;

    static{
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " INNER JOIN " +
                        FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        " ON " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.MovieEntry._ID);

        sWeatherByLocationSettingQueryBuilder.setTables(
                FavoriteMovieContract.PopMovieEntry.TABLE_NAME + " INNER JOIN " +
                        FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        " ON " + FavoriteMovieContract.PopMovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.PopMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.MovieEntry._ID);
    }

    static {
        sWoviesbyTopRatedQueryBuilder = new SQLiteQueryBuilder();

        sWoviesbyTopRatedQueryBuilder.setTables(
                FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME + " INNER JOIN " +
                        FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        " ON " + FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.TopRatedMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.MovieEntry._ID);
    }

    static {
        sWoviesbyPopQueryBuilder = new SQLiteQueryBuilder();

        sWoviesbyPopQueryBuilder.setTables(
                FavoriteMovieContract.PopMovieEntry.TABLE_NAME + " INNER JOIN " +
                        FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        " ON " + FavoriteMovieContract.PopMovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.PopMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + FavoriteMovieContract.MovieEntry.TABLE_NAME +
                        "." + FavoriteMovieContract.MovieEntry._ID);
    }

    private Cursor getMoviesByPopMovies(
            Uri uri, String[] projection, String sortOrder) {
//        String locationSetting = FavoriteMovieContract.MovieEntry.getMovieFromUri(uri);

        return sWoviesbyPopQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMoviesByTopRated(
            Uri uri, String[] projection, String sortOrder) {
//        String locationSetting = FavoriteMovieContract.MovieEntry.getMovieFromUri(uri);

        return sWoviesbyTopRatedQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
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

            case MOVIE_REVIEW_AND_TRAILERS:
            {
//                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_WITH_ID: {
//                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "weather"
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }
            // "weather"
            case FAVORITE_MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }

            case POP_MOVIE: {
                retCursor = getMoviesByPopMovies(uri, projection, sortOrder);
                //Me quede aqui, viendo como hago el query que me traiga solo las pop_movies
                //Dame all de lo de movie q este en PopMovie


//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        FavoriteMovieContract.PopMovieEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        null
//                );
                break;
            }

            case TOP_RATED_MOVIE: {
                retCursor = getMoviesByTopRated(uri, projection, sortOrder);

                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
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
            case FAVORITE_MOVIE:
                return FavoriteMovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            case MOVIE:
                return FavoriteMovieContract.MovieEntry.CONTENT_TYPE;
            case POP_MOVIE:
                return FavoriteMovieContract.MovieEntry.CONTENT_TYPE;
            case TOP_RATED_MOVIE:
                return FavoriteMovieContract.MovieEntry.CONTENT_TYPE;
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
                long _id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoriteMovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(FavoriteMovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FavoriteMovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POP_MOVIE: {
                long _id = db.insert(FavoriteMovieContract.PopMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FavoriteMovieContract.PopMovieEntry.buildPopMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOP_RATED_MOVIE: {
                long _id = db.insert(FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FavoriteMovieContract.TopRatedMovieEntry.buildTopRatedMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
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
                        FavoriteMovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_MOVIE:
                rowsDeleted = db.delete(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case POP_MOVIE:
                rowsDeleted = db.delete(
                        FavoriteMovieContract.PopMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIE:
                rowsDeleted = db.delete(
                        FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME, selection, selectionArgs);
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
                rowsUpdated = db.update(FavoriteMovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITE_MOVIE:
                rowsUpdated = db.update(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case POP_MOVIE:
                rowsUpdated = db.update(FavoriteMovieContract.PopMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TOP_RATED_MOVIE:
                rowsUpdated = db.update(FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME, values, selection,
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
            case FAVORITE_MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case POP_MOVIE:
                db.beginTransaction();
                int returnCount2 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FavoriteMovieContract.MovieEntry.TABLE_NAME, null, value);
                        ContentValues favMovieValues = new ContentValues();
                        favMovieValues.put(FavoriteMovieContract.PopMovieEntry.COLUMN_MOVIE_KEY, String.valueOf(_id));
                        db.insert(FavoriteMovieContract.PopMovieEntry.TABLE_NAME, null, favMovieValues);
                        if (_id != -1) {
                            returnCount2++;
                        }
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
                        long _id = db.insert(FavoriteMovieContract.MovieEntry.TABLE_NAME, null, value);
                        ContentValues favMovieValues = new ContentValues();
                        favMovieValues.put(FavoriteMovieContract.TopRatedMovieEntry.COLUMN_MOVIE_KEY, String.valueOf(_id));
                        db.insert(FavoriteMovieContract.TopRatedMovieEntry.TABLE_NAME, null, favMovieValues);
                        if (_id != -1) {
                            returnCount3++;
                        }
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
        final String authority = FavoriteMovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteMovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, FavoriteMovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, FavoriteMovieContract.PATH_FAVORITE_MOVIE, FAVORITE_MOVIE);
        matcher.addURI(authority, FavoriteMovieContract.PATH_POP_MOVIE, POP_MOVIE);
        matcher.addURI(authority, FavoriteMovieContract.PATH_TOP_RATED_MOVIE, TOP_RATED_MOVIE);
//        matcher.addURI(authority, FavoriteMovieContract.PATH_MOVIE + "/*/#", MOVIE_REVIEW_AND_TRAILERS);

        return matcher;
    }
}
