package com.tq.comic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tq.comic.R;
import com.tq.comic.dto.response.other.CommentResponse;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReviewViewHolder> {

    private List<CommentResponse> comments;

    public CommentAdapter(List<CommentResponse> comments) {
        this.comments = comments;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtUsername, txtContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        CommentResponse comment = comments.get(position);
        holder.txtUsername.setText(comment.getUsername());
        holder.txtContent.setText(comment.getContent());
//        holder.imgAvatar.setImageResource(comment.getAvatarResId());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public void addComment(CommentResponse comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1);
    }
}
