package com.tq.comic;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tq.comic.adapter.ViewPager2_MainHomeAdapter;
import com.tq.comic.databinding.ActivityMainHomeBinding;

public class MainHomeActivity extends AppCompatActivity {

    private ActivityMainHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
                        bottomNav.setSelectedItemId(R.id.nav_manga); break;
                    case 2:
                        bottomNav.setSelectedItemId(R.id.nav_search); break;
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
            } else if (itemId == R.id.nav_manga) {
                viewPager2.setCurrentItem(1, false);
                return true;
            } else if (itemId == R.id.nav_search) {
                viewPager2.setCurrentItem(2, false);
                return true;
            } else if (itemId == R.id.nav_account) {
                viewPager2.setCurrentItem(3, false);
                return true;
            }
            return false;
        });
    }
}
