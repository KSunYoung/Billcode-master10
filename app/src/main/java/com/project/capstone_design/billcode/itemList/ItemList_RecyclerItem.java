package com.project.capstone_design.billcode.itemList;

public class ItemList_RecyclerItem {
    private String name;
    private String expDate;
    private String image;
    private int pushChecked;



    public ItemList_RecyclerItem(String name, String image, String expDate, int pushChecked) {
        this.name = name;
        this.image = image;
        this.expDate = expDate;
        this.pushChecked = pushChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public void setImage(String image) {
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

    public int getPushChecked() {
        return pushChecked;
    }

    public void setPushChecked(int pushChecked) {
        this.pushChecked = pushChecked;
    }
}