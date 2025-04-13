package com.example.a2hcomic.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.admin.StoryDetailFragment;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private List<Comic> comicList;
    private FirebaseService fb = new FirebaseService();

    public ComicAdapter(List<Comic> comicList) {
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        holder.tvTitle.setText(comic.getTitle());
        Picasso.get().load(comic.getImg_url()).into(holder.imgTitle);
        holder.tvTime.setText("Thời gian đăng: " + formatTime(comic.getCreated_at()));

        getAuthorName(comic.getAuthor_id(), holder.tvAuthor);

        // Xoá truyện
        holder.btnDelete.setOnClickListener(v -> {

        });

        // Click vào item
        holder.itemView.setOnClickListener(v -> {
            goToDetail(comic, holder.itemView.getContext());
        });
    }

    private void goToDetail(Comic comic, Context context) {
        // Chuẩn bị Fragment và Bundle
        StoryDetailFragment detailFragment = new StoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("comic", comic);
        detailFragment.setArguments(bundle);

        // Thay thế Fragment hiện tại bằng Fragment chi tiết
        ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_admin, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void getAuthorName(String authorId, TextView tvAuthor) {
        fb.getAuthorRef().child(authorId).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String authorName = dataSnapshot.child("name").getValue(String.class);
                tvAuthor.setText("Tác giả: " + authorName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList != null ? comicList.size() : 0;
    }

    private String formatTime(long timestamp) {
        if (timestamp <= 0) return "";
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTitle;
        TextView tvTitle, tvAuthor, tvTime;
        ImageButton btnDelete;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTitle = itemView.findViewById(R.id.img_title);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

}
