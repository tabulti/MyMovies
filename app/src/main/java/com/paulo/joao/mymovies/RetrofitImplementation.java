package com.paulo.joao.mymovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by joao.paulo.d.ribeiro on 08/02/2017.
 */

public class RetrofitImplementation {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/?")
            .build();

    OmdbService service = retrofit.create(OmdbService.class);
}
