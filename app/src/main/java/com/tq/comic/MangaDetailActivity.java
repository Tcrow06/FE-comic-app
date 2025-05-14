package com.tq.comic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tq.comic.adapter.ChapterAdapter;
import com.tq.comic.adapter.ChapterComponentAdapter;
import com.tq.comic.adapter.CommentAdapter;
import com.tq.comic.config.PrefManager;
import com.tq.comic.dto.request.other.FavoriteRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.chapter.ChapterComponentResponse;
import com.tq.comic.dto.response.other.CommentResponse;
import com.tq.comic.dto.response.other.FavoriteResponse;
import com.tq.comic.dto.response.story.GenerateResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.model.Chapter;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.favorite.FavoriteService;
import com.tq.comic.service.story.StoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MangaDetailActivity extends BaseActivity {

    private RecyclerView chapterRecyclerView;
    private TextView desc, title, titleBig, noChapter;
    private ImageView arrowToggle,headerImage;
    private View fadeView;

    private StoryResponse story;

    private StoryService storyService;
    private ImageButton saveStory;

    private FavoriteService favoriteService;
    private boolean isSave;
    private String storyCode;
    private Button viewComment;


    private int currentPage = 0;
    private final int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manga_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        storyCode = getIntent().getStringExtra("story_code");


        desc = findViewById(R.id.txtDesc);
        arrowToggle = findViewById(R.id.arrowToggle);
        fadeView = findViewById(R.id.fadeView);
        title = findViewById(R.id.title);
        titleBig = findViewById(R.id.titleText);
        noChapter = findViewById(R.id.txtNoChapter);
        headerImage = findViewById(R.id.headerImage);
        chapterRecyclerView = findViewById(R.id.chapterRecyclerView);
        saveStory=findViewById(R.id.btnSaveStory);
        viewComment= findViewById(R.id.viewComment);

        ImageButton btnPrev = findViewById(R.id.btnPrev);
        ImageButton btnNext = findViewById(R.id.btnNext);


        storyService = new StoryService();
        loadChapters(storyCode, currentPage, pageSize);

        checkAndRefreshToken(auth -> {
            updateButtonSave(auth);
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadChapters(storyCode, currentPage, pageSize);
            }
        });

        btnNext.setOnClickListener(v -> {
            currentPage++;
            loadChapters(storyCode, currentPage, pageSize);
        });

        final boolean[] isExpanded = {false};


        arrowToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded[0] = !isExpanded[0];
                if (isExpanded[0]) {
                    desc.setMaxLines(Integer.MAX_VALUE);
                    arrowToggle.setImageResource(R.drawable.ic_arrow_up); // Mũi tên hướng lên
                    fadeView.setVisibility(View.GONE);
                } else {
                    desc.setMaxLines(2);
                    arrowToggle.setImageResource(R.drawable.ic_arrow_down); // Mũi tên hướng xuống
                    fadeView.setVisibility(View.VISIBLE);
                }
            }
        });
        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewComment.setOnClickListener(v -> {
            Intent intent = new Intent(MangaDetailActivity.this, CommentActivity.class);
            intent.putExtra("story_code", story.getCode());
            startActivity(intent);
        });

    }

    private void loadChapters(String storyCode, int page, int size) {

        storyService.getStoryByCode(storyCode, page, size, new ServiceExecutor.CallBack<StoryResponse>() {
            @Override
            public void onSuccess(ApiResponse<StoryResponse> response) {
                story = response.getResult();
                desc.setText(story.getDescription());
                title.setText(story.getTitle());
                titleBig.setText(story.getTitle());
                noChapter.setText(story.getChapters().size() + " Chapters");
                updateButtonState(story.getTotalChapter());
                Log.d("total", String.valueOf(story.getTotalChapter()));
                Glide.with(MangaDetailActivity.this)
                        .load(story.getCoverImage())
                        .placeholder(R.drawable.ic_avatar)
                        .into(headerImage);
                ChapterComponentAdapter adapter = new ChapterComponentAdapter(story.getChapters());
                chapterRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MangaDetailActivity.this, "Lỗi lấy chương: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateButtonState(int totalChapters) {
        ImageButton btnPrev = findViewById(R.id.btnPrev);
        ImageButton btnNext = findViewById(R.id.btnNext);

        boolean isFirstPage = currentPage == 0;
        boolean isLastPage = (currentPage + 1) * pageSize >= totalChapters;

        btnPrev.setEnabled(!isFirstPage);
        btnPrev.setAlpha(isFirstPage ? 0.3f : 1f);

        btnNext.setEnabled(!isLastPage);
        btnNext.setAlpha(isLastPage ? 0.3f : 1f);
    }
    public void updateButtonSave(AuthenticationResponse auth) {

        if (auth != null && auth.getAccessToken() != null) {
            favoriteService = new FavoriteService();
            saveStory.setAlpha(1f);
            saveStory.setEnabled(true);
            favoriteService.checkFavorite(new FavoriteRequest(auth.getUsername(),storyCode),new ServiceExecutor.CallBack<FavoriteResponse>(){
                @Override
                public void onSuccess(ApiResponse<FavoriteResponse> result) {
                    FavoriteResponse fav = result.getResult();
                    isSave = fav.isCheck();
                    if(isSave){
                        saveStory.setImageResource(R.drawable.save);
                    }else{
                        saveStory.setImageResource(R.drawable.un_save);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MangaDetailActivity.this, "Lưu thất bại: "+ errorMessage , Toast.LENGTH_SHORT).show();
                }
            });
            saveStory.setOnClickListener(v -> {
                if(!isSave){
                    favoriteService.addFavorite(new FavoriteRequest(auth.getUsername(),story.getCode()),new ServiceExecutor.CallBack<FavoriteResponse>(){
                        @Override
                        public void onSuccess(ApiResponse<FavoriteResponse> result) {
                            saveStory.setImageResource(R.drawable.save);
                            isSave=!isSave;
                            Toast.makeText(MangaDetailActivity.this, "Lưu thành công" , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(MangaDetailActivity.this, "Lưu thất bại: "+ errorMessage , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    favoriteService.deleteFavorite(new FavoriteRequest(auth.getUsername(),story.getCode()),new ServiceExecutor.CallBack<FavoriteResponse>(){
                        @Override
                        public void onSuccess(ApiResponse<FavoriteResponse> result) {
                            saveStory.setImageResource(R.drawable.un_save);
                            isSave=!isSave;
                            Toast.makeText(MangaDetailActivity.this, "Xoá lưu thành công" , Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(MangaDetailActivity.this, "Xóa lưu thất bại: "+ errorMessage , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else {
            saveStory.setAlpha(0.3f);
            saveStory.setEnabled(false);
        }
    }





}
