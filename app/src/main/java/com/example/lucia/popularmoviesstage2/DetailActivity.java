package com.example.lucia.popularmoviesstage2;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucia.popularmoviesstage2.data.FavoriteMovieUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

import static com.example.lucia.popularmoviesstage2.data.MovieContract.FavoriteMovieEntry.CONTENT_URI;
import static java.lang.String.valueOf;

/**
 * This class populates the detail activity layout with movies.
 */
public class DetailActivity extends AppCompatActivity {

    //variables
    private static final String VIDEO_URL_PATH = "videos";
    private static final String REVIEW_URL_PATH = "reviews";
    private final int TRAILER_LOADER = 8;
    private final int REVIEWS_LOADER = 4;
    public RecyclerView trailerRecyclerView;
    public RecyclerView reviewsRecyclerView;
    TextView dReleaseDate, dRating, dPlotSynopsis, dTitle, dNoTrailer, dNoReview;
    ImageView dMoviePoster;
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;
    Movie currentMovie;
    ProgressBar progressBarReview, progressBarTrailer;
    Trailer firstTrailer;
    FloatingActionButton favoriteButton;


    //The loader that loads the trailers in the activity_detail.xml layout
    LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoader = new LoaderManager
            .LoaderCallbacks<List<Trailer>>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Trailer>>(DetailActivity.this) {

                List<Trailer> trailerList;

                @Override
                protected void onStartLoading() {
                    if (trailerList != null) {
                        deliverResult(trailerList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Trailer> loadInBackground() {
                    List<Trailer> trailerList = new ArrayList<>();
                    try {
                        URL requestUrl = JsonUtils.getUrlResponseById(valueOf(currentMovie.getMovieId()), VIDEO_URL_PATH);
                        Response response = JsonUtils.fetchData(requestUrl.toString());
                        trailerList = JsonUtils.parseTrailerJson(response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    return trailerList;
                }

                @Override
                public void deliverResult(List<Trailer> data) {
                    trailerList = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailerList) {
            progressBarTrailer.setVisibility(View.GONE);
            mTrailerAdapter.addAll(trailerList);
            firstTrailer = trailerList.get(0);
            if (trailerList.isEmpty()) dNoTrailer.setVisibility(View.VISIBLE);

        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            mTrailerAdapter.clearAll();
        }
    };

    //The loader loads the reviews in the activity_detail.xml layout
    LoaderManager.LoaderCallbacks<List<Review>> reviewLoader = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<Review>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Review>>(DetailActivity.this) {

                List<Review> reviewList;

                @Override
                protected void onStartLoading() {
                    if (reviewList != null) {
                        deliverResult(reviewList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Review> loadInBackground() {
                    List<Review> reviewList = new ArrayList<>();
                    try {
                        URL requestUrl = JsonUtils.getUrlResponseById(valueOf(currentMovie.getMovieId()), REVIEW_URL_PATH);
                        Response response = JsonUtils.fetchData(requestUrl.toString());
                        reviewList = JsonUtils.parseReviewJson(response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return reviewList;
                }

                @Override
                public void deliverResult(List<Review> data) {
                    reviewList = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> reviewList) {
            progressBarReview.setVisibility(View.GONE);
            mReviewAdapter.addAll(reviewList);
            if (reviewList.isEmpty()) dNoReview.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            mReviewAdapter.clearAll();
        }
    };
    private Intent intent;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        reviewsRecyclerView = findViewById(R.id.reviews_recycler);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(reviewsRecyclerView.getContext());
        reviewsRecyclerView.setLayoutManager(layoutManagerReviews);

        mReviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewsRecyclerView.setAdapter(mReviewAdapter);

        getLoaderManager().initLoader(REVIEWS_LOADER, null, reviewLoader);

        trailerRecyclerView = findViewById(R.id.trailer_recycler);
        RecyclerView.LayoutManager layoutManagerTrailers = new
                LinearLayoutManager(trailerRecyclerView.getContext());
        trailerRecyclerView.setLayoutManager(layoutManagerTrailers);

        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        trailerRecyclerView.setAdapter(mTrailerAdapter);

        getLoaderManager().initLoader(TRAILER_LOADER, null, trailerLoader);

        progressBarReview = findViewById(R.id.progress_bar_reviews);
        dNoReview = findViewById(R.id.no_review);
        progressBarTrailer = findViewById(R.id.progress_bar_trailers);
        dNoTrailer = findViewById(R.id.no_trailer);
        trailerRecyclerView = findViewById(R.id.trailer_recycler);
        dMoviePoster = findViewById(R.id.detail_movie_poster_details_view);

        Intent intent = getIntent();
        currentMovie = intent.getParcelableExtra("Movie");

        String moviePosterUrlString = MovieAdapter.buildPosterUrl(currentMovie.getMoviePoster());
        Picasso.with(this).load(moviePosterUrlString).into(dMoviePoster);
        displayMovieUI(currentMovie);

        setTitle(currentMovie.getTitle());

        //favorite button that shows if a movie is in the favorite list or not
        favoriteButton = findViewById(R.id.favorite_details_content);
        if (FavoriteMovieUtils.isAmongFavorites(this, currentMovie.getMovieId())) {
            favoriteButton.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        //onClickListener on favorite Button that puts or removes a movie in/from the
        //favorite movie list
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (FavoriteMovieUtils.isAmongFavorites(DetailActivity.this,
                        currentMovie.getMovieId())) {
                    favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                    int moviesRemoved = FavoriteMovieUtils.removeFromFavorite(
                            DetailActivity.this, currentMovie.getMovieId());
                    if (moviesRemoved > 0) Toast.makeText(DetailActivity.this,
                            getString(R.string.deleted), Toast.LENGTH_SHORT).show();

                } else {
                    favoriteButton.setImageResource(R.drawable.ic_star_black_24dp);
                    ContentValues cv = FavoriteMovieUtils.addMovieToFavoritesList(currentMovie);
                    Uri uri = getContentResolver().insert(CONTENT_URI, cv);
                    if (uri != null)
                        Toast.makeText(DetailActivity.this, getString(R.string.added),
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method takes information from movie objects and displays it
     * in the drawer_layout_main.xml layout
     *
     * @param movie the Movie object that contains all information needed
     */
    private void displayMovieUI(Movie movie) {

        dTitle = findViewById(R.id.detail_movie_title);
        dTitle.setText(movie.getTitle());

        dReleaseDate = findViewById(R.id.detail_movie_release_date);
        dReleaseDate.setText(movie.getReleaseDate());

        dRating = findViewById(R.id.detail_movie_vote_average);
        dRating.setText(valueOf(movie.getRating()));

        dPlotSynopsis = findViewById(R.id.detail_movie_plot_synopsis);
        dPlotSynopsis.setText(movie.getPlotSynopsis());

    }
}

