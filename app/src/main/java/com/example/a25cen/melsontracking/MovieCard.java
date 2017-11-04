package com.example.a25cen.melsontracking;

/**
 * Created by Innocent on 11/4/17.
 */

public class MovieCard {

    private String title, releaseYear = "";
    private int runtime, rating = 0;
    private  float budget = 0;

    public MovieCard(String title, String releaseYear, int runtime, int rating, float budget ) {
        this.title = title;
        this.budget = budget;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
    }

    //No getters or setters because it is all handled in the DB and this is only used to transfer
    //information to the movie
}
