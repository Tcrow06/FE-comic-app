package com.tq.comic.service.story;

import android.util.Log;

import com.google.gson.Gson;
import com.tq.comic.R;
import com.tq.comic.config.RetrofitClient;
import com.tq.comic.dto.request.authentication.IntrospectRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.IntrospectResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.model.Manga;
import com.tq.comic.service.auth.AuthService;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.user.UserAPIService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryService {

    private final StoryAPIService apiService;

    public StoryService() {
        apiService = RetrofitClient.getRetrofitInstance().create(StoryAPIService.class);
    }

    public void getStoryTrending(ServiceExecutor.CallBack<Map<String, Object>> callback) {
        Call<ApiResponse<Map<String, Object>>> call = apiService.getAllStories(0,6);
        ServiceExecutor.enqueue(call, callback);
    }

    public void getStoryByCode(String code, int pageNo, int pageSize, ServiceExecutor.CallBack<StoryResponse> callback) {
        Call<ApiResponse<StoryResponse>> call = apiService.getStoryByCode(code, pageNo, pageSize);
        ServiceExecutor.enqueue(call, callback);
    }
    public void getStoryByGenerateCode(String code, ServiceExecutor.CallBack<List<StoryResponse>> callback) {
        Call<ApiResponse<List<StoryResponse>>> call = apiService.getStoryByGenerateCode(code);
        ServiceExecutor.enqueue(call, callback);
    }
    public void searchStory(String title, ServiceExecutor.CallBack<List<StoryResponse>> callback) {
        Call<ApiResponse<List<StoryResponse>>> call = apiService.searchStory(title);
        ServiceExecutor.enqueue(call, callback);
    }



}
