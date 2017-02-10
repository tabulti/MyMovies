package com.paulo.joao.mymovies.fragments;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.adapters.MoviesListAdapter;
import com.paulo.joao.mymovies.adapters.MoviesListPosterAdapter;
import com.paulo.joao.mymovies.model.MyMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao Paulo Ribeiro on 09/02/2017.
 */

public class SearchableFragment extends BaseFragment {

    private ListView moviesListView;
    private MoviesListPosterAdapter adapter;
    private List<MyMovie> movies;

    public void updateMoviesFound(String movie){
        MyMovie myObject = new Gson().fromJson(movie, MyMovie.class);
        movies = new ArrayList<>();
        movies.add(myObject);

        if (adapter == null) {
            adapter = new MoviesListPosterAdapter(this.getContext(), movies);
            moviesListView.setAdapter(adapter);
        } else {
            adapter.setMovies(movies);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String movie = getArguments().getString("movieSearched","");

        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        moviesListView = (ListView) view.findViewById(R.id.movies_searched_list_view);

        updateMoviesFound(movie);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
