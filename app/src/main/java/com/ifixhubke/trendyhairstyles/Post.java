package com.ifixhubke.trendyhairstyles;

public class Post {
    private String user_name;
    private String profile_image_url;
    private String style_name;
    private String style_price;
    private String salon_name;
    private String caption;
    private String style_photo_url;
    private String date_time;
    private int likes;


    public Post(String user_name, String profile_image_url, String style_name, String style_price, String salon_name, String caption, String style_photo_url, String date_time, int likes) {
        this.user_name = user_name;
        this.profile_image_url = profile_image_url;
        this.style_name = style_name;
        this.style_price = style_price;
        this.salon_name = salon_name;
        this.caption = caption;
        this.style_photo_url = style_photo_url;
        this.date_time = date_time;
        this.likes = likes;
    }

    public Post() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
    }

    public String getStyle_price() {
        return style_price;
    }

    public void setStyle_price(String style_price) {
        this.style_price = style_price;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getStyle_photo_url() {
        return style_photo_url;
    }

    public void setStyle_photo_url(String style_photo_url) {
        this.style_photo_url = style_photo_url;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
