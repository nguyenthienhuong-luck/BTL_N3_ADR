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
import com.example.a2hcomic.models.Author;

public class UpLoadAuthorFragment extends Fragment {

    private ImageButton btnBack;
    private EditText edtAuthorName;
    private Button btnUpload;

    private FirebaseService fb;

    public UpLoadAuthorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_up_load_author, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        edtAuthorName = v.findViewById(R.id.edtComicName);
        btnUpload = v.findViewById(R.id.btnUpload);

        fb = new FirebaseService();

        btnBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());

        btnUpload.setOnClickListener(view -> {
            String authorName = edtAuthorName.getText().toString().trim();

            if (TextUtils.isEmpty(authorName)) {
                Toast.makeText(getContext(), "Vui lòng nhập tên tác giả", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadAuthor(authorName);
        });

        return v;
    }

    private void uploadAuthor(String authorName) {
        String authorId = fb.getAuthorRef().push().getKey();

        Author author = new Author(authorId, authorName);

        fb.getAuthorRef().child(authorId).setValue(author);
        Toast.makeText(getContext(), "Thêm tác giả thành công", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
