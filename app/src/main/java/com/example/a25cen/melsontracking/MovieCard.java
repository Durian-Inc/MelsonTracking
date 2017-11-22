package com.example.a25cen.melsontracking;

public class MovieCard {

    private String title;
    private int runtime, releaseYear, budget = -1;
    private long MID;

    //Default constructor
    public MovieCard() {}

    public MovieCard(String title, int releaseYear, int runtime, int budget ) {
        this.title = title;
        this.budget = budget;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
    }


    //Setter and getters for each member variable of this class
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRuntime(int runtime) {this.runtime = runtime;}

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public long getMID() {
        return MID;
    }

    public void setMID(long MID) {
        this.MID = MID;
    }
}

