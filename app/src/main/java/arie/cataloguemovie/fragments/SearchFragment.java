package arie.cataloguemovie.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import arie.cataloguemovie.BuildConfig;
import arie.cataloguemovie.SearchActivity;
import arie.cataloguemovie.R;
import arie.cataloguemovie.adapters.MovieAdapter;
import arie.cataloguemovie.connections.MovieItems;
import arie.cataloguemovie.connections.MyAsyncTaskLoader;

public class SearchFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{
    private TextView txtCountResult;
    private RecyclerView rvMovie;
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private ArrayList<MovieItems> arrayList;
    private SearchActivity searchActivity;
    private String myQuery="";
    Bundle bundle = new Bundle();
    private static String SEARCH_EXTRAS = "search_extras";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        searchActivity = (SearchActivity)getContext();
        searchActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchActivity.setTitle(R.string.title_cari);
        txtCountResult = view.findViewById(R.id.txt_count_result);
        progressBar = view.findViewById(R.id.progressbar);
        rvMovie = view.findViewById(R.id.rv_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        movieAdapter = new MovieAdapter(getContext(), arrayList);
        rvMovie.setAdapter(movieAdapter);

        progressBar.setVisibility(View.INVISIBLE);

        bundle.putString(SEARCH_EXTRAS, "");
        getLoaderManager().initLoader(0, bundle, this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (movieAdapter.getItemCount()== 0) {
            if (!(myQuery == null)) {
                progressBar.setVisibility(View.VISIBLE);

//                WITHOUT LOADER
//                AsyncGetMovies asyncGetMovies = new AsyncGetMovies();
//                asyncGetMovies.execute(myQuery);
            }
        }else{
            rvMovie.setAdapter(movieAdapter);
            movieAdapter.notifyDataSetChanged();
        }
    }

    public void getQuery(String cari){
        myQuery = cari;
        progressBar.setVisibility(View.VISIBLE);

        getLoaderManager().restartLoader(0, bundle, this);

//        WITHOUT LOADER
//        AsyncGetMovies asyncGetMovies = new AsyncGetMovies();
//        asyncGetMovies.execute(cari);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.requestFocus();
        searchView.setQueryHint(getResources().getString(R.string.hint_cari));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        String movie = "";

        if (args != null){
            movie = args.getString(SEARCH_EXTRAS);
        }

        return new MyAsyncTaskLoader(this.getActivity().getApplicationContext(),
                movie, BuildConfig.UrlSearch, "&query="+myQuery);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        movieAdapter.setData(data);
        movieAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        movieAdapter.setData(null);
    }

//    WITHOUT LOADER
//    private class AsyncGetMovies extends AsyncTask<String, Void, ArrayList<MovieItems>> {
//        @Override
//        protected ArrayList<MovieItems> doInBackground(String... strings) {
//            Log.i("Doing background", strings[0]);
//            return getData(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<MovieItems> aVoid) {
//            super.onPostExecute(aVoid);
//            movieAdapter.setData(aVoid);
//            txtCountResult.setText(String.valueOf(movieAdapter.getItemCount()) + " " +
//                    getResources().getString(R.string.count_result));
//            movieAdapter.notifyDataSetChanged();
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    private ArrayList<MovieItems> getData(String query){
//        SyncHttpClient syncHttpClient = new SyncHttpClient();
//        final ArrayList<MovieItems> movieItems = new ArrayList<>();
//        String url = "https://api.themoviedb.org/3/search/movie?" +
//                "api_key="+ BuildConfig.ApiKey+"&language=en-US&query="+query;
//
//        syncHttpClient.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.i("Success out try", String.valueOf(responseBody));
//                try {
//                    String result = new String(responseBody);
//                    JSONObject rslt = new JSONObject(result);
//                    JSONArray jsonArray = rslt.getJSONArray("results");
//
//                    for (int i = 0; i<jsonArray.length(); i++) {
//                        JSONObject movie = jsonArray.getJSONObject(i);
//                        MovieItems items = new MovieItems(movie);
//                        movieItems.add(items);
//                    }
//                    Log.i("Success in try", String.valueOf(result));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("ERROR CATCH SYNC MOVIE", String.valueOf(e));
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.e("ERROR GET SYNC MOVIE", String.valueOf(responseBody));
//            }
//        });
//        return movieItems;
//    }
}
