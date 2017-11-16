package com.example.a25cen.melsontracking;

/**
 * Created by Innocent on 11/4/17.
 */

public class PersonCard {

    private String name[];
    private String gender = "";
    private String role;

    public PersonCard() {
    }

    public PersonCard(String[] name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    //No setters and getters because they will be handled by the DB and SQL
}
