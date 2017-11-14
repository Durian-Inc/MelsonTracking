package com.example.a25cen.melsontracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Innocent on 11/4/17.
 */

public class MovieCard {

    private String title;
    private int runtime, releaseYear, budget = 0;

    public MovieCard(String title, int releaseYear, int runtime, int budget ) {
        this.title = title;
        this.budget = budget;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
    }

    //No getters or setters because it is all handled in the DB and this is only used to transfer
    //information to the movie

    public String getTitle() {
        return title;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getBudget() {
        return budget;
    }
}

