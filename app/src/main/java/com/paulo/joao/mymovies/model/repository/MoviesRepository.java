package com.paulo.joao.mymovies.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.paulo.joao.mymovies.model.MyMovie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Joao Paulo Ribeiro on 09/02/2017.
 */

public class MoviesRepository{

    private final String MOVIES_FILE = "my_movies_file";
    private List<MyMovie> myMovies;
    private static MoviesRepository _instance_;
    private static File file;

    public static MoviesRepository getInstance_() {
        if (_instance_ == null) {
        }
        return _instance_;
    }

    private void initMoviesRepository(Context context) throws IOException, ClassNotFoundException {
        file = context.getFileStreamPath(MOVIES_FILE);
        myMovies = new ArrayList<>();
        myMovies = getListOfMoviesFromMemory();

    }

    private void saveListMoviesOnMemory(List<MyMovie> movies) throws IOException {

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(movies);
        oos.close();
        fos.close();
    }

    private List<MyMovie> getListOfMoviesFromMemory() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<MyMovie> listFound = (List<MyMovie>) ois.readObject();
        fis.close();
        ois.close();

        return listFound;
    }

    private void saveMovie(MyMovie movie) throws IOException {
        if (!hasMovie(movie)) {
            myMovies.add(movie);
            saveListMoviesOnMemory(myMovies);
        }
    }

    private boolean hasMovie(MyMovie movieToSearch) {
        for (MyMovie movie : myMovies){
            if(movie.getTitle().equals(movieToSearch.getTitle())){
                return true;
            }
        }
        return false;
    }
}
