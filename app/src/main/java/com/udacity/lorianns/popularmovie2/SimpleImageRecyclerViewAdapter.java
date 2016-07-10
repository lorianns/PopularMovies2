package com.udacity.lorianns.popularmovie2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lorianns on 7/6/16.
 */
public class SimpleImageRecyclerViewAdapter extends RecyclerView.Adapter<SimpleImageRecyclerViewAdapter.ViewHolder>{

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<MovieEntity> mValues;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MovieEntity mMovieEntity;

        public final View mView;
        public final ImageView mImageView;
//        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageView);
//            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

    }

    public SimpleImageRecyclerViewAdapter(Context context, List<MovieEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mMovieEntity = mValues.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("MOVIE_DATA", holder.mMovieEntity);

                context.startActivity(intent);
            }
        });

        Picasso.with(holder.mImageView.getContext()).load(holder.mMovieEntity.getImagePath()).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void clear(){
        mValues.clear();
    }
}
