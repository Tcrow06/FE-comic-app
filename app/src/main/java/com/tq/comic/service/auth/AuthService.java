package com.tq.comic.service.auth;

import android.content.Context;

import com.google.gson.Gson;
import com.tq.comic.dto.request.authentication.ActiveAccountRequest;
import com.tq.comic.dto.request.authentication.IntrospectRequest;
import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.request.authentication.RefreshTokenRequest;
import com.tq.comic.dto.request.authentication.RegisterRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.config.RetrofitClient;
import com.tq.comic.dto.response.authentication.IntrospectResponse;
import com.tq.comic.dto.response.authentication.ResendOtpResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.service.callback.ServiceExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private final AuthAPIService apiService;
    public AuthService() {
        apiService = RetrofitClient.getRetrofitInstance().create(AuthAPIService.class);
    }

    // Dành cho gọi có token
    public AuthService(Context context, String token) {
        this.apiService = RetrofitClient.getRetrofitWithAuth(context, token).create(AuthAPIService.class);
    }

    public AuthAPIService getApi() {
        return apiService;
    }

    public void introspect(IntrospectRequest request, CallBack<IntrospectResponse> callback) {
        Call<ApiResponse<IntrospectResponse>> call = apiService.introspect(request);
        call.enqueue(new Callback<ApiResponse<IntrospectResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<IntrospectResponse>> call, Response<ApiResponse<IntrospectResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Token introspect failed or result is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<IntrospectResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }


    public void checkEmailExists(String email ,CallBack<Boolean> callback) {
        Call<ApiResponse<Boolean>> call = apiService.checkEmailExists(email);
        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                callback.onFailure(t.toString());
            }
        });
    }

    public void login(LoginRequest request, CallBack<AuthenticationResponse> callback) {
        Call<ApiResponse<AuthenticationResponse>> call = apiService.login(request);
        call.enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Lấy lỗi từ body
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ApiResponse errorResponse = gson.fromJson(errorBody, ApiResponse.class);
                        callback.onFailure(errorResponse.getMessage());
                    } catch (Exception e) {
                        callback.onFailure("Không đọc được lỗi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage() != null ? t.getMessage() : "Lỗi không xác định");
            }
        });
    }


    // dùng để đăng nập bằng google
    public void outBoundAuthentication(
            String code,
            ServiceExecutor.CallBack<AuthenticationResponse> callback
    ) {
        Call<ApiResponse<AuthenticationResponse>> call = apiService.outBoundAuthentication(code);
        ServiceExecutor.enqueue(call, callback);
    }

    public void refreshToken(RefreshTokenRequest request, CallBack<AuthenticationResponse> callback) {
        Call<ApiResponse<AuthenticationResponse>> call = apiService.refreshToken(request);
        call.enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Refresh token failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void register(RegisterRequest request, CallBack<UserResponse> callback) {
        Call<ApiResponse<UserResponse>> call = apiService.register(request);
        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Lấy lỗi từ body
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ApiResponse errorResponse = gson.fromJson(errorBody, ApiResponse.class);
                        callback.onFailure(errorResponse.getMessage());
                    } catch (Exception e) {
                        callback.onFailure("Không đọc được lỗi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void active(ActiveAccountRequest request, CallBack<UserResponse> callback) {
        Call<ApiResponse<UserResponse>> call = apiService.active(request);
        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Lấy lỗi từ body
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ApiResponse errorResponse = gson.fromJson(errorBody, ApiResponse.class);
                        callback.onFailure(errorResponse.getMessage());
                    } catch (Exception e) {
                        callback.onFailure("Không đọc được lỗi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void resendOtpUsername(ActiveAccountRequest request, CallBack<ResendOtpResponse> callback) {
        Call<ApiResponse<ResendOtpResponse>> call = apiService.resendOtpUsername(request);
        call.enqueue(new Callback<ApiResponse<ResendOtpResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResendOtpResponse>> call, Response<ApiResponse<ResendOtpResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Lấy lỗi từ body
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ApiResponse errorResponse = gson.fromJson(errorBody, ApiResponse.class);
                        callback.onFailure(errorResponse.getMessage());
                    } catch (Exception e) {
                        callback.onFailure("Không đọc được lỗi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResendOtpResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }





    // Interface Callback sửa lại để nhận giá trị Boolean từ API
    public interface CallBack <T> {
        void onSuccess(ApiResponse<T> result);
        void onFailure(String errorMessage);
    }
}
