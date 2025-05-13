package com.tq.comic.service.generate;

import com.tq.comic.config.RetrofitClient;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.story.GenerateResponse;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.story.StoryAPIService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class GenerateService {

    private final GenerateAPIService apiService;

    public GenerateService() {
        apiService = RetrofitClient.getRetrofitInstance().create(GenerateAPIService.class);
    }

    public void getAllGenerates(ServiceExecutor.CallBack<List<GenerateResponse>> callback) {
        Call<ApiResponse<List<GenerateResponse>>> call = apiService.getAllGenerates();
        ServiceExecutor.enqueue(call, callback);
    }


}
