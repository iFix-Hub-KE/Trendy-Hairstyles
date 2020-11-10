package com.ifixhubke.trendyhairstyles;

public class Saved {
    String styleImage;
    String styleName;
    String stylePrice;
    String salonName;
    Boolean isSaved;

    public Saved() {
    }

    public Saved(String styleImage, String styleName, String stylePrice, String salonName, Boolean isSaved) {
        this.styleImage = styleImage;
        this.styleName = styleName;
        this.stylePrice = stylePrice;
        this.salonName = salonName;
        this.isSaved = isSaved;
    }

    public String getStyleImage() {
        return styleImage;
    }

    public Boolean getIsSaved(){
        return isSaved;
    }

    public void setIsSaved(Boolean isSaved){
        this.isSaved = isSaved;
    }

    public void setStyleImage(String styleImage) {
        this.styleImage = styleImage;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStylePrice() {
        return stylePrice;
    }

    public void setStylePrice(String stylePrice) {
        this.stylePrice = stylePrice;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }
}
