package com.udacity.lorianns.popularmovie2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lorianns on 8/6/16.
 */
public class FetchMovieReviewTask extends AsyncTask<String, Void, ReviewEntity[]> {
    private final String LOG_TAG = FetchMovieReviewTask.class.getSimpleName();
    public FetchMovieReviewCallback mListener;

    public FetchMovieReviewTask() {
        super();
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface FetchMovieReviewCallback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onMovieReviewPreExecute();

        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onMovieReviewFetchCompleted(ReviewEntity[] result);
    }

    public void setListener(FetchMovieReviewCallback listener){
        mListener = listener;
    }

    private ReviewEntity[] getMovieReviewDataFromJson(String movieJsonStr) throws JSONException {

        final String _LIST = "results";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieJsonArray = movieJson.getJSONArray(_LIST);
        ReviewEntity[] movieList = new ReviewEntity[movieJsonArray.length()];

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject movie = movieJsonArray.getJSONObject(i);
            movieList[i] = new ReviewEntity(movie);
        }
        return movieList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onMovieReviewPreExecute();
    }

    @Override
    protected ReviewEntity[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        String movieId = params[0];

        try {
            // Construct the URL for the themoviedb query
            // Possible parameters are avaiable at themoviedb API page, at
            // https://www.themoviedb.org/
            final String FORECAST_BASE_URL =
                    "https://api.themoviedb.org/3/movie/" + movieId + "/reviews" + "?";
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
            return getMovieReviewDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ReviewEntity[] result) {
        mListener.onMovieReviewFetchCompleted(result);
    }
}
