package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;
import com.example.flixster.models.MovieTrailerActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.ivPoster) ImageView ivPoster;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    AsyncHttpClient client;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        client = new AsyncHttpClient();
        ButterKnife.bind(this);

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

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConfiguration();
            }
        });

    }

    //gets configuration from api
    private void getConfiguration() {
        String url = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos";
        //assign request parameters (values that get appended to url)
        RequestParams params = new RequestParams();
        params.put("api_key", getString(R.string.api_key)); //call getString bc value api_key is numeric
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject obj = results.getJSONObject(2);
                    key = obj.getString("key");

                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("video_key", key);
                    MovieDetailsActivity.this.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handles errors, logs and alerts user that there was an error
    private void logError(String message, Throwable error, boolean alertUser) {
        Log.e("MovieDetailsActivity", message, error);
        //alerts user of error
        if(alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}
