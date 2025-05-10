package com.example.a2hcomic.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.ComicHomeAdapter;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.example.a2hcomic.models.Slider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView rvNew, rvTopViewed;
    private ImageSlider imageSlider;
    private BottomNavigationView nav_main;

    private FirebaseService fb;
    private ComicHomeAdapter adapterNew, adapterTopViewed;
    private ArrayList<Comic> comicNewest, comicsTopViewed;
    private ArrayList<Slider> listSlider;

    public HomeFragment() {
        // Constructor mặc định
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các view
        rvNew = view.findViewById(R.id.listNew);
        rvTopViewed = view.findViewById(R.id.listTopViewed); // dùng lại ID cũ nếu layout chưa sửa
        imageSlider = view.findViewById(R.id.imageSlider);
        nav_main = getActivity().findViewById(R.id.nav_main);

        // Khởi tạo Firebase service
        fb = new FirebaseService();

        // Tạo danh sách và adapter
        comicNewest = new ArrayList<>();
        comicsTopViewed = new ArrayList<>();
        listSlider = new ArrayList<>();

        adapterNew = new ComicHomeAdapter(comicNewest);
        adapterTopViewed = new ComicHomeAdapter(comicsTopViewed);

        // Thiết lập layout và adapter cho danh sách truyện
        rvNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNew.setAdapter(adapterNew);

        rvTopViewed.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTopViewed.setAdapter(adapterTopViewed);

        // Lấy dữ liệu truyện và slider
        getComicNewest();
        getTopViewedComics();
        getSlider();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nav_main.setVisibility(View.VISIBLE);
    }

    // Lấy dữ liệu truyện mới nhất từ Firebase
    private void getComicNewest() {
        fb.getComicRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comicNewest.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comic comic = dataSnapshot.getValue(Comic.class);
                    if (comic != null) {
                        comicNewest.add(comic);
                    }
                }
                // Sắp xếp theo thời gian tạo giảm dần
                comicNewest.sort((o1, o2) -> (int) (o2.getCreated_at() - o1.getCreated_at()));
                adapterNew.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // Lấy danh sách truyện có lượt xem cao nhất
    private void getTopViewedComics() {
        fb.getComicRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comicsTopViewed.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Comic comic = child.getValue(Comic.class);
                    if (comic != null) {
                        comicsTopViewed.add(comic);
                    }
                }

                // Sắp xếp theo lượt xem giảm dần
                comicsTopViewed.sort((c1, c2) -> Integer.compare(c2.getViewCount(), c1.getViewCount()));

                // Giới hạn top 10 truyện
                if (comicsTopViewed.size() > 10) {
                    comicsTopViewed = new ArrayList<>(comicsTopViewed.subList(0, 10));
                }

                adapterTopViewed.setComicList(comicsTopViewed);
                adapterTopViewed.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // Hàm thiết lập slider
    private void getSlider() {
        fb.getSliderRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listSlider.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Slider slider = dataSnapshot.getValue(Slider.class);
                        if (slider != null) {
                            listSlider.add(slider);
                        }
                    }
                    setSlider();
                }
            }

            public void setSlider() {
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (Slider s : listSlider) {
                    slideModels.add(new SlideModel(s.getImg_url(), ScaleTypes.CENTER_CROP));
                }
                imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
