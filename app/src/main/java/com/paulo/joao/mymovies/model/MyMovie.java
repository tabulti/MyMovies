package com.paulo.joao.mymovies.model;

import java.io.Serializable;

/**
 * Created by Joao Paulo Ribeiro on 07/02/2017.
 */
public class MyMovie implements Serializable{
    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Coutry;
    private String Awards;
    private String Poster;
    private String MetaScore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private boolean isSavedOnMobile;

    public MyMovie() {
    }

    public boolean isSavedOnMobile() {
        return isSavedOnMobile;
    }

    public void setSavedOnMobile(boolean savedOnMobile) {
        isSavedOnMobile = savedOnMobile;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        this.Rated = rated;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        this.Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        this.Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        this.Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        this.Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        this.Writer = writer;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        this.Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        this.Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        this.Language = language;
    }

    public String getCoutry() {
        return Coutry;
    }

    public void setCoutry(String coutry) {
        this.Coutry = coutry;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        this.Awards = awards;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    public String getMetaScore() {
        return MetaScore;
    }

    public void setMetaScore(String metaScore) {
        this.MetaScore = metaScore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbId() {
        return imdbID;
    }

    public void setImdbId(String imdbId) {
        this.imdbID = imdbId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }
}
