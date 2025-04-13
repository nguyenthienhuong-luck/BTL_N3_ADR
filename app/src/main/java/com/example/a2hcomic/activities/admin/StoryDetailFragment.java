package com.example.a2hcomic.activities.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.GenreAdapter;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.example.a2hcomic.models.Genre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class StoryDetailFragment extends Fragment {

    private ImageButton btn_back;
    private ImageView img_banner, img_title;
    private TextView tv_name_story, tv_author_story, tvTime, des_tv;
    private TextView btn_favorite;
    private RecyclerView rcv_genres;
    private TextView btn_read, btn_cmt;

    private GenreAdapter genreAdapter;
    private ArrayList<Genre> genreList = new ArrayList<>();
    private Comic comic;

    private FirebaseService fb;

    public StoryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_story_detail, container, false);

        btn_back = v.findViewById(R.id.btn_back);
        img_banner = v.findViewById(R.id.img_banner);
        img_title = v.findViewById(R.id.img_title);
        tv_name_story = v.findViewById(R.id.tv_name_story);
        tv_author_story = v.findViewById(R.id.tv_author_story);
        tvTime = v.findViewById(R.id.tvTime);
        des_tv = v.findViewById(R.id.des_tv);
        btn_favorite = v.findViewById(R.id.btn_favorite);
        rcv_genres = v.findViewById(R.id.rcv_genres);
        btn_read = v.findViewById(R.id.btn_read);
        btn_cmt = v.findViewById(R.id.btn_cmt);

        fb = new FirebaseService();

        genreAdapter = new GenreAdapter(genreList, true);
        rcv_genres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcv_genres.setAdapter(genreAdapter);

        // nhận Comic từ ComicAdapter thông qua bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            comic = (Comic) bundle.getSerializable("comic");
            if (comic != null) {
                setData();
            }
        }

        btn_back.setOnClickListener(v1 -> requireActivity().getSupportFragmentManager().popBackStack());

        btn_read.setOnClickListener(v1 -> {
            readComic();
        });

        return v;
    }

    private void readComic() {
        Toast.makeText(getContext(), "Đọc truyện: " + comic.getUrl_pdf(), Toast.LENGTH_SHORT).show();
    }

    private void getGenre(String comicId) {
        fb.getComicGenreRef().child(comicId).get().addOnSuccessListener(dataSnapshot -> {
            genreList.clear();
            if (dataSnapshot.exists()) {
                String genreId = dataSnapshot.getValue(String.class);
                fb.getGenreRef().child(genreId).get().addOnSuccessListener(dataSnapshot1 -> {
                    if (dataSnapshot1.exists()) {
                        Genre genre = dataSnapshot1.getValue(Genre.class);
                        genreList.add(genre);
                        genreAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    private void getAuthorName(String authorId) {
        fb.getAuthorRef().child(authorId).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String authorName = dataSnapshot.child("name").getValue(String.class);
                tv_author_story.setText(authorName);
            }
        });
    }

    private void setData() {
        tv_name_story.setText(comic.getTitle());
        tvTime.setText(formatTime(comic.getCreated_at()));
        des_tv.setText(comic.getDescription());

        Picasso.get().load(comic.getImg_url()).into(img_title);
        Picasso.get().load(comic.getBanner_url()).into(img_banner);

        getGenre(comic.getId());
        getAuthorName(comic.getAuthor_id());
    }

    private String formatTime(long timestamp) {
        if (timestamp <= 0) return "";
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}