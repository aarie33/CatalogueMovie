package arie.cataloguemovie.fragments;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import arie.cataloguemovie.BuildConfig;
import arie.cataloguemovie.R;
import arie.cataloguemovie.SearchActivity;
import cz.msebera.android.httpclient.Header;

import static arie.cataloguemovie.db.DbContract.MovieColumns.ID_MOVIE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ORIGINAL_LANGUAGE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.OVERVIEW;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POPULARITY;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RUNTIME;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TITLE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.VOTE_AVERAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailSearchFragment extends Fragment {
    public static String TAG = MovieDetailSearchFragment.class.getSimpleName();
    private ImageView imgBackdrop, imgPoster, imgShare, imgFavorite;
    private TextView txtTitle, txtRating, txtBahasa, txtReleaseDate,
            txtRuntime, txtOverview, txtTagline, txtTrailer, txtFavorite;
    private SearchActivity searchActivity;
    private Bitmap backdrop, poster;
    private final static String POSTER_BITMAP = "poster_bitmap";
    private final static String BACKDROP_BITMAP = "backdrop_bitmap";

    public static MovieDetailSearchFragment createInstance(int id, String nama){
        MovieDetailSearchFragment fragmentDetail = new MovieDetailSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nama", nama);
        bundle.putInt("id", id);
        fragmentDetail.setArguments(bundle);
        return fragmentDetail;
    }

    public MovieDetailSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ID_MOVIE, getArguments().getString("id"));
        outState.putString(TITLE, getArguments().getString("nama"));
        outState.putString(VOTE_AVERAGE, txtRating.getText().toString());
        outState.putString(POPULARITY, txtRating.getText().toString());
        outState.putParcelable(POSTER_BITMAP, poster);
        outState.putParcelable(BACKDROP_BITMAP, backdrop);
        outState.putString(ORIGINAL_LANGUAGE, txtBahasa.getText().toString());
        outState.putString(OVERVIEW, txtOverview.getText().toString());
        outState.putString(RELEASE_DATE, txtReleaseDate.getText().toString());
        outState.putString(RUNTIME, txtRuntime.getText().toString());
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        txtRating.setText(savedInstanceState.getString(VOTE_AVERAGE));
//        txtReleaseDate.setText(savedInstanceState.getString(RELEASE_DATE));
//        txtBahasa.setText(savedInstanceState.getString(ORIGINAL_LANGUAGE));
//        txtRuntime.setText(savedInstanceState.getString(RUNTIME));
//        txtOverview.setText(savedInstanceState.getString(OVERVIEW));
//        txtTagline.setText(savedInstanceState.getString(TAGLINE));
//        imgPoster.setImageBitmap((Bitmap) savedInstanceState.getParcelable(POSTER_BITMAP));
//        imgBackdrop.setImageBitmap((Bitmap) savedInstanceState.getParcelable(BACKDROP_BITMAP));
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        searchActivity = (SearchActivity)getActivity();
        searchActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBackdrop = view.findViewById(R.id.img_backdrop_path);
        imgPoster = view.findViewById(R.id.img_poster_path);
        txtTitle = view.findViewById(R.id.txt_title);
        txtRating = view.findViewById(R.id.txt_rate);
        txtBahasa = view.findViewById(R.id.txt_bahasa);
        txtReleaseDate = view.findViewById(R.id.txt_release_date);
        txtRuntime = view.findViewById(R.id.txt_runtime);
        txtOverview = view.findViewById(R.id.txt_overview);
        txtTagline = view.findViewById(R.id.txt_tagline);

        Bundle myBundle = this.getArguments();
        getActivity().setTitle(myBundle.getString("nama"));
        txtTitle.setText(myBundle.getString("nama"));
        AsyncDetailMovie asyncDetailMovie = new AsyncDetailMovie();
        asyncDetailMovie.execute(myBundle.getInt("id"));
        return view;
    }

    private class AsyncDetailMovie extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... integers) {
            JSONObject object = getDetail(integers[0]);
            try {
                poster = Glide.with(getContext())
                        .asBitmap()
                        .load(BuildConfig.UrlImage + object.getString("poster_path").toString())
                        .into(185, 278)
                        .get();

                backdrop = Glide.with(getContext())
                        .asBitmap()
                        .load(BuildConfig.UrlImage + object.getString("backdrop_path").toString())
                        .into(185, 278)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            Log.i("Post Execute" , String.valueOf(object));
            try {
                Log.i("OBJECT", object.getString("overview").toString());
                txtRating.setText(String.valueOf(object.getInt("vote_average")));
                txtReleaseDate.setText(object.getString("release_date").toString());
                txtBahasa.setText(object.getString("original_language").toString());
                txtRuntime.setText(String.valueOf(object.getDouble("runtime")));
                txtOverview.setText(object.getString("overview").toString());
                txtTagline.setText(object.getString("tagline").toString());

                imgPoster.setImageBitmap(poster);
                imgBackdrop.setImageBitmap(backdrop);

//                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w185/" +
//                        object.getString("poster_path").toString()).into(imgPoster);
//                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w342/" +
//                        object.getString("backdrop_path").toString()).into(imgBackdrop);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject getDetail(int movieId){
        SyncHttpClient syncHttpClient = new SyncHttpClient();
        final JSONObject[] jsonObject = {new JSONObject()};
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?" +
                "api_key="+ BuildConfig.ApiKey+"&language=en-US&query="+movieId;

        syncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Success out try", String.valueOf(responseBody));
                JSONObject object;
                try {
                    String result = new String(responseBody);
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    object = null;
                    Log.e("ERROR CATCH SYNC MOVIE", String.valueOf(e));
                }
                jsonObject[0] = object;

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ERROR GET SYNC MOVIE", String.valueOf(responseBody));
            }
        });
        return jsonObject[0];
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
