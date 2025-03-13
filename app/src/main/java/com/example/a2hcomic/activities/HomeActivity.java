package com.example.a2hcomic.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.ComicHomeAdapter;
import com.example.a2hcomic.models.Comic;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvnew = findViewById(R.id.listNew);
        RecyclerView rvfav = findViewById(R.id.listFav);

        ArrayList<Comic> comics = new ArrayList<>();
        comics.add(new Comic("1","1","1","1","1","1","1",1));
        comics.add(new Comic("2","2","2","2","2","2","2",2));
        comics.add(new Comic("3","3","3","3","3","3","3",3));
        comics.add(new Comic("4","4","4","4","4","4","4",4));
        comics.add(new Comic("5","5","5","5","5","5","5",5));

        ComicHomeAdapter adapterNew = new ComicHomeAdapter(comics);

        rvnew.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        rvnew.setAdapter(adapterNew);

        rvfav.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        rvfav.setAdapter(adapterNew);

    }


}