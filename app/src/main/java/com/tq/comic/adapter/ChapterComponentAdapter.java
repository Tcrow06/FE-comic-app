package com.tq.comic.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.ChapterDetailActivity;
import com.tq.comic.MangaDetailActivity;
import com.tq.comic.R;
import com.tq.comic.dto.response.chapter.ChapterComponentResponse;
import com.tq.comic.model.Chapter;

import java.util.List;

public class ChapterComponentAdapter extends RecyclerView.Adapter<ChapterComponentAdapter.ChapterViewHolder> {

    private List<ChapterComponentResponse> chapterList;

    public ChapterComponentAdapter(List<ChapterComponentResponse> chapterList) {
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        ChapterComponentResponse chapter = chapterList.get(position);
        holder.chapterNumber.setText(String.valueOf(chapter.getChapterNumber()));
        holder.chapterTitle.setText(chapter.getTitle());
        holder.chapterDate.setText(chapter.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ChapterDetailActivity.class);
            intent.putExtra("story_id", chapter.getStoryId());
            intent.putExtra("chapter_number",chapter.getChapterNumber());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterNumber, chapterTitle, chapterDate;
        ImageView imgRead, imgAllowRead,imgAllowLock;
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterNumber = itemView.findViewById(R.id.chapterNumber);
            chapterTitle = itemView.findViewById(R.id.chapterTitle);
            chapterDate = itemView.findViewById(R.id.chapterDate);
            imgAllowRead = itemView.findViewById(R.id.imgAllowRead);
            imgAllowLock = itemView.findViewById(R.id.imgAllowLock);
        }
    }
}
