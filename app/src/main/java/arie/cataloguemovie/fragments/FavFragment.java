package arie.cataloguemovie.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import arie.cataloguemovie.R;
import arie.cataloguemovie.adapters.FavAdapter;

import static arie.cataloguemovie.db.DbContract.CONTENT_URI;

public class FavFragment extends Fragment {
    private FavAdapter adapter;
    private RecyclerView recyclerView;
    private Cursor list;

    public FavFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        recyclerView = view.findViewById(R.id.rvFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor cursor = getActivity().getContentResolver()
                .query(CONTENT_URI, null, null, null, null, null);
        adapter = new FavAdapter(getContext(), cursor);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
