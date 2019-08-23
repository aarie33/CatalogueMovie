package arie.cataloguemovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import arie.cataloguemovie.DetailMovie;
import arie.cataloguemovie.R;
import arie.cataloguemovie.SearchActivity;
import arie.cataloguemovie.connections.MovieItems;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    public Context ctx;
    public ArrayList<MovieItems> list;

    public MovieAdapter(Context ctx, ArrayList list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item, parent, false);

        return new MyViewHolder(view);
    }

    public void setData(ArrayList<MovieItems> items) {
        list = items;
        notifyDataSetChanged();
    }
    public void addItem(final MovieItems items) {
        list.add(items);
        notifyDataSetChanged();
    }
    public void clearData() {
        list.clear();
    }

    public ArrayList<MovieItems> getList() {
        return list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MovieItems items = list.get(position);
        holder.txtTitle.setText(items.getTitle());
        holder.txtOverview.setText(items.getOverview());
        holder.txtReleaseDate.setText(items.getRelease_date());
        try {
            Glide.with(ctx).load("https://image.tmdb.org/t/p/w185/" + items.getPoster_path())
                    .into(holder.imgPoster);
        } catch (Exception e) {
            holder.imgPoster.setImageResource(R.drawable.ic_image_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle, txtOverview, txtReleaseDate;
        ImageView imgPoster;
        ArrayList arrayList;

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
            SearchActivity searchActivity = (SearchActivity)ctx;

//            MovieDetailSearchFragment movieDetailSearchFragment = MovieDetailSearchFragment.createInstance(
//                    list.get(this.getAdapterPosition()).getId(), list.get(this.getAdapterPosition()).getTitle());
//
//            FragmentManager fragmentManager = searchActivity.getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.frame_container, movieDetailSearchFragment,
//                    SearchFragment.class.getSimpleName());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

//            MainActTab mainActTab = (MainActTab)ctx;
            Intent intent = new Intent(ctx, DetailMovie.class);
            intent.putExtra("id", list.get(this.getAdapterPosition()).getId());
            intent.putExtra("nama", list.get(this.getAdapterPosition()).getTitle());
            searchActivity.startActivity(intent);
        }
    }
}
