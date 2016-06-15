package com.example.yask.popularmoviesfinal;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {
    MovieDetailFragment fragmentMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie item = (Movie) getIntent().getSerializableExtra("item");
        if (savedInstanceState == null) {
            // Insert detail fragment based on the item passed
            fragmentMovieDetail = new MovieDetailFragment().newInstance(item);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentMovieDetail);
            ft.commit();
        }
    }
}
