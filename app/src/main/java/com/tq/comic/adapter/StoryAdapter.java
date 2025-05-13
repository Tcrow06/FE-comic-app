package com.tq.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tq.comic.MangaDetailActivity;
import com.tq.comic.R;
import com.tq.comic.dto.response.story.StoryResponse;
import com.tq.comic.model.Manga;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    List<StoryResponse> storyList;
    boolean isHorizontal;

    public StoryAdapter(List<StoryResponse> mangaList, boolean isHorizontal) {
        this.storyList = mangaList;
        this.isHorizontal = isHorizontal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isHorizontal) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga_trend, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga_category, parent, false);
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoryResponse story = storyList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(story.getCoverImage())
                .placeholder(R.drawable.logo_tq)
                .into(holder.imgCover);
        holder.txtTitle.setText(story.getTitle());
        if (holder.txtAuthor != null) holder.txtAuthor.setText(story.getAuthor());
        if (holder.txtChapter != null && !story.getChapters().isEmpty()) {
            holder.txtChapter.setText("N.Chapter " + String.valueOf(story.getChapters().size()));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MangaDetailActivity.class);
            intent.putExtra("story_code", story.getCode()); // Gửi thông tin manga
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return storyList == null ? 0 : storyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView txtTitle, txtAuthor, txtChapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtChapter = itemView.findViewById(R.id.txtChapter);
        }
    }

    public void updateList(List<StoryResponse> newList) {
        this.storyList = newList;
        notifyDataSetChanged();
    }
}
