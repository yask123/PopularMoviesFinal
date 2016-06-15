package com.example.yask.popularmoviesfinal;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

/**
 * Created by yask on 16/06/16.
 */

public class IMDBClient {

    private final String KEY = "";
    private AsyncHttpClient client;

    public IMDBClient() {
        this.client = new AsyncHttpClient();
    }

    public void getMovies(String selected_option, JsonHttpResponseHandler handler) {

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("api_key", KEY);
        RequestParams params = new RequestParams(paramMap);

        client.get("http://api.themoviedb.org/3/movie/" + selected_option + "?api_key=" + KEY, handler);
    }


    public void getTrailer(String ID, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams("api_key", KEY);

        client.get("http://api.themoviedb.org/3/movie/" + ID + "/videos", params, handler);

    }

    public void getReviews(String ID, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams("api_key", KEY);
        client.get("http://api.themoviedb.org/3/movie/" + ID + "/reviews", params, handler);
    }


}
