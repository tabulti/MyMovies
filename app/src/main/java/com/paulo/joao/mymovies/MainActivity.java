package com.paulo.joao.mymovies;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.paulo.joao.mymovies.adapters.MoviesListPosterAdapter;
import com.paulo.joao.mymovies.fragments.HomeFragment;
import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.retrofit.OmdbService;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MoviesListPosterAdapter adapter;
    private MyMovie movieSearched = null;
    private SearchView searchView;
    private MenuItem menuSearchItem;
    private SearchManager searchManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitImplementation.getInstance().initRetrofit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(findViewById(R.id.main_content_fragment) != null) {

            if(savedInstanceState != null) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("teste", "from Activity");

            HomeFragment fragment = new HomeFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content_fragment, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        configureSearchBar(searchView);

        return super.onCreateOptionsMenu(menu);
    }

    public void configureSearchBar(SearchView view){
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        componentName = new ComponentName(MainActivity.this, SearchableActivity.class);
        view.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        view.setQueryHint("Título do Filme");
        view.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        view.setOnQueryTextListener(searchListener);
    }


    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(final String newText) {
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(final String query) {

            RetrofitImplementation.getInstance().getMovieByName(query, new OmdbService.GetMovieBySimpleNameHandler() {
                @Override
                public void onGetMovieBySimpleName(MyMovie res, Error err) {
                    movieSearched = res;
                }
            });

            if (movieSearched != null) {
                List<MyMovie> movies = new ArrayList<>();
                movies.add(movieSearched);
            }
            return true;
        }
    };
}
