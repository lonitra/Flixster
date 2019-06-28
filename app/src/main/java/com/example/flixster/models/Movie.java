package com.example.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {
    public String title;
    public String overview;
    public String posterPath; // only path
    public double voteAverage;


    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        voteAverage = object.getDouble("vote_average");
    }

    //no arg constructor
    public Movie() {

    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
