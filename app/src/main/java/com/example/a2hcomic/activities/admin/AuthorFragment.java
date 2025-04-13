package com.example.a2hcomic.activities.admin;

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
import com.example.a2hcomic.adapters.AuthorAdapter;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Author;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AuthorFragment extends Fragment {

    private ImageButton btnBack, btnAddAuthor;
    private RecyclerView recyclerAuthor;
    private AuthorAdapter authorAdapter;
    private ArrayList<Author> authorList = new ArrayList<>();
    private FirebaseService firebaseService;

    public AuthorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseService = new FirebaseService(); // Khởi tạo Firebase service
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_author, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        btnAddAuthor = v.findViewById(R.id.btnAddAuthor);
        recyclerAuthor = v.findViewById(R.id.recyclerAuthor);

        // Thiết lập RecyclerView
        recyclerAuthor.setLayoutManager(new LinearLayoutManager(getContext()));
        authorAdapter = new AuthorAdapter(authorList);
        recyclerAuthor.setAdapter(authorAdapter);

        // Load dữ liệu từ Firebase
        loadAuthorsFromFirebase();

        // Xử lý nút quay lại
        btnBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Xử lý nút thêm tác giả
        btnAddAuthor.setOnClickListener(view -> {
            changeFragment(new UpLoadAuthorFragment());
        });

        return v;
    }

    private void loadAuthorsFromFirebase() {
        firebaseService.getAuthorRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authorList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Author author = child.getValue(Author.class);
                    if (author != null) {
                        authorList.add(author);
                    }
                }
                authorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO: Xử lý lỗi nếu cần
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_admin, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
