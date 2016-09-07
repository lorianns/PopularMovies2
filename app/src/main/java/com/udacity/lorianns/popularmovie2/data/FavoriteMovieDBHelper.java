package com.udacity.lorianns.popularmovie2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.lorianns.popularmovie2.data.MovieContract.PopMovieEntry;
import com.udacity.lorianns.popularmovie2.data.MovieContract.TopRatedMovieEntry;
import com.udacity.lorianns.popularmovie2.data.MovieContract.FavoriteMovieEntry;
import com.udacity.lorianns.popularmovie2.data.MovieContract.MovieEntry;

/**
 * Created by lorianns on 7/10/16.
 */
public class FavoriteMovieDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorite_movie.db";


    public FavoriteMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME +" (" +

                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RATING + " TEXT NOT NULL," +
                MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL," +
                "UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                " );";

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                FavoriteMovieEntry.COLUMN_MOVIE_KEY + " TEXT UNIQUE NOT NULL ," +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + FavoriteMovieEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " + //ON DELETE CASCADE
                ");";

        final String SQL_CREATE_POP_MOVIE_TABLE = "CREATE TABLE " + PopMovieEntry.TABLE_NAME + " (" +
                PopMovieEntry._ID + " INTEGER PRIMARY KEY," +
                PopMovieEntry.COLUMN_MOVIE_KEY + " TEXT UNIQUE NOT NULL ," +

                // Set up the movie_key column as a foreign key to movie table.
                " FOREIGN KEY (" + PopMovieEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ")  ON DELETE CASCADE" +
                ");";

        final String SQL_CREATE_TOP_RATED_MOVIE_TABLE = "CREATE TABLE " + TopRatedMovieEntry.TABLE_NAME + " (" +
                TopRatedMovieEntry._ID + " INTEGER PRIMARY KEY," +
                TopRatedMovieEntry.COLUMN_MOVIE_KEY + " TEXT UNIQUE NOT NULL ," +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + TopRatedMovieEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ")  ON DELETE CASCADE" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_POP_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
