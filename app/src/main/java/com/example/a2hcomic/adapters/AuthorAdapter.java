package com.example.a2hcomic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2hcomic.R;
import com.example.a2hcomic.models.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    private List<Author> authorList;

    // Constructor
    public AuthorAdapter(List<Author> authorList) {
        this.authorList = authorList != null ? authorList : new ArrayList<>();
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = authorList.get(position);
        holder.tvAuthorName.setText(author.getName() != null ? author.getName() : "");

        // Sự kiện click nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            removeAuthor(position);
        });
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    // Xóa tác giả
    public void removeAuthor(int position) {
        if (position >= 0 && position < authorList.size()) {
            authorList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, authorList.size());
            notifyDataSetChanged();
        }
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName;
        ImageButton btnDelete;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}