package com.tq.comic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tq.comic.LoginActivity;
import com.tq.comic.MangaDetailActivity;
import com.tq.comic.R;
import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.adapter.StoryAdapter;
import com.tq.comic.config.PrefManager;
import com.tq.comic.databinding.FragmentMangaBinding;
import com.tq.comic.databinding.FragmentStorySavedBinding;
import com.tq.comic.dto.request.other.FavoriteRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.other.FavoriteResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.model.Manga;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.favorite.FavoriteService;

import java.util.ArrayList;
import java.util.List;

public class StorySavedFragment extends Fragment {

    private FragmentStorySavedBinding binding;
    private MangaAdapter trendingAdapter, listAdapter;
    private FavoriteService favoriteService;

    public StorySavedFragment() {
        super(R.layout.fragment_manga);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PrefManager prefManager = new PrefManager(requireContext());
        AuthenticationResponse auth = prefManager.getAuthResponse();

        if (auth != null) {
            binding = FragmentStorySavedBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }else{
            View guestView = inflater.inflate(R.layout.fragment_story_save_guest, container, false);

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
        getStorySaved();
    }
    private void getStorySaved(){
        favoriteService = new FavoriteService();
        PrefManager prefManager = new PrefManager(requireContext());
        AuthenticationResponse authenticationResponse = prefManager.getAuthResponse();
        if (authenticationResponse == null) {
            Log.e("getStorySave", "Không có thông tin đăng nhập. Hủy tải truyện đã lưu.");
            return;
        }
        favoriteService.getAllFavoritesFromUser(authenticationResponse.getUsername(),new ServiceExecutor.CallBack<List<StoryResponse>>(){
            @Override
            public void onSuccess(ApiResponse<List<StoryResponse>> result) {
                List<StoryResponse> list = result.getResult();
                if (list == null || list.isEmpty()) {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.listManga.setVisibility(View.GONE);
                } else {
                    binding.tvEmpty.setVisibility(View.GONE);
                    binding.listManga.setVisibility(View.VISIBLE);
                    binding.listManga.setLayoutManager(new LinearLayoutManager(getContext()));
                    StoryAdapter storyAdapter = new StoryAdapter(list, false);
                    binding.listManga.setAdapter(storyAdapter);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Lỗi "+ errorMessage , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // tránh leak memory
    }
    @Override
    public void onResume() {
        super.onResume();
        getStorySaved(); // Gọi lại API mỗi khi fragment hiện ra
    }

}

