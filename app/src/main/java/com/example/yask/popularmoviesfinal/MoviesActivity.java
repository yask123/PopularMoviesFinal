package com.example.yask.popularmoviesfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

public class MoviesActivity extends AppCompatActivity implements MovieListFragment.OnListItemSelectedListener{


    private boolean isTwoPane = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        determinePaneLayout();
        if (isTwoPane){
            Log.e("Test","Is two pane");
        }
        else
        {
            Log.e("Test","Not 2 pane");
        }

    }

    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.flDetailContainer);
        // If there is a second pane for details
        if (fragmentItemDetail != null) {
            isTwoPane = true;
        }
    }

    public void onItemSelected(Movie item) {
        if (isTwoPane) { // single activity with list and detail
            // Replace framelayout with new detail fragment
            MovieDetailFragment fragmentItem = MovieDetailFragment.newInstance(item);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItem);
            ft.commit();
        } else { // go to separate activity
            // launch detail activity using intent
            Intent i = new Intent(this, MovieDetailActivity.class);
            i.putExtra("item", item);
            startActivity(i);
        }
    }
}
