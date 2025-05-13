package com.tq.comic.service.favorite;

import com.tq.comic.config.RetrofitClient;
import com.tq.comic.dto.request.other.FavoriteRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.other.FavoriteResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.service.callback.ServiceExecutor;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class FavoriteService {

    private final FavoriteAPIService apiService;

    public FavoriteService() {
        apiService = RetrofitClient.getRetrofitInstance().create(FavoriteAPIService.class);
    }

    public void addFavorite(FavoriteRequest request, ServiceExecutor.CallBack<FavoriteResponse> callback) {
        Call<ApiResponse<FavoriteResponse>> call = apiService.addFavorite(request);
        ServiceExecutor.enqueue(call, callback);
    }
    public void deleteFavorite(FavoriteRequest request, ServiceExecutor.CallBack<FavoriteResponse> callback) {
        Call<ApiResponse<FavoriteResponse>> call = apiService.deleteFavorite(request.getUsername(),request.getStoryCode());
        ServiceExecutor.enqueue(call, callback);
    }
    public void checkFavorite(FavoriteRequest request, ServiceExecutor.CallBack<FavoriteResponse> callback) {
        Call<ApiResponse<FavoriteResponse>> call = apiService.checkFavorite(request.getUsername(),request.getStoryCode());
        ServiceExecutor.enqueue(call, callback);
    }
    public void getAllFavoritesFromUser(String username, ServiceExecutor.CallBack<List<StoryResponse>> callback) {
        Call<ApiResponse<List<StoryResponse>>> call = apiService.getAllFavoritesFromUser(username);
        ServiceExecutor.enqueue(call, callback);
    }




}
