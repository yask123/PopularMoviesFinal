package com.example.yask.popularmoviesfinal;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by yask on 16/06/16.
 */
public class User extends SugarRecord {
    @Unique
    public String FAVID;
    public String favtitle;
    public String favoverview;
    public String favrelease_date;
    public  String favstars;
    public String favimage;

    public  User(){

    }
    public User(String ID,String title, String overview, String releasedate, String stars, String image){
        FAVID = ID;
        favtitle = title;
        favoverview = overview;
        favrelease_date = releasedate;
        favimage = image;
        favstars = stars;
    }
}
