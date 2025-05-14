package com.tq.comic.service.comment;

import com.tq.comic.config.RetrofitClient;
import com.tq.comic.dto.request.other.CommentRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.other.CommentResponse;
import com.tq.comic.service.callback.ServiceExecutor;

import java.util.List;

import retrofit2.Call;

public class CommentService {

    private final CommentAPIService apiService;

    public CommentService() {
        apiService = RetrofitClient.getRetrofitInstance().create(CommentAPIService.class);
    }

    public void comment(CommentRequest commentRequest, ServiceExecutor.CallBack<CommentResponse> callback) {
        Call<ApiResponse<CommentResponse>> call = apiService.comment(commentRequest);
        ServiceExecutor.enqueue(call, callback);
    }
    public void getAllComments(String storyCode, ServiceExecutor.CallBack<List<CommentResponse>> callback) {
        Call<ApiResponse<List<CommentResponse>>> call = apiService.getAllComments(storyCode);
        ServiceExecutor.enqueue(call, callback);
    }
}
