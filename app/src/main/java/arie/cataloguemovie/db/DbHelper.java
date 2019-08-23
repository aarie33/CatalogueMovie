package arie.cataloguemovie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static arie.cataloguemovie.db.DbContract.MovieColumns.BACKDROP_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ID_MOVIE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ORIGINAL_LANGUAGE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.OVERVIEW;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POPULARITY;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POSTER_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TITLE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.VOTE_AVERAGE;
import static arie.cataloguemovie.db.DbContract.TABLE_MOVIE;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbcatalogmovie";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_MOVIE,
            DbContract.MovieColumns._ID,
            ID_MOVIE,
            TITLE,
            POSTER_PATH,
            ORIGINAL_LANGUAGE,
            BACKDROP_PATH,
            OVERVIEW,
            RELEASE_DATE,
            VOTE_AVERAGE,
            POPULARITY
    );
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }
}
