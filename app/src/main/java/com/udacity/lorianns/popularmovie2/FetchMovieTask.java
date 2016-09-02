package com.udacity.lorianns.popularmovie2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by lorianns on 7/10/16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, MovieEntity[]> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private Context mContext;
    public FetchMovieCallback mListener;

    public FetchMovieTask(Context context) {
        super();
        mContext = context;
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface FetchMovieCallback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onMoviePreExecute();

        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onMovieFetchCompleted(MovieEntity[] result);
    }

    public void setListener(FetchMovieCallback listener){
        mListener = listener;
    }

    private MovieEntity[] getMovieDataFromJson(String sortBy, String movieJsonStr) throws JSONException {

        final String _LIST = "results";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieJsonArray = movieJson.getJSONArray(_LIST);
        MovieEntity[] movieList = new MovieEntity[movieJsonArray.length()];

        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieJsonArray.length());

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject movie = movieJsonArray.getJSONObject(i);
            movieList[i] = new MovieEntity(movie);

            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movieList[i].getApiId());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_IMAGE, movieList[i].getImagePath());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movieList[i].getTitle());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_SYNOPSIS, movieList[i].getOverview());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RATING, movieList[i].getRating());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieList[i].getReleaseDate());
            cVVector.add(movieValues);
        }

        int inserted = 0;
        Uri uri = null;


            if(sortBy.equals(mContext.getString(R.string.pref_sort_by_top_rated)))
                uri = FavoriteMovieContract.TopRatedMovieEntry.CONTENT_URI;
            else if(sortBy.equals(mContext.getString(R.string.pref_sort_by_default))){
                uri = FavoriteMovieContract.PopMovieEntry.CONTENT_URI;
            }


        // add to database
        if ( cVVector.size() > 0 ) {

            // delete old data so we don't build up an endless history
            mContext.getContentResolver().delete(FavoriteMovieContract.MovieEntry.CONTENT_URI, null, null);

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(uri, cvArray);

            // delete old data so we don't build up an endless history
//            mContext.getContentResolver().delete(FavoriteMovieContract.MovieEntry.CONTENT_URI,
//                    null,
//                    null);

//            notifyWeather();
        }

        Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");



//        Uri insertedUri = getActivity().getContentResolver().insert(FavoriteMovieContract.MovieEntry.CONTENT_URI,
//                movieValues);

//        locationId = ContentUris.parseId(insertedUri);

        return movieList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onMoviePreExecute();
    }

    @Override
    protected MovieEntity[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        String sortBy = params[0];

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
                return null;
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
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
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

        try {
            return getMovieDataFromJson(sortBy, movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(MovieEntity[] result) {
        mListener.onMovieFetchCompleted(result);
    }

}
