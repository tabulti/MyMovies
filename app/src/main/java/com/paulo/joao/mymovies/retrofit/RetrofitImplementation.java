package com.paulo.joao.mymovies.retrofit;

import com.paulo.joao.mymovies.model.MyMovie;

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
    private OmdbService service = null;


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
                .baseUrl("http://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(client)
                .build();



        service = retrofit.create(OmdbService.class);
    }


    /*public void getMovieByName(String name, final OmdbService.GetMovieBySimpleNameHandler handler) {

        if (service != null) {

            final Call<MyMovie> res = service.getSimpleName(name, "", ParamsConsts.PLOT_SHORT, ParamsConsts.DATA_TYPE_RETURN_JSON);

            res.enqueue(new Callback<MyMovie>() {
                @Override
                public void onResponse(Call<MyMovie> call, Response<MyMovie> response) {
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
                public void onFailure(Call<MyMovie> call, Throwable t) {
                    if (t != null) {
                        handler.onGetMovieBySimpleName(null, new Error(t.toString()));
                    }
                }
            });
        }
    }*/

    public void getMovieByName(String url, final OmdbService.GetMovieBySimpleNameHandler handler) {

        if (service != null) {

            final Call<MyMovie> res = service.getMovie(url);

            res.enqueue(new Callback<MyMovie>() {
                @Override
                public void onResponse(Call<MyMovie> call, Response<MyMovie> response) {
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
                public void onFailure(Call<MyMovie> call, Throwable t) {
                    if (t != null) {
                        handler.onGetMovieBySimpleName(null, new Error(t.toString()));
                    }
                }
            });
        }
    }


}
