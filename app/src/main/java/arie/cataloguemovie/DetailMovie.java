package arie.cataloguemovie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

import static arie.cataloguemovie.db.DbContract.CONTENT_URI;
import static arie.cataloguemovie.db.DbContract.MovieColumns.BACKDROP_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ID_MOVIE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ORIGINAL_LANGUAGE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.OVERVIEW;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POPULARITY;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POSTER_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RUNTIME;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TAGLINE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TITLE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.VOTE_AVERAGE;

public class DetailMovie extends AppCompatActivity {
    public static String TAG = DetailMovie.class.getSimpleName();
    private ImageView imgBackdrop, imgPoster, imgShare, imgFavorite;
    private TextView txtTitle, txtRating, txtBahasa, txtReleaseDate,
            txtRuntime, txtOverview, txtTagline, txtTrailer, txtFavorite;
    private String id_movie, urlPoster, urlBackdrop;
    private boolean isFavorite;
    private Menu menuItem = null;
    private Uri uri;
    private Bitmap backdrop, poster;
    private final static String POSTER_BITMAP = "poster_bitmap";
    private final static String BACKDROP_BITMAP = "backdrop_bitmap";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ID_MOVIE, id_movie);
        outState.putString(TITLE, txtTitle.getText().toString());
        outState.putString(VOTE_AVERAGE, txtRating.getText().toString());
        outState.putString(POPULARITY, txtRating.getText().toString());
        outState.putString(POSTER_PATH, urlPoster);
        outState.putParcelable(POSTER_BITMAP, poster);
        outState.putParcelable(BACKDROP_BITMAP, backdrop);
        outState.putString(ORIGINAL_LANGUAGE, txtBahasa.getText().toString());
        outState.putString(BACKDROP_PATH, urlBackdrop);
        outState.putString(OVERVIEW, txtOverview.getText().toString());
        outState.putString(RELEASE_DATE, txtReleaseDate.getText().toString());
        outState.putString(RUNTIME, txtRuntime.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        txtRating.setText(savedInstanceState.getString(VOTE_AVERAGE));
        txtReleaseDate.setText(savedInstanceState.getString(RELEASE_DATE));
        txtBahasa.setText(savedInstanceState.getString(ORIGINAL_LANGUAGE));
        txtRuntime.setText(savedInstanceState.getString(RUNTIME));
        txtOverview.setText(savedInstanceState.getString(OVERVIEW));
        txtTagline.setText(savedInstanceState.getString(TAGLINE));
        imgPoster.setImageBitmap((Bitmap) savedInstanceState.getParcelable(POSTER_BITMAP));
        imgBackdrop.setImageBitmap((Bitmap) savedInstanceState.getParcelable(BACKDROP_BITMAP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id_movie = String.valueOf(getIntent().getIntExtra("id",0));
        uri = Uri.parse(CONTENT_URI+"/"+id_movie);

        imgBackdrop = findViewById(R.id.img_backdrop_path);
        imgPoster = findViewById(R.id.img_poster_path);
        txtTitle = findViewById(R.id.txt_title);
        txtRating = findViewById(R.id.txt_rate);
        txtBahasa = findViewById(R.id.txt_bahasa);
        txtReleaseDate = findViewById(R.id.txt_release_date);
        txtRuntime = findViewById(R.id.txt_runtime);
        txtOverview = findViewById(R.id.txt_overview);
        txtTagline = findViewById(R.id.txt_tagline);

        setTitle(getIntent().getStringExtra("nama"));
        Log.i("ID Intent", id_movie);
        txtTitle.setText(getIntent().getStringExtra("nama"));
        AsyncDetailMovie asyncDetailMovie = new AsyncDetailMovie();
        asyncDetailMovie.execute(Integer.valueOf(id_movie));
        favoriteState();
    }

    private class AsyncDetailMovie extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... integers) {
            JSONObject object = getDetail(integers[0]);
            try {
                poster = Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(BuildConfig.UrlImage + object.getString("poster_path").toString())
                        .into(185, 278)
                        .get();

                backdrop = Glide.with(getApplicationContext())
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

//                WITHOUT SAVE STATE
//                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w185/" +
//                        object.getString("poster_path").toString()).into(imgPoster);
//
//                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342/" +
//                        object.getString("backdrop_path").toString()).into(imgBackdrop);
                urlPoster = object.getString("poster_path").toString();
                urlBackdrop = object.getString("backdrop_path").toString();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        menuItem = menu;
        setFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return false;
        }else if (item.getItemId() == R.id.action_favorite){
            if (isFavorite){
                removeFromFavorite();
            }else{
                addToFavorite();
            }
            setFavorite();
            return true;
//        }else if (item.getItemId() == R.id.action_share){

        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite(){
        ContentValues values = new ContentValues();
        values.put(ID_MOVIE, id_movie);
        values.put(TITLE, txtTitle.getText().toString());
        values.put(VOTE_AVERAGE, "");
        values.put(POPULARITY, txtRating.getText().toString());
        values.put(POSTER_PATH, urlPoster);
        values.put(ORIGINAL_LANGUAGE, txtBahasa.getText().toString());
        values.put(BACKDROP_PATH, urlBackdrop);
        values.put(OVERVIEW, txtOverview.getText().toString());
        values.put(RELEASE_DATE, txtReleaseDate.getText().toString());
        getContentResolver().insert(CONTENT_URI, values);

        Snackbar.make(txtTagline, "Added to favorite", Snackbar.LENGTH_SHORT).show();
        isFavorite = true;
        setFavorite();
    }
    private void removeFromFavorite(){
        getContentResolver().delete(uri, ID_MOVIE + " = ? ",
                new String[]{id_movie});

        Snackbar.make(txtTagline, "Removed from favorite", Snackbar.LENGTH_SHORT).show();
        isFavorite = false;
        setFavorite();
    }
    private void setFavorite() {
        if (isFavorite)
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_added));
        else
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_add));
    }
    private void favoriteState(){
        final ContentResolver resolver = getContentResolver();
        final String[] projection = { ID_MOVIE};
        Cursor cursor = resolver.query(uri, projection, ID_MOVIE + " = ?",
                new String[] { id_movie }, null);
        cursor.moveToFirst();

        if (cursor.getCount()>0)
            isFavorite = true;
    }
}
