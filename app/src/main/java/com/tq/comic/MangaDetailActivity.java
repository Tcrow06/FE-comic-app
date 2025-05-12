package com.tq.comic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.adapter.ChapterAdapter;
import com.tq.comic.model.Chapter;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailActivity extends AppCompatActivity {

    private RecyclerView chapterRecyclerView;
    private TextView aboutContent;
    private ImageView arrowToggle;
    private View fadeView;
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

        String title = getIntent().getStringExtra("manga_title");
        int imageResId = getIntent().getIntExtra("manga_image", -1);

        final boolean[] isExpanded = {false};
        aboutContent = findViewById(R.id.aboutContent);
        arrowToggle = findViewById(R.id.arrowToggle);
        fadeView = findViewById(R.id.fadeView);

        arrowToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded[0] = !isExpanded[0];
                if (isExpanded[0]) {
                    aboutContent.setMaxLines(Integer.MAX_VALUE);
                    arrowToggle.setImageResource(R.drawable.ic_arrow_up); // Mũi tên hướng lên
                    fadeView.setVisibility(View.GONE);
                } else {
                    aboutContent.setMaxLines(2);
                    arrowToggle.setImageResource(R.drawable.ic_arrow_down); // Mũi tên hướng xuống
                    fadeView.setVisibility(View.VISIBLE);
                }
            }
        });



        chapterRecyclerView = findViewById(R.id.chapterRecyclerView);
        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Chapter> chapterList = new ArrayList<>();
        chapterList.add(new Chapter("01", "Chapter One: Beginning", "30 Page", true,true));
        chapterList.add(new Chapter("02", "Chapter Two: Battle", "28 Page", false,true));
        chapterList.add(new Chapter("03", "Chapter Three: Endgame", "35 Page", false, false));

        ChapterAdapter adapter = new ChapterAdapter(chapterList);
        chapterRecyclerView.setAdapter(adapter);




    }
}
