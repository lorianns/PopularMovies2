package com.udacity.lorianns.popularmovie2;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements FetchMovieTask.FetchMovieCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<MovieEntity> movieArray = new ArrayList<>();
    private Cursor movieCursorArray;
    private String selectedSort;
    private ProgressBar progressBar;
    private RecyclerView rv;
//        private SimpleImageRecyclerViewAdapter imageOldAdapter;
    private SimpleImageCursorAdapter imageAdapter;
    public int mPosition = ListView.INVALID_POSITION;
    View rootView;
    private Uri mUri;
    //    private static final int DETAIL_LOADER = 0;

    private static final int POP_MOVIE_LOADER = 0;
    private static final int TOP_RATED_MOVIE_LOADER = 1;
    private static final int FAVORITE_MOVIE_LOADER = 2;

    private static final String SELECTED_KEY = "selected_position";

    private static final String[] FAVORITES_MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            FavoriteMovieContract.MovieEntry.TABLE_NAME + "." + FavoriteMovieContract.MovieEntry._ID,
            FavoriteMovieContract.MovieEntry.COLUMN_SYNOPSIS,
            FavoriteMovieContract.MovieEntry.COLUMN_RATING,
            FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            FavoriteMovieContract.MovieEntry.COLUMN_IMAGE,
            FavoriteMovieContract.MovieEntry.COLUMN_TITLE,
            FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID,
    };

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(MovieEntity movieEntity, int position);
    }

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        selectedSort = getString(R.string.pref_sort_by_default);
        imageAdapter = new SimpleImageCursorAdapter(getActivity(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Get a reference to the ProgressBar
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        setupRecyclerView(rv);

//        if (savedInstanceState == null || !savedInstanceState.containsKey("MOVIE_ARRAY")) {
//            movieArray = new ArrayList<>();
//        } else

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            movieArray = savedInstanceState.getParcelableArrayList("MOVIE_ARRAY");
//            imageAdapter.swapCursor(movieArray);
//            selectedSort = savedInstanceState.getString("SORT_BY");
        }
//        else{
//            rv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//            rv.setLayoutManager(
//                    new LinearLayoutManager(rv.getContext())
//            );
//            rv.setAdapter(imageAdapter);
//        }

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        imageAdapter = new SimpleImageRecyclerViewAdapter(getActivity(),
//                movieArray);
//        recyclerView.setAdapter(imageAdapter);

        rv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

//        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return (3 - position % 3);
//            }
//        });

//        rv.setLayoutManager(manager);

//        rv.setLayoutManager(
//                new GridLayoutManager(rv.getContext(), 1)
//        );

//        imageAdapter = new SimpleImageCursorAdapter(getActivity(), null);
        rv.setAdapter(imageAdapter);

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
                //old
//                imageOldAdapter = new SimpleImageRecyclerViewAdapter(getActivity(), movieArray);
//                rv.setAdapter(imageOldAdapter);
                selectedSort = getString(R.string.pref_sort_by_default);
                fetchMovieData(getString(R.string.pref_sort_by_default));
//                getLoaderManager().restartLoader(POP_MOVIE_LOADER, null, this);
                return true;
            case R.id.action_top_rated:
                //old
//                imageOldAdapter = new SimpleImageRecyclerViewAdapter(getActivity(), movieArray);
//                rv.setAdapter(imageOldAdapter);
                selectedSort = getString(R.string.pref_sort_by_top_rated);
                fetchMovieData(getString(R.string.pref_sort_by_top_rated));
//                getLoaderManager().restartLoader(TOP_RATED_MOVIE_LOADER, null, this);
                return true;
            case R.id.action_favorites:
                Uri uri = mUri;
//                if (null != uri) {
//                    Uri updatedUri = FavoriteMovieContract.MovieEntry.buildMovieUri();
//                    mUri = updatedUri;
                selectedSort = getString(R.string.pref_sort_by_favorite);
//                getLoaderManager().restartLoader(TOP_RATED_MOVIE_LOADER, null, this);
//                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        outState.putParcelableArrayList("MOVIE_ARRAY", movieArray);
        outState.putString("SORT_BY", selectedSort);
    }

    private void fetchMovieData(String sortBy) {
        selectedSort = sortBy;
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
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
//            imageAdapter.clear();
            //Update the Gridview adapter with the fetch movies results
            movieArray.addAll(new ArrayList<MovieEntity>(Arrays.asList(result)));
//            imageOldAdapter.notifyDataSetChanged();

            getLoaderManager().initLoader(POP_MOVIE_LOADER, null, this);
        }
        progressBar.setVisibility(View.GONE);

    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        getLoaderManager().initLoader(TOP_RATED_MOVIE_LOADER, null, this);
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, filter the query to return weather only for
        // dates after or including today.

        // Sort order:  Ascending, by date.
//        String sortOrder = FavoriteMovieContract.MovieEntry.TABLE_NAME + " ASC";
        Uri weatherForLocationUri = null;
        String[] columns = null;
        switch (id){

            case POP_MOVIE_LOADER:
                weatherForLocationUri = FavoriteMovieContract.PopMovieEntry.buildFavoritePopMovieWithMovie();
//                weatherForLocationUri = FavoriteMovieContract.MovieEntry.buildMovieUri();
                columns = FAVORITES_MOVIE_COLUMNS;
                break;

            case TOP_RATED_MOVIE_LOADER:
                weatherForLocationUri = FavoriteMovieContract.TopRatedMovieEntry.buildTopRatedMovieUri();
                columns = FAVORITES_MOVIE_COLUMNS;
                break;

            case FAVORITE_MOVIE_LOADER:
                weatherForLocationUri = FavoriteMovieContract.FavoriteMovieEntry.buildFavoriteMovieUri();
                columns = FAVORITES_MOVIE_COLUMNS;
                break;
        }

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                columns,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("LOADER", "finished");
//        Log.d("LOADER", data.getString(0));
        movieCursorArray = data;
        imageAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
//             If we don't need to restart the loader, and there's a desired position to restore
//             to, do so now.
//            imageAdapter.smoothScrollToPosition(mPosition);
            rv.scrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageAdapter.swapCursor(null);
    }
}
