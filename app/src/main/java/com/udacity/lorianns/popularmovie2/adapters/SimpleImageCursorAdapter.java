package com.udacity.lorianns.popularmovie2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.lorianns.popularmovie2.MovieListFragment;
import com.udacity.lorianns.popularmovie2.R;
import com.udacity.lorianns.popularmovie2.data.MovieContract;
import com.udacity.lorianns.popularmovie2.entities.MovieEntity;

/**
 * Created by lorianns on 7/31/16.
 */
public class SimpleImageCursorAdapter extends CursorRecyclerViewAdapter<SimpleImageCursorAdapter.ViewHolder> {

    private Context mContext;

    public SimpleImageCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public View mborderLineTop;
        public View mborderLineBottom;
        public final View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mborderLineTop = view.findViewById(R.id.borderLineTop);
            mborderLineBottom = view.findViewById(R.id.borderLineBottom);
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
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {

        final MovieEntity movieEntity = new MovieEntity();
        movieEntity.fromCursor(cursor);
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                movieEntity.setSelected(true);
                if(viewHolder.mborderLineTop != null){
                    viewHolder.mborderLineTop.setVisibility(View.VISIBLE);
                    viewHolder.mborderLineBottom.setVisibility(View.VISIBLE);
                }

                ((MovieListFragment.Callback) context)
                        .onItemSelected(movieEntity, cursor.getPosition());
            }
        });

        Picasso.with(mContext)
                .load(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_IMAGE))
                .into( viewHolder.mImageView);

        if(movieEntity.isSelected() && viewHolder.mborderLineTop != null){
            viewHolder.mborderLineTop.setVisibility(View.VISIBLE);
            viewHolder.mborderLineBottom.setVisibility(View.VISIBLE);
        }
        else if(viewHolder.mborderLineTop != null){
            viewHolder.mborderLineTop.setVisibility(View.GONE);
            viewHolder.mborderLineBottom.setVisibility(View.GONE);
        }
    }
}
