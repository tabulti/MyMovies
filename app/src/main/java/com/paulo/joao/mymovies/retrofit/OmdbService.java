package com.paulo.joao.mymovies.retrofit;

import com.paulo.joao.mymovies.model.MyMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by joao.paulo.d.ribeiro on 08/02/2017.
 */

public interface OmdbService {

    interface GetMovieBySimpleNameHandler {
        void onGetMovieBySimpleName(MyMovie res, Error err);
    }

    @GET("/")
    Call<MyMovie> getSimpleName(@Query(value = "t") String name,
                                @Query(value = "y") String year,
                                @Query(value = "plot") String plot,
                                @Query(value = "r") String dataTypeReturn);
}
