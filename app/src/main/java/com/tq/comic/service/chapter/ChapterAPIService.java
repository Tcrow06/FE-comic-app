package com.tq.comic.service.chapter;

import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.chapter.ChapterResponse;
import com.tq.comic.dto.response.story.StoryResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChapterAPIService {

    @GET("api/chapter_story_id")
    Call<ApiResponse<ChapterResponse>> getChapterByChapterNumber(
            @Query("story-id") String storyId,
            @Query("chapter-number") String chapterNumber);

}
