package com.example.eshopproject;

import com.google.gson.annotations.SerializedName;

public class Order {
   private User user;
    @SerializedName("orderid")
   private int orderId;
    @SerializedName("shopid")
    private int shopId;
    @SerializedName("state")
    private String state;
    @SerializedName("orderType")
    private  String orderType;
    @SerializedName("orderTime")
    private String orderTime;
    @SerializedName("orderComment")
    private String comment;

    @SerializedName("products")
    Product[] products;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Product[] getProducts() {
        return products;
    }

    @Override
    public String toString() {
        String s = "Order{" +
                "user=" + user +
                ", orderId=" + orderId +
                ", shopId=" + shopId +
                ", state='" + state + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", comment='" + comment + '\'' +
                '}';
        for (int i = 0; i< products.length; i++) {
            s += products[i].toString();
        }
        return s;
    }
}
