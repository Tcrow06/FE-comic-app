package com.tq.comic.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tq.comic.R;
import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.adapter.MangaPagerAdapter;
import com.tq.comic.adapter.StoryAdapter;
import com.tq.comic.adapter.StoryPagerAdapter;
import com.tq.comic.databinding.FragmentHomeBinding;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.story.GenerateResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.model.Manga;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.generate.GenerateService;
import com.tq.comic.service.story.StoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment {

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    private FragmentHomeBinding binding;
    private MangaAdapter  listAdapter;
    private StoryAdapter trendingAdapter;
    private StoryService storyService;
    private GenerateService generateService;

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

        storyService = new StoryService();
        storyService.getStoryTrending(new ServiceExecutor.CallBack<Map<String, Object>>() {
            @Override
            public void onSuccess(ApiResponse<Map<String, Object>> response) {
                if (response.getResult() != null) {
                    Map<String, Object> result = response.getResult();

                    // Parse result.get("result") thành List<StoryResponse>
                    Gson gson = new Gson();
                    List<StoryResponse> stories = new ArrayList<>();
                    List<?> rawList = (List<?>) result.get("content");
                    if(rawList == null){
                        rawList = (List<?>) result.get("result");
                    }
                    if(rawList!=null){
                        for (Object item : rawList) {
                            String json = gson.toJson(item);
                            StoryResponse story = gson.fromJson(json, StoryResponse.class);
                            stories.add(story);
                        }
                    }

                    trendingAdapter = new StoryAdapter(stories, true);
                    binding.recyclerTrending.setAdapter(trendingAdapter);

                    sliderRunnable = new Runnable() {
                        int currentIndex = 0;

                        @Override
                        public void run() {
                            if (trendingAdapter == null || trendingAdapter.getItemCount() == 0) return;

                            if (currentIndex == trendingAdapter.getItemCount()) {
                                currentIndex = 0;
                            }
                            binding.recyclerTrending.smoothScrollToPosition(currentIndex++);
                            sliderHandler.postDelayed(this, 3000);
                        }
                    };
                    sliderHandler.postDelayed(sliderRunnable, 3000);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("getStoryTrending", "Lỗi gọi API: " + errorMessage);
                Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        binding.recyclerTrending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        generateService = new GenerateService();
        generateService.getAllGenerates(new ServiceExecutor.CallBack<List<GenerateResponse>>() {
            @Override
            public void onSuccess(ApiResponse<List<GenerateResponse>> response) {
                List<GenerateResponse> generateList = response.getResult();
                List<String> tabTitles = new ArrayList<>();
                List<List<StoryResponse>> allStories = new ArrayList<>();

                if (generateList == null || generateList.isEmpty()) return;

                // Dùng Counter để kiểm tra tất cả các thể loại đã load xong chưa
                final int totalTab = generateList.size();
                final AtomicInteger loadedTab = new AtomicInteger(0);

                for (GenerateResponse generate : generateList) {
                    tabTitles.add(generate.getName());

                    storyService.getStoryByGenerateCode(generate.getCode(),  new ServiceExecutor.CallBack<List<StoryResponse>>() {
                        @Override
                        public void onSuccess(ApiResponse<List<StoryResponse>> storyResponseApiResponse) {
                            List<StoryResponse> listStoryResp = storyResponseApiResponse.getResult();
                            if (listStoryResp == null) listStoryResp = new ArrayList<>();
                            allStories.add(listStoryResp);
                            if (loadedTab.incrementAndGet() == totalTab) {
                                // Sau khi load đủ tất cả tab
                                setupTabAndPager(tabTitles, allStories);
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            allStories.add(new ArrayList<>());
                            if (loadedTab.incrementAndGet() == totalTab) {
                                setupTabAndPager(tabTitles, allStories);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Lỗi lấy generate: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.listStory.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.listStory.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
    private void setupTabAndPager(List<String> tabTitles, List<List<StoryResponse>> storyLists) {
        StoryPagerAdapter storyPagerAdapter = new StoryPagerAdapter(this, storyLists);
        binding.listStory.setAdapter(storyPagerAdapter);

        binding.tabLayout.removeAllTabs();
        for (String title : tabTitles) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.listStory.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.listStory.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }
}

