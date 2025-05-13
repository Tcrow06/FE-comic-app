package com.tq.comic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.adapter.StoryAdapter;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.model.Manga;

import java.util.ArrayList;
import java.util.List;

public class StoryListFragment extends Fragment {

    private static final String ARG_MANGA_LIST = "story_list";
    private List
            <StoryResponse> mangaList;

    public static StoryListFragment newInstance(List<StoryResponse> list) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MANGA_LIST, new ArrayList<>(list));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mangaList = (List<StoryResponse>) getArguments().getSerializable(ARG_MANGA_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new StoryAdapter(mangaList, false));
        return recyclerView;
    }
}
