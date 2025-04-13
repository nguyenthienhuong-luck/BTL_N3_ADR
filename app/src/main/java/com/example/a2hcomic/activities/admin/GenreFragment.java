package com.example.a2hcomic.activities.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.GenreAdapter;
import com.example.a2hcomic.models.Genre;
import com.example.a2hcomic.db.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GenreFragment extends Fragment {

    private ImageButton btnBack, btnAddGenre;
    private RecyclerView recyclerGenre;
    private GenreAdapter genreAdapter;
    private ArrayList<Genre> genreList = new ArrayList<>();
    private FirebaseService firebaseService;

    public GenreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre, container, false);

        // Lấy các View từ layout
        // Sửa lại id cho phù hợp với layout (btn_back thay vì btnBack)
        btnBack = v.findViewById(R.id.btn_back);
        btnAddGenre = v.findViewById(R.id.btnAddGenre);
        recyclerGenre = v.findViewById(R.id.recyclerGenre);

        firebaseService = new FirebaseService(); // Khởi tạo Firebase service

        // Thiết lập RecyclerView
        recyclerGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        genreAdapter = new GenreAdapter(genreList, false);
        recyclerGenre.setAdapter(genreAdapter);

        // Load dữ liệu từ Firebase
        loadGenresFromFirebase();

        // Xử lý nút quay lại
        btnBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Xử lý nút thêm thể loại
        btnAddGenre.setOnClickListener(view -> {
            changeFragment(new UpLoadGenreFragment());
        });

        return v;
    }

    private void loadGenresFromFirebase() {
        // Sử dụng FirebaseService để lấy dữ liệu từ node "genres"
        firebaseService.getGenreRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                genreList.clear();  // Xóa danh sách thể loại cũ
                for (DataSnapshot child : snapshot.getChildren()) {
                    Genre genre = child.getValue(Genre.class);
                    if (genre != null) {
                        genreList.add(genre);  // Thêm thể loại vào danh sách
                    }
                }
                genreAdapter.notifyDataSetChanged();  // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi trong quá trình lấy dữ liệu
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_admin, fragment);
        transaction.addToBackStack(null); // Thêm vào back stack
        transaction.commit();
    }
}
