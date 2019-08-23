package arie.cataloguemovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import arie.cataloguemovie.DetailMovie;
import arie.cataloguemovie.MainActTab;
import arie.cataloguemovie.R;

import static arie.cataloguemovie.db.DbContract.MovieColumns.ID_MOVIE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.OVERVIEW;
import static arie.cataloguemovie.db.DbContract.MovieColumns.POSTER_PATH;
import static arie.cataloguemovie.db.DbContract.MovieColumns.RELEASE_DATE;
import static arie.cataloguemovie.db.DbContract.MovieColumns.TITLE;
import static arie.cataloguemovie.db.DbContract.getColumnString;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {
    public Context ctx;
    public Cursor list;

    public FavAdapter(Context ctx, Cursor list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_card, parent, false);

        return new MyViewHolder(view);
    }

    public void setData(Cursor items) {
        list = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (list != null){
            list.moveToPosition(position);
            holder.txtTitle.setText(getColumnString(list,TITLE));
            holder.txtOverview.setText(getColumnString(list, OVERVIEW));
            holder.txtReleaseDate.setText(getColumnString(list, RELEASE_DATE));

            Glide.with(ctx).load("https://image.tmdb.org/t/p/w185/"
                    + getColumnString(list, POSTER_PATH)).into(holder.imgPoster);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.getCount();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle, txtOverview, txtReleaseDate;
        ImageView imgPoster;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtOverview = itemView.findViewById(R.id.txt_overview);
            txtReleaseDate = itemView.findViewById(R.id.txt_release_date);
            imgPoster = itemView.findViewById(R.id.img_movie);
        }

        @Override
        public void onClick(View view) {
            list.moveToPosition(getAdapterPosition());
            MainActTab mainActTab = (MainActTab)ctx;
            Intent intent = new Intent(ctx, DetailMovie.class);
            intent.putExtra("id", Integer.parseInt(getColumnString(list, ID_MOVIE)));
            intent.putExtra("nama", getColumnString(list, TITLE));
            mainActTab.startActivity(intent);
        }
    }
}

