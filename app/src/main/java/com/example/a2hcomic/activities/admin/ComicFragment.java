package com.example.a2hcomic.activities.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.ComicAdapter;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComicFragment extends Fragment {

    private ImageButton btnBack, btnAddComic;
    private RecyclerView recyclerComic;
    private ComicAdapter comicAdapter;
    private ArrayList<Comic> comicList;
    private FirebaseService fb;

    public ComicFragment() {
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
        View v = inflater.inflate(R.layout.fragment_comic, container, false);

        btnBack = v.findViewById(R.id.btn_back);
        btnAddComic = v.findViewById(R.id.btnAddComic);
        recyclerComic = v.findViewById(R.id.recyclerComic);

        fb = new FirebaseService();
        comicList = new ArrayList<>();
        comicAdapter = new ComicAdapter(comicList);
        recyclerComic.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerComic.setAdapter(comicAdapter);

        getListComic();

        btnBack.setOnClickListener(v1 -> {
            // đóng fragment hiện tại
            getActivity().getSupportFragmentManager().popBackStack();
        });

        btnAddComic.setOnClickListener(v1 -> {
            changeFragment(new UpLoadComicFragment());
        });

        return v;
    }

    private void getListComic() {
        fb.getComicRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comicList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Comic comic = child.getValue(Comic.class);
                    comicList.add(comic);
                }
                // sắp xếp theo ngày đăng Create_at
                comicList.sort((o1, o2) -> (int) (o2.getCreated_at() - o1.getCreated_at()));
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_admin, fragment);
        transaction.addToBackStack(null); // Thêm vào back stack
        transaction.commit();
    }
}