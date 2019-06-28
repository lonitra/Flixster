package com.example.flixster;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    ArrayList<Movie> movies;
    Config config;
    Context context;

    public static final String INFO_TRANSFER = Movie.class.getSimpleName();
    public static final String IMAGE_TRANSFER = "page2Image";

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @NonNull

    //creates and inflates new view
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie movie = movies.get(i);
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        //TODO--set image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(viewHolder.ivPosterImage);
    }


    // returns total number of item in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            itemView.setOnClickListener(this);
        }

        //shows movie details for movie user selected
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(INFO_TRANSFER, Parcels.wrap(movie));
                String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
                intent.putExtra(IMAGE_TRANSFER, imageUrl);
                context.startActivity(intent);
            }
        }

    }
}
