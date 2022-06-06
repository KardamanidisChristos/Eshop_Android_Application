package com.example.eshopproject;

import com.google.gson.annotations.SerializedName;

public class Employee extends User{
    @SerializedName("employeeid")
    private short employeeid;
    @SerializedName("title")
    private String title;
    @SerializedName("identity")
    private String identity;
    @SerializedName("startOfContract")
    private String startOfContract;
    @SerializedName("endOfContract")
    private String endOfContract;
    @SerializedName("role")
    private String role;
    @SerializedName("worksIn")
    private String worksIn;

    public Employee() {
    }

    public Employee(short employeeid, String title, String identity, String startOfContract, String endOfContract, String role) {
        this.employeeid = employeeid;
        this.title = title;
        this.identity = identity;
        this.startOfContract = startOfContract;
        this.endOfContract = endOfContract;
        this.role = role;
    }

    public short getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(short employeeid) {
        this.employeeid = employeeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getStartOfContract() {
        return startOfContract;
    }

    public void setStartOfContract(String startOfContract) {
        this.startOfContract = startOfContract;
    }

    public String getEndOfContract() {
        return endOfContract;
    }

    public void setEndOfContract(String endOfContract) {
        this.endOfContract = endOfContract;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWorksIn() {
        return worksIn;
    }

    public void setWorksIn(String worksIn) {
        this.worksIn = worksIn;
    }

    @Override
    public String toString() {
        String s = super.toString();
        s+= "Employee{" +
                "employeeid=" + employeeid +
                ", title='" + title + '\'' +
                ", identity='" + identity + '\'' +
                ", startOfContract='" + startOfContract + '\'' +
                ", endOfContract='" + endOfContract + '\'' +
                ", role='" + role + '\'' +
                ", worksIn='" + worksIn + '\'' +
                '}';
        return s;
    }
}
