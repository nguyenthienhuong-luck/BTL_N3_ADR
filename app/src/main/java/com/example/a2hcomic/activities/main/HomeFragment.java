package com.example.a2hcomic.activities.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.ComicHomeAdapter;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView rvnew, rvfav;
    private ImageSlider imageSlider;

    private FirebaseService fb;


    private ComicHomeAdapter adapterNew, adapterFav;
    private  ArrayList<Comic> comicNewest, comicsFav;
    public HomeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvnew = view.findViewById(R.id.listNew);
        rvfav = view.findViewById(R.id.listFav);
        imageSlider = view.findViewById(R.id.imageSlider);

        fb = new FirebaseService();

        comicNewest = new ArrayList<>();
        comicsFav = new ArrayList<>();

        adapterNew = new ComicHomeAdapter(comicNewest);
        adapterFav = new ComicHomeAdapter(comicsFav);

        rvnew.setLayoutManager(new LinearLayoutManager( getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        rvnew.setAdapter(adapterNew);

        rvfav.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        rvfav.setAdapter(adapterFav);

        createComic();

        // Tạo slider vào thêm ảnh vào slider
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slider_1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider_2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider_3, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider_4 , ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);

        return view;
    }
    public void createComic(){
        fb.getComicRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comic comic = dataSnapshot.getValue(Comic.class);
                    comicNewest.add(comic);
                }
                comicNewest.sort((o1, o2) -> o2.getCreate_at() - o1.getCreate_at());

                adapterNew.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
