package arie.cataloguemovie.connections;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import arie.cataloguemovie.BuildConfig;
import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {
    private ArrayList<MovieItems> mData;
    private boolean mHasResult = false;
    private String movies;
    private String url;
    private String query;

    public MyAsyncTaskLoader(final Context context, String myMovies, String url, String query) {
        super(context);

        onContentChanged();
        this.movies = myMovies;
        this.url = url;
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mData);
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> movieItems = new ArrayList<>();
        String url = this.url + BuildConfig.ApiKey + "&language=en-US" + this.query;
//        Log.e("URL nya", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject rslt = new JSONObject(result);
                    JSONArray jsonArray = rslt.getJSONArray("results");
                    for (int i = 0; i<jsonArray.length(); i++) {
                        JSONObject movie = jsonArray.getJSONObject(i);
                        MovieItems items = new MovieItems(movie);
                        movieItems.add(items);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieItems;
    }

    protected void onReleaseResources(ArrayList<MovieItems> data) {

    }

}
