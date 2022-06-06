package com.example.eshopproject;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("SHOPID")
    private String id;
    @SerializedName("ADDRESS")
    private String address;
    @SerializedName("OWNER_FIRST_NAME")
    private String ownerFirstName;
    @SerializedName("OWNER_LAST_NAME")
    private String ownerLastName;
    @SerializedName("SHOPNAME")
    private String shopname;

    public Shop() {
    }
    public Shop(String id, String address, String ownerFirstName, String ownerLastName, String shopname) {}



    public String getId() {
        return id;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getAddress() {
        return address;
    }

    public String getShopname() {
        return shopname;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    @NonNull
    @Override
    public String toString() {
        return "id =>" + id +
                "\n address =>" + address +
                "\n ownerFirstName =>" + ownerFirstName +
                "\n ownerLastName =>" + ownerLastName +
                "\n shopname " + shopname;

    }


}
