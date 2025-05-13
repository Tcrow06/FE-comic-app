package com.tq.comic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.tq.comic.LoginActivity;
import com.tq.comic.MainHomeActivity;
import com.tq.comic.R;
import com.tq.comic.config.PrefManager;
import com.tq.comic.databinding.FragmentHomeBinding;
import com.tq.comic.databinding.FragmentInfoBinding;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.model.Manga;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;
    private UserResponse userResponse;

    public InfoFragment() {
        super(R.layout.fragment_info);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        PrefManager prefManager = new PrefManager(requireContext());
        AuthenticationResponse auth = prefManager.getAuthResponse();

        if (auth != null) {
            binding = FragmentInfoBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } else {
            View guestView = inflater.inflate(R.layout.fragment_info_guest, container, false);

            // Ánh xạ và xử lý sự kiện nút đăng nhập
            Button loginButton = guestView.findViewById(R.id.btn_login);
            loginButton.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), LoginActivity.class));
            });

            return guestView;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMyProfile();

    }

    private void getMyProfile(){
        PrefManager prefManager = new PrefManager(requireContext());
        AuthenticationResponse authenticationResponse = prefManager.getAuthResponse();
        if (authenticationResponse == null) {
            Log.e("getProfile", "Không có thông tin đăng nhập. Hủy tải profile.");
            // Có thể điều hướng người dùng đến màn hình đăng nhập, hoặc ẩn UI tương ứng
            return;
        }
        UserService userService = new UserService();
        userService.getInfo(authenticationResponse.getUsername(),new ServiceExecutor.CallBack<UserResponse>() {
            @Override
            public void onSuccess(ApiResponse<UserResponse> result) {
                userResponse = result.getResult();
                if (userResponse != null) {
                    // Giả sử UserResponse có các getter tương ứng, ví dụ: getFullName(), getNickname(), getEmail(), ...
                    binding.editFirstName.setText(userResponse.getFirstName());
                    binding.editLastName.setText(userResponse.getLastName());
                    binding.editTextUsername.setText(userResponse.getUsername());
                    binding.editTextEmail.setText(userResponse.getEmail());

                    // Nếu bạn không muốn hiển thị mật khẩu, có thể để trống:
                    binding.editTextPassword.setText("");
                    binding.editTextConfirmPassword.setText("");
                } else {
                    Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                Log.e("getProfile", "Lỗi gọi API: " + errorMessage);
                Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


