package com.paulo.joao.mymovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.main_content_fragment) != null) {

            if(savedInstanceState != null) {
                return;
            }

//            CarouselFragment fragment = new CarouselFragment();
            HomeFragment fragment = new HomeFragment();

            /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.main_content_fragment, fragment);
            transaction.addToBackStack(null);

            transaction.commit();*/

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content_fragment, fragment).commit();
        }
    }

}
