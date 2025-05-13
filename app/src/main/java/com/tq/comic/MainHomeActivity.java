package com.tq.comic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tq.comic.adapter.ViewPager2_MainHomeAdapter;
import com.tq.comic.databinding.ActivityMainHomeBinding;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;

    public class MainHomeActivity extends BaseActivity {

        private ActivityMainHomeBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

    //        EdgeToEdge.enable(this);
    //        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
    //            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
    //            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
    //            return insets;
    //        });

            ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            checkAndRefreshToken(auth -> {
                loadUserInfo(auth);
            });


            ViewPager2 viewPager2 = binding.viewPagerMainHome;
            BottomNavigationView bottomNav = binding.bottomNav;

            viewPager2.setAdapter(new ViewPager2_MainHomeAdapter(this));

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    switch (position) {
                        case 0:
                            bottomNav.setSelectedItemId(R.id.nav_home); break;
                        case 1:
                            bottomNav.setSelectedItemId(R.id.nav_search); break;
                        case 2:
                            bottomNav.setSelectedItemId(R.id.nav_manga); break;
                        case 3:
                            bottomNav.setSelectedItemId(R.id.nav_account); break;
                    }
                }
            });

            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    viewPager2.setCurrentItem(0, false);
                    return true;
                } else if (itemId == R.id.nav_search) {
                    viewPager2.setCurrentItem(1, false);
                    return true;
                } else if (itemId == R.id.nav_manga) {
                    viewPager2.setCurrentItem(2, false);
                    return true;
                } else if (itemId == R.id.nav_account) {
                    viewPager2.setCurrentItem(3, false);
                    return true;
                }
                return false;
            });
        }

    public void loadUserInfo(AuthenticationResponse auth) {
        TextView firstNameTextView = findViewById(R.id.firstName);
        TextView lastNameTextView = findViewById(R.id.lastName);
        ImageView avatarImageView = findViewById(R.id.avatar);

        Button loginButton = findViewById(R.id.loginButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        TextView notLoggedInMessage = findViewById(R.id.notLoggedInMessage);
        LinearLayout headerLayoutLoggedIn = findViewById(R.id.headerLayoutLoggedIn);
        LinearLayout headerLayoutNotLoggedIn = findViewById(R.id.headerLayoutNotLoggedIn);

        if (auth != null && auth.getAccessToken() != null) {
            // Đã đăng nhập
            firstNameTextView.setText(auth.getFirstName());
            lastNameTextView.setText(auth.getLastName());
            Glide.with(this)
                    .load(auth.getPicture())
                    .placeholder(R.drawable.logo_tq)
                    .circleCrop()
                    .into(avatarImageView);

            headerLayoutLoggedIn.setVisibility(View.VISIBLE);
            headerLayoutNotLoggedIn.setVisibility(View.GONE);
        } else {
            headerLayoutLoggedIn.setVisibility(View.GONE);
            headerLayoutNotLoggedIn.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
            });
        }

        logoutButton.setOnClickListener(v -> {
            prefManager.clearAuthResponse();
            recreate();
        });
    }


}
