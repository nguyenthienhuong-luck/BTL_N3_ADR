package com.example.a2hcomic.activities.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a2hcomic.R;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Genre;

public class UpLoadGenreFragment extends Fragment {

    private ImageButton btnBack;
    private EditText edtGenreName;
    private Button btnUpload;

    private FirebaseService fb;

    public UpLoadGenreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fb = new FirebaseService(); // Khởi tạo FirebaseService
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_up_load_genre, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        edtGenreName = v.findViewById(R.id.edtGenreName);
        btnUpload = v.findViewById(R.id.btnUpload);

        // Nút quay lại
        btnBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());

        // Nút upload thể loại
        btnUpload.setOnClickListener(view -> {
            String genreName = edtGenreName.getText().toString().trim();

            if (TextUtils.isEmpty(genreName)) {
                Toast.makeText(getContext(), "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadGenre(genreName);
        });

        return v;
    }

    private void uploadGenre(String genreName) {
        String genreId = fb.getGenreRef().push().getKey();

        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setTitle(genreName); // Đặt tên thể loại

        fb.getGenreRef().child(genreId).setValue(genre);
        Toast.makeText(getContext(), "Thêm thể loại thành công", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
