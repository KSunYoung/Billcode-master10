package com.project.capstone_design.billcode.addItem;

public class AddItem_RecyclerItem {
    private String name;
    private String expDate;
    private String image;
    private boolean pushChecked;

    public AddItem_RecyclerItem(String name, String expDate, String image) {
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

    public boolean isPushChecked() {
        return pushChecked;
    }

    public void setPushChecked(boolean pushChecked) {
        this.pushChecked = pushChecked;
    }
}