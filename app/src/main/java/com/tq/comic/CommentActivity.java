package com.tq.comic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.adapter.CommentAdapter;
import com.tq.comic.adapter.StoryAdapter;
import com.tq.comic.config.PrefManager;
import com.tq.comic.databinding.ActivityCommentBinding;
import com.tq.comic.databinding.ActivityRegisterBinding;
import com.tq.comic.dto.request.authentication.RegisterRequest;
import com.tq.comic.dto.request.other.CommentRequest;
import com.tq.comic.dto.request.other.FavoriteRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.other.CommentResponse;
import com.tq.comic.dto.response.other.FavoriteResponse;
import com.tq.comic.dto.response.user.UserResponse;
import com.tq.comic.service.auth.AuthService;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.comment.CommentService;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText commentInput;

    private ActivityCommentBinding binding;

    private String content, username, storyCode;
    private List<CommentResponse> commentList;
    private RecyclerView commentRV;

    private CommentAdapter commentAdapter;

    private CommentResponse pendingComment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Đặt layout chính là binding root

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        storyCode = getIntent().getStringExtra("story_code");
        PrefManager prefManager = new PrefManager(this);
        AuthenticationResponse authenticationResponse = prefManager.getAuthResponse();
        btnSubmit = binding.btnSubmitReview;
        commentInput = binding.reviewInput;
        commentRV = binding.reviewRecyclerView;

        if (authenticationResponse == null) {
            btnSubmit.setVisibility(View.GONE);
            commentInput.setVisibility(View.GONE);
        }else {
            btnSubmit.setVisibility(View.VISIBLE);
            commentInput.setVisibility(View.VISIBLE);

        }

        btnSubmit.setOnClickListener(v -> {
            if (authenticationResponse == null) {
                Toast.makeText(CommentActivity.this, "Vui lòng đăng nhập để đánh giá" , Toast.LENGTH_SHORT).show();
                return;
            }

            username = authenticationResponse.getUsername();
            content = binding.reviewInput.getText().toString();
            if(content.isEmpty()){
                Toast.makeText(CommentActivity.this, "Vui lòng nhập đánh giá" , Toast.LENGTH_SHORT).show();
                return;
            }
            comment();
        });

        loadComment();
    }
    private void comment() {
        CommentService commentService = new CommentService();
        CommentRequest request = CommentRequest.builder()
                .username(username)
                .content(content)
                .storyCode(storyCode)
                .build();
        commentService.comment(request,new ServiceExecutor.CallBack<CommentResponse>(){
            @Override
            public void onSuccess(ApiResponse<CommentResponse> result) {
                if (result.getResult() != null) {
                    Toast.makeText(CommentActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (commentAdapter != null) {
                        commentAdapter.addComment(result.getResult());
                        binding.reviewRecyclerView.scrollToPosition(commentList.size() - 1);
                        binding.reviewInput.setText(""); // Clear input
                    } else {
                        pendingComment = result.getResult(); // lưu lại comment để thêm sau
                    }
//
//                    commentAdapter.addComment(result.getResult());
//                    binding.reviewRecyclerView.scrollToPosition(commentList.size() - 1);
//                    binding.reviewInput.setText(""); // Clear input
//                    //Cập nhật lại reclyer view

                } else {
                    Toast.makeText(CommentActivity.this, "Lôi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(CommentActivity.this, "Comment thất bại: "+ errorMessage , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadComment(){
        CommentService commentService = new CommentService();
        commentService.getAllComments(storyCode,new ServiceExecutor.CallBack<List<CommentResponse>>(){
            @Override
            public void onSuccess(ApiResponse<List<CommentResponse>> result) {
                if (result.getResult() != null) {
                    commentList = result.getResult();

                    if (commentList == null) {
                        commentList = new ArrayList<>();
                    }
                    commentAdapter = new CommentAdapter(commentList);


                    binding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                    binding.reviewRecyclerView.setAdapter(commentAdapter);

                    // Nếu có comment chờ thì thêm
                    if (pendingComment != null) {
                        commentAdapter.addComment(pendingComment);
                        binding.reviewRecyclerView.scrollToPosition(commentList.size() - 1);
                        pendingComment = null; // clear để tránh thêm lại
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Lôi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(CommentActivity.this, "Lấy comment thất bại: "+ errorMessage , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
