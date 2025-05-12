package com.tq.comic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.tq.comic.config.PrefManager;
import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.exception.ErrorResponse;
import com.tq.comic.service.auth.AuthAPIService;
import com.tq.comic.service.auth.AuthService;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.user.UserAPIService;
import com.tq.comic.service.user.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button continueButton;
    private TextView registerText;
    private String username, password;

    private AuthAPIService authApiService;

    private UserAPIService userApiService;
    private UserResponse userResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = findViewById(R.id.main);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerText = findViewById(R.id.registerText);
        continueButton = findViewById(R.id.continueButton);


        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        continueButton.setOnClickListener(v -> {
            EditText usernameEditText = findViewById(R.id.usernameEditText);
            EditText passwordEditText = findViewById(R.id.passwordEditText);

            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
//
//            username = findViewById(R.id.usernameEditText).toString();
//            password = findViewById(R.id.passwordEditText).toString();

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(v, "Vui lòng nhập tài khoản và mật khẩu!", Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("LoginActivity", "Gửi yêu cầu đăng nhập...");

            LoginRequest request = LoginRequest.builder()
                    .username(username)
                    .password(password)
                    .build();


            login(username,password);

//            apiService.login(request).enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
//                @Override
//                public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Snackbar.make(v,"Đăng nhập thành công!", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
//                        Log.d("MainActivity", "Đăng nhập thành công!");
//                        AuthenticationResponse authenticationResponse = response.body().getResult();
//
//                        String token = authenticationResponse.getToken();
//
//                        Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                    else if (response.errorBody()!=null){
//                        try {
//                            Gson gson = new Gson();
//                            ErrorResponse error = gson.fromJson(response.errorBody().charStream(), ErrorResponse.class);
//
//                            Log.e("LoginError", "Code: " + error.getCode() + ", Message: " + error.getMessage());
//                            Snackbar.make(v, "Lỗi: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
////                            Toast.makeText(LoginActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            Log.e("LoginError", "Exception parsing error body", e);
//                            Snackbar.make(v, "Lỗi: " + "Lỗi không xác định", Snackbar.LENGTH_LONG).show();
////                            Toast.makeText(LoginActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
//                    Snackbar.make(v,"Lỗi kết nối: ", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.e("MainActivity", "Lỗi kết nối: " + t.getMessage());
//                }
//            });
//            Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
//            startActivity(intent);
        });
    }

    private void login(String email, String password) {
        AuthService authService = new AuthService();
        LoginRequest request = LoginRequest.builder()
                .username(email)
                .password(password)
                .build();

        authService.login(request ,new AuthService.CallBack<AuthenticationResponse>() {
            @Override
            public void onSuccess(ApiResponse<AuthenticationResponse> result) {
                if (result.getResult() != null) {
                    // Navigate to password entry
                    PrefManager prefManager = new PrefManager(LoginActivity.this);
                    prefManager.saveAuthResponse(result.getResult());



                    UserService userService = new UserService(prefManager.getAuthResponse().getToken());
                    userService.getUserProfile(new ServiceExecutor.CallBack<UserResponse>() {
                        @Override
                        public void onSuccess(ApiResponse<UserResponse> result) {
                            userResponse = result.getResult();
                            prefManager.saveUserResponse(userResponse);

                        }
                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(LoginActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
                    startActivity(intent);

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
