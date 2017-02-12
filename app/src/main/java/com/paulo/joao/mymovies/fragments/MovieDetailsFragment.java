package com.paulo.joao.mymovies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.google.gson.Gson;
import com.paulo.joao.mymovies.MainActivity;
import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.model.MyMovie;
import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends BaseFragment {

    private ImageView imageBanner;
    private TextView imageBannerTitle;
    private TextView description;
    private MyMovie movieFound;


    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        setHasOptionsMenu(true);

        imageBanner = (ImageView) view.findViewById(R.id.movie_banner);
        imageBannerTitle = (TextView) view.findViewById(R.id.movie_banner_title);
        description = (TextView) view.findViewById(R.id.movie_description);

        String movie = getArguments().getString("movieFoundToDetails","");

        movieFound = new Gson().fromJson(movie, MyMovie.class);

        imageBannerTitle.setText(movieFound.getTitle());
        description.setText(movieFound.getPlot());
        Picasso.with(getContext()).load(movieFound.getPoster())
                .into(imageBanner,
                        PicassoPalette.with(movieFound.getPoster(), imageBanner)
                                .use(PicassoPalette.Profile.VIBRANT_DARK)
                                .intoBackground(imageBanner));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().invalidateOptionsMenu();
        menu.clear();
        inflater.inflate(R.menu.options_menu_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_ic:
                Log.i("i", "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
