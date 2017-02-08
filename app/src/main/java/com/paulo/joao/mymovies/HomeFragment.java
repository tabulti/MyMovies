package com.paulo.joao.mymovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createRetrofit();


        if(view.findViewById(R.id.carousel_container) != null && view.findViewById(R.id.movies_list_container) != null) {

            if(savedInstanceState != null) {
                return;
            }
            CarouselFragment carouselFragment = new CarouselFragment();
            MoviesListFragment moviesListFragment = new MoviesListFragment();

            getChildFragmentManager().beginTransaction()
                    .add(R.id.carousel_container, carouselFragment)
                    .add(R.id.movies_list_container, moviesListFragment)
                    .commit();
        }

    }
    private MyMovie movie;

    public void createRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(client)
                .build();



        OmdbService service = retrofit.create(OmdbService.class);


        Call<MyMovie> respo = service.getSimpleName_("minions", "", "short", "json");

       /* try {
            movie = respo.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        respo.enqueue(new Callback<MyMovie>() {
            @Override
            public void onResponse(Call<MyMovie> call, Response<MyMovie> response) {
                movie = response.body();
            }

            @Override
            public void onFailure(Call<MyMovie> call, Throwable t) {
                Log.i("TAGERROR", "yes");
            }
        });
    }

}
