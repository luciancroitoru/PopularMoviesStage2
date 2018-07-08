package com.example.lucia.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    //the authority
    public static final String AUTHORITY = "com.example.lucia.popularmoviesstage2";

    //the base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //this is the path for the "favorites" movie list from database
    public static final String PATH_FAVORITES = "favorites";

    //declaration of database constants
    public static final class FavoriteMovieEntry implements BaseColumns {

        //URI content = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        //table name
        public static final String TABLE_NAME = "favorites";
        //table columns names
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}

