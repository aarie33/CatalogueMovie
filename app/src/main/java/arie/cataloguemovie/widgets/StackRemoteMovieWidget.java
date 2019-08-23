package arie.cataloguemovie.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import arie.cataloguemovie.DetailMovie;
import arie.cataloguemovie.R;

import static arie.cataloguemovie.db.DbContract.CONTENT_URI;
import static arie.cataloguemovie.db.DbContract.MovieColumns.BACKDROP_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ID_MOVIE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TITLE;
import static arie.cataloguemovie.db.DbContract.getColumnString;

public class StackRemoteMovieWidget implements RemoteViewsService.RemoteViewsFactory {
    private Cursor list;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private Context context;
    private int widgetId;

    public StackRemoteMovieWidget(Context ctx, Intent intent) {
        this.context = ctx;
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        list = context.getContentResolver()
                .query(CONTENT_URI, null, null, null, null, null);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        list.moveToPosition(i);

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.movie_widget_item);

        Bitmap backdrop = null;
        try {
            backdrop = Glide.with(context)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w185" + getColumnString(list, BACKDROP_PATH))
                    .into(185, 278)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", String.valueOf(e));
        }

        rv.setImageViewBitmap(R.id.img_widget, backdrop);
        rv.setTextViewText(R.id.txt_time, getColumnString(list, RELEASE_DATE));
        Bundle extras = new Bundle();
        extras.putInt(MovieBanner.EXTRA_ITEM, i);
        Intent fill = new Intent(context, DetailMovie.class);
        fill.putExtra("id", getColumnString(list, ID_MOVIE));
        fill.putExtra("nama", getColumnString(list, TITLE));
        fill.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.img_widget, fill);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
