package com.example.a2hcomic.activities.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.ComicHomeAdapter;
import com.example.a2hcomic.models.Comic;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView rvnew, rvfav;

    private  ArrayList<Comic> comics ;
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

        comics = new ArrayList<>();
        createComic();
        ComicHomeAdapter adapterNew = new ComicHomeAdapter(comics);

        rvnew.setLayoutManager(new LinearLayoutManager( getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        rvnew.setAdapter(adapterNew);

        rvfav.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        rvfav.setAdapter(adapterNew);
        return view;
    }
    public void createComic(){
        comics.add(new Comic("1", "1", "1", "1", "1", "1", "1", 1));
        comics.add(new Comic("2", "2", "2", "2", "2", "2", "2", 2));
        comics.add(new Comic("3", "3", "3", "3", "3", "3", "3", 3));
        comics.add(new Comic("4", "4", "4", "4", "4", "4", "4", 4));
    }
}
