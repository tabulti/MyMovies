package com.paulo.joao.mymovies.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.paulo.joao.mymovies.model.MyMovie;

import java.util.List;
import java.util.Map;

/**
 * Created by Joao Paulo Ribeiro on 09/02/2017.
 */

public class MoviesRepository {

    public static final String PREFS_NAME = "MoviesPrefsFile";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private static MoviesRepository _instance_;

    public static MoviesRepository getInstance_() {
        if (_instance_ == null) {
            _instance_ = new MoviesRepository();
        }
        return _instance_;
    }

    private void initMoviesRepository(Context context){
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = mPrefs.edit();
    }

    private void saveMovie(MyMovie movie) {
        Gson gson = new Gson();
        String json = gson.toJson(movie);
        if (editor != null) {
            editor.putString(movie.getImdbId(), json);
            editor.commit();
        }
    }

    private boolean hasMovie(MyMovie movie){
        Gson gson = new Gson();
        String json = mPrefs.getString(movie.getImdbId(), "");
        if (mPrefs != null){
            MyMovie movieFound = gson.fromJson(json, MyMovie.class);
            if (movieFound != null) {
                return true;
            }
        }
        return false;
    }

    private Map<String, ?> loadMovies(){
        mPrefs.
        return mPrefs.getAll();
    }
}
