package com.paulo.joao.mymovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulo.joao.mymovies.R;


public class HomeFragment extends Fragment {


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(view.findViewById(R.id.carousel_container) != null && view.findViewById(R.id.movies_list_container) != null) {

            if(savedInstanceState != null) {
                return;
            }
            CarouselFragment carouselFragment = new CarouselFragment();
            MoviesListFragment moviesListFragment = new MoviesListFragment();

            getChildFragmentManager().beginTransaction()
                    .add(R.id.carousel_container, carouselFragment)
                    .add(R.id.movies_list_container, moviesListFragment)
                    .commit();
        }

    }
}
