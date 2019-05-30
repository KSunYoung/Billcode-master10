package com.project.capstone_design.billcode.itemList;

public class ItemList_RecyclerItem {


    private String product_code;
    private String name;
    private String expDate;
    private int pushChecked;


    public ItemList_RecyclerItem(String name, String product_code, String expDate, int pushChecked) {

        this.name = name;
        this.product_code = product_code;
        this.expDate = expDate;
        this.pushChecked = pushChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }


    public String getName() {
        return name;
    }

    public String getExpDate() {
        return expDate;
    }

    public int getPushChecked() {
        return pushChecked;
    }

    public void setPushChecked(int pushChecked) {
        this.pushChecked = pushChecked;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}