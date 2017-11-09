package com.example.a25cen.melsontracking;

/**
 * Created by Innocent on 11/4/17.
 */

public class PersonCard {

    private String name = "";
    private char gender = ' ';
    private int type; // Type defines what type of person they are 0-2
                        // 0 - Actor
                        // 1 - Director
                        // 2 - Writer
    public PersonCard(String name, char gender, int type) {
        this.gender = gender;
        this.type = type;
        this.name = name;
    }

    //Oveloaded constructor
    public  PersonCard(String name, char gender) {
        this.gender = gender;
        this.name = name;
        this.type = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //No setters and getters because they will be handled by the DB and SQL
}
