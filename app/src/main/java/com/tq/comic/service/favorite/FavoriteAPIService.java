package com.tq.comic.service.favorite;

import com.tq.comic.dto.request.other.FavoriteRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.other.FavoriteResponse;
import com.tq.comic.dto.response.story.StoryResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteAPIService {

    @POST("api/favorite")
    Call<ApiResponse<FavoriteResponse>> addFavorite(@Body FavoriteRequest request);
    @GET("api/favorite")
    Call<ApiResponse<List<StoryResponse>>> getAllFavoritesFromUser(@Query("username") String username);
    @DELETE("api/favorite")
    Call<ApiResponse<FavoriteResponse>> deleteFavorite(
            @Query("username") String username, @Query("code") String code
    );
    @GET("api/favorite/check-save")
    Call<ApiResponse<FavoriteResponse>> checkFavorite( @Query("username") String username, @Query("code") String code);

}
