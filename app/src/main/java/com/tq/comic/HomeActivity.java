package com.tq.comic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.model.Manga;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerTrending, recyclerList;
    MangaAdapter trendingAdapter, listAdapter;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        recyclerTrending = findViewById(R.id.recyclerTrending);
        recyclerList = findViewById(R.id.recyclerList);
        tabLayout = findViewById(R.id.tabLayout);

        // Dummy data
        List<Manga> trendingList = new ArrayList<>();
        trendingList.add(new Manga("Solo Leveling", "Chu-Gong", R.drawable.solo_leveling));
        trendingList.add(new Manga("Komi-san wa...", "Tomohito Oda", R.drawable.komi));
        trendingList.add(new Manga("Kanojo, Okari...", "Reiji Miyajima", R.drawable.kanojo));

        List<Manga> mangaList = new ArrayList<>();
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));

        // Setup trending
        trendingAdapter = new MangaAdapter(trendingList, true);
        recyclerTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerTrending.setAdapter(trendingAdapter);

        // Setup list manga
        listAdapter = new MangaAdapter(mangaList, false);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setAdapter(listAdapter);

        // Tabs
        tabLayout.addTab(tabLayout.newTab().setText("Semua"));
        tabLayout.addTab(tabLayout.newTab().setText("Populer"));
        tabLayout.addTab(tabLayout.newTab().setText("Baru"));

    }
}
