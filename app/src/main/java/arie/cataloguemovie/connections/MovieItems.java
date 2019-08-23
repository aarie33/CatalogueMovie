package arie.cataloguemovie.connections;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static arie.cataloguemovie.db.DbContract.MovieColumns.BACKDROP_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.ORIGINAL_LANGUAGE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.OVERVIEW;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POPULARITY;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POSTER_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TITLE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.VOTE_AVERAGE;
import static arie.cataloguemovie.db.DbContract.getColumnInt;
import static arie.cataloguemovie.db.DbContract.getColumnString;

public class MovieItems implements Parcelable {
    private int id;
    private double vote_average, popularity;
    private String title, poster_path, original_language, backdrop_path, overview, release_date;

    public MovieItems(JSONObject object) {
        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String overview = object.getString("overview");
            String poster_path = object.getString("poster_path");
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.poster_path = poster_path;
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeDouble(this.vote_average);
        dest.writeDouble(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_language);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    public MovieItems() {

    }

    public MovieItems(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.vote_average = getColumnInt(cursor, VOTE_AVERAGE);
        this.popularity = getColumnInt(cursor, POPULARITY);
        this.poster_path = getColumnString(cursor, POSTER_PATH);
        this.original_language = getColumnString(cursor, ORIGINAL_LANGUAGE);
        this.backdrop_path = getColumnString(cursor, BACKDROP_PATH);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.release_date = getColumnString(cursor, RELEASE_DATE);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.vote_average = in.readDouble();
        this.popularity = in.readDouble();
        this.poster_path = in.readString();
        this.original_language = in.readString();
        this.backdrop_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<MovieItems> CREATOR = new Parcelable.Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}
