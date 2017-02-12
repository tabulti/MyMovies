package com.paulo.joao.mymovies.fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.paulo.joao.mymovies.MainActivity;
import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.adapters.CoverFlowAdapter;
import com.paulo.joao.mymovies.adapters.MoviesListAdapter;
import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.retrofit.OmdbService;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;
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
    private SearchView searchView;
    private HomeFragment mFragment;
    private SearchManager searchManager;
    private ComponentName componentName;

    private RelativeLayout searchProgress;
    private MyMovie movieSearched;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainContainer = (LinearLayout) view.findViewById(R.id.main_container);

        searchProgress = (RelativeLayout) view.findViewById(R.id.progress);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        
        inflater.inflate(R.menu.options_menu, menu);


        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mFragment != null) {
                    mFragment.showMainContainer(!b);
                }
            }
        });

        configureSearchBar(searchView);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public void configureSearchBar(SearchView view){
        searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        componentName = new ComponentName(getContext(), SearchableFragment.class);
        view.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        view.setQueryHint("Título do Filme");
        view.setOnQueryTextListener(searchListener);
    }



    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(final String newText) {
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(final String query) {

            searchProgress.setVisibility(View.VISIBLE);

            RetrofitImplementation.getInstance().getMovieByName(query, new OmdbService.GetMovieBySimpleNameHandler() {
                @Override
                public void onGetMovieBySimpleName(MyMovie res, Error err) {

                    if (res != null && err == null) {
                        movieSearched = res;
                        if (movieSearched != null) {

                            Bundle bundle = new Bundle();
                            bundle.putString("movieSearched", new Gson().toJson(movieSearched));
                            SearchableFragment fragment = new SearchableFragment();
                            fragment.setArguments(bundle);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.main_content_fragment, fragment, "TestSearch")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog();
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            searchProgress.setVisibility(View.GONE);
                        }
                    });
                }
            });
            return true;
        }
    };

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                .setTitle("Ops. Algo deu errado").setMessage("Estamos com problemas" +
                        " para encontrar o seu filme, tente novamente mais tarde");

        alert.show();
    }

    public void initMovies(){
        movies = new ArrayList<>();
        movies.add(new MyMovie("Star Wars",/* R.drawable.star_wars_image, */Utils.formatYear("1992"), true));
        movies.add(new MyMovie("Harry Potter",/* R.drawable.harry_potter_image,*/Utils.formatYear("2012"), false));
        movies.add(new MyMovie("The Lord Of The Rings", /*R.drawable.lotr_image,*/Utils.formatYear("2001"), false));
    }
}
