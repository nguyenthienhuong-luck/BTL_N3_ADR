package com.example.a2hcomic.adapters;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.admin.StoryDetailFragment;
import com.example.a2hcomic.models.Comic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicHomeAdapter extends RecyclerView.Adapter<ComicHomeAdapter.ViewHolder> {
    private List<Comic> comics;

    public ComicHomeAdapter(List<Comic> comics){
        this.comics = comics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_comic_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comic comic = comics.get(position);
        holder.textView.setText(comic.getTitle());

        // Sử dụng thư viện Picasso để tải và hiển thị ảnh vào ImageView
        Picasso.get().load(comic.getImg_url()).into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            goToDetail(comic, holder.itemView.getContext());
        });
    }

    private void goToDetail(Comic comic, Context context) {
        // Chuẩn bị Fragment và Bundle
        StoryDetailFragment detailFragment = new StoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("comic", comic);
        detailFragment.setArguments(bundle);

        ((FragmentActivity) context).findViewById(R.id.nav_main).setVisibility(View.GONE);

        // Thay thế Fragment hiện tại bằng Fragment chi tiết
        ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_imgcomic);
            textView = itemView.findViewById(R.id.item_titlecomic);
        }
    }
    
}
