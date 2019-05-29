package com.project.capstone_design.billcode.model;

public class ExpirationData {
    private String user_id;
    private String product_code;
    private String product_expiration_date;
    private int push_alert;

    public ExpirationData(String user_id, String product_code, String product_expiration_date, int push_alert) {
        this.user_id = user_id;
        this.product_code = product_code;
        this.product_expiration_date = product_expiration_date;
        this.push_alert = push_alert;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String user_id) {
        this.user_id = user_id;
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

    public int getPush_alert() {
        return push_alert;
    }

    public void setPush_alert(int push_alert) {
        this.push_alert = push_alert;
    }
}
