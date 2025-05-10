package com.example.a2hcomic.activities.admin;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.example.a2hcomic.models.Slider;
import com.google.firebase.storage.StorageReference;

public class AddSliderFragment extends Fragment {

    private ImageButton btnBack;
    private ImageView ivSlider;
    private Button btnUpload;
    private RelativeLayout loading;
    private Uri imageUri;

    private FirebaseService fb;

    public AddSliderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }// khởi tạo fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,// khởi tạo view
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment// khởi tạo layout
        View v = inflater.inflate(R.layout.fragment_add_slider, container, false);
        // Ánh xạ các view
        btnBack = v.findViewById(R.id.btnBack);
        ivSlider = v.findViewById(R.id.ivSlider);
        btnUpload = v.findViewById(R.id.btnUpload);
        loading = v.findViewById(R.id.loading);

        fb = new FirebaseService();
        // Xử lý sự kiện cho các view
        btnBack.setOnClickListener(v1 -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        ivSlider.setOnClickListener(v1 -> {
            openImagePicker();
        });


        btnUpload.setOnClickListener(v1 -> {
            uploadSlider();
        });

        return v;
    }
    //chọn ảnh từ thư viện
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    // ActivityResultLauncher cho (ảnh đươc chọn thành công)
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                getActivity();
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();//lưu  đ dẫn ảnh
                    ivSlider.setImageURI(imageUri);//hieenrn thi anh
                }
            });

    private void uploadSlider() {
        loading.setVisibility(View.VISIBLE);

        String id = fb.getSliderRef().push().getKey();// tao id mơi cho slider

        Slider newSlider = new Slider();
        newSlider.setId(id);

        if (imageUri != null) {
            String fileName = id + ".jpg";
            StorageReference fileRef = fb.getSliderStorageRef().child(fileName);// up load Stograe
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {// gọi ham de luu thong tin vao realtime database
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            newSlider.setImg_url(uri.toString());
                            checkAndUpdateDatabase(newSlider);// neu thnah cong luu vao Slider
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi tải ảnh slider: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void checkAndUpdateDatabase(Slider newSlider) {// kiểm tra và lưu vào database
        fb.getSliderRef().child(newSlider.getId()).setValue(newSlider);
        Toast.makeText(getActivity(), "Tải ảnh slider lên thành công!", Toast.LENGTH_SHORT).show();

        imageUri = null;
        ivSlider.setImageResource(R.drawable.ic_uploadcomic);
        loading.setVisibility(View.GONE);
    }

}