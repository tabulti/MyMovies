package com.paulo.joao.mymovies.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by joao.paulo.d.ribeiro on 21/06/2017.
 */

public class TmdbResponse implements Serializable {


    private Integer page;
    private Integer total_results;
    private Integer total_pages;
    private List<MyMovie> results;

    public TmdbResponse() {
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public List<MyMovie> getResults() {
        return results;
    }
}
