package com.example.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //base url for API
    public static final String API_BASE_URL="https://api.themoviedb.org/3";
    //parameter name for API key
    public static final String API_KEY_PARAM="api_key";
    public static final String TAG="Flixster";

    //instance field
    AsyncHttpClient client;
    @BindView(R.id.rvMovies) RecyclerView rvMovies;
    ArrayList<Movie> movies;
    MovieAdapter adapter;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();
        movies = new ArrayList<Movie>();
        adapter = new MovieAdapter(movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
        getConfiguration();

    }

    // gets list of currently playing movies from api
    private void getNowPlaying() {
        String url = API_BASE_URL + "/movie/now_playing"; //create url
        //assign request parameters (values that get appended to url)
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //call getString bc value api_key is numeric
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load results
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate and create movie objects into a list
                    for(int i = 0; i < results.length(); i++) {
                        movies.add(new Movie(results.getJSONObject(i)));
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed parsing now playing movies", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    //gets configuration from api
    private void getConfiguration() {
        String url = API_BASE_URL + "/configuration"; //create url
        //assign request parameters (values that get appended to url)
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //call getString bc value api_key is numeric
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(), config.getPosterSize()));
                    adapter.setConfig(config);
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
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
        Log.e(TAG, message, error);
        //alerts user of error
        if(alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
