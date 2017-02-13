package com.paulo.joao.mymovies.fragments;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
    private MyMovie movieFound;

    private RelativeLayout searchContainer;

    public void updateMoviesFound(String movie){
        movieFound = new Gson().fromJson(movie, MyMovie.class);
        movies = new ArrayList<>();
        movies.add(movieFound);

        if (adapter == null) {
            adapter = new MoviesListPosterAdapter(this.getContext(), movies);
            moviesListView.setAdapter(adapter);
        } else {
            adapter.setMovies(movies);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        YoYo.with(Techniques.FadeInUp).duration(700).playOn(searchContainer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String movie = getArguments().getString("movieSearched","");

        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        searchContainer = (RelativeLayout) view.findViewById(R.id.search_container);

        moviesListView = (ListView) view.findViewById(R.id.movies_searched_list_view);

        moviesListView.setOnItemClickListener(listener);

        updateMoviesFound(movie);

        setHasOptionsMenu(true);

        return view;
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle bundle = new Bundle();
            bundle.putString("movieFoundToDetails", new Gson().toJson(movieFound));
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(bundle);

            getFragmentManager().popBackStack();

            getFragmentManager().beginTransaction()
                    .replace(R.id.main_content_fragment, fragment, MovieDetailsFragment.class.getSimpleName())
                    .addToBackStack("null")
                    .commit();
        }

    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
