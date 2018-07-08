package com.example.lucia.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry;

public class MovieDBHelper extends SQLiteOpenHelper {

    //the name of the database
    private static final String DATABASE_NAME = "movie.db";
    //the version number of the database
    private static final int DATABASE_VERSION = 1;

    /**
     * The databaseHelper constructor
     *
     * @param context
     */
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate method, called when the database is first created
     *
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " +
                FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT," +
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER," +
                FavoriteMovieEntry.COLUMN_MOVIE_POSTER + " TEXT," +
                FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT," +
                FavoriteMovieEntry.COLUMN_RATING + " REAL," +
                FavoriteMovieEntry.COLUMN_POPULARITY + " REAL," +
                FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    /**
     * This method is called when the database is updated; the old database
     * is discarded and a new one is created.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
