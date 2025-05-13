package com.tq.comic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tq.comic.fragment.MangaListFragment;
import com.tq.comic.model.Manga;

import java.util.List;

public class MangaPagerAdapter extends FragmentStateAdapter {

    private final List<List<Manga>> mangaLists;

    public MangaPagerAdapter(@NonNull Fragment fragment, List<List<Manga>> mangaLists) {
        super(fragment);
        this.mangaLists = mangaLists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MangaListFragment.newInstance(mangaLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mangaLists.size();
    }
}
