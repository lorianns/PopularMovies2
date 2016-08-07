package com.udacity.lorianns.popularmovie2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.lorianns.popularmovie2.data.FavoriteMovieContract;

/**
 * Created by lorianns on 7/31/16.
 */
public class SimpleImageCursorAdapter extends CursorRecyclerViewAdapter<SimpleImageCursorAdapter.ViewHolder>{

    private Context mContext;
    private Cursor mCursor;

    public SimpleImageCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        mContext = context;
        mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public final View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        final MovieEntity movieEntity = new MovieEntity();
        movieEntity.fromCursor(cursor);
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("MOVIE_DATA", movieEntity);
                context.startActivity(intent);
            }
        });

        Picasso.with(mContext)
                .load(cursor.getString(FavoriteMovieContract.MovieEntry.COL_MOVIE_IMAGE))
                .into( viewHolder.mImageView);

    }
//
//    @Override
//    public int getItemCount() {
//        if (mCursor != null) {
//            return mCursor.getCount();
//        }
//        return 0;
//    }

}
