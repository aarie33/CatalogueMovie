package arie.favmovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import arie.favmovie.R;

import static arie.favmovie.db.DbContract.MovieColumns.OVERVIEW;
import static arie.favmovie.db.DbContract.MovieColumns.POSTER_PATH;
import static arie.favmovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.favmovie.db.DbContract.MovieColumns.TITLE;
import static arie.favmovie.db.DbContract.getColumnString;

public class FavMovieAdapter extends CursorAdapter {

    public FavMovieAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, viewGroup, false);
        return view;
    }


    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            TextView txtTitle, txtOverview, txtReleaseDate;
            ImageView imgPoster;

            txtTitle = view.findViewById(R.id.txt_title);
            txtOverview = view.findViewById(R.id.txt_overview);
            txtReleaseDate = view.findViewById(R.id.txt_release_date);
            imgPoster = view.findViewById(R.id.img_movie);

            txtTitle.setText(getColumnString(cursor,TITLE));
            txtOverview.setText(getColumnString(cursor, OVERVIEW));
            txtReleaseDate.setText(getColumnString(cursor, RELEASE_DATE));

            Glide.with(context).load("https://image.tmdb.org/t/p/w185/" + getColumnString(cursor, POSTER_PATH)).into(imgPoster);
        }
    }
}

