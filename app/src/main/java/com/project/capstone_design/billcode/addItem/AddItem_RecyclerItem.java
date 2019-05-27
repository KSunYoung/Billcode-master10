package com.project.capstone_design.billcode.addItem;

public class AddItem_RecyclerItem {
    private String product_code;
    private String product_name;
    private String expDate;
    private String image;
    private int pushChecked;

    public AddItem_RecyclerItem(String product_code, String expDate, String image) {
        this.product_code = product_code;
        this.expDate = expDate;
        this.image = image;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public void setImage(String image) {
        this.image = image;
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