package com.example.a2hcomic.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a2hcomic.R;
import com.example.a2hcomic.models.Comic;
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
        holder.imageView.setImageResource(R.drawable.item_imgcomic);

        holder.itemView.setOnClickListener(view -> {
            // Xử lý khi item được nhấn
        });
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
