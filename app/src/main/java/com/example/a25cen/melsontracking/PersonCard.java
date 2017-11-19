package com.example.a25cen.melsontracking;

import java.util.ArrayList;

/**
 * Created by Innocent on 11/4/17.
 */

public class PersonCard {

    private String name[];
    private String gender = "";
    private String role;
    private ArrayList<String> movies;
    private long PID = -1;

    public PersonCard() {
    }

    public PersonCard(String[] name, String gender, String role) {
        this.name = name;
        this.gender = gender;
        this.role = role;
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

    public long getPID() {
        return PID;
    }

    public void setPID(long PID) {
        this.PID = PID;
    }
}
