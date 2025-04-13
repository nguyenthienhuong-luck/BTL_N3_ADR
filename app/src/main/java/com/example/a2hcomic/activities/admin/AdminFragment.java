package com.example.a2hcomic.activities.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.a2hcomic.R;

public class    AdminFragment extends Fragment {

    private Button btnComic, btnAuthors, btnGenre, btnUser;
    private ImageButton btnBack;

    public AdminFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        btnComic = v.findViewById(R.id.btn_comic);
        btnAuthors = v.findViewById(R.id.btn_authors);
        btnGenre = v.findViewById(R.id.btn_genre);
        btnBack = v.findViewById(R.id.btnBack);
        btnUser = v.findViewById(R.id.btn_user);

        btnComic.setOnClickListener(v1 -> changeFragment(new ComicFragment()));
        btnAuthors.setOnClickListener(v1 -> changeFragment(new AuthorFragment()));
        btnGenre.setOnClickListener(v1 -> changeFragment(new GenreFragment()));
        btnUser.setOnClickListener(v1 -> changeFragment(new UserFragment()));

        btnBack.setOnClickListener(v1 -> {
            getActivity().finish();
        });

        return v;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_admin, fragment);
        transaction.addToBackStack(null); // Thêm vào back stack
        transaction.commit();
    }
}