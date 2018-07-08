package com.example.lucia.popularmoviesstage2;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lucia.popularmoviesstage2.data.MovieContract;
import com.example.lucia.popularmoviesstage2.data.MovieDBHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER;
import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE;
import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW;
import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY;
import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_RATING;
import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER_ID = 1;
    public final int FAVORITE_LOADER = 6;
    public String sortBy = "top_rated";
    public View noConnectionView;
    public RecyclerView movieRV;
    SQLiteDatabase mDb;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView noFavoriteTextView;


    //The loader that helps load the movie list in the drawer_layout_main.xml layout
    LoaderManager.LoaderCallbacks<List<Movie>> movieLoader = new LoaderManager
            .LoaderCallbacks<List<Movie>>() {

        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Movie>>(MainActivity.this) {

                List<Movie> movieList;

                @Override
                protected void onStartLoading() {
                    if (movieList != null) {
                        deliverResult(movieList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Movie> loadInBackground() {
                    List<Movie> movieList = new ArrayList<>();
                    URL requestUrl = null;
                    try {
                        requestUrl = JsonUtils.getUrlResponse(sortBy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Response response = JsonUtils.fetchData(requestUrl.toString());
                        movieList = JsonUtils.parseMovieJson(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return movieList;
                }

                @Override
                public void deliverResult(List<Movie> data) {
                    movieList = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieList) {
            noFavoriteTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            movieRV.setVisibility(View.VISIBLE);
            movieAdapter.addAll(movieList);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

            movieAdapter.clearAll();
        }
    };

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri URI = MovieContract.FavoriteMovieEntry.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(this,
                URI,
                null,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        if (data.getCount() == 0) {
            noFavoriteTextView.setVisibility(View.VISIBLE);
            movieRV.setVisibility(View.INVISIBLE);
        } else {
            List<Movie> movieList = new ArrayList<>();
            while (data.moveToNext()) {
                int movieId = data.getInt(data.getColumnIndex(COLUMN_MOVIE_ID));
                String moviePoster = data.getString(data.getColumnIndex(COLUMN_MOVIE_POSTER));
                String overview = data.getString(data.getColumnIndex(COLUMN_OVERVIEW));
                double rating = data.getDouble(data.getColumnIndex(COLUMN_RATING));
                String releaseDate = data.getString(data.getColumnIndex(COLUMN_RELEASE_DATE));
                String originalTitle = data.getString(data.getColumnIndex(COLUMN_ORIGINAL_TITLE));
                double popularity = data.getDouble(data.getColumnIndex(COLUMN_POPULARITY));
                Movie movie = new Movie(originalTitle, moviePoster,
                        overview, rating, popularity, releaseDate, movieId);
                movieList.add(movie);
                movieAdapter.addAll(movieList);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        movieAdapter.clearAll();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_main);

        progressBar = findViewById(R.id.progress_bar);
        noFavoriteTextView = findViewById(R.id.no_favorite_list);

        MovieDBHelper favoriteDBHelper = new MovieDBHelper(this);
        mDb = favoriteDBHelper.getWritableDatabase();

        //Setup the navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setup of the recycler view that hosts the movie list
        movieRV = findViewById(R.id.movie_list);
        noConnectionView = findViewById(R.id.error_loading_movie_list);

        //setup of the movie list
        int gridRows = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(movieRV.getContext(),
                gridRows);
        movieRV.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        movieRV.setAdapter(movieAdapter);

        //set up an error message if there is no connection to the database
        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, movieLoader);
        } else {
            noConnectionView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * This method verifies if the device has internet connection
     *
     * @return true if there is connection and false if connection cannot be made
     */
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasConnection = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            hasConnection = true;
        }
        return hasConnection;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.sort_by_most_popular) {
            // sets order for the movie list by most popular
            sortBy = getString(R.string.popular);
            drawerLayout.closeDrawer(GravityCompat.START);
            if (isConnected()) {
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, movieLoader);
                noConnectionView.setVisibility(View.GONE);
            } else {
                movieRV.setAdapter(null);
                noConnectionView.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.sort_by_top_rated) {
            // sets order for the movie list by top rated
            sortBy = getString(R.string.rated);
            drawerLayout.closeDrawer(GravityCompat.START);
            if (isConnected()) {
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, movieLoader);
                noConnectionView.setVisibility(View.GONE);
            } else {
                movieRV.setAdapter(null);
                noConnectionView.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.sort_by_favorite_movie) {
            // displays on the main layout the favorite movie list
            drawerLayout.closeDrawer(GravityCompat.START);
            if (isConnected()) {
                movieRV.setAdapter(movieAdapter);
                getLoaderManager().restartLoader(FAVORITE_LOADER, null, this);
                noConnectionView.setVisibility(View.GONE);
            } else {
                movieRV.setAdapter(null);
                noConnectionView.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    /**
     * When the back button is clicked and the user has the navigationDrawer opened, this method
     * closes the drawer
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}