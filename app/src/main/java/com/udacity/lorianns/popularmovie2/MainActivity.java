package com.udacity.lorianns.popularmovie2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.udacity.lorianns.popularmovie2.entities.MovieEntity;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public void onItemSelected(MovieEntity movieEntity, int position) {
        ((MovieListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container)).mPosition = position;
        ((MovieListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container)).mRecyclerView.scrollToPosition(position);

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable("MOVIE_DATA", movieEntity);

            showDetailsView(true);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("MOVIE_DATA", movieEntity);
            startActivity(intent);
        }
    }

    public void showDetailsView(Boolean flag){
        if(mTwoPane)
            if(flag)
                findViewById(R.id.movie_detail_container).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.movie_detail_container).setVisibility(View.GONE);
    }

    public Boolean isShowedDetailsView(){
        if(mTwoPane)
            if(findViewById(R.id.movie_detail_container).getVisibility() == View.VISIBLE)
                return true;

        return false;
    }
}
