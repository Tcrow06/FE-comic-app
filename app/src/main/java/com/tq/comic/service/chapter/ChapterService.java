package com.tq.comic.service.chapter;

import com.tq.comic.config.RetrofitClient;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.chapter.ChapterResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.story.StoryAPIService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class ChapterService {

    private final ChapterAPIService apiService;

    public ChapterService() {
        apiService = RetrofitClient.getRetrofitInstance().create(ChapterAPIService.class);
    }

    public void getChapterByChapterNumber(String storyCode, String chapterNumber ,ServiceExecutor.CallBack<ChapterResponse> callback) {
        Call<ApiResponse<ChapterResponse>> call = apiService.getChapterByChapterNumber(storyCode,chapterNumber);
        ServiceExecutor.enqueue(call, callback);
    }
}
