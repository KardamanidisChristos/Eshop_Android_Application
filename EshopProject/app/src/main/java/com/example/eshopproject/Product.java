package com.example.eshopproject;


import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("productid")
    private int id;
    @SerializedName("product_title")
    private String title;
    @SerializedName("productCost")
    private double cost;
    private byte[] bytesImage;
    private String image_path;
    private int categoryid;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public byte[] getByteImage() {
        return bytesImage;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public byte[] getBytesImage() {
        return bytesImage;
    }

    public void setBytesImage(byte[] bytesImage) {
        this.bytesImage = bytesImage;
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", cost=" + cost +
                ", categoryid=" + categoryid +
                ", quantity=" + quantity +
                '}';
    }
}
