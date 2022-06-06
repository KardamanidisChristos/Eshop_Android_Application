package com.example.eshopproject;



import com.google.gson.annotations.SerializedName;

public class User {
    private short id;
    @SerializedName("username")
    private String username;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("password")
    private String password;
    @SerializedName("primaryemail")
    private String primaryemail;
    @SerializedName("secondaryemail")
    private String secondaryemail;
    @SerializedName("address")
    private String address;
    @SerializedName("gender")
    private String gender;
    @SerializedName("mobilephone")
    private String mobilephone;
    @SerializedName("homephone")
    private String homephone;
    public  User() {}

    public User(short id, String firstName, String lastName, String password, String primaryemail, String secondaryemail, String address, String gender, String mobilephone, String homephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.primaryemail = primaryemail;
        this.secondaryemail = secondaryemail;
        this.address = address;
        this.gender = gender;
        this.mobilephone = mobilephone;
        this.homephone = homephone;
    }

    public short getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPrimaryemail() {
        return primaryemail;
    }

    public String getSecondaryemail() {
        return secondaryemail;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setId(short id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrimaryemail(String primaryemail) {
        this.primaryemail = primaryemail;
    }

    public void setSecondaryemail(String secondaryemail) {
        this.secondaryemail = secondaryemail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", primaryemail='" + primaryemail + '\'' +
                ", secondaryemail='" + secondaryemail + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", mobilephone='" + mobilephone + '\'' +
                ", homephone='" + homephone + '\'' +
                '}';
    }
}
