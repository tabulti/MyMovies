package com.paulo.joao.mymovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.adapters.CoverFlowAdapter;
import com.paulo.joao.mymovies.adapters.MoviesListAdapter;
import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.util.Utils;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


public class HomeFragment extends Fragment {

    private LinearLayout mainContainer;
    private FrameLayout carousel;
    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter coverFlowAdapter;
    private List<MyMovie> movies;
    private MoviesListAdapter moviesListAdapter;
    private ListView moviesListView;

    private final String TAG = HomeFragment.class.getSimpleName();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainContainer = (LinearLayout) view.findViewById(R.id.main_container);

        carousel = (FrameLayout) view.findViewById(R.id.carousel_container);
        coverFlow = (FeatureCoverFlow) view.findViewById(R.id.coverflow);

        initMovies();

        coverFlowAdapter = new CoverFlowAdapter(movies, getContext());

        coverFlow.setAdapter(coverFlowAdapter);
        coverFlow.setOnScrollPositionListener(onScrollPositionListener());

        moviesListView = (ListView) view.findViewById(R.id.movies_list_view);

        moviesListAdapter = new MoviesListAdapter(getContext(), movies);

        moviesListView.setAdapter(moviesListAdapter);

        return view;
    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollPositionListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                Log.v(TAG, "position: " + position);
            }

            @Override
            public void onScrolling() {
                Log.i(TAG, "scrolling");
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showMainContainer(boolean b) {
        if (b) {
            mainContainer.setVisibility(View.VISIBLE);/*
            carousel.setVisibility(View.VISIBLE);*/
        } else {
            mainContainer.setVisibility(View.GONE);/*
            carousel.setVisibility(View.GONE);*/
        }

    }


    public void initMovies(){
        movies = new ArrayList<>();
        movies.add(new MyMovie("Star Wars",/* R.drawable.star_wars_image, */Utils.formatYear("1992"), true));
        movies.add(new MyMovie("Harry Potter",/* R.drawable.harry_potter_image,*/Utils.formatYear("2012"), false));
        movies.add(new MyMovie("The Lord Of The Rings", /*R.drawable.lotr_image,*/Utils.formatYear("2001"), false));
    }
}
