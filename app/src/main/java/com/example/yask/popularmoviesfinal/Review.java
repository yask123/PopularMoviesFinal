package com.example.yask.popularmoviesfinal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yask on 16/06/16.
 */

public class Review {
    String author;
    String author_review;

    public static Review fromJson(JSONObject obj) {
        Review b = new Review();
        try {
            b.author = obj.getString("author");
            b.author_review = obj.getString("content");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;

    }


    public static ArrayList<Review> fromJson(JSONArray arr) {
        JSONObject item;
        ArrayList<Review> reviews = new ArrayList<Review>();

        for (int i = 0; i < arr.length(); i++) {
            try {
                item = arr.getJSONObject(i);
                reviews.add(fromJson(item));

            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }

        }
        return reviews;
    }
}