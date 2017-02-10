package com.paulo.joao.mymovies.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.adapters.CoverFlowAdapter;
import com.paulo.joao.mymovies.util.Utils;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselFragment extends BaseFragment {

    private List<MyMovie> movies;
    private static final String TAG = CarouselFragment.class.getSimpleName();
    private static final String STAR_WARS_URL = "https://images-na.ssl-images-amazon.com/images/M/MV5BZGEzZTExMDEtNjg4OC00NjQxLTk5NTUtNjRkNjA3MmYwZjg1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg";
    private static final String HARRY_POTTER_URL = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTY2MTk3MDQ1N15BMl5BanBnXkFtZTcwMzI4NzA2NQ@@._V1_SX300.jpg";
    private static final String LOTR_URL = "https://images-na.ssl-images-amazon.com/images/M/MV5BNTEyMjAwMDU1OV5BMl5BanBnXkFtZTcwNDQyNTkxMw@@._V1_SX300.jpg";

    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter adapter;


    public CarouselFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coverFlow = (FeatureCoverFlow) view.findViewById(R.id.coverflow);

        initMovies();

        adapter = new CoverFlowAdapter(movies, getContext());

        coverFlow.setAdapter(adapter);
        coverFlow.setOnScrollPositionListener(onScrollPositionListener());
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

    public void initMovies(){
        movies = new ArrayList<>();

        movies.add(new MyMovie("Star Wars",/* R.drawable.star_wars_image, */Utils.formatYear("1992"), true));
        movies.add(new MyMovie("Harry Potter",/* R.drawable.harry_potter_image,*/Utils.formatYear("2012"), false));
        movies.add(new MyMovie("The Lord Of The Rings", /*R.drawable.lotr_image,*/Utils.formatYear("2001"), false));
    }

}
