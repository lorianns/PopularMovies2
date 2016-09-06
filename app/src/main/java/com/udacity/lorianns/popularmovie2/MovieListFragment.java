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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.udacity.lorianns.popularmovie2.adapters.SimpleImageCursorAdapter;
import com.udacity.lorianns.popularmovie2.async_tasks.FetchMovieTask;
import com.udacity.lorianns.popularmovie2.data.MovieContract;
import com.udacity.lorianns.popularmovie2.entities.MovieEntity;


/**
 * Encapsulates fetching the movies and displaying it as a {@link RecyclerView} layout.
 */
public class MovieListFragment extends Fragment implements FetchMovieTask.FetchMovieCallback, LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleImageCursorAdapter mImageAdapter;

    private RecyclerView mRecyclerView;
    public int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";
    private static final String SELECTED_LOADER = "selected_loader";

    private static final int POP_MOVIE_LOADER = 0;
    private static final int TOP_RATED_MOVIE_LOADER = 1;
    private static final int FAVORITE_MOVIE_LOADER = 2;

    private int selectedLoader = POP_MOVIE_LOADER;
    private int selectedPosition;

    private ProgressBar progressBar;

    private static final String[] FAVORITES_MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.movielistfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The SimpleImageCursorAdapter will take data from a source and
        // use it to populate the RecyclerView it's attached to.
        mImageAdapter = new SimpleImageCursorAdapter(getActivity(), null);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Get a reference to the ProgressBar
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        setupRecyclerView(rootView, mRecyclerView);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_LOADER)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            getLoaderManager().restartLoader(selectedLoader, null, this);
        }

        return rootView;
    }

    private void setupRecyclerView(View view, RecyclerView recyclerView) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_most_popular:
                selectedLoader = POP_MOVIE_LOADER;
                fetchMovieData(getString(R.string.pref_sort_by_default));
                return true;
            case R.id.action_top_rated:
                selectedLoader = TOP_RATED_MOVIE_LOADER;
                fetchMovieData(getString(R.string.pref_sort_by_top_rated));
                return true;
            case R.id.action_favorites:
                selectedLoader = FAVORITE_MOVIE_LOADER;
//                if(getLoaderManager().getLoader(selectedLoader) == null )
                    getLoaderManager().initLoader(selectedLoader, null, this);
//                else
//                    getLoaderManager().restartLoader(selectedLoader, null, this);

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

        outState.putInt(SELECTED_LOADER, selectedLoader);
    }

    private void fetchMovieData(String sortBy) {
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

        if (result != null)
            getLoaderManager().initLoader(selectedLoader, null, this);

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            fetchMovieData(getString(R.string.pref_sort_by_default));

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        Uri uri = null;
        String[] columns = FAVORITES_MOVIE_COLUMNS;
        switch (id){

            case POP_MOVIE_LOADER:
                uri = MovieContract.PopMovieEntry.buildFavoritePopMovieWithMovie();
                break;

            case TOP_RATED_MOVIE_LOADER:
                uri = MovieContract.TopRatedMovieEntry.buildTopRatedMovieUri();
                break;

            case FAVORITE_MOVIE_LOADER:
                uri = MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri();
                break;
        }

        return new CursorLoader(getActivity(),
                uri,
                columns,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mImageAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
//             If we don't need to restart the loader, and there's a desired position to restore
//             to, do so now.
//            mImageAdapter.smoothScrollToPosition(mPosition);
            mRecyclerView.scrollToPosition(mPosition);
        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mImageAdapter.swapCursor(null);
    }
}
