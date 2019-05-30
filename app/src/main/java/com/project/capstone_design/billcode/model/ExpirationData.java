package com.project.capstone_design.billcode.model;

public class ExpirationData {
    private String id;
    private String product_code;
    private String product_expiration_date;
    private int pushChecked;

    public ExpirationData(String id, String product_code, String product_expiration_date, int pushChecked) {
        this.id = id;
        this.product_code = product_code;
        this.product_expiration_date = product_expiration_date;
        this.pushChecked = pushChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_expiration_date() {
        return product_expiration_date;
    }

    public void setProduct_expiration_date(String product_expiration_date) {
        this.product_expiration_date = product_expiration_date;
    }

    public int getPushChecked() {
        return pushChecked;
    }

    public void setPushChecked(int pushChecked) {
        this.pushChecked = pushChecked;
    }
}
