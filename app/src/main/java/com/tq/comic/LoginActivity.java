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
import com.tq.comic.dto.request.authentication.ActiveAccountRequest;
import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.authentication.ResendOtpResponse;
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

            username = usernameEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(v, "Vui lòng nhập tài khoản và mật khẩu!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Log.d("LoginActivity", "Gửi yêu cầu đăng nhập...");

            LoginRequest request = LoginRequest.builder()
                    .username(username)
                    .password(password)
                    .build();


            login(username,password);


        });
    }

    private void getProfile(){
        PrefManager prefManager = new PrefManager(LoginActivity.this);
        AuthenticationResponse authenticationResponse = prefManager.getAuthResponse();

       try{
           UserService userService = new UserService(prefManager.getAuthResponse().getAccessToken());
           userService.getUserProfile(new ServiceExecutor.CallBack<UserResponse>() {
               @Override
               public void onSuccess(ApiResponse<UserResponse> result) {
                   userResponse = result.getResult();
                   prefManager.saveUserResponse(userResponse);

                   Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
                   startActivity(intent);

               }
               @Override
               public void onFailure(String errorMessage) {
                   Log.e("getProfile", "Lỗi gọi API: " + errorMessage);
                   Toast.makeText(LoginActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
               }
           });
       }catch (Exception e){
           e.getMessage();
       }
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

                    PrefManager prefManager = new PrefManager(LoginActivity.this);
                    prefManager.saveAuthResponse(result.getResult());

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
                    startActivity(intent);

                } else {

                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                if(errorMessage.equals("You must verify your account.")){
                    Intent intent = new Intent(LoginActivity.this, OptVerificationActivity.class);
                    authService.resendOtpUsername(new ActiveAccountRequest(username,null, null) ,new AuthService.CallBack<ResendOtpResponse>() {
                        @Override
                        public void onSuccess(ApiResponse<ResendOtpResponse> result) {
                            if (result.getResult() != null) {
                                intent.putExtra("email", result.getResult().getEmail());
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Lôi không xác định không thể đăng nhập", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
