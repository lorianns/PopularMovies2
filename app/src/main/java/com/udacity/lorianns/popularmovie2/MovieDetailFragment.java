package com.udacity.lorianns.popularmovie2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lorianns.popularmovie2.adapters.MovieDetailsRecyclerViewAdapter;
import com.udacity.lorianns.popularmovie2.async_tasks.FetchMovieReviewTask;
import com.udacity.lorianns.popularmovie2.async_tasks.FetchMovieVideoTask;
import com.udacity.lorianns.popularmovie2.custom_recycler_view.DividerItemDecoration;
import com.udacity.lorianns.popularmovie2.entities.MovieEntity;
import com.udacity.lorianns.popularmovie2.entities.ReviewEntity;
import com.udacity.lorianns.popularmovie2.entities.VideoEntity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;


/**
 * Encapsulates fetching the movie trailers and reviews and displaying with the movie description
 * as a {@link RecyclerView} layout.
 */
public class MovieDetailFragment extends Fragment implements FetchMovieReviewTask.FetchMovieReviewCallback, FetchMovieVideoTask.FetchMovieVideoCallback{

//    @BindView(R.id.btnFavorite)
//    Button btnFavorite;

    private MovieEntity movie;
    private MovieDetailsRecyclerViewAdapter adapter;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        adapter = new MovieDetailsRecyclerViewAdapter(getActivity());

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(recyclerView.getContext())
        );

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.list_divider));
        recyclerView.addItemDecoration(itemDecoration);

        // The detail Activity called via intent.  Inspect the intent for movie data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MOVIE_DATA")) {
            movie = intent.getParcelableExtra("MOVIE_DATA");
            adapter.setHeader(movie);
        }
        else if(getArguments() != null && getArguments().containsKey("MOVIE_DATA")){
            movie = getArguments().getParcelable("MOVIE_DATA");
            adapter.setHeader(movie);
        }

        recyclerView.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        if(movie != null) {
            fetchMovieReviewData(movie.getApiId());
            fetchMovieVideoData(movie.getApiId());
        }
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

    @Override
    public void onMovieReviewPreExecute() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieReviewFetchCompleted(ReviewEntity[] result) {
        if (result != null) {
            Log.e("List", result.toString());
            adapter.setReview(new ArrayList<ReviewEntity>(Arrays.asList(result)));
        }
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
