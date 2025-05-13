package com.tq.comic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.tq.comic.adapter.ChapterComponentAdapter;
import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.databinding.ActivityChapterDetailBinding;
import com.tq.comic.databinding.ActivityMainHomeBinding;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.chapter.ChapterResponse;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.model.Manga;
import com.tq.comic.service.callback.ServiceExecutor;
import com.tq.comic.service.chapter.ChapterAPIService;
import com.tq.comic.service.chapter.ChapterService;

import java.util.ArrayList;
import java.util.List;

public class ChapterDetailActivity extends AppCompatActivity {

    private ActivityChapterDetailBinding binding;
    private Button nutChuongTruoc, nutChuongSau;

    private ChapterService chapterService;
    private String storyId;
    private Integer chapterNumber; // sửa thành int để dễ thao tác tăng/giảm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityChapterDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nutChuongTruoc = binding.nutChuongTruoc;
        nutChuongSau = binding.nutChuongSau;

        storyId = getIntent().getStringExtra("story_id");
        // Chuyển chapterNumber từ String sang int
        chapterNumber = getIntent().getIntExtra("chapter_number", 1); // hoặc parse từ getStringExtra nếu bạn không truyền bằng int

        chapterService = new ChapterService();
        loadChapter(chapterNumber); // gọi hàm tải chương ban đầu

        // Bắt sự kiện nút Chương Trước
        nutChuongTruoc.setOnClickListener(v -> {
            if (chapterNumber > 1) {
                chapterNumber--;
                loadChapter(chapterNumber);
            } else {
                Toast.makeText(this, "Đây là chương đầu tiên!", Toast.LENGTH_SHORT).show();
            }
        });

        // Bắt sự kiện nút Chương Sau
        nutChuongSau.setOnClickListener(v -> {
            chapterNumber++;
            loadChapter(chapterNumber);
        });
    }

    private void loadChapter(int chapterNum) {
        chapterService.getChapterByChapterNumber(storyId, String.valueOf(chapterNum), new ServiceExecutor.CallBack<ChapterResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ApiResponse<ChapterResponse> response) {
                ChapterResponse chapterResponse = response.getResult();
                binding.tieuDeTruyen.setText(chapterResponse.getStory().getTitle());
                String thongTinChuong = "Chương " + chapterResponse.getChapterNumber() + ": " + chapterResponse.getTitle();
                binding.thongTinChuong.setText(thongTinChuong);
                binding.noiDungTruyen.loadDataWithBaseURL(null, chapterResponse.getContent(), "text/html", "UTF-8", null);

//                binding.noiDungTruyen.setText(Html.fromHtml(chapterResponse.getContent(), Html.FROM_HTML_MODE_LEGACY));
//                binding.noiDungTruyen.setText(chapterResponse.getContent());
                binding.chanTrangChuong.setText("Chương " + chapterNum + " / " + chapterResponse.getStory().getChapters().size());

                int totalChapters = chapterResponse.getStory().getChapters().size();
                updateButtonState(chapterNum, totalChapters);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChapterDetailActivity.this, "Lỗi lấy chương: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateButtonState(int chapterNum, int totalChapters) {
        if (chapterNumber <= 1) {
            nutChuongTruoc.setEnabled(false);
            nutChuongTruoc.setBackgroundResource(R.drawable.button_disabled);
        } else {
            nutChuongTruoc.setEnabled(true);
            nutChuongTruoc.setBackgroundResource(R.drawable.button_enabled);
        }

        if (chapterNumber >= totalChapters) {
            nutChuongSau.setEnabled(false);
            nutChuongSau.setBackgroundResource(R.drawable.button_disabled);
        } else {
            nutChuongSau.setEnabled(true);
            nutChuongSau.setBackgroundResource(R.drawable.button_enabled);
        }
    }

}
