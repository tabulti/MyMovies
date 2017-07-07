package com.paulo.joao.mymovies.retrofit;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.model.TmdbResponse;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by joao.paulo.d.ribeiro on 08/02/2017.
 */

public interface TmdbService {

    interface GetMovieBySimpleNameHandler {
        void onGetMovieBySimpleName(TmdbResponse res, Error err);
    }

    /*@GET("/")
    Call<MyMovie> getSimpleName(@Query(value = "t") String name,
                                @Query(value = "y") String year,
                                @Query(value = "plot") String plot,
                                @Query(value = "r") String dataTypeReturn);*/

    @GET
    public Call<TmdbResponse> getMovie(@Url String url);
}
