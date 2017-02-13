package com.paulo.joao.mymovies.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


public class MovieDetailsFragment extends BaseFragment {

    private ImageView imageBanner;
    private TextView imageBannerTitle;
    private TextView description;
    private TextView actors;
    private TextView genre;
    private TextView awards;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private MyMovie movieFound;

    private Toolbar detailsToolbar;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        detailsToolbar = (Toolbar) view.findViewById(R.id.description_toolbar);
        detailsToolbar.setTitle(R.string.movie_details);
        detailsToolbar.setTitleTextColor(Color.WHITE);

        setHasOptionsMenu(true);

        star1 = (ImageView) view.findViewById(R.id.star1);
        star2 = (ImageView) view.findViewById(R.id.star2);
        star3 = (ImageView) view.findViewById(R.id.star3);
        star4 = (ImageView) view.findViewById(R.id.star4);
        star5 = (ImageView) view.findViewById(R.id.star5);

        imageBanner = (ImageView) view.findViewById(R.id.movie_banner);
        imageBannerTitle = (TextView) view.findViewById(R.id.movie_banner_title);
        description = (TextView) view.findViewById(R.id.movie_description);
        actors = (TextView) view.findViewById(R.id.actors_tv);
        genre = (TextView) view.findViewById(R.id.genre_tv);
        awards = (TextView) view.findViewById(R.id.awards_tv);

        String movie = getArguments().getString("movieFoundToDetails","");

        movieFound = new Gson().fromJson(movie, MyMovie.class);

        Picasso.with(getContext()).load(movieFound.getPoster())
                .into(imageBanner,
                        PicassoPalette.with(movieFound.getPoster(), imageBanner)
                                .use(PicassoPalette.Profile.MUTED_LIGHT)
                                .intoBackground(imageBanner)
                                .use(PicassoPalette.Profile.MUTED_DARK)
                                .intoBackground(detailsToolbar)
                                .intoCallBack(new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(final Palette palette) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (palette != null && palette.getDarkMutedSwatch() != null) {
                                                    Utils.setStatusBarColor(getActivity(), palette.getDarkMutedSwatch().getRgb());
                                                }
                                            }
                                        });
                                    }
                                }));


        imageBannerTitle.setText(movieFound.getTitle() + "\n" + Utils.formatYear(movieFound.getRuntime()));
        description.setText(movieFound.getPlot());
        actors.setText(movieFound.getActors());
        genre.setText(movieFound.getGenre());
        awards.setText(movieFound.getAwards());

        int res = calculateStar(Double.valueOf(movieFound.getImdbRating()));

        setStars(res);

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setToolbar(detailsToolbar, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_ic:
                if (movieFound != null) {
                    boolean wasMovieSaved = MoviesRepository.getInstance().saveMovie(movieFound);
                    String msgResponse = getString(R.string.movie_not_saved);

                    if (wasMovieSaved) {
                        msgResponse = getString(R.string.movie_saved);
                    }

                    Toast.makeText(getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.delete_ic:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.dialog_title_delete_movie)
                        .setMessage(R.string.dialog_msg_delete_movie)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MoviesRepository.getInstance().deleteMovie(movieFound);
                            }
                        }).setNegativeButton(R.string.no, null);
                alert.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int calculateStar(Double imdbRating) {
        return (int) (imdbRating / 2);
    }

    private void setStars(int number) {
        switch (number) {
            case 1:
                star1.setImageResource(R.drawable.star_enable);
                star2.setImageResource(R.drawable.star_disable);
                star3.setImageResource(R.drawable.star_disable);
                star4.setImageResource(R.drawable.star_disable);
                star5.setImageResource(R.drawable.star_disable);
                break;
            case 2:
                star1.setImageResource(R.drawable.star_enable);
                star2.setImageResource(R.drawable.star_enable);
                star3.setImageResource(R.drawable.star_disable);
                star4.setImageResource(R.drawable.star_disable);
                star5.setImageResource(R.drawable.star_disable);
                break;
            case 3:
                star1.setImageResource(R.drawable.star_enable);
                star2.setImageResource(R.drawable.star_enable);
                star3.setImageResource(R.drawable.star_enable);
                star4.setImageResource(R.drawable.star_disable);
                star5.setImageResource(R.drawable.star_disable);
                break;
            case 4:
                star1.setImageResource(R.drawable.star_enable);
                star2.setImageResource(R.drawable.star_enable);
                star3.setImageResource(R.drawable.star_enable);
                star4.setImageResource(R.drawable.star_enable);
                star5.setImageResource(R.drawable.star_disable);
                break;
            case 5:
                star1.setImageResource(R.drawable.star_enable);
                star2.setImageResource(R.drawable.star_enable);
                star3.setImageResource(R.drawable.star_enable);
                star4.setImageResource(R.drawable.star_enable);
                star5.setImageResource(R.drawable.star_enable);
                break;
        }
    }
}
