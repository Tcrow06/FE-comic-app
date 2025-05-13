package com.tq.comic.service.generate;

import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.story.GenerateResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenerateAPIService {

    @GET("api/generate")
    Call<ApiResponse<List<GenerateResponse>>> getAllGenerates();
}
