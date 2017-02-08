package com.paulo.joao.mymovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.paulo.joao.mymovies.adapters.MoviesListAdapter;

import java.util.ArrayList;

/**
 * Created by Joao Paulo Ribeiro on 07/02/2017.
 */

public class MoviesListFragment extends BaseFragment {
    private static final String TAG = MoviesListFragment.class.getSimpleName();
    private ArrayList<MyMovie> movies;
    private MoviesListAdapter adapter;
    private ListView moviesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initMovies();

        moviesListView = (ListView) view.findViewById(R.id.movies_list_view);

        adapter = new MoviesListAdapter(getContext(), movies);

        moviesListView.setAdapter(adapter);

    }

    public void initMovies(){
        movies = new ArrayList<>();

        movies.add(new MyMovie("Star Wars", R.drawable.star_wars_image, true));
        movies.add(new MyMovie("Harry Potter", R.drawable.harry_potter_image, false));
        movies.add(new MyMovie("The Lord Of The Rings", R.drawable.lotr_image, false));
    }
}
