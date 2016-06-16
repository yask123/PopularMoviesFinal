package com.example.yask.popularmoviesfinal;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {
    static final String SCROLL_INDEX = "index";
    public MoviesAdapter adapterMovie;
    public IMDBClient client;
    View rootView;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Parcelable state;
    GridView gvMovie;
    int index;
    private OnListItemSelectedListener listener;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        state = gvMovie.onSaveInstanceState();

        savedInstanceState.putInt(SCROLL_INDEX, gvMovie.getFirstVisiblePosition());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) activity;
        } else {
            throw new ClassCastException(
                    activity.toString()
                            + " must implement ItemsListFragment.OnListItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            index = savedInstanceState.getInt(SCROLL_INDEX);

        }
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actions_textview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.popular:
                fetchMovies("popular");
                editor = mSettings.edit();
                editor.putString("sort_order", "popular");
                editor.apply();
                return true;
            case R.id.top_rated:
                fetchMovies("top_rated");
                editor = mSettings.edit();
                editor.putString("sort_order", "top_rated");
                editor.apply();
                Log.e("YE", mSettings.getString("sort_order", "missing"));
                return true;
            case R.id.fav:
                editor = mSettings.edit();
                editor.putString("sort_order", "fav");
                editor.apply();
                fetchMovies("fav");
                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ArrayList<Movie> temp = new ArrayList<Movie>();

        adapterMovie = new MoviesAdapter(getActivity(), temp);
        gvMovie = (GridView) rootView.findViewById(R.id.gvMovie);
        gvMovie.setAdapter(adapterMovie);
        gvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = adapterMovie.getItem(position);
                listener.onItemSelected(item);
            }
        });
        mSettings = getActivity().getSharedPreferences("Settings", 0);
        String Psort = mSettings.getString("sort_order", "missing");
        editor = mSettings.edit();


        if (Psort.equals("missing")) {
            fetchMovies("popular");

            editor.putString("sort_order", "popular");
            editor.apply();
            Log.e("Test", "Started Populer coz mussing");
        } else {
            fetchMovies(Psort);
            Log.e("Test", "Started " + Psort);

        }


        return rootView;

    }

    private void fetchMovies(String order) {
        adapterMovie.clear();
        adapterMovie.notifyDataSetChanged();

        if (order.equals("fav")) {

            List<User> users_movies = User.findWithQuery(User.class, "Select * from User");
            for (User each_movie : users_movies) {
                Movie temp = new Movie();
                temp.movieid = each_movie.FAVID;
                temp.title = each_movie.favtitle;
                temp.overview = each_movie.favoverview;
                temp.image_url = each_movie.favimage;
                temp.releasedate = each_movie.favrelease_date;
                temp.vote = each_movie.favstars;
                adapterMovie.add(temp);
            }
            adapterMovie.notifyDataSetChanged();
            gvMovie.setSelection(index);

        } else {
            client = new IMDBClient();
            client.getMovies(order, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.e("Test", errorResponse.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray items = response.getJSONArray("results");
                        ArrayList<Movie> movie_response = Movie.fromJson(items);
                        for (Movie each_movie : movie_response) {
                            adapterMovie.add(each_movie);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapterMovie.notifyDataSetChanged();
                    gvMovie.setSelection(index);

                }
            });


        }


    }


    public interface OnListItemSelectedListener {
        void onItemSelected(Movie item);
    }

}
