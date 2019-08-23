package arie.cataloguemovie.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {
    public static String TABLE_MOVIE = "movie";

    public static final class MovieColumns implements BaseColumns {
        public static String ID_MOVIE = "movie_id";
        public static String TITLE = "title";
        public static String POSTER_PATH = "poster_path";
        public static String ORIGINAL_LANGUAGE = "original_language";
        public static String BACKDROP_PATH = "backdrop_path";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";
        public static String VOTE_AVERAGE = "vote_average";
        public static String POPULARITY = "popularity";
        public static String RUNTIME = "runtime";
        public static String TAGLINE = "tagline";
    }

    public static final String AUTHORITY = "arie.cataloguemovie";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}