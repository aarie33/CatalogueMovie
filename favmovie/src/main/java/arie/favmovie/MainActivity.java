package arie.favmovie;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import arie.favmovie.adapters.FavMovieAdapter;

import static arie.favmovie.db.DbContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity{
    private FavMovieAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Favorite Movies");

        if (ContextCompat.checkSelfPermission(this, "arie.cataloguemovie.READ_DATABASE") == PackageManager.PERMISSION_GRANTED) {
            listView = findViewById(R.id.lvFav);
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
            adapter = new FavMovieAdapter(this, cursor, true);
            listView.setAdapter(adapter);

            Log.e("Permisi", "Granted");
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{"arie.cataloguemovie.READ_DATABASE","arie.cataloguemovie.WRITE_DATABASE"},1);
            Log.e("Permisi", "Not Granted");
        }
//
//        Log.e("Cursor", String.valueOf(cursor));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}