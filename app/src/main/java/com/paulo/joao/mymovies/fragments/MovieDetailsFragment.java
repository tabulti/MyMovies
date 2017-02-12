package com.paulo.joao.mymovies.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.picassopalette.PicassoPalette;
import com.google.gson.Gson;
import com.paulo.joao.mymovies.MainActivity;
import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.model.repository.MoviesRepository;
import com.paulo.joao.mymovies.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MovieDetailsFragment extends BaseFragment {

    private ImageView imageBanner;
    private TextView imageBannerTitle;
    private TextView description;
    private MyMovie movieFound;

    private Toolbar detailsToolbar;

    public MovieDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        detailsToolbar = (Toolbar) view.findViewById(R.id.description_toolbar);
        detailsToolbar.setTitle("Movie Details");
        detailsToolbar.setTitleTextColor(Color.WHITE);

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
                                .use(PicassoPalette.Profile.MUTED_LIGHT)
                                .intoBackground(imageBanner)
                                .use(PicassoPalette.Profile.MUTED_DARK)
                                .intoBackground(detailsToolbar));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().invalidateOptionsMenu();
        menu.clear();
        inflater.inflate(R.menu.options_menu_details, menu);

        if (movieFound != null && MoviesRepository.getInstance().hasMovie(movieFound)) {
            menu.findItem(R.id.save_ic).setVisible(false);
        } else {
            menu.findItem(R.id.delete_ic).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setToolbar(detailsToolbar, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_ic:
                if (movieFound != null) {
                    boolean wasMovieSaved = MoviesRepository.getInstance().saveMovie(movieFound);
                    String msgResponse = "Infelizmente não foi possível salvar o seu filme";

                    if (wasMovieSaved) {
                        msgResponse = "Filme salvo com sucesso";
                    }

                    Toast.makeText(getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.delete_ic:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                        .setTitle("Apagar Filme")
                        .setMessage("Tem certeza de que deseja apagar?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MoviesRepository.getInstance().deleteMovie(movieFound);
                            }
                        }).setNegativeButton("Não", null);
                alert.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
