package com.paulo.joao.mymovies;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.paulo.joao.mymovies.adapters.CoverFlowAdapter;
import com.paulo.joao.mymovies.adapters.MoviesListAdapter;
import com.paulo.joao.mymovies.adapters.MoviesListPosterAdapter;
import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.model.TmdbResponse;
import com.paulo.joao.mymovies.model.repository.MoviesRepository;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;
import com.paulo.joao.mymovies.retrofit.TmdbService;
import com.paulo.joao.mymovies.util.Utils;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends AppCompatActivity {

    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter coverFlowAdapter;
    private List<MyMovie> movies;
    private final String TAG = MainActivity.class.getSimpleName();
    private SearchView searchView;
    private SearchManager searchManager;
    private Button noMoviesBtn;
    private MoviesListPosterAdapter adapter;
    private List<MyMovie> lastMovieListSearched = null;
    private RelativeLayout noMoviesContainer;
    private LinearLayout mainContainer;
    private ListView moviesSearchedListView;
    private Toolbar homeToolbar;
    private RelativeLayout searchProgress;
    private MenuItem searchMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setStatusBarColor(this, Color.RED);

        RetrofitImplementation.getInstance().initRetrofit();

        MoviesRepository.getInstance().initMoviesRepository(this);

        mainContainer = (LinearLayout) findViewById(R.id.main_container);
        homeToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        homeToolbar.setBackgroundColor(Color.RED);
        homeToolbar.setTitleTextColor(Color.WHITE);
        searchProgress = (RelativeLayout) findViewById(R.id.progress);
        noMoviesContainer = (RelativeLayout) findViewById(R.id.no_movies_container);
        mainContainer = (LinearLayout) findViewById(R.id.main_container) ;
        moviesSearchedListView = (ListView) findViewById(R.id.movies_searched_list_view_main);
        noMoviesBtn = (Button) findViewById(R.id.no_movies_button);
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);

        initMovies();

        coverFlowAdapter = new CoverFlowAdapter(movies, this);

        coverFlow.setAdapter(coverFlowAdapter);
        coverFlow.setOnScrollPositionListener(onScrollPositionListener());

        noMoviesBtn.setOnClickListener(btnNoMoviesListener());

        setToolbar(homeToolbar, false);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            MyMovie selectedMovie = lastMovieListSearched.get(i);

            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);

            intent.putExtra("movieFoundToDetails", new Gson().toJson(selectedMovie));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.movie_banner), "movie");

            MovieDetailsActivity activity = new MovieDetailsActivity();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                activity.startActivity(intent);
            }
        }

    };


    public void configureSearchBar(SearchView view){
        searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
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

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


            if(!Utils.isNetworkAvailable(getApplicationContext())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext())
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

                RetrofitImplementation.getInstance().getMoviesByName(queryToSend, new TmdbService.GetMovieBySimpleNameHandler() {
                    TmdbResponse moviesSearchedResponse = null;

                    @Override
                    public void onGetMovieBySimpleName(TmdbResponse res, final Error err) {

                        if (res != null && err == null) {
                            moviesSearchedResponse = res;
                            if (moviesSearchedResponse != null) {

                                lastMovieListSearched = moviesSearchedResponse.getResults();

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (adapter == null) {
                                            adapter = new MoviesListPosterAdapter(getApplicationContext(), lastMovieListSearched);
                                            moviesSearchedListView.setAdapter(adapter);
                                            moviesSearchedListView.setOnItemClickListener(listener);

                                            changeContainerVisibility(Container.SEARCH);

                                        } else {
                                            adapter.setMovies(lastMovieListSearched);
                                        }
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showErrorDialog();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
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

    private View.OnClickListener btnNoMoviesListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMenuItem.expandActionView();
            }
        };
    }


    @Override
    protected void onResume() {
        Utils.setStatusBarColor(this, Color.RED);
        invalidateOptionsMenu();

        YoYo.with(Techniques.SlideInRight).duration(700).playOn(mainContainer);

        YoYo.with(Techniques.SlideInRight).duration(700).playOn(noMoviesContainer);

        initMovies();

        if (movies != null && !movies.isEmpty()) {

            if(coverFlow != null && coverFlowAdapter != null){
                coverFlow.setAdapter(coverFlowAdapter);
            }
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        searchMenuItem = menu.findItem(R.id.search);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {

                        changeContainerVisibility(Container.SEARCH);
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {

                        if(movies != null && !movies.isEmpty()) {
                            changeContainerVisibility(Container.HOME);
                        } else {
                            changeContainerVisibility(Container.NO_MOVIES);
                        }
                        return true;
                    }
                });

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        configureSearchBar(searchView);

        return true;
    }

    public void setToolbar(Toolbar toolbar, boolean isBackEnable) {
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            if(isBackEnable){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            }
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeContainerVisibility(Container containerToBeVisible) {
        switch (containerToBeVisible) {
            case NO_MOVIES:
                moviesSearchedListView.setVisibility(View.GONE);
                mainContainer.setVisibility(View.GONE);
                noMoviesContainer.setVisibility(View.VISIBLE);
                break;
            case SEARCH:
                mainContainer.setVisibility(View.GONE);
                noMoviesContainer.setVisibility(View.GONE);
                moviesSearchedListView.setVisibility(View.VISIBLE);
                break;
            case HOME:
                moviesSearchedListView.setVisibility(View.GONE);
                noMoviesContainer.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

    private enum Container {
        NO_MOVIES,
        SEARCH,
        HOME;
        Container(){
        }
    }
}
