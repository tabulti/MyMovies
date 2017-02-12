package com.paulo.joao.mymovies;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paulo.joao.mymovies.fragments.HomeFragment;
import com.paulo.joao.mymovies.retrofit.RetrofitImplementation;

public class MainActivity extends AppCompatActivity {

    private HomeFragment mFragment;

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
            mFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_fragment, mFragment).addToBackStack(null).commit();
        }
    }
}
