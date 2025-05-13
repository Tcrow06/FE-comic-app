package com.tq.comic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tq.comic.LoginActivity;
import com.tq.comic.R;
import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.adapter.StoryAdapter;
import com.tq.comic.config.PrefManager;
import com.tq.comic.databinding.FragmentSearchBinding;
import com.tq.comic.databinding.FragmentStorySavedBinding;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.favorite.FavoriteService;
import com.tq.comic.service.story.StoryService;

import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private MangaAdapter trendingAdapter, listAdapter;
    private StoryService storyService;
    private EditText searchBar;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            binding = FragmentSearchBinding.inflate(inflater, container, false);
            return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBar =  binding.searchBar;
        ImageButton btnSearch = binding.btnSearch;

        // Gọi tìm kiếm khi click nút tìm
        btnSearch.setOnClickListener(v -> {
            String title = searchBar.getText().toString().trim();
            getStorySearch(title);
//            if (!title.isEmpty()) {
//                getStorySearch(title);
//            }
        });
        // Gọi tìm kiếm khi nhấn Enter (bàn phím)
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String title = searchBar.getText().toString().trim();
//                if (!title.isEmpty()) {
//                    getStorySearch(title);
//                }
                getStorySearch(title);
                return true;
            }
            return false;
        });

        // Optional: tự động load danh sách ban đầu
        getStorySearch(""); // hoặc null nếu backend chấp nhận
    }
    private void getStorySearch(String title){
        storyService = new StoryService();
        storyService.searchStory(title,new ServiceExecutor.CallBack<List<StoryResponse>>(){
            @Override
            public void onSuccess(ApiResponse<List<StoryResponse>> result) {
                List<StoryResponse> list = result.getResult();
                binding.listManga.setLayoutManager(new LinearLayoutManager(getContext()));
                StoryAdapter storyAdapter = new StoryAdapter(list, false);
                binding.listManga.setAdapter(storyAdapter);
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
}

