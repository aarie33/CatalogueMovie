package arie.cataloguemovie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import arie.cataloguemovie.connections.MovieItems;

import static android.provider.BaseColumns._ID;
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

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_MOVIE;
    private Context context;
    private DbHelper dataBaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context){
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dataBaseHelper = new DbHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public ArrayList<MovieItems> query(){
        ArrayList<MovieItems> arrayList = new ArrayList<MovieItems>();
        Cursor cursor = database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null,_ID +" DESC"
                ,null);
        cursor.moveToFirst();
        MovieItems movieItems;
        if (cursor.getCount()>0) {
            do {
                movieItems = new MovieItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_MOVIE)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movieItems.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                movieItems.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movieItems.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movieItems.setOriginal_language(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));
                movieItems.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                movieItems.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movieItems.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));

                arrayList.add(movieItems);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MovieItems movie){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(ID_MOVIE, movie.getId());
        initialValues.put(TITLE, movie.getTitle());
        initialValues.put(POSTER_PATH, movie.getPoster_path());
        initialValues.put(ORIGINAL_LANGUAGE, movie.getOriginal_language());
        initialValues.put(BACKDROP_PATH, movie.getBackdrop_path());
        initialValues.put(OVERVIEW, movie.getOverview());
        initialValues.put(RELEASE_DATE, movie.getRelease_date());
        initialValues.put(VOTE_AVERAGE, movie.getVote_average());
        initialValues.put(POPULARITY, movie.getPopularity());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(MovieItems movie){
        ContentValues args = new ContentValues();
        args.put(ID_MOVIE, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(POSTER_PATH, movie.getPoster_path());
        args.put(ORIGINAL_LANGUAGE, movie.getOriginal_language());
        args.put(BACKDROP_PATH, movie.getBackdrop_path());
        args.put(OVERVIEW, movie.getOverview());
        args.put(RELEASE_DATE, movie.getRelease_date());
        args.put(VOTE_AVERAGE, movie.getVote_average());
        args.put(POPULARITY, movie.getPopularity());
        return database.update(DATABASE_TABLE, args, ID_MOVIE + "= '" + movie.getId() + "'", null);
    }

    public int delete(int id){
        return database.delete(TABLE_MOVIE, ID_MOVIE + " = '"+id+"'", null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,ID_MOVIE + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }
    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }
    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,ID_MOVIE +" = ?",new String[]{id} );
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,ID_MOVIE + " = ?", new String[]{id});
    }
}
