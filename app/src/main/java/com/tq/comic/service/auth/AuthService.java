package com.tq.comic.service.auth;

import com.google.gson.Gson;
import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.config.RetrofitClient;
import com.tq.comic.service.callback.ServiceExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private final AuthAPIService apiService;
    public AuthService() {
        apiService = RetrofitClient.getRetrofitInstance().create(AuthAPIService.class);
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



    // Interface Callback sửa lại để nhận giá trị Boolean từ API
    public interface CallBack <T> {
        void onSuccess(ApiResponse<T> result);
        void onFailure(String errorMessage);
    }
}
