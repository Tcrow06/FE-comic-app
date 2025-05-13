package com.tq.comic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tq.comic.R;
import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.databinding.FragmentHomeBinding;
import com.tq.comic.databinding.FragmentMangaBinding;
import com.tq.comic.model.Manga;

import java.util.ArrayList;
import java.util.List;

public class MangaFragment extends Fragment {

    private FragmentMangaBinding binding;
    private MangaAdapter trendingAdapter, listAdapter;

    public MangaFragment() {
        super(R.layout.fragment_manga);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMangaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Manga> mangaList = new ArrayList<>();
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));
        mangaList.add(new Manga("Fumetsu no Anata e", "Yoshitoki Ōima", "Chapter 51", R.drawable.fumetsu));
        mangaList.add(new Manga("One Piece", "Oda, Eiichiro", "Chapter 1081", R.drawable.onepiece));

        binding.listManga.setLayoutManager(new LinearLayoutManager(getContext()));
        MangaAdapter mangaAdapter = new MangaAdapter(mangaList, false);
        binding.listManga.setAdapter(mangaAdapter);


//        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager_main_home);
//        if (parentViewPager != null) {
//            parentViewPager.setUserInputEnabled(false); // Khóa vuốt cha
//        }




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // tránh leak memory
    }
}

