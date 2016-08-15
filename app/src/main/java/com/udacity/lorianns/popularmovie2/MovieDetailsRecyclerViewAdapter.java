package com.udacity.lorianns.popularmovie2;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorianns on 8/14/16.
 */
public class MovieDetailsRecyclerViewAdapter extends RecyclerView.Adapter<MovieDetailsRecyclerViewAdapter.ViewHolder>{

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<VideoEntity> mTrailerValues;
    private List<ReviewEntity> mReviewValues;
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
//        mDataSet = list;
//        notifyDataSetChanged();
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
                        .inflate(R.layout.list_trailer_item, parent, false);
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
                holder.mVideoEntity = mTrailerValues.get(position);
                setTrailerData(holder);
                break;

            case TYPE_REVIEW:
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
        else {
            switch (position) {
//            switch (mDataSet.get(position - 1)) {
                case TYPE_TRAILER:
                    return TYPE_TRAILER;
                case TYPE_REVIEW:
                    return TYPE_REVIEW;
                default:
                    return TYPE_HEADER;
            }
        }
    }

    private void setHeaderData(ViewHolder holder) {
         holder.title.setText(holder.movieEntity.getTitle());
        holder.releaseYear.setText(holder.movieEntity.getReleaseDate());
        holder.rating.setText(String.format(context.getString(R.string.rating), holder.movieEntity.getRating()));
        holder.overview.setText(holder.movieEntity.getOverview());

        Picasso.with(context).load(holder.movieEntity.getImagePath()).into(holder.ivPoster);

        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                insertData();
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

    private void setReviewData(ViewHolder holder) {

    }
}
