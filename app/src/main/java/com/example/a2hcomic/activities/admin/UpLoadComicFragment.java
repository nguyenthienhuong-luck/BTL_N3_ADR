package com.example.a2hcomic.activities.admin;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Author;
import com.example.a2hcomic.models.Comic;
import com.example.a2hcomic.models.Genre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UpLoadComicFragment extends Fragment {

    private Button btnUpload;
    private ImageButton btnBack;
    private Spinner spGenre, spAuthor;
    private TextView tvPdf;
    private EditText edtComicName, edtComicDescription, edtAuthorName;
    private ImageView imgAvt, imgBanner;
    private RelativeLayout loading;

    private FirebaseService fb;
    private String selectedIdGenre = "", selectedIdAuthor = "";
    private int currentSelection;
    private static final int SELECT_TITLE = 1;
    private static final int SELECT_BANNER = 2;
    private static final int SELECT_PDF = 3;
    private Uri imgAvtUri, imgBannerUri, pdfUri;
    private ArrayList<Genre> listGenre;
    private ArrayList<Author> listAuthor;
    private ArrayAdapter<Genre> adapterGenre;
    private ArrayAdapter<Author> adapterAuthor;

    public UpLoadComicFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_up_load_comic, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        btnUpload = v.findViewById(R.id.btnUpload);
        spGenre = v.findViewById(R.id.spGenre);
        edtComicName = v.findViewById(R.id.edtComicName);
        spAuthor = v.findViewById(R.id.spAuthor);
        edtComicDescription = v.findViewById(R.id.edtSummary);
        imgAvt = v.findViewById(R.id.imgAvt);
        imgBanner = v.findViewById(R.id.imgBanner);
        tvPdf = v.findViewById(R.id.tv_pdf);
        loading = v.findViewById(R.id.loading);

        fb = new FirebaseService();
        listGenre = new ArrayList<>();
        listGenre.add(new Genre("","--- Chọn thể loại ---"));
        listAuthor = new ArrayList<>();
        listAuthor.add(new Author("","--- Chọn tác giả ---"));

        // lấy danh sách tác giả, thể loại từ firebase
        getAuthor();
        getGenre();

        tvPdf.setOnClickListener(v1 -> {
            currentSelection = SELECT_PDF;
            openPDFPicker();
        });

        imgAvt.setOnClickListener(v1 -> {
            currentSelection = SELECT_TITLE;
            openImagePicker();
        });

        imgBanner.setOnClickListener(v1 -> {
            currentSelection = SELECT_BANNER;
            openImagePicker();
        });

        btnBack.setOnClickListener(v1 -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        btnUpload.setOnClickListener(v1 -> {
            uploadComic();
        });

        return v;
    }

    // Mở bộ chọn ảnh
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    // Mở bộ chọn PDF
    private void openPDFPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pdfPickerLauncher.launch(intent);
    }

    // ActivityResultLauncher cho ảnh
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                getActivity();
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedUri = result.getData().getData();
                    if (currentSelection == SELECT_TITLE) {
                        imgAvtUri = selectedUri;
                        imgAvt.setImageURI(imgAvtUri);
                    } else if (currentSelection == SELECT_BANNER) {
                        imgBannerUri = selectedUri;
                        imgBanner.setImageURI(imgBannerUri);
                    }
                }
            });

    // ActivityResultLauncher cho PDF
    private final ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    pdfUri = result.getData().getData();
                    tvPdf.setText("Đã chọn file PDF");
                }
            });

    // Tải file lên Firebase
    private void uploadComic() {
        loading.setVisibility(View.VISIBLE);
        String comicId = fb.getComicRef().push().getKey(); // ID duy nhất cho comic
        // user id trong Shared preference
        String userId = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).getString("id", "");;

        Comic newComic = new Comic();
        newComic.setId(comicId);
        newComic.setTitle(edtComicName.getText().toString());
        newComic.setDescription(edtComicDescription.getText().toString());
        newComic.setAuthor_id(selectedIdAuthor);
        newComic.setUser_id(userId);
        newComic.setCreated_at(System.currentTimeMillis());

        int pendingUploads = 0;

        // Đếm số file cần tải lên
        if (imgAvtUri != null) pendingUploads++;
        if (imgBannerUri != null) pendingUploads++;
        if (pdfUri != null) pendingUploads++;

        if (pendingUploads == 0) return;

        // Biến final để theo dõi số lượng file đã tải lên
        final int[] completedUploads = {0};

        // Tải ảnh tiêu đề
        if (imgAvt != null) {
            String fileName = comicId + "/img_title.jpg";
            StorageReference fileRef = fb.getStorageComicRef().child(fileName);
            int finalPendingUploads = pendingUploads;
            fileRef.putFile(imgAvtUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            newComic.setImg_url(uri.toString());
                            completedUploads[0]++;
                            checkAndUpdateDatabase(comicId, newComic, finalPendingUploads, completedUploads);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi tải ảnh tiêu đề: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        // Tải ảnh banner
        if (imgBannerUri != null) {
            String fileName = comicId + "/img_banner.jpg";
            StorageReference fileRef = fb.getStorageComicRef().child(fileName);
            int finalPendingUploads1 = pendingUploads;
            fileRef.putFile(imgBannerUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            newComic.setBanner_url(uri.toString());
                            completedUploads[0]++;
                            checkAndUpdateDatabase(comicId, newComic, finalPendingUploads1, completedUploads);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi tải ảnh banner: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        // Tải file PDF
        if (pdfUri != null) {
            String fileName = comicId + "/comic.pdf";
            StorageReference fileRef = fb.getStorageComicRef().child(fileName);
            int finalPendingUploads2 = pendingUploads;
            fileRef.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            newComic.setUrl_pdf(uri.toString());
                            completedUploads[0]++;
                            checkAndUpdateDatabase(comicId, newComic, finalPendingUploads2, completedUploads);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi tải PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Kiểm tra và cập nhật Realtime Database
    private void checkAndUpdateDatabase(String comicId,Comic comicData,
                                        int pendingUploads, int[] completedUploads) {
        if (completedUploads[0] >= pendingUploads) {
            fb.getComicRef().child(comicId).setValue(comicData);
            fb.getComicGenreRef().child(comicId).setValue(selectedIdGenre);

            Toast.makeText(getActivity(), "Tải truyện lên thành công!", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void addSpinnerAuthor() {
        adapterAuthor = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                listAuthor);
        adapterAuthor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAuthor.setAdapter(adapterAuthor);
        spAuthor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Author authorSelected = (Author) parent.getItemAtPosition(position);
                selectedIdAuthor = authorSelected.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void addSpinnerGenre() {
        adapterGenre = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                listGenre);
        adapterGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spGenre.setAdapter(adapterGenre);

        spGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Genre genreSelected = (Genre) parent.getItemAtPosition(position);
                selectedIdGenre = genreSelected.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void getAuthor() {
        fb.getAuthorRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Author a = ds.getValue(Author.class);
                    listAuthor.add(a);
                }
                addSpinnerAuthor();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getGenre() {
        fb.getGenreRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Genre g = ds.getValue(Genre.class);
                    listGenre.add(g);
                }
                addSpinnerGenre();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

}