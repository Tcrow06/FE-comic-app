package com.tq.comic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.adapter.MangaAdapter;
import com.tq.comic.model.Manga;

import java.util.ArrayList;
import java.util.List;

public class MangaListFragment extends Fragment {

    private static final String ARG_MANGA_LIST = "manga_list";
    private List
            <Manga> mangaList;

    public static MangaListFragment newInstance(List<Manga> list) {
        MangaListFragment fragment = new MangaListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MANGA_LIST, new ArrayList<>(list));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mangaList = (List<Manga>) getArguments().getSerializable(ARG_MANGA_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MangaAdapter(mangaList, false));
        return recyclerView;
    }
}
