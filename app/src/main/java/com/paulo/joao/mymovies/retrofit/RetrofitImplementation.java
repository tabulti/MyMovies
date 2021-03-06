package com.paulo.joao.mymovies.retrofit;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.model.TmdbResponse;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joao.paulo.d.ribeiro on 08/02/2017.
 */

public class RetrofitImplementation {

    private static RetrofitImplementation _instance_ = null;
    private TmdbService service = null;


    private RetrofitImplementation(){

    }

    public static RetrofitImplementation getInstance(){
        if (_instance_ == null) {
            _instance_ = new RetrofitImplementation();
        }
        return _instance_;
    }


    public void initRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(client)
                .build();



        service = retrofit.create(TmdbService.class);
    }


    public void getMoviesByName(String url, final TmdbService.GetMovieBySimpleNameHandler handler) {

        if (service != null) {

            final Call<TmdbResponse> res = service.getMovie(url);

            res.enqueue(new Callback<TmdbResponse>() {
                @Override
                public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
                    if(response != null) {
                        if (response.errorBody() == null && response.body() != null){
                            handler.onGetMovieBySimpleName(response.body(), null);
                        } else {
                            String err = "Code: " + response.raw().code() + "\n" +
                                    "Message: " + response.raw().message();
                            handler.onGetMovieBySimpleName(null, new Error(err));

                        }
                    }
                }
                @Override
                public void onFailure(Call<TmdbResponse> call, Throwable t) {
                    if (t != null) {
                        handler.onGetMovieBySimpleName(null, new Error(t.toString()));
                    }
                }
            });
        }
    }


}
