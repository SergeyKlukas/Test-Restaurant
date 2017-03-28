package com.beat.restaurantapp.models;

/**
 * Created by beat on 3/26/2017.
 */

public class Restaurant {

    private String picture;
    private String strName;
    private String strCategory;

    public Restaurant() {}

    public Restaurant(String strName, String strCategory, String picture) {
        this.strName = strName;
        this.strCategory = strCategory;
        this.picture = picture;
    }

    public String getPicture() { return this.picture; }
    public String getStrName() { return this.strName; }
    public String getStrCategory() { return this.strCategory; }

    public void setPicture(String picture) { this.picture = picture; }
    public void setStrName(String strName) { this.strName = strName; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }
}
