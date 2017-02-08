package com.paulo.joao.mymovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by joao.paulo.d.ribeiro on 08/02/2017.
 */

public interface OmdbService {

    @GET("t={name}&y=&plot=short&r=json")
    Call<List<MyMovie>> getName(@Path("name") String name);

    @GET("t={name}&y=&plot=short&r=json")
    Call<MyMovie> getSimpleName(@Path("name") String name);

    @GET("/")
    Call<MyMovie> getSimpleName_(@Query(value = "t") String name,
                                 @Query(value = "y") String year,
                                 @Query(value = "plot") String plot,
                                 @Query(value = "r") String dataTypeReturn);

}
