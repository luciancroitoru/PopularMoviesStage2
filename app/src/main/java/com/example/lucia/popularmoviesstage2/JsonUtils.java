package com.example.lucia.popularmoviesstage2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class helps manage all the Json's String changes, since it is formed and until all the data is parsed
 * and fetched from the Json String.
 */
public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getName();

    //Constants that help parse Json String
    private static final String ROOT_JSON = "results";
    private static final String RATE = "vote_average";
    private static final String IMAGE = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String POPULARITY = "popularity";
    private static final String MOVIE_ID = "id";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";

    //Constants for creating the URL
    private static final String BASE_URL
            = "https://api.themoviedb.org/3/movie";
    private static final String PARAM_KEY = "api_key";
    private static final String API_KEY = ""; // <- put your API KEY here :)

    /**
     * This method creates the URL that is sent in the fetchData method
     *
     * @param selection is the parameter that is put to sortBy user preference
     * @return the URL concatenated in this method
     */
    public static URL getUrlResponse(String selection) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(selection)
                .addQueryParameter(PARAM_KEY, API_KEY)
                .build();

        return urlBuilder.build().url();

    }

    /**
     * This method creates the URL for each movie Id and it concatenates with video or
     * reviews that are sent in the fetchData method
     *
     * @param selection is the parameter that is put(video or reviews)
     * @return the URL concatenated in this method
     */
    public static URL getUrlResponseById(String id, String selection) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(id)
                .addEncodedPathSegment(selection)
                .addQueryParameter(PARAM_KEY, API_KEY)
                .build();

        return urlBuilder.build().url();

    }

    /**
     * This method parses the Json String received from the fetchData method,
     * that has all movie information.
     *
     * @param json is the string received from the fetchData that is a response converted
     *             in a string
     * @return a Movie list that has all needed param that are used to populate the UI
     * in the main layout.
     */
    public static List<Movie> parseMovieJson(String json) {
        //Create an empty arrayList of Movie objects in which movies can be added.
        List<Movie> moviesList = new ArrayList<>();
        //The Json string parsing.
        try {
            //Create JsonObject from json String.
            JSONObject rootJsonObject = new JSONObject(json);
            //Extract "results" Array that has all the information need to be parsed
            JSONArray rootArray = rootJsonObject.getJSONArray(ROOT_JSON);
            //Extract from each element information needed to create a movieList and
            // for each movie object all the details needed.
            for (int i = 0; i < rootArray.length(); i++) {
                JSONObject movieJsonObject = rootArray.getJSONObject(i);
                String originalTitle = movieJsonObject.getString(ORIGINAL_TITLE);

                String moviePoster = movieJsonObject.getString(IMAGE);

                String overview = movieJsonObject.getString(OVERVIEW);

                double rating = movieJsonObject.getDouble(RATE);

                double popularity = movieJsonObject.getDouble(POPULARITY);

                String releaseDate = movieJsonObject.getString(RELEASE_DATE);

                int movieId = movieJsonObject.getInt(MOVIE_ID);

                moviesList.add(new Movie(originalTitle, moviePoster, overview, rating,
                        popularity, releaseDate, movieId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    /**
     * This method parses the Json String received from the fetchData method,
     * that has all review info.
     *
     * @param json is the string received from the fetchData that is a response converted
     *             in a string
     * @return a Review object list that has all needed param that are used to populate the UI
     * later in the detail layout.
     */
    public static List<Review> parseReviewJson(String json) {
        List<Review> reviewsList = new ArrayList<>();
        try {
            JSONObject rootReviewJsonObject = new JSONObject(json);
            JSONArray rootReviewsArray = rootReviewJsonObject.getJSONArray(ROOT_JSON);
            for (int i = 0; i < rootReviewsArray.length(); i++) {
                JSONObject reviewsJsonObject = rootReviewsArray.getJSONObject(i);
                String reviewsAuthor = reviewsJsonObject.getString(REVIEW_AUTHOR);
                String reviewsContent = reviewsJsonObject.getString(REVIEW_CONTENT);
                reviewsList.add(new Review(reviewsAuthor, reviewsContent));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    /**
     * This method parses the Json String received from the fetchData method,
     * that has all trailer info.
     *
     * @param json is the string received from the fetchData that is response converted
     *             in a string
     * @return a Trailer object list that has all needed param that are used to populate the UI
     * later in the detail layout.
     */
    public static List<Trailer> parseTrailerJson(String json) {
        List<Trailer> trailersList = new ArrayList<>();
        try {
            JSONObject rootTrailerJsonObject = new JSONObject(json);
            JSONArray rootTrailersArray = rootTrailerJsonObject.getJSONArray(ROOT_JSON);
            for (int i = 0; i < rootTrailersArray.length(); i++) {
                JSONObject trailersJsonObject = rootTrailersArray.getJSONObject(i);

                String trailerKey = trailersJsonObject.getString(TRAILER_KEY);
                String trailerName = trailersJsonObject.getString(TRAILER_NAME);
                trailersList.add(new Trailer(trailerKey, trailerName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersList;
    }

    /**
     * This method helps to fetch data
     *
     * @param requestUrl
     * @return
     */
    public static Response fetchData(String requestUrl) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}