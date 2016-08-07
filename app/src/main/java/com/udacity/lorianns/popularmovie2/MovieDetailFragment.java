package com.udacity.lorianns.popularmovie2;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements FetchMovieReviewTask.FetchMovieReviewCallback, FetchMovieVideoTask.FetchMovieVideoCallback{

//    @BindView(R.id.btnFavorite)
//    Button btnFavorite;

    private MovieEntity movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView releaseYear = (TextView) rootView.findViewById(R.id.releaseYear);
        TextView rating = (TextView) rootView.findViewById(R.id.rating);
        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.imageView);

        // The detail Activity called via intent.  Inspect the intent for movie data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MOVIE_DATA")) {
            movie = intent.getParcelableExtra("MOVIE_DATA");

            title.setText(movie.getTitle());
            releaseYear.setText(movie.getReleaseDate());
            rating.setText(String.format(getString(R.string.rating), movie.getRating()));
            overview.setText(movie.getOverview());

            Picasso.with(getActivity()).load(movie.getImagePath()).into(ivPoster);
        }

        Button btnFavorite = (Button) rootView.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
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
        ContentValues movieValues = new ContentValues();

        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_IMAGE, movie.getImagePath());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        // Insert our ContentValues
        getActivity().getContentResolver().insert(FavoriteMovieContract.MovieEntry.CONTENT_URI,
                movieValues);
    }

    @Override
    public void onMovieReviewPreExecute() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieReviewFetchCompleted(ReviewEntity[] result) {
        Log.e("List", result.toString());
//        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMovieVideoPreExecute() {

    }

    @Override
    public void onMovieVideoFetchCompleted(VideoEntity[] result) {
        Log.e("List", result.toString());
    }
}
