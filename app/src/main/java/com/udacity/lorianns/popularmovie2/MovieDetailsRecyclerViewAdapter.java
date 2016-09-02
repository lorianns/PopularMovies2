package com.udacity.lorianns.popularmovie2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorianns on 8/14/16.
 */
public class MovieDetailsRecyclerViewAdapter extends RecyclerView.Adapter<MovieDetailsRecyclerViewAdapter.ViewHolder>{

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<VideoEntity> mTrailerValues = new ArrayList<>();
    private List<ReviewEntity> mReviewValues = new ArrayList<>();;
    private static final int TYPE_HEADER = 0, TYPE_TRAILER = 1, TYPE_REVIEW = 2;
    private MovieEntity movieEntity;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MovieEntity movieEntity;
        public VideoEntity mVideoEntity;
        public ReviewEntity mReviewEntity;

        public final View mView;

        TextView mTextView;
        TextView title, releaseYear, rating, overview;
        TextView content, author;
        ImageView ivPoster;

        Button btnFavorite;

        public ViewHolder(View view, int viewType) {
            super(view);
            mView = view;

            switch(viewType)
            {
                case TYPE_HEADER:
                    title = (TextView) view.findViewById(R.id.title);
                    releaseYear = (TextView) view.findViewById(R.id.releaseYear);
                    rating = (TextView) view.findViewById(R.id.rating);
                    overview = (TextView) view.findViewById(R.id.overview);
                    ivPoster = (ImageView) view.findViewById(R.id.imageView);
                    btnFavorite = (Button) view.findViewById(R.id.btnFavorite);

                    break;

                case TYPE_TRAILER:
                    mTextView = (TextView) view.findViewById(R.id.textView);
                    break;

                case TYPE_REVIEW:
                    content = (TextView) view.findViewById(R.id.tvContent);
                    author = (TextView) view.findViewById(R.id.tvAuthor);
                    break;
            }
        }
    }

    public MovieDetailsRecyclerViewAdapter(Context context) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.context = context;
    }

    public void setHeader(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
        notifyDataSetChanged();
    }

    public void setTrailer(List<VideoEntity> items) {
        mTrailerValues = items;
//        mDataSet = list;
        notifyDataSetChanged();
    }

    public void setReview(ArrayList<ReviewEntity> list) {
        mReviewValues =list;
//        mDataSet = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view;

        switch(viewType)
        {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_header_item, parent, false);
                holder = new ViewHolder(view, TYPE_HEADER);
                break;

            case TYPE_TRAILER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_trailer_item, parent, false);
                view.setBackgroundResource(mBackground);
                holder = new ViewHolder(view, TYPE_TRAILER);
                break;

            case TYPE_REVIEW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_review_item, parent, false);
                view.setBackgroundResource(mBackground);
                holder = new ViewHolder(view, TYPE_REVIEW);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        switch(getItemViewType(position)) {
            case TYPE_HEADER:
                holder.movieEntity = movieEntity;
                if(movieEntity != null)
                    setHeaderData(holder);
                break;

            case TYPE_TRAILER:
                holder.mVideoEntity = mTrailerValues.get(position - 1);
                setTrailerData(holder);
                break;

            case TYPE_REVIEW:
                if(mTrailerValues.size() > 0)
                    position = position - mTrailerValues.size();
                holder.mReviewEntity = mReviewValues.get(position - 1);
                setReviewData(holder);
                break;
        }
    }


    public void clear(){
        mTrailerValues.clear();
    }

    @Override
    public int getItemCount() {
        int listSize = 0;

        if(mTrailerValues != null)
            listSize = mTrailerValues.size();

        if(mReviewValues != null)
            listSize += mReviewValues.size();

        return  listSize + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        else if(mTrailerValues !=null && mTrailerValues.size() > 0 && position < mTrailerValues.size() + 1){
            return TYPE_TRAILER;
        }
        else if(mReviewValues !=null && mReviewValues.size() > 0 && position < mReviewValues.size() + mTrailerValues.size() + 1){
            return TYPE_REVIEW;
        }
        else
            return TYPE_HEADER;

//            switch (position) {
//            switch (mDataSet.get(position - 1)) {
//                case TYPE_TRAILER:
//                    return TYPE_TRAILER;
//                case TYPE_REVIEW:
//                    return TYPE_REVIEW;
//                default:
//                    return TYPE_HEADER;
//            }
//        }
    }

    private void setHeaderData(final ViewHolder holder) {
        holder.title.setText(holder.movieEntity.getTitle());
        holder.releaseYear.setText(holder.movieEntity.getReleaseDate());
        holder.rating.setText(String.format(context.getString(R.string.rating), holder.movieEntity.getRating()));
        holder.overview.setText(holder.movieEntity.getOverview());

        Picasso.with(context).load(holder.movieEntity.getImagePath()).into(holder.ivPoster);

        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(holder.movieEntity);

                // delete old data so we don't build up an endless history
//                context.getContentResolver().delete(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
//                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_KEY + " = ?",
//                        new String[] {movieEntity.getId()});
            }
        });
    }

    private void setTrailerData(final ViewHolder holder) {

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + holder.mVideoEntity.getKey()));
                context.startActivity(intent);
            }
        });

        holder.mTextView.setText(holder.mVideoEntity.getName());
    }

    private void setReviewData(final ViewHolder holder) {
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.mReviewEntity.getUrl()));
                context.startActivity(intent);
            }
        });

        holder.content.setText(holder.mReviewEntity.getContent());
        holder.author.setText(holder.mReviewEntity.getAuthor());
    }

    // insert data into database
    public void insertData(MovieEntity movieEntity){

        long locationId = addMovie(movieEntity);


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
        context.getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
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
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getApiId());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_IMAGE, movie.getImagePath());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        movieValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        Uri insertedUri = context.getContentResolver().insert(FavoriteMovieContract.MovieEntry.CONTENT_URI,
                movieValues);

        locationId = ContentUris.parseId(insertedUri);
//        }
//        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }
}
