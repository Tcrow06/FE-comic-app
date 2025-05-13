package com.tq.comic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.fragment.MangaListFragment;
import com.tq.comic.fragment.StoryListFragment;
import com.tq.comic.model.Manga;

import java.util.List;

public class StoryPagerAdapter extends FragmentStateAdapter {

    private final List<List<StoryResponse>> mangaLists;

    public StoryPagerAdapter(@NonNull Fragment fragment, List<List<StoryResponse>> mangaLists) {
        super(fragment);
        this.mangaLists = mangaLists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return StoryListFragment.newInstance(mangaLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mangaLists.size();
    }
}
