package com.example.a25cen.melsontracking;


public class PersonCard {

    private String name[];
    private String gender = "";
    private String role;
    private long PID = -1;

    public PersonCard() {
    }

    //Constructor
    public PersonCard(String[] name, String gender, String role) {
        this.name = name;
        this.gender = gender;
        this.role = role;
    }


    //Getters and setters for the personcard class
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
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
