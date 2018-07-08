package com.example.lucia.popularmoviesstage2.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.lucia.popularmoviesstage2.Movie;

import static java.lang.String.valueOf;

import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.CONTENT_URI;

public class FavoriteMovieUtils extends ContentResolver {

    /**
     * This is the class constructor
     *
     * @param context
     */
    public FavoriteMovieUtils(Context context) {
        super(context);
    }

    /**
     * This method helps to add a movie to the favorites movie list
     *
     * @param favoriteMovie this object has all the movie's details that will be put in the database
     * @return the content values that will be used to populate the database
     */
    public static ContentValues addMovieToFavoritesList(Movie favoriteMovie) {

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER, favoriteMovie.getMoviePoster());
        cv.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, favoriteMovie.getPlotSynopsis());
        cv.put(MovieContract.FavoriteMovieEntry.COLUMN_RATING, favoriteMovie.getRating());
        cv.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, favoriteMovie.getReleaseDate());
        cv.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, favoriteMovie.getMovieId());
        return cv;
    }

    /**
     * This method is used to find out if a movie is in the favorite movie list
     *
     * @param context
     * @param movieId that will be used to query the database for a specific movie
     * @return true if the movie is in the favorite list or false if is not
     */
    public static boolean isAmongFavorites(Context context, int movieId) {
        String selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {""};
        boolean isFavorite = false;
        selectionArgs[0] = valueOf(movieId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }
        return isFavorite;
    }

    /**
     * This method helps to remove a specific movie from the favorite movies list
     *
     * @param context
     * @param movieId that is used to query the database for a specific movie
     * @return the value of the raw that was deleted
     */
    public static int removeFromFavorite(Context context, int movieId) {
        Uri uri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
        Uri removeUri = uri.buildUpon().appendPath(valueOf(movieId)).build();
        return context.getContentResolver().delete(removeUri, null, null);
    }
}
