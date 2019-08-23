package arie.cataloguemovie.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import arie.cataloguemovie.BuildConfig;
import arie.cataloguemovie.R;
import arie.cataloguemovie.adapters.MovieAdapterNowUpComing;
import arie.cataloguemovie.connections.MovieItems;
import arie.cataloguemovie.connections.MyAsyncTaskLoader;

public class NowPlayingFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{
    private RecyclerView rvNowPlaying;
    private MovieAdapterNowUpComing movieAdapter;
    private ArrayList<MovieItems> arrayList;
    private static String NOW_EXTRAS = "now_extras";

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        rvNowPlaying = view.findViewById(R.id.rv_now_playing);
        rvNowPlaying.setLayoutManager(new LinearLayoutManager(getContext()));
        movieAdapter = new MovieAdapterNowUpComing(getContext(), arrayList);
        rvNowPlaying.setAdapter(movieAdapter);

//        without LOADER
//        GetNowMovies getNowMovies = new GetNowMovies();
//        getNowMovies.execute();

        Bundle bundle = new Bundle();
        bundle.putString(NOW_EXTRAS, "");

        getLoaderManager().initLoader(0, bundle, this);

        return view;
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        String movie = "";

        if (args != null){
            movie = args.getString(NOW_EXTRAS);
        }

        return new MyAsyncTaskLoader(this.getActivity().getApplicationContext(), movie, BuildConfig.UrlNowPlaying, null);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        movieAdapter.setData(data);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        movieAdapter.setData(null);
    }

//    WITHOUT LOADER
//    private class GetNowMovies extends AsyncTask<String, Void, ArrayList<MovieItems>> {
//        @Override
//        protected ArrayList<MovieItems> doInBackground(String... strings) {
//            return getData();
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<MovieItems> movieItems) {
//            super.onPostExecute(movieItems);
//
//            movieAdapter.setData(movieItems);
//            movieAdapter.notifyDataSetChanged();
//        }
//    }
//    private ArrayList<MovieItems> getData(){
//        SyncHttpClient syncHttpClient = new SyncHttpClient();
//        final ArrayList<MovieItems> list = new ArrayList<>();
//        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ BuildConfig.ApiKey +
//                "&language=en-US";
//        syncHttpClient.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    String result = new String(responseBody);
//                    JSONObject rslt = new JSONObject(result);
//                    JSONArray jsonArray = rslt.getJSONArray("results");
//                    for (int i = 0; i<jsonArray.length(); i++) {
//                        JSONObject movie = jsonArray.getJSONObject(i);
//                        MovieItems movieItems = new MovieItems(movie);
//                        list.add(movieItems);
//                    }
//                } catch (Exception e) {
//                    Log.e("Exception get Data ", String.valueOf(e));
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.e("Error onFailure gData", String.valueOf(error));
//            }
//        });
//        return list;
//    }
}
