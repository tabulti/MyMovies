package com.paulo.joao.mymovies;


import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.paulo.joao.mymovies.fragments.HomeFragment;
import com.paulo.joao.mymovies.model.repository.MoviesRepository;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;
import com.paulo.joao.mymovies.util.Utils;

public class MainActivity extends AppCompatActivity {

    private HomeFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setStatusBarColor(this, Color.RED);

        RetrofitImplementation.getInstance().initRetrofit();

        MoviesRepository.getInstance().initMoviesRepository(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(findViewById(R.id.main_content_fragment) != null) {

            if(savedInstanceState != null) {
                return;
            }
            mFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_fragment, mFragment).addToBackStack(null).commit();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        onBackPressed();
        return super.onKeyDown(keyCode, event);
    }
}
