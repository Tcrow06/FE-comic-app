package com.tq.comic.service.auth;


import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthAPIService {
    @GET("auth/email-exists")
    Call<ApiResponse<Boolean>> checkEmailExists(@Query("email") String email);

    @POST("api/auth/login")
    Call<ApiResponse<AuthenticationResponse>> login(@Body LoginRequest request);

    @POST("auth/outbound/authentication")
    Call<ApiResponse<AuthenticationResponse>> outBoundAuthentication(@Query("code") String code);
}
