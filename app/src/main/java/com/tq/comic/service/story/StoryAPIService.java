package com.tq.comic.service.story;

import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.story.StoryResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoryAPIService {

    @GET("api/stories")
    Call<ApiResponse<Map<String, Object>>> getAllStories(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize
    );

    @GET("api/story/{code}")
    Call<ApiResponse<StoryResponse>> getStoryByCode(
            @Path("code") String code,
            @Query("page-no") int pageNo,
            @Query("page-size") int pageSize
    );
    @GET("api/stories/search")
    Call<ApiResponse<List<StoryResponse>>> getStoryByGenerateCode(
            @Query ("code") String code);
    @GET("api/story/search-title")
    Call<ApiResponse<List<StoryResponse>>> searchStory(
            @Query ("title") String title);

}
