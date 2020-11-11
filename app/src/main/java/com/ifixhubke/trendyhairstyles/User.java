package com.ifixhubke.trendyhairstyles;

public class User {

    String full_names;
    String email_address;
    String about_you;
    String profile_url;

    public User() {
    }

    public User(String full_names, String email_address, String about_you, String profile_url) {
        this.full_names = full_names;
        this.email_address = email_address;
        this.about_you = about_you;
        this.profile_url = profile_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

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
}

