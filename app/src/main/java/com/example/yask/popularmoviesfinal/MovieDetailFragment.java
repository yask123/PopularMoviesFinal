package com.example.yask.popularmoviesfinal;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    public Movie item;
    public View view;
    TextView title;
    ImageView ivPosterImage;
    TextView overview;
    TextView votes;
    IMDBClient client;
    LinearLayout ll_detail;
    Button fav;
    Movie current_movie;
    @BindView(R.id.tvCriticsScore) TextView rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Movie) getArguments().getSerializable("item");

    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(Movie item) {
        MovieDetailFragment fragmentDemo = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        title =(TextView) view.findViewById(R.id.tvTitle);
        title.setText(item.title);

        ivPosterImage = (ImageView)view.findViewById(R.id.ivPosterImage);
        Picasso.with(getContext()).load(item.getImageUrl()).into(ivPosterImage);

        votes = (TextView)view.findViewById(R.id.tvCriticsScore);
        votes.setText("Average Ratings: "+item.vote);

        TextView overview = (TextView) view.findViewById(R.id.overview);
        overview.setText(item.overview);

        ll_detail = (LinearLayout)view.findViewById(R.id.ll_detail);
        fetchVideos(item.movieid);
        current_movie = item;
        fav  = (Button) view.findViewById(R.id.fav);
        fav_button(item.movieid);

        if (!User.find(User.class,"favid = ?",item.movieid).isEmpty()){
            fav.setText("Starred");
        }
        return view;
    }

    public void fetchReviws(String id){

        client = new IMDBClient();
        client.getReviews(id,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonArray;
                TextView review_heading = new TextView(getContext());
                review_heading.setText("Reviews");
                review_heading.setTextColor(Color.BLACK);
                review_heading.setTextSize(20);
                ll_detail.addView(review_heading);
                try {
                    Log.e("Review",response.toString());
                    jsonArray = response.getJSONArray("results");
                    ArrayList<Review> reviews = Review.fromJson(jsonArray);

                    for (Review each_review : reviews){

                        Log.e("ytest",each_review.author_review);
                        TextView new_review = new TextView(getContext());
                        TextView new_author = new TextView(getContext());
                        new_author.setText(each_review.author+" wrote: ");
                        new_review.setText(each_review.author_review);
                        new_review.setTextColor(Color.BLACK);
                        new_author.setTextColor(Color.BLUE);
                        ll_detail.addView(new_author);
                        ll_detail.addView(new_review);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }


        });
    }

    public void fetchVideos(String id){
        client = new IMDBClient();
        client.getTrailer(id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonarray;


                try {
                    TextView trailer_heading = new TextView(getContext());
                    trailer_heading.setText("Trailers");
                    trailer_heading.setTextColor(Color.BLACK);
                    trailer_heading.setTextSize(20);
                    ll_detail.addView(trailer_heading);
                    jsonarray = response.getJSONArray("results");
                    //JSONObject item = jsonarray.getJSONObject(0);
                    //video.setText(item.getString("key"));
                    for(int i=0;i < jsonarray.length();i++){
                        JSONObject item = jsonarray.getJSONObject(i);
                        final Button new_trailer = new Button(getContext());
                        final String vid_id = item.getString("key");
                        new_trailer.setText("Trailer " + (i+1));
                        new_trailer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + vid_id)));
                            }
                        });
                        ll_detail.addView(new_trailer);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        fetchReviws(id);
    }
    public void fav_button(final String ID){
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (User.find(User.class,"favid = ?",ID).isEmpty()){
                    User new_fav = new User(current_movie.movieid,current_movie.title,current_movie.overview,current_movie.releasedate,current_movie.vote,current_movie.image_url);
                    new_fav.save();
                    fav.setText("Starred");
                }
                else {
                    Log.e("DB","Movie already selected");
                    fav.setText("Starred");
                }
            }
        });
    }
}


