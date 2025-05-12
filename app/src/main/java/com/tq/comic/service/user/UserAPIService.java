package com.tq.comic.service.user;

import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.user.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserAPIService {
    @GET("/api/user/my-info")
    Call<ApiResponse<UserResponse>> getUserProfile();

    @Multipart
    @POST("auth/outbound/active-account")
    Call<ApiResponse<Boolean>> activeAccount(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part img // Đối với ảnh, sử dụng MultipartBody.Part
    );

    @Multipart
    @PUT("/update-user-image")
    Call<ApiResponse<UserResponse>> updateImage(
            @Part MultipartBody.Part file
    );
    @POST("/edit-profile")
    @FormUrlEncoded
    Call<ApiResponse<UserResponse>> editCustomerProfile(
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("email") String email
    );
}
