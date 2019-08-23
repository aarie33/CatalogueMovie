package arie.cataloguemovie;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import arie.cataloguemovie.fragments.SearchFragment;

public class SearchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        mFragmentTransaction.add(R.id.frame_container, searchFragment, SearchFragment.class.getSimpleName());
        Log.d("Movie Fragment", "Fragment Name : " + SearchFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
