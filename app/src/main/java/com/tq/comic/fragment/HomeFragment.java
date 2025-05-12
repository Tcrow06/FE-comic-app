package com.tq.comic.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.tq.comic.R;
import com.tq.comic.databinding.FragmentHomeBinding;
import com.tq.comic.model.Manga;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    private FragmentHomeBinding binding;
    private MangaAdapter trendingAdapter, listAdapter;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Dummy data
        List<Manga> trendingList = new ArrayList<>();
        trendingList.add(new Manga("Solo Leveling", "Chu-Gong", R.drawable.solo_leveling));
        trendingList.add(new Manga("Komi-san wa...", "Tomohito Oda", R.drawable.komi));
        trendingList.add(new Manga("Kanojo, Okari...", "Reiji Miyajima", R.drawable.kanojo));
        trendingList.add(new Manga("Komi-san wa...", "Tomohito Oda", R.drawable.komi));

        List<Manga> mangaList = new ArrayList<>();
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));

        trendingAdapter = new MangaAdapter(trendingList, true);
        binding.recyclerTrending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerTrending.setAdapter(trendingAdapter);


        ///slider
        // Auto scroll
        sliderRunnable = new Runnable() {
            int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex == trendingAdapter.getItemCount()) {
                    currentIndex = 0;
                }
                binding.recyclerTrending.smoothScrollToPosition(currentIndex++);
                sliderHandler.postDelayed(this, 1000); // 3 giây chuyển ảnh 1 lần
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 1000);





        List<List<Manga>> mangaLists = new ArrayList<>();
        mangaLists.add(mangaList);        // Semua
        mangaLists.add(trendingList);      // Populer
        mangaLists.add(mangaList);

        MangaPagerAdapter mangaPagerAdapter = new MangaPagerAdapter(this, mangaLists);
        binding.listManga.setAdapter(mangaPagerAdapter);
        binding.listManga.setAdapter(mangaPagerAdapter);


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Semua"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Populer"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Baru"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.listManga.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.listManga.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager_main_home);
        if (parentViewPager != null) {
            parentViewPager.setUserInputEnabled(false); // Khóa vuốt cha
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager_main_home);
        if (parentViewPager != null) {
            parentViewPager.setUserInputEnabled(true); // Mở lại vuốt cha
        }
        binding = null; // tránh leak memory

    }
}

