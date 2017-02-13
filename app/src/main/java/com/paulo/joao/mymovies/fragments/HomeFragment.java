package com.paulo.joao.mymovies.fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.paulo.joao.mymovies.model.repository.MoviesRepository;
import com.paulo.joao.mymovies.retrofit.OmdbService;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;
import com.paulo.joao.mymovies.util.Utils;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import okhttp3.internal.Util;


public class HomeFragment extends Fragment {

    private LinearLayout mainContainer;
    private RelativeLayout carousel;
    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter coverFlowAdapter;
    private List<MyMovie> movies;
    private MoviesListAdapter moviesListAdapter;
    private ListView moviesListView;

    private final String TAG = HomeFragment.class.getSimpleName();
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private HomeFragment mFragment;
    private SearchManager searchManager;
    private ComponentName componentName;

    private Button noMoviesBtn;

    private RelativeLayout noMoviesContainer;

    private Toolbar homeToolbar;

    private RelativeLayout searchProgress;
    private MyMovie movieSearched;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainContainer = (LinearLayout) view.findViewById(R.id.main_container);

        homeToolbar = (Toolbar) view.findViewById(R.id.home_toolbar);
        homeToolbar.setBackgroundColor(Color.RED);
        homeToolbar.setTitleTextColor(Color.WHITE);

        searchProgress = (RelativeLayout) view.findViewById(R.id.progress);

        noMoviesContainer = (RelativeLayout) view.findViewById(R.id.no_movies_container);

        noMoviesBtn = (Button) view.findViewById(R.id.no_movies_button);

        carousel = (RelativeLayout) view.findViewById(R.id.carousel_container);
        coverFlow = (FeatureCoverFlow) view.findViewById(R.id.coverflow);

        initMovies();

        coverFlowAdapter = new CoverFlowAdapter(movies, getContext());

        coverFlow.setAdapter(coverFlowAdapter);
        coverFlow.setOnScrollPositionListener(onScrollPositionListener());

        moviesListView = (ListView) view.findViewById(R.id.movies_list_view);

        moviesListAdapter = new MoviesListAdapter(getContext(), movies);

        moviesListView.setAdapter(moviesListAdapter);

        moviesListView.setOnItemClickListener(listenerListView());

        noMoviesBtn.setOnClickListener(btnNoMoviesListener());

//        coverFlow.setOnClickListener();

        return view;
    }

    private View.OnClickListener btnNoMoviesListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenuItem.expandActionView();
        }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.setStatusBarColor(getActivity(), Color.RED);

        if (movies != null && !movies.isEmpty()) {
            noMoviesContainer.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
        }
    }

    private AdapterView.OnItemClickListener listenerListView () {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MyMovie movie = (MyMovie) adapterView.getItemAtPosition(i);

                if (movie != null) {
                    MovieDetailsFragment fragment = new MovieDetailsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("movieFoundToDetails", new Gson().toJson(movie));

                    fragment.setArguments(bundle);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_content_fragment, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        };
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showMainContainer(boolean b) {
        if (b) {
            mainContainer.setVisibility(View.VISIBLE);
        } else {
            mainContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        
        inflater.inflate(R.menu.options_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchMenuItem = (MenuItem) menu.findItem(R.id.search);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public void configureSearchBar(SearchView view){
        searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        componentName = new ComponentName(getContext(), SearchableFragment.class);
        view.setQueryHint(getString(R.string.search_bar_hint));
        view.setOnQueryTextListener(searchListener);
    }

    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(final String newText) {
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(final String query) {

            String queryToSend = Utils.formatUrlString(query);

            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


            if(!Utils.isNetworkAvailable(getContext())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_warning_title)
                        .setCancelable(false)
                        .setMessage(getString(R.string.alert_no_connection_msg))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                                startActivity(intent);
                            }
                        }).setNegativeButton(getString(R.string.no), null);

                alert.show();
            } else {
                searchProgress.bringToFront();
                searchProgress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Do nothing to avoid click action on background layout
                    }
                });
                searchProgress.setVisibility(View.VISIBLE);

                RetrofitImplementation.getInstance().getMovieByName(queryToSend, new OmdbService.GetMovieBySimpleNameHandler() {
                    @Override
                    public void onGetMovieBySimpleName(MyMovie res, final Error err) {

                        if (res != null && err == null) {
                            movieSearched = res;
                            if (movieSearched != null) {

                                Bundle bundle = new Bundle();
                                bundle.putString("movieSearched", new Gson().toJson(movieSearched));
                                SearchableFragment fragment = new SearchableFragment();
                                fragment.setArguments(bundle);

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.main_content_fragment, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showErrorDialog();
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
            }
            return true;
        }
    };

    private void showErrorDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_error_title)
                .setMessage(getString(R.string.alert_error_msg))
                .setPositiveButton(getString(R.string.yes), null);
        alert.show();
    }

    public void initMovies(){
        movies = new ArrayList<>();
        movies = MoviesRepository.getInstance().getMovies();
        if (movies == null || movies.isEmpty()) {
            mainContainer.setVisibility(View.GONE);
            noMoviesContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setToolbar(homeToolbar, false);
    }


}
