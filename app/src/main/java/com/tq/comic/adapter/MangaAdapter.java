package com.tq.comic.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.MangaDetailActivity;
import com.tq.comic.R;
import com.tq.comic.model.Manga;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder> {

    List<Manga> mangaList;
    boolean isHorizontal;

    public MangaAdapter(List<Manga> mangaList, boolean isHorizontal) {
        this.mangaList = mangaList;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        holder.imgCover.setImageResource(manga.imageResId);
        holder.txtTitle.setText(manga.title);
        if (holder.txtAuthor != null) holder.txtAuthor.setText(manga.author);
        if (holder.txtChapter != null && !manga.chapter.isEmpty()) {
            holder.txtChapter.setText(manga.chapter);
        }

        holder.itemView.setOnClickListener(v -> {
            // Chuyển sang MangaDetailActivity
            Intent intent = new Intent(holder.itemView.getContext(), MangaDetailActivity.class);
            intent.putExtra("manga_title", manga.title); // Gửi thông tin manga
            intent.putExtra("manga_image", manga.imageResId); // Gửi thông tin hình ảnh
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
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

    public void updateList(List<Manga> newList) {
        this.mangaList = newList;
        notifyDataSetChanged();
    }
}
