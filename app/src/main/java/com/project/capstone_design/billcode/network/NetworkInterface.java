package com.project.capstone_design.billcode.network;

import com.google.gson.JsonObject;
import com.project.capstone_design.billcode.model.ExpirationData;
import com.project.capstone_design.billcode.model.ProductCode;
import com.project.capstone_design.billcode.model.UserData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkInterface {

    @GET("user")
    Call<ArrayList<JsonObject>> getAllUserData();

    @FormUrlEncoded
    @POST("token/checkToken")
    Call<JsonObject> CheckToken(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @PUT("token/updateToken")
    Call<JsonObject> UpdateToken(
            @Field("user_id") String user_id,
            @Field("user_token") String user_token
    );

    @GET("login/{user_id}")
    Call<JsonObject> CheckID(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> UserCheck(@Field("user_id") String user_id,
                               @Field("user_pw") String user_pw);

    @GET("user/{id}")
    Call<JsonObject> getUser(@Path("id") String id);


    @GET("getProductName/{product_code}")
    //Call<ArrayList<JsonObject>> GetProductNameByCode(@Body String productCodes);
    Call<JsonObject> GetProductNameOne(@Path("product_code") String product_code);
    //Call<ArrayList<JsonObject>> GetProductNameByCode(@Body JsonArray productCodes);
    // Call<ArrayList<JsonObject>> getProductByCode(@Path("productCode") String productCode);
    // Call<ProductResponse> getProductByCode(@Path("productCode") String productCode);

    @POST("getProductName")
    Call<JsonObject> GetProductNameMany(@Body ArrayList<ProductCode> mProductCode);

    @GET("getUserItemList/{user_id}")
    Call<JsonObject> GetUserItemList(@Path("user_id") String user_id);

    @GET("getUserItemList/byExpDateUp/{user_id}")
    Call<JsonObject> GetUserItemListByExpDateUp(@Path("user_id") String user_id);


    //@FormUrlEncoded
    @POST("user")
    Call<JsonObject> Register_User(@Body UserData mUserData);
    // Call<JsonObject> user(@Field("id") String id, @Field("password") String password, @Field("name") String name, @Field("phone") String phone);

    // @GET("product/{productCode}.jpg")
    // Call<JsonObject> GetProductImage(@Field("productCode") String productCode);

    @POST("putUserItems")
    Call<JsonObject> InsertExpirationData(@Body ArrayList<ExpirationData> mExpirationData);
}
/*
public interface NetworkInterface {

    @GET("club")
    Call<ArrayList<JsonObject>> getAllClub();

    @GET("main")
    Call<ArrayList<JsonObject>> getMainImage();

    @GET("club/search?")
    Call<ArrayList<JsonObject>> getInformation(@Query("keyword") String clubname);

    @GET("/club/info/{clubnum}")
    Call<ArrayList<JsonObject>> getDetailInformation(@Path("clubnum") int num);

    @POST("/club/info/{club}")
    Call<ErrorMsgResult> giveModifiedContents(@Path("club") String num2, @Body ModifyClubInfo modifyClubInfo);

    @POST("user/login")
    Call<String> getLoginInfo(@Body LoginData loginData);

    @Multipart
    @POST("club/image/{clubnum}/{imagenum}")
    Call<ErrorMsgResult> imageUpload(@Path("clubnum") int clubnum, @Path("imagenum") int imagenum,
                                     @Part("userfile") RequestBody userfile, @Part MultipartBody.Part file);

    @GET("event/list/{date}")
    Call<ArrayList<JsonObject>> getTotalEvent(@Path("date") String date);

    @GET("club/event/{clubnum}")
    Call<ArrayList<JsonObject>> getClubEvent(@Path("clubnum") String clubnum);

    @POST("event/new")
    Call<ArrayList<JsonObject>> updateEvent(@Body UpdateEvent updateEvent);

    @POST("event/{eventnum}/edit")
    Call<ArrayList<JsonObject>> editEvent(@Path("eventnum") int eventnum, @Body UpdateEvent updateEvent);

    @POST("event/{eventnum}/delete")
    Call<ErrorMsgResult> deleteEvent(@Path("eventnum") int eventnum);

    @DELETE("club/image/{clubnum}/{imagenum}")
    Call<ErrorMsgResult> deleteImage(@Path("clubnum") int clubnum, @Path("imagenum") int imagenum);
}
*/
