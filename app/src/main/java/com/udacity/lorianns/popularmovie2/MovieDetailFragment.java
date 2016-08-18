package com.udacity.lorianns.popularmovie2;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements FetchMovieReviewTask.FetchMovieReviewCallback, FetchMovieVideoTask.FetchMovieVideoCallback{

//    @BindView(R.id.btnFavorite)
//    Button btnFavorite;

    private MovieEntity movie;
    private MovieDetailsRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(recyclerView.getContext())
        );
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider));
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new MovieDetailsRecyclerViewAdapter(getActivity());

        // The detail Activity called via intent.  Inspect the intent for movie data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MOVIE_DATA")) {
            movie = intent.getParcelableExtra("MOVIE_DATA");
            adapter.setHeader(movie);
        }

        recyclerView.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovieReviewData(movie.getId());
        fetchMovieVideoData(movie.getId());
    }

    private void fetchMovieReviewData(String id) {
        FetchMovieReviewTask movieReviewTask = new FetchMovieReviewTask();
        movieReviewTask.setListener(this);
        movieReviewTask.execute(id);
    }

    private void fetchMovieVideoData(String id) {
        FetchMovieVideoTask movieVideoTask = new FetchMovieVideoTask();
        movieVideoTask.setListener(this);
        movieVideoTask.execute(id);
    }

//    @OnClick(R.id.btnFavorite)
//    void onClickFavoriteData(Button view){
//        insertData();
//    }

    // insert data into database
    public void insertData(){

        long locationId = addMovie(movie);


        ContentValues favMovieValues = new ContentValues();
        favMovieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_KEY, String.valueOf(locationId));
//        favMovieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_IMAGE, movie.getImagePath());

//        int inserted = 0;
//        // add to database
//        if ( cVVector.size() > 0 ) {
//            ContentValues[] cvArray = new ContentValues[cVVector.size()];
//            cVVector.toArray(cvArray);
//            getContext().getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);
//
//            // delete old data so we don't build up an endless history
//            getContext().getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,
//                    WeatherContract.WeatherEntry.COLUMN_DATE + " <= ?",
//                    new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});
//
//            notifyWeather();
//        }


        // Insert our ContentValues
        getActivity().getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                favMovieValues);

        // Insert our ContentValues
//        getActivity().getContentResolver().insert(FavoriteMovieContract.MovieEntry.CONTENT_URI,
//                movieValues);
    }

    private long addMovie(MovieEntity movie){

        long locationId;

        // First, check if the location with this city name exists in the db
//        Cursor locationCursor = getContext().getContentResolver().query(
//                FavoriteMovieContract.MovieEntry.CONTENT_URI,
//                new String[]{FavoriteMovieContract.MovieEntry._ID},
//                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
//                new String[]{movie.getId()},
//                null);
//
//        if (locationCursor.moveToFirst()) {
//            int locationIdIndex = locationCursor.getColumnIndex(FavoriteMovieContract.MovieEntry._ID);
//            locationId = locationCursor.getLong(locationIdIndex);
//        } else {

            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_IMAGE, movie.getImagePath());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
            movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

            Uri insertedUri = getActivity().getContentResolver().insert(FavoriteMovieContract.MovieEntry.CONTENT_URI,
                    movieValues);

            locationId = ContentUris.parseId(insertedUri);
//        }
//        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }
    @Override
    public void onMovieReviewPreExecute() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieReviewFetchCompleted(ReviewEntity[] result) {
        Log.e("List", result.toString());
        adapter.setReview(new ArrayList<ReviewEntity>(Arrays.asList(result)));
//        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onMovieVideoPreExecute() {

    }

    @Override
    public void onMovieVideoFetchCompleted(VideoEntity[] result) {
        Log.e("List", result.toString());
        adapter.setTrailer(new ArrayList<VideoEntity>(Arrays.asList(result)));
    }
}
