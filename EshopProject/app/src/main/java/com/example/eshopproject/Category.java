package com.example.eshopproject;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("categoryid")
    private int categoryid;
    @SerializedName("categoryname")
    private String categoryname;

    public Category() {}

    public Category(int categoryid, String categoryname) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryid=" + categoryid +
                ", categoryname='" + categoryname + '\'' +
                '}';
    }
}
