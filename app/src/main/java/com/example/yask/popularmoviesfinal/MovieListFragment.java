package com.example.yask.popularmoviesfinal;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public  MoviesAdapter adapterMovie;
    public IMDBClient client;
    View rootView;

    private OnListItemSelectedListener listener;




    public interface OnListItemSelectedListener {
        public void onItemSelected(Movie item);
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


    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                return true;
            case R.id.top_rated:
                fetchMovies("top_rated");
                return true;
            case  R.id.fav:
                fetchMovies("fav");
                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_movie_list, container, false);

        ArrayList<Movie>  temp = new ArrayList<Movie>();

        adapterMovie = new MoviesAdapter(getActivity(),temp);
        GridView gvMovie =(GridView) rootView.findViewById(R.id.gvMovie);
        gvMovie.setAdapter(adapterMovie);
        gvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = adapterMovie.getItem(position);
                listener.onItemSelected(item);
            }
        });
        fetchMovies("popular");
        return  rootView;

    }

    private void fetchMovies(String order){
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
        }
        else{
            client = new IMDBClient();
            client.getMovies(order, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray items = response.getJSONArray("results");
                        ArrayList<Movie> movie_response = Movie.fromJson(items);
                        for(Movie each_movie: movie_response){
                            adapterMovie.add(each_movie);
                        }

                    }
                    catch (Exception e ){
                        e.printStackTrace();
                    }
                    adapterMovie.notifyDataSetChanged();
                }
            });

        }



    }

}
