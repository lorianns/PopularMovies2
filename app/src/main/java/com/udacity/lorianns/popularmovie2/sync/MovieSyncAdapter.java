package com.udacity.lorianns.popularmovie2.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.udacity.lorianns.popularmovie2.BuildConfig;
import com.udacity.lorianns.popularmovie2.MovieEntity;
import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by lorianns on 7/7/16.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        String sortBy = "";

        try {
            // Construct the URL for the themoviedb query
            // Possible parameters are avaiable at themoviedb API page, at
            // https://www.themoviedb.org/
            final String FORECAST_BASE_URL =
                    "https://api.themoviedb.org/3/movie/" + sortBy + "?";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to themoviedb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return;
    }

    private void getMovieDataFromJson(String movieJsonStr) throws JSONException {

        final String _LIST = "results";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieJsonArray = movieJson.getJSONArray(_LIST);
        MovieEntity[] movieList = new MovieEntity[movieJsonArray.length()];

        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieList.length);

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject movie = movieJsonArray.getJSONObject(i);
            movieList[i] = new MovieEntity(movie);

            ContentValues weatherValues = new ContentValues();

            weatherValues.put(FavoriteMovieContract.MovieEntry.COLUMN_IMAGE, movieList[i].getImagePath());
            weatherValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movieList[i].getTitle());
            weatherValues.put(FavoriteMovieContract.MovieEntry.COLUMN_SYNOPSIS, movieList[i].getOverview());
            weatherValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RATING, movieList[i].getRating());
            weatherValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieList[i].getReleaseDate());

            cVVector.add(weatherValues);
        }



        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(FavoriteMovieContract.MovieEntry.CONTENT_URI, cvArray);

            // delete old data so we don't build up an endless history
//            getContext().getContentResolver().delete(FavoriteMovieContract.MovieEntry.CONTENT_URI,
//                    FavoriteMovieContract.MovieEntry.COLUMN_DATE + " <= ?",
//                    new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});

//            notifyWeather();
        }

        Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

    }
}
