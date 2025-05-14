package com.tq.comic.service.comment;

import com.tq.comic.dto.request.other.CommentRequest;
import com.tq.comic.dto.request.other.FavoriteRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.other.CommentResponse;
import com.tq.comic.dto.response.other.FavoriteResponse;
import com.tq.comic.dto.response.story.StoryResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentAPIService {

    @POST("api/comment")
    Call<ApiResponse<CommentResponse>> comment(@Body CommentRequest request);
    @GET("api/comments/{story-code}")
    Call<ApiResponse<List<CommentResponse>>> getAllComments(
            @Path("story-code") String storyCode);
}
