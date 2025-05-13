package com.tq.comic;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tq.comic.databinding.ActivityOtpVerificationBinding;
import com.tq.comic.databinding.ActivityRegisterBinding;
import com.tq.comic.dto.request.authentication.ActiveAccountRequest;
import com.tq.comic.dto.request.authentication.RegisterRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.service.auth.AuthService;

public class OptVerificationActivity extends AppCompatActivity {

    private TextView loginText;

    private Button verifyButton;

    private String username, pass, rePass, email,otp;

    private ActivityOtpVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Đặt layout chính là binding root

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.otpLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupOtpAutoMove();
        email = getIntent().getStringExtra("email");
        binding.emailText.setText(email);
        verifyButton = findViewById(R.id.verifyButton);


        verifyButton.setOnClickListener(v -> {
            otp = binding.otp1.getText().toString() + binding.otp2.getText().toString() +binding.otp3.getText().toString() +
                    binding.otp4.getText().toString() + binding.otp5.getText().toString() + binding.otp6.getText().toString();
            if (otp.length() != 6 || !otp.matches("\\d{6}")) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ 6 số OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            active();
        });
    }
    private void active() {
        AuthService authService = new AuthService();
        ActiveAccountRequest request = ActiveAccountRequest.builder()
                .email(email)
                .otpCode(otp)
                .build();
        Log.d("OTP: " , otp);
        authService.active(request ,new AuthService.CallBack<UserResponse>() {
            @Override
            public void onSuccess(ApiResponse<UserResponse> result) {
                if (result.getResult() != null) {
                    Toast.makeText(OptVerificationActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OptVerificationActivity.this, LoginActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(OptVerificationActivity.this, "Lôi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(OptVerificationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupOtpAutoMove() {
        EditText[] otpFields = {
                binding.otp1, binding.otp2, binding.otp3,
                binding.otp4, binding.otp5, binding.otp6
        };

        for (int i = 0; i < otpFields.length - 1; i++) {
            final int current = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1) {
                        otpFields[current + 1].requestFocus();
                    }
                }
            });
        }
    }

}
