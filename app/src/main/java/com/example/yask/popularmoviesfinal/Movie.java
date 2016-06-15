package com.example.yask.popularmoviesfinal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yask on 15/06/16.
 */

public class Movie implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;

    String movieid;
    String image_url;
    String overview;
    String releasedate;
    String title;
    String vote;

    public static Movie fromJson(JSONObject jsonObject){
        Movie b = new Movie();
        try {
            b.movieid = jsonObject.getString("id");
            b.image_url = jsonObject.getString("poster_path");
            b.overview = jsonObject.getString("overview");
            b.releasedate = jsonObject.getString("release_date");
            b.title = jsonObject.getString("title");
            b.vote = jsonObject.getString("vote_average");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return b;
    }

    public static ArrayList<Movie> fromJson(JSONArray jsonArray){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        JSONObject item = null;

        for (int i=0;i < jsonArray.length();i++){
            try {
                item  = jsonArray.getJSONObject(i);

            }
            catch (Exception e){
                e.printStackTrace();
            }
            Movie b = fromJson(item);
            movies.add(b);
        }
        return  movies;
    }

    public String getImageUrl(){
        return "http://image.tmdb.org/t/p/w185/"+image_url;
    }
}
