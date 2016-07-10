package com.udacity.lorianns.popularmovie2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements FetchMovieTask.FetchMovieCallback {

    private ArrayList<MovieEntity> movieArray;
    private String selectedSort;
    private ProgressBar progressBar;
    private SimpleImageRecyclerViewAdapter imageAdapter;
    View rootView;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectedSort = getString(R.string.pref_sort_by_default);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        if (savedInstanceState == null || !savedInstanceState.containsKey("MOVIE_ARRAY")) {
            movieArray = new ArrayList<>();
        } else {
            movieArray = savedInstanceState.getParcelableArrayList("MOVIE_ARRAY");
            selectedSort = savedInstanceState.getString("SORT_BY");
        }

        // Get a reference to the ProgressBar
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        setupRecyclerView(rv);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        imageAdapter = new SimpleImageRecyclerViewAdapter(getActivity(),
                movieArray);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovieData(selectedSort);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.movielistfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_most_popular:
                fetchMovieData(getString(R.string.pref_sort_by_default));
                return true;
            case R.id.action_top_rated:
                fetchMovieData(getString(R.string.pref_sort_by_top_rated));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("MOVIE_ARRAY", movieArray);
        outState.putString("SORT_BY", selectedSort);
    }

    private void fetchMovieData(String sortBy) {
        selectedSort = sortBy;
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.setListener(this);
        movieTask.execute(sortBy);
    }

    @Override
    public void onMoviePreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieFetchCompleted(MovieEntity[] result) {

        if (result != null) {
            imageAdapter.clear();
            //Update the Gridview adapter with the fetch movies results
            movieArray.addAll(new ArrayList<MovieEntity>(Arrays.asList(result)));
            imageAdapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);

    }

}
