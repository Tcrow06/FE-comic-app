package com.tq.comic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.R;
import com.tq.comic.model.Chapter;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<Chapter> chapterList;

    public ChapterAdapter(List<Chapter> chapterList) {
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
        Chapter chapter = chapterList.get(position);
        holder.chapterNumber.setText(chapter.getNumber());
        holder.chapterTitle.setText(chapter.getTitle());
        holder.chapterPageCount.setText(chapter.getPageCount());

        // Nếu đã đọc thì hiển thị icon, chưa đọc thì ẩn
        holder.imgRead.setVisibility(chapter.isRead() ? View.VISIBLE : View.GONE);


        if (!chapter.isAllowRead()) {
            holder.imgAllowRead.setVisibility(View.GONE);  // Ẩn ảnh mặc định
            holder.imgAllowLock.setVisibility(View.VISIBLE);  // Hiển thị ảnh khóa
            holder.itemView.setAlpha(0.5f);  // Làm mờ toàn bộ item
        } else {
            holder.imgAllowRead.setVisibility(View.VISIBLE);  // Hiển thị ảnh mặc định
            holder.imgAllowLock.setVisibility(View.GONE);  // Ẩn ảnh khóa
            holder.itemView.setAlpha(1f);  // Không làm mờ
        }

    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterNumber, chapterTitle, chapterPageCount;
        ImageView imgRead, imgAllowRead,imgAllowLock;
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterNumber = itemView.findViewById(R.id.chapterNumber);
            chapterTitle = itemView.findViewById(R.id.chapterTitle);
            chapterPageCount = itemView.findViewById(R.id.chapterPageCount);
            imgRead = itemView.findViewById(R.id.imgRead);
            imgAllowRead = itemView.findViewById(R.id.imgAllowRead);
            imgAllowLock = itemView.findViewById(R.id.imgAllowLock);
        }
    }
}
