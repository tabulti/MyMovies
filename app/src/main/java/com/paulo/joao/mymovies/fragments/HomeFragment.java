package com.paulo.joao.mymovies.fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
    private HomeFragment mFragment;
    private SearchManager searchManager;
    private ComponentName componentName;

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

//        coverFlow.setOnClickListener();

        return view;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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

            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

            searchProgress.bringToFront();
            searchProgress.setVisibility(View.VISIBLE);

            RetrofitImplementation.getInstance().getMovieByName(query, new OmdbService.GetMovieBySimpleNameHandler() {
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
                                showErrorDialog(err);
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

    private void showErrorDialog(Error error) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                .setTitle("Ops. Algo deu errado").setMessage("Estamos com problemas" +
                        " para encontrar o seu filme, tente novamente mais tarde\n\nError code: " + error.toString());

        alert.show();
    }

    public void initMovies(){
        movies = new ArrayList<>();
        movies = MoviesRepository.getInstance().getMovies();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setToolbar(homeToolbar, false);
    }
}
