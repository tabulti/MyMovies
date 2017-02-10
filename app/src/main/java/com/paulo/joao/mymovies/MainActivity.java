package com.paulo.joao.mymovies;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.paulo.joao.mymovies.adapters.MoviesListPosterAdapter;
import com.paulo.joao.mymovies.fragments.HomeFragment;
import com.paulo.joao.mymovies.fragments.SearchableFragment;
import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.retrofit.OmdbService;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;

public class MainActivity extends AppCompatActivity {

    private MyMovie movieSearched = null;
    private SearchView searchView;
    private SearchManager searchManager;
    private ComponentName componentName;
    private RelativeLayout searchProgress;
    private HomeFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchProgress = (RelativeLayout) findViewById(R.id.progress);

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

            mFragment = new HomeFragment();
            mFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_fragment, mFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
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

        return super.onCreateOptionsMenu(menu);
    }

    public void configureSearchBar(SearchView view){
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        componentName = new ComponentName(MainActivity.this, SearchableFragment.class);
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

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_content_fragment, fragment, "TestSearch")
                                        .addToBackStack(null)
                                        .commit();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog();
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
            return true;
        }
    };

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Ops. Algo deu errado").setMessage("Estamos com problemas" +
                        " para encontrar o seu filme, tente novamente mais tarde");

        alert.show();
    }
}
