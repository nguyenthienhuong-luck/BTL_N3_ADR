package com.example.a2hcomic.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a2hcomic.R;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReadComicFragment extends Fragment {

    private ImageView btn_back, btn_music;
    private TextView tvTitle, tvNumber;
    private PDFView pdfView;
    private ProgressBar progressBar;
    private Comic comic;
    private FirebaseService fb;

    public ReadComicFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_comic, container, false);

        // Ánh xạ view
        tvTitle = view.findViewById(R.id.tvTitle);
        btn_back = view.findViewById(R.id.btn_back);
        pdfView = view.findViewById(R.id.pdfView);
        tvNumber = view.findViewById(R.id.tvNumber);
        btn_music = view.findViewById(R.id.btn_music);
        progressBar = view.findViewById(R.id.progressBar);

        fb = new FirebaseService();

        // Nhận Comic từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            comic = (Comic) bundle.getSerializable("comic");
            if (comic != null) {
                loadComic();
            }
        }

        btn_music.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Music", Toast.LENGTH_SHORT).show();
        });

        btn_back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void loadComic() {
        progressBar.setVisibility(View.VISIBLE);
        tvTitle.setText(comic.getTitle());

        // ✅ Tăng view count khi mở truyện
        fb.increaseComicViewCount(comic.getId());

        String urlPdf = comic.getUrl_pdf();

        // Lấy trang đã đọc gần nhất từ SharedPreferences
        int lastPage = requireActivity()
                .getSharedPreferences("comic_reader", getContext().MODE_PRIVATE)
                .getInt("last_page_" + comic.getId(), 0);

        StorageReference pdfRef = FirebaseStorage
                .getInstance("gs://comic4t.appspot.com")
                .getReferenceFromUrl(urlPdf);

        pdfRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            pdfView.fromBytes(bytes)
                    .defaultPage(lastPage)
                    .swipeHorizontal(false)
                    .enableAntialiasing(true)
                    .onPageChange((page, pageCount) -> {
                        int currentPage = page;
                        tvNumber.setText((currentPage + 1) + " / " + pageCount);

                        // Lưu trang hiện tại vào SharedPreferences
                        requireActivity()
                                .getSharedPreferences("comic_reader", getContext().MODE_PRIVATE)
                                .edit()
                                .putInt("last_page_" + comic.getId(), currentPage)
                                .apply();
                    })
                    .onError(t -> {
                        Toast.makeText(getContext(), "Lỗi khi tải PDF", Toast.LENGTH_SHORT).show();
                    })
                    .onPageError((page, t) -> {
                        Toast.makeText(getContext(), "Lỗi khi tải trang " + page + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .load();

            progressBar.setVisibility(View.GONE);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Lỗi khi tải PDF", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });
    }
}
