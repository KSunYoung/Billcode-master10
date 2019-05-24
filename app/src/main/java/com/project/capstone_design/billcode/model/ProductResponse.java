package com.project.capstone_design.billcode.model;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("_id")
    private int _id;
    public int get_id(){return _id;}

    @SerializedName("productCode")
    private String productCode;
    public String getProductCode(){return productCode;}

    @SerializedName("productNumber")
    private String productNumber;
    public String getProductNumber(){return productNumber;}

    @SerializedName("productNationalCode")
    private String productNationalCode;
    public String getProductNationalCode(){return productNationalCode;}

    @SerializedName("productNationalName")
    private String productNationalName;
    public String getProductNationalName(){return productNationalName;}

    @SerializedName("productProducerCode")
    private String productProducerCode;
    public String getProductProducerCode(){return productProducerCode;}

    @SerializedName("productProducerName")
    private String productProducerName;
    public String getProductProducerName(){return productProducerName;}

    @SerializedName("productName")
    private String productName;
    public String getProductName(){return productName;}

    @SerializedName("productImage")
    private String productImage;
    public String getProductImage(){return productImage;}
}
