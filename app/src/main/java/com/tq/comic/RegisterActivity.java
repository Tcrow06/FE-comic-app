package com.tq.comic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tq.comic.config.PrefManager;
import com.tq.comic.databinding.ActivityRegisterBinding;
import com.tq.comic.dto.request.authentication.LoginRequest;
import com.tq.comic.dto.request.authentication.RegisterRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.service.auth.AuthService;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginText;

    private Button continueButton;

    private String username, pass, rePass, email;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Đặt layout chính là binding root

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginText = findViewById(R.id.loginText);
        continueButton = findViewById(R.id.continueButton);
        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        continueButton.setOnClickListener(v -> {
            username = binding.usernameEditText.getText().toString().trim();

            pass = binding.passwordEditText.getText().toString();
            rePass = binding.completePasswordEditText.getText().toString();
            email = binding.emailEditText.getText().toString().trim();
            if(!pass.equals(rePass)){
                Toast.makeText(RegisterActivity.this, "Lỗi: Mật khẩu và Nhập lại mật khẩu không khớp" , Toast.LENGTH_SHORT).show();
                return;
            }
            register();
        });
    }
    private void register() {
        AuthService authService = new AuthService();
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(pass)
                .confirmPassword(rePass)
                .email(email)
                .build();

        authService.register(request ,new AuthService.CallBack<UserResponse>() {
            @Override
            public void onSuccess(ApiResponse<UserResponse> result) {
                if (result.getResult() != null) {
                    Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, OptVerificationActivity.class);
                    intent.putExtra("email", result.getResult().getEmail());
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Lôi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
