package com.example.a2hcomic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2hcomic.R;
import com.example.a2hcomic.models.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<Genre> genreList;

    private boolean isDetail;

    // Constructor
    public GenreAdapter(List<Genre> genreList, boolean isDetail) {
        this.genreList = genreList != null ? genreList : new ArrayList<>();
        this.isDetail = isDetail;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.tvCategoryName.setText(genre.getTitle() != null ? genre.getTitle() : "");
        if (!isDetail) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        // Sự kiện click nút xoá
        holder.btnDelete.setOnClickListener(v -> {
            removeGenre(position);
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    // Xoá thể loại
    public void removeGenre(int position) {
        if (position >= 0 && position < genreList.size()) {
            genreList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, genreList.size());
            notifyDataSetChanged();
        }
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageButton btnDelete;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
