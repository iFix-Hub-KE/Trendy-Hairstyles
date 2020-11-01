package com.ifixhubke.trendyhairstyles;

public class User {

    String full_names;

    public String getFull_names() {
        return full_names;
    }

    public void setFull_names(String full_names) {
        this.full_names = full_names;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getAbout_you() {
        return about_you;
    }

    public void setAbout_you(String about_you) {
        this.about_you = about_you;
    }

    String email_address;
    String about_you;

    public User() {
    }

    public User(String full_names, String email_address, String about_you) {
        this.full_names = full_names;
        this.email_address = email_address;
        this.about_you = about_you;
    }
}

