package com.project.capstone_design.billcode.home;

public class Home_RecyclerItem {
    private String name;
    private String expDate;
    private String image;

    public Home_RecyclerItem(String name, String expDate, String image) {
        this.name = name;
        this.expDate = expDate;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getImage() {
        return image;
    }
}