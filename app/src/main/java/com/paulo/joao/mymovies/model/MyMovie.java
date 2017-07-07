package com.paulo.joao.mymovies.model;

import java.io.Serializable;

/**
 * Created by Joao Paulo Ribeiro on 07/02/2017.
 */
public class MyMovie implements Serializable{
    private Integer vote_count;
    private Integer id;
    private boolean video;
    private Double vote_average;
    private String title;
    private Double popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private boolean isSavedOnMobile;

    public MyMovie() {
    }

    public boolean isSavedOnMobile() {
        return isSavedOnMobile;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public Integer getId() {
        return id;
    }

    public boolean isVideo() {
        return video;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }
}
