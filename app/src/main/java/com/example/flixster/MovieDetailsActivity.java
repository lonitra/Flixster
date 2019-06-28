package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;
    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivPoster = (ImageView) findViewById(R.id.ivPoster);

        Intent intent = getIntent();

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for %s", movie.getTitle()));

        tvOverview.setText(movie.getOverview());
        tvTitle.setText(movie.getTitle());
        String img = intent.getStringExtra(MovieAdapter.IMAGE_TRANSFER);
        Log.d("MovieDetailsActivity", "intent received");

        Glide.with(this)
                .load(img)
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(ivPoster);






        float voteAverage = (float) movie.getVoteAverage();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }
}
