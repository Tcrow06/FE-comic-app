package com.tq.comic.service.auth;


import com.tq.comic.dto.request.authentication.ActiveAccountRequest;
import com.tq.comic.dto.request.authentication.IntrospectRequest;
import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.request.authentication.RefreshTokenRequest;
import com.tq.comic.dto.request.authentication.RegisterRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.authentication.IntrospectResponse;
import com.tq.comic.dto.response.authentication.ResendOtpResponse;
import com.tq.comic.dto.response.user.UserResponse;

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

    @POST("api/auth/outbound/authentication")
    Call<ApiResponse<AuthenticationResponse>> outBoundAuthentication(@Query("code") String code);

    @POST("api/auth/introspect")
    Call<ApiResponse<IntrospectResponse>> introspect(@Body IntrospectRequest request);
    @POST("api/auth/register")
    Call<ApiResponse<UserResponse>> register(@Body RegisterRequest request);
    @POST("api/auth/active-account")
    Call<ApiResponse<UserResponse>> active(@Body ActiveAccountRequest request);
    @POST("api/auth/resend-otp-username")
    Call<ApiResponse<ResendOtpResponse>> resendOtpUsername(@Body ActiveAccountRequest request);

    @POST("api/auth/refresh-token")
    Call<ApiResponse<AuthenticationResponse>> refreshToken(@Body RefreshTokenRequest request);

}
